
package uk.co.mruoc.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import uk.co.mruoc.demo.adapter.s3.LoggingPaymentPersistor;
import uk.co.mruoc.demo.domain.service.PaymentPersistor;
import uk.co.mruoc.demo.domain.service.QuoteClient;
import uk.co.mruoc.demo.adapter.quote.StubbedQuoteClient;
import uk.co.mruoc.json.JsonConverter;

@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@Slf4j
@Profile("stubbed")
public class ApplicationStubbedConfig {

    @Bean
    public QuoteClient quoteClient() {
        log.warn("configuring stubbed quote client, this should only be done for local or testing purposes");
        return new StubbedQuoteClient();
    }

    @Bean
    public PaymentPersistor paymentPersistor(JsonConverter jsonConverter) {
        log.warn("configuring logging payment persistor, this should only be done for local or testing purposes");
        return new LoggingPaymentPersistor(jsonConverter);
    }

}
