package uk.co.mruoc.demo.adapter.camunda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.service.UpdateApproval;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CamundaUpdateApproval implements UpdateApproval {

    private final RuntimeService runtimeService;
    private final PaymentConverter paymentConverter;

    @Override
    public void update(Payment payment) {
        log.info("updating approval for payment {}", payment);
        String processInstanceId = loadProcessInstanceId(payment.getId());
        log.info("loaded approval process {} for payment {}", processInstanceId, payment.getId());
        runtimeService.setVariables(processInstanceId, paymentConverter.toVariables(payment));
    }

    private String loadProcessInstanceId(String id) {
        return Optional.ofNullable(findProcess(id))
                .map(Execution::getProcessInstanceId)
                .orElseThrow(() -> new ProcessInstanceNotFoundException(id));
    }

    private ProcessInstance findProcess(String id) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(id)
                .singleResult();
    }

}
