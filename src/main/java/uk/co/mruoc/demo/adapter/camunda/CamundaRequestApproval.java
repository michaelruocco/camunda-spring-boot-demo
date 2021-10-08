package uk.co.mruoc.demo.adapter.camunda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.service.RequestApproval;

@RequiredArgsConstructor
@Slf4j
//TODO unit test
public class CamundaRequestApproval implements RequestApproval {

    private final RuntimeService runtimeService;
    private final PaymentConverter paymentConverter;

    @Override
    public void requestApproval(Payment payment) {
        log.info("requesting approval for payment {}", payment);
        runtimeService.createProcessInstanceByKey("request-payment-approval")
                .setVariables(paymentConverter.toVariables(payment))
                .businessKey(payment.getId())
                .execute();
    }

}
