package top.potatohub.ctrip.backend.controller;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.potatohub.ctrip.backend.service.OrderService;
import top.potatohub.ctrip.backend.utils.PseudoAlipayApi;

import java.io.IOException;

@RestController
@RequestMapping("/alipay")
public class AlipayController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/pay")
    public void pay(@RequestParam String outTradeNo,
                    @RequestParam String subject,
                    @RequestParam String totalAmount,
                    HttpServletResponse response) throws IOException {
        
        String bizContent = String.format("{\"out_trade_no\":\"%s\", \"total_amount\":\"%s\", \"subject\":\"%s\", \"product_code\":\"FAST_INSTANT_TRADE_PAY\"}",
                outTradeNo, totalAmount, subject);
        
        // Return URL: where the browser is redirected after payment
        String returnUrl = "http://localhost:5173/user?payment=success"; 
        
        // Notify URL: where the backend receives async notification
        String notifyUrl = "http://localhost:8080/alipay/notify"; 

        String form = PseudoAlipayApi.alipayTradePagePay(bizContent, returnUrl, notifyUrl);
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
    }

    @PostMapping("/callback")
    public void callback(@RequestParam("out_trade_no") String outTradeNo,
                         @RequestParam("total_amount") String totalAmount,
                         @RequestParam(value = "notify_url", required = false) String notifyUrl,
                         @RequestParam(value = "return_url", required = false) String returnUrl,
                         HttpServletResponse response) throws IOException {
        
        // Update order status to 'Paid'
        orderService.updateStatus(outTradeNo, "Paid");

        // Process the callback using PseudoAlipayApi logic
        String redirectUrl = PseudoAlipayApi.callback(outTradeNo, totalAmount, notifyUrl, returnUrl);
        
        // Redirect the user to the return URL (which is the frontend URL)
        response.sendRedirect(redirectUrl);
    }
}
