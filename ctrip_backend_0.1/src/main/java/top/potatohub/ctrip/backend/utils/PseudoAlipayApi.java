package top.potatohub.ctrip.backend.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

public class PseudoAlipayApi {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public static String alipayTradePagePay(String bizContent, String returnUrl, String notifyUrl) {
        String amount = "0.00";
        String outTradeNo = "";
        try {
            if (bizContent != null && !bizContent.isEmpty()) {
                JsonNode node = OBJECT_MAPPER.readTree(bizContent);
                if (node.has("total_amount")) {
                    amount = node.get("total_amount").asText();
                }
                if (node.has("out_trade_no")) {
                    outTradeNo = node.get("out_trade_no").asText();
                }
            }
        } catch (Exception ignored) {}

        String html;
        try {
            ClassPathResource resource = new ClassPathResource("templates/alipay_pay.html");
            html = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            html = "<html><body><h1>Vivo " + amount + "</h1></body></html>";
        }

        html = html.replace("{{ amount }}", amount)
                .replace("{{ out_trade_no }}", outTradeNo)
                .replace("{{ notify_url }}", notifyUrl == null ? "" : notifyUrl)
                .replace("{{ return_url }}", returnUrl == null ? "" : returnUrl);

        return html;
    }

    public static String callback(String outTradeNo, String amount, String notifyUrl, String returnUrl) {
        if (notifyUrl != null && !notifyUrl.isEmpty()) {
            try {
                MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                map.add("trade_status", "TRADE_SUCCESS");
                map.add("out_trade_no", outTradeNo);
                map.add("total_amount", amount);
                map.add("trade_no", "ALIPAY_PSEUDO_" + System.currentTimeMillis());
                map.add("app_id", "PSEUDO_ALIPAY");
                new Thread(() -> {
                    try {
                        REST_TEMPLATE.postForEntity(notifyUrl, map, String.class);
                    } catch (Exception ignored) {}
                }).start();
            } catch (Exception ignored) {}
        }

        if (returnUrl != null && !returnUrl.isEmpty()) {
            String separator = returnUrl.contains("?") ? "&" : "?";
            return returnUrl + separator +
                    "out_trade_no=" + outTradeNo +
                    "&total_amount=" + amount +
                    "&trade_no=ALIPAY_PSEUDO_" + System.currentTimeMillis() +
                    "&charset=utf-8";
        }

        return "<h1>Payment Success</h1><p>No return_url provided.</p>";
    }
}
