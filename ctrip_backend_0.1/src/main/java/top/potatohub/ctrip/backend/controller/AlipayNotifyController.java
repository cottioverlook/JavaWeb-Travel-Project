package top.potatohub.ctrip.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import top.potatohub.ctrip.backend.service.OrderService;

import java.util.Map;

@RestController
@RequestMapping("/alipay")
public class AlipayNotifyController {

    @Autowired
    private OrderService orderService;

    @Value("${app.mock-payment-enabled:false}")
    private boolean mockPaymentEnabled;

    @PostMapping("/notify")
    public String notify(@RequestParam Map<String, String> params) {
        if (!mockPaymentEnabled) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        
        String tradeStatus = params.get("trade_status");
        String outTradeNo = params.get("out_trade_no");

        if ("TRADE_SUCCESS".equals(tradeStatus)) {
            orderService.updateStatus(outTradeNo, "Paid");
            return "success";
        }

        return "fail";
    }
}
