package uk.co.mruoc.demo.domain.service;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentUpdaterTest {

    private final PreparePayment preparePayment = mock(PreparePayment.class);
    private final PaymentRepository repository = mock(PaymentRepository.class);
    private final UpdateApproval updateApproval = mock(UpdateApproval.class);

    private final PaymentUpdater updater = new PaymentUpdater(preparePayment, repository, updateApproval);

    @Test
    void shouldPreparePaymentBeforeSaving() {
        Payment payment = PaymentMother.build();
        Payment preparedPayment = mock(Payment.class);
        when(preparePayment.prepare(payment)).thenReturn(preparedPayment);

        updater.update(payment);

        verify(repository).save(preparedPayment);
    }

    @Test
    void shouldUpdateApprovalAfterSaving() {
        Payment payment = PaymentMother.build();
        Payment preparedPayment = mock(Payment.class);
        when(preparePayment.prepare(payment)).thenReturn(preparedPayment);

        updater.update(payment);

        InOrder inOrder = inOrder(repository, updateApproval);
        inOrder.verify(repository).save(preparedPayment);
        inOrder.verify(updateApproval).update(preparedPayment);
    }

}
