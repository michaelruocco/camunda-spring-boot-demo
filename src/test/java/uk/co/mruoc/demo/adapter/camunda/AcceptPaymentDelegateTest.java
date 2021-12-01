package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;
import uk.co.mruoc.demo.domain.service.AcceptPayment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AcceptPaymentDelegateTest {

    private final VariableExtractor extractor = mock(VariableExtractor.class);
    private final AcceptPayment acceptPayment = mock(AcceptPayment.class);

    private final AcceptPaymentDelegate delegate = new AcceptPaymentDelegate(extractor, acceptPayment);

    @Test
    void shouldAcceptPayment() {
        DelegateExecution execution = mock(DelegateExecution.class);
        Payment payment = PaymentMother.pending();
        when(extractor.extractPaymentId(execution)).thenReturn(payment.getId());

        delegate.execute(execution);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(acceptPayment).accept(captor.capture());
        assertThat(captor.getValue()).isEqualTo(payment.getId());
    }

}
