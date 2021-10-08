package uk.co.mruoc.demo.domain.service;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

class PaymentUpdaterTest {

    private final PaymentRepository repository = mock(PaymentRepository.class);
    private final UpdateApproval updateApproval = mock(UpdateApproval.class);

    private final PaymentUpdater updater = new PaymentUpdater(repository, updateApproval);

    @Test
    void shouldSavePaymentAndUpdateApproval() {
        Payment payment = PaymentMother.pending();

        updater.update(payment);

        InOrder inOrder = inOrder(repository, updateApproval);
        inOrder.verify(repository).save(payment);
        inOrder.verify(updateApproval).update(payment);
    }

}
