
package com.civicpilot.controller;

import com.civicpilot.dto.SmsRequest;
import com.civicpilot.dto.SmsResponse;
import com.civicpilot.service.SmsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send")
    public ResponseEntity<SmsResponse> sendSms(
            @RequestBody SmsRequest request) {

        String sid = smsService.sendSms(
                request.getPhoneNumber(),
                request.getMessage()
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new SmsResponse(
                        true,
                        "SMS accepted by Twilio.",
                        sid
                )
        );
    }
}
