package uk.co.mruoc.demo.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.service.PaymentService;

import java.io.IOException;
import java.io.UncheckedIOException;

@RequiredArgsConstructor
@Slf4j
public class ProcessPaymentListener {

    private final ObjectMapper mapper;
    private final PaymentService service;

    @KafkaListener(topics = "#{'${kafka.payment.topic}'}", groupId = "#{'${kafka.process.payment.group}'}")
    public void processPaymentListener(String json) {
        log.info("received message in payment-group {}", json);
        service.process(toPayment(json));
    }

    private Payment toPayment(String json) {
        try {
            return mapper.readValue(json, Payment.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
