package uk.co.mruoc.demo.domain.service;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentUpdaterTest {

    private final PaymentLoader loader = mock(PaymentLoader.class);
    private final PaymentRepository repository = mock(PaymentRepository.class);
    private final UpdateApproval updateApproval = mock(UpdateApproval.class);

    private final PaymentUpdater updater = new PaymentUpdater(loader, repository, updateApproval);

    @Test
    void shouldThrowExceptionIfExistingPaymentIsNotPending() {
        Payment payment = PaymentMother.pending();
        Payment existing = PaymentMother.accepted();
        when(loader.load(payment.getId())).thenReturn(existing);

        Throwable error = catchThrowable(() -> updater.update(payment));

        assertThat(error)
                .isInstanceOf(PaymentAlreadyProcessedException.class)
                .hasMessage(payment.getId());
    }

    @Test
    void shouldSavePaymentAndUpdateApprovalIfExistingPaymentIsPending() {
        Payment payment = PaymentMother.pending();
        when(loader.load(payment.getId())).thenReturn(payment);

        updater.update(payment);

        InOrder inOrder = inOrder(repository, updateApproval);
        inOrder.verify(repository).save(payment);
        inOrder.verify(updateApproval).update(payment);
    }

}
