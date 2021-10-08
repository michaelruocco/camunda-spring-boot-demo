package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;
import uk.co.mruoc.demo.domain.service.RequestApproval;

import java.util.Map;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CamundaRequestApprovalTest {

    private final RuntimeService runtimeService = mock(RuntimeService.class);
    private final PaymentConverter converter = mock(PaymentConverter.class);

    private final RequestApproval requestApproval = new CamundaRequestApproval(runtimeService, converter);

    @Test
    void shouldPopulateVariablesOnProcess() {
        Payment payment = PaymentMother.build();
        Map<String, Object> variables = givenConvertsToVariables(payment);
        ProcessInstantiationBuilder builder = givenProcessBuilder();
        when(builder.setVariables(variables)).thenReturn(builder);
        when(builder.businessKey(payment.getId())).thenReturn(builder);
        ProcessInstance instance = givenProcessInstanceWithId();
        when(builder.execute()).thenReturn(instance);

        requestApproval.requestApproval(payment);

        InOrder inOrder = inOrder(builder);
        inOrder.verify(builder).setVariables(variables);
        inOrder.verify(builder).businessKey(payment.getId());
        inOrder.verify(builder).execute();
    }

    private Map<String, Object> givenConvertsToVariables(Payment payment) {
        Map<String, Object> variables = Map.of();
        when(converter.toVariables(payment)).thenReturn(variables);
        return variables;
    }

    private ProcessInstantiationBuilder givenProcessBuilder() {
        ProcessInstantiationBuilder builder = mock(ProcessInstantiationBuilder.class);
        when(runtimeService.createProcessInstanceByKey("request-payment-approval")).thenReturn(builder);
        return builder;
    }

    private ProcessInstance givenProcessInstanceWithId() {
        ProcessInstance process = mock(ProcessInstance.class);
        when(process.getId()).thenReturn("process-instance-id");
        return process;
    }

}
