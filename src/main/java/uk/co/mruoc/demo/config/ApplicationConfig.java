package uk.co.mruoc.demo.config;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mruoc.demo.adapter.camunda.AcceptPayment;
import uk.co.mruoc.demo.adapter.camunda.CamundaRequestApproval;
import uk.co.mruoc.demo.adapter.camunda.RejectPayment;
import uk.co.mruoc.demo.adapter.camunda.VariableExtractor;
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
    public RequestApproval requestApproval(RuntimeService runtimeService) {
        return new CamundaRequestApproval(runtimeService);
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

    @Bean
    public VariableExtractor variableExtractor() {
        return new VariableExtractor();
    }

    @Bean
    public AcceptPayment acceptPayment(VariableExtractor extractor,
                                       PaymentLoader loader,
                                       PaymentRepository repository) {
        return new AcceptPayment(extractor, loader, repository);
    }
    
    @Bean
    public RejectPayment rejectPayment(VariableExtractor extractor,
                                       PaymentLoader loader,
                                       PaymentRepository repository) {
        return new RejectPayment(extractor, loader, repository);
    }

    @Bean
    public RejectPayment repository(VariableExtractor extractor,
                                    PaymentLoader loader,
                                    PaymentRepository repository) {
        return new RejectPayment(extractor, loader, repository);
    }

}
