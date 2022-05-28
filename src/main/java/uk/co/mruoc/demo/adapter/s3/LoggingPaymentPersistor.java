package uk.co.mruoc.demo.adapter.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.service.PaymentPersistor;
import uk.co.mruoc.json.JsonConverter;

@RequiredArgsConstructor
@Slf4j
public class LoggingPaymentPersistor implements PaymentPersistor {

    private final JsonConverter converter;

    @Override
    public void persist(Payment payment) {
        log.info("would persist payment {}", converter.toJson(payment));
    }

}
