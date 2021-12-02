package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class SendExternalNotification {

    public void send(String paymentId) {
        log.info("sending external notification {}", paymentId);
        if (paymentId.endsWith("9")) {
            throw new ExternalNotificationFailedException(paymentId);
        }
    }

}
