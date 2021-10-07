package uk.co.mruoc.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mruoc.demo.domain.service.PaymentService;
import uk.co.mruoc.demo.domain.service.StubPaymentService;

@Configuration
public class ApplicationConfig {

    @Bean
    public PaymentService paymentService() {
        return new StubPaymentService();
    }

}
