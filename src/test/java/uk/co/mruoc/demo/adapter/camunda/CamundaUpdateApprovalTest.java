package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;
import uk.co.mruoc.demo.domain.service.UpdateApproval;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CamundaUpdateApprovalTest {

    private final RuntimeService runtimeService = mock(RuntimeService.class);
    private final PaymentConverter converter = mock(PaymentConverter.class);

    private final UpdateApproval updateApproval = new CamundaUpdateApproval(runtimeService, converter);

    @Test
    void shouldThrowExceptionIfProcessInstanceNotFound() {
        Payment payment = PaymentMother.build();
        ProcessInstanceQuery query = givenProcessInstanceQuery(payment.getId());
        when(query.singleResult()).thenReturn(null);

        Throwable error = catchThrowable(() -> updateApproval.update(payment));

        assertThat(error)
                .isInstanceOf(ProcessInstanceNotFoundException.class)
                .hasMessage(payment.getId());
    }

    @Test
    void shouldUpdateProcessVariablesIfProcessInstanceFound() {
        Payment payment = PaymentMother.build();
        ProcessInstanceQuery query = givenProcessInstanceQuery(payment.getId());
        Map<String, Object> variables = givenConvertsToVariables(payment);
        String processInstanceId = "process-instance-id";
        ProcessInstance process = givenProcessInstanceWithId(processInstanceId);
        when(query.singleResult()).thenReturn(process);

        updateApproval.update(payment);

        verify(runtimeService).setVariables(processInstanceId, variables);
    }

    private Map<String, Object> givenConvertsToVariables(Payment payment) {
        Map<String, Object> variables = Map.of();
        when(converter.toVariables(payment)).thenReturn(variables);
        return variables;
    }

    private ProcessInstanceQuery givenProcessInstanceQuery(String id) {
        ProcessInstanceQuery query = mock(ProcessInstanceQuery.class);
        when(runtimeService.createProcessInstanceQuery()).thenReturn(query);
        when(query.processInstanceBusinessKey(id)).thenReturn(query);
        return query;
    }

    private ProcessInstance givenProcessInstanceWithId(String processInstanceId) {
        ProcessInstance process = mock(ProcessInstance.class);
        when(process.getProcessInstanceId()).thenReturn(processInstanceId);
        return process;
    }

}
