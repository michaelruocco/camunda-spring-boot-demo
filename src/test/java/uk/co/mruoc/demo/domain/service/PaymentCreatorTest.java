package uk.co.mruoc.demo.domain.service;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentCreatorTest {

    private final PreparePayment preparePayment = mock(PreparePayment.class);
    private final PaymentPersistor persistor = mock(PaymentPersistor.class);
    private final PaymentRepository repository = mock(PaymentRepository.class);
    private final RequestApproval requestApproval = mock(RequestApproval.class);

    private final PaymentCreator creator = PaymentCreator.builder()
            .persistor(persistor)
            .preparePayment(preparePayment)
            .repository(repository)
            .requestApproval(requestApproval)
            .build();

    @Test
    void shouldPreparePaymentBeforeSaving() {
        Payment payment = PaymentMother.build();
        Payment preparedPayment = mock(Payment.class);
        when(preparePayment.prepare(payment)).thenReturn(preparedPayment);

        creator.create(payment);

        verify(repository).save(preparedPayment);
    }

    @Test
    void shouldPersistPaymentAndRequestApprovalAfterSaving() {
        Payment payment = PaymentMother.build();
        Payment preparedPayment = mock(Payment.class);
        when(preparePayment.prepare(payment)).thenReturn(preparedPayment);

        creator.create(payment);

        InOrder inOrder = inOrder(persistor, repository, requestApproval);
        inOrder.verify(persistor).persist(preparedPayment);
        inOrder.verify(repository).save(preparedPayment);
        inOrder.verify(requestApproval).request(preparedPayment);
    }

}
