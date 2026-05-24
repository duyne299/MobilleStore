package com.duy.do_an_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    @PostMapping("/vietqr/create")
    public ResponseEntity<Map<String, String>> createVietQRPayment(@RequestBody Map<String, Object> request) {
        Double amount = ((Number) request.get("amount")).doubleValue();
        String description = (String) request.get("description");

        // Format: https://img.vietqr.io/image/<BANK_ID>-<ACCOUNT_NO>-<TEMPLATE>.jpg?amount=<AMOUNT>&addInfo=<DESCRIPTION>
        String bankId = "970422"; // MBBank
        String accountNo = "0987654321"; // Mock demo account
        String template = "qr_only";
        String qrCodeUrl = String.format("https://img.vietqr.io/image/%s-%s-%s.jpg?amount=%.0f&addInfo=%s",
                bankId, accountNo, template, amount, description != null ? description.replace(" ", "%20") : "");

        Map<String, String> response = new HashMap<>();
        response.put("qrCode", qrCodeUrl);
        response.put("transactionCode", "TXN-" + System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/vnpay/create")
    public ResponseEntity<Map<String, String>> createVNPayPayment(@RequestBody Map<String, Object> request) {
        Long orderId = ((Number) request.get("orderId")).longValue();
        Double amount = ((Number) request.get("amount")).doubleValue();
        String returnUrl = (String) request.get("returnUrl");

        // Mock VNPay payment URL that redirects back to returnUrl with standard VNPay success parameters
        String transactionNo = "VNP" + System.currentTimeMillis();
        String mockPaymentUrl = returnUrl + "?vnp_ResponseCode=00&vnp_TxnRef=" + orderId + "&vnp_Amount=" + amount + "&vnp_TransactionNo=" + transactionNo;

        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", mockPaymentUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{orderId}")
    public ResponseEntity<Map<String, Object>> checkPaymentStatus(@PathVariable Long orderId) {
        // Automatically mock successful payment status
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("status", "SUCCESS");
        return ResponseEntity.ok(response);
    }
}
