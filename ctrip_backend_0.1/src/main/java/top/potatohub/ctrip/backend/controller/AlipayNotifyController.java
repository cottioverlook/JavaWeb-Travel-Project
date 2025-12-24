package top.potatohub.ctrip.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.potatohub.ctrip.backend.service.OrderService;

import java.util.Map;

@RestController
@RequestMapping("/alipay")
public class AlipayNotifyController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/notify")
    public String notify(@RequestParam Map<String, String> params) {
        System.out.println("Received Alipay notification: " + params);
        
        String tradeStatus = params.get("trade_status");
        String outTradeNo = params.get("out_trade_no");

        if ("TRADE_SUCCESS".equals(tradeStatus)) {
            System.out.println("Payment success for order: " + outTradeNo);
            orderService.updateStatus(outTradeNo, "Paid");
            return "success";
        }

        return "fail";
    }
}
