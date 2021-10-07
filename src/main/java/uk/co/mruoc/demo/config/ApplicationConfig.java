package uk.co.mruoc.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mruoc.demo.adapter.camunda.CamundaRequestApproval;
import uk.co.mruoc.demo.adapter.quote.QuoteClient;
import uk.co.mruoc.demo.adapter.repository.InMemoryPaymentRepository;
import uk.co.mruoc.demo.domain.service.PaymentLoader;
import uk.co.mruoc.demo.domain.service.PaymentProcessor;
import uk.co.mruoc.demo.domain.service.PaymentRepository;
import uk.co.mruoc.demo.domain.service.PaymentService;
import uk.co.mruoc.demo.domain.service.PreparePayment;
import uk.co.mruoc.demo.domain.service.RequestApproval;

@Configuration
public class ApplicationConfig {

    @Bean
    public QuoteClient quoteClient() {
        return new QuoteClient();
    }

    @Bean
    public PreparePayment preparePayment(QuoteClient client) {
        return new PreparePayment(client);
    }

    @Bean
    public PaymentRepository paymentRepository() {
        return new InMemoryPaymentRepository();
    }

    @Bean
    public PaymentLoader paymentLoader(PaymentRepository repository) {
        return new PaymentLoader(repository);
    }

    @Bean
    public RequestApproval requestApproval() {
        return new CamundaRequestApproval();
    }

    @Bean
    public PaymentProcessor paymentProcessor(PreparePayment preparePayment,
                                             PaymentRepository repository,
                                             RequestApproval requestApproval) {
        return new PaymentProcessor(preparePayment, repository, requestApproval);
    }

    @Bean
    public PaymentService paymentService(PaymentProcessor processor, PaymentLoader loader) {
        return new PaymentService(processor, loader);
    }

}
