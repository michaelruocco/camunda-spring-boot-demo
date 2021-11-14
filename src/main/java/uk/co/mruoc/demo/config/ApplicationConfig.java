package uk.co.mruoc.demo.config;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mruoc.demo.adapter.camunda.AcceptPayment;
import uk.co.mruoc.demo.adapter.camunda.ApprovalFormFactory;
import uk.co.mruoc.demo.adapter.camunda.CamundaRequestApproval;
import uk.co.mruoc.demo.adapter.camunda.CamundaUpdateApproval;
import uk.co.mruoc.demo.adapter.camunda.PaymentConverter;
import uk.co.mruoc.demo.adapter.camunda.RejectPayment;
import uk.co.mruoc.demo.adapter.camunda.SendExternalNotification;
import uk.co.mruoc.demo.adapter.camunda.VariableExtractor;
import uk.co.mruoc.demo.adapter.quote.QuoteClient;
import uk.co.mruoc.demo.adapter.repository.InMemoryPaymentRepository;
import uk.co.mruoc.demo.domain.service.PaymentCreator;
import uk.co.mruoc.demo.domain.service.PaymentLoader;
import uk.co.mruoc.demo.domain.service.PaymentProcessor;
import uk.co.mruoc.demo.domain.service.PaymentRepository;
import uk.co.mruoc.demo.domain.service.PaymentService;
import uk.co.mruoc.demo.domain.service.PaymentUpdater;
import uk.co.mruoc.demo.domain.service.PreparePayment;
import uk.co.mruoc.demo.domain.service.RequestApproval;
import uk.co.mruoc.demo.domain.service.UpdateApproval;



import static org.apache.http.ssl.SSLContexts.*;

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
    public ApprovalFormFactory approvalFormFactory() {
        return new ApprovalFormFactory();
    }

    @Bean
    public PaymentConverter paymentConverter(ApprovalFormFactory formFactory) {
        return new PaymentConverter(formFactory);
    }

    @Bean
    public RequestApproval requestApproval(RuntimeService runtimeService, PaymentConverter converter) {
        return new CamundaRequestApproval(runtimeService, converter);
    }

    @Bean
    public PaymentCreator paymentCreator(PreparePayment preparePayment,
                                         PaymentRepository repository,
                                         RequestApproval requestApproval) {
        return new PaymentCreator(preparePayment, repository, requestApproval);
    }

    @Bean
    public UpdateApproval updateApproval(RuntimeService runtimeService, PaymentConverter converter) {
        return new CamundaUpdateApproval(runtimeService, converter);
    }

    @Bean
    public PaymentUpdater paymentUpdater(PreparePayment preparePayment,
                                         PaymentRepository repository,
                                         UpdateApproval updateApproval) {
        return new PaymentUpdater(preparePayment, repository, updateApproval);
    }

    @Bean
    public PaymentProcessor paymentProcessor(PaymentRepository repository,
                                             PaymentCreator creator,
                                             PaymentUpdater updater) {
        return new PaymentProcessor(repository, creator, updater);
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
    public SendExternalNotification sendExternalNotification(VariableExtractor extractor) {
        return new SendExternalNotification(extractor);
    }

    @Bean
    public RejectPayment repository(VariableExtractor extractor,
                                    PaymentLoader loader,
                                    PaymentRepository repository) {
        return new RejectPayment(extractor, loader, repository);
    }

}
