package uk.co.mruoc.demo.adapter.camunda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.service.RequestApproval;

@RequiredArgsConstructor
@Slf4j
public class CamundaRequestApproval implements RequestApproval {

    private final RuntimeService runtimeService;
    private final PaymentConverter paymentConverter;

    @Override
    public void request(Payment payment) {
        log.info("requesting approval for payment {}", payment);
        ProcessInstance process = triggerProcess(payment);
        log.info("triggered payment request {} for payment {}",
                process.getProcessInstanceId(),
                payment.getId()
        );
    }

    private ProcessInstance triggerProcess(Payment payment) {
        return runtimeService.createProcessInstanceByKey("request-payment-approval")
                .setVariables(paymentConverter.toVariables(payment))
                .businessKey(payment.getId())
                .execute();
    }

}
