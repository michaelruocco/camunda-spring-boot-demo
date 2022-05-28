package uk.co.mruoc.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.service.PaymentService;
import uk.co.mruoc.json.JsonConverter;

@RequiredArgsConstructor
@Slf4j
public class ProcessPaymentListener {

    private final JsonConverter jsonConverter;
    private final PaymentService service;

    @KafkaListener(topics = "#{'${kafka.payment.topic}'}", groupId = "#{'${kafka.process.payment.group}'}")
    public void processPayment(String json) {
        log.info("received message in payment-group {}", json);
        service.process(toPayment(json));
    }

    private Payment toPayment(String json) {
        return jsonConverter.toObject(json, Payment.class);
    }
}
