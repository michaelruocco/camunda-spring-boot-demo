package uk.co.mruoc.demo.adapter.repository;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;
import uk.co.mruoc.demo.domain.service.PaymentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryPaymentRepositoryTest {

    private final PaymentRepository repository = new InMemoryPaymentRepository();

    @Test
    void shouldReturnEmptyPaymentDoesNotExist() {
        String id = "any-id";

        Optional<Payment> payment = repository.read(id);

        assertThat(payment).isEmpty();
    }

    @Test
    void shouldReturnPaymentIfExists() {
        Payment payment = PaymentMother.build();
        repository.save(payment);

        Optional<Payment> loaded = repository.read(payment.getId());

        assertThat(loaded).contains(payment);
    }

}
