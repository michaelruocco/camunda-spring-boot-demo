package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.spin.json.SpinJsonNode;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class ApprovalFormFactoryTest {

    private final ApprovalFormFactory factory = new ApprovalFormFactory();

    @Test
    void shouldConvertPaymentToApprovalForm() {
        Payment payment = PaymentMother.build();

        SpinJsonNode approvalForm = factory.toApprovalForm(payment);

        String expectedApprovalForm = ApprovalFormJsonMother.build();
        assertThatJson(approvalForm.toString()).isEqualTo(expectedApprovalForm);
    }

}
