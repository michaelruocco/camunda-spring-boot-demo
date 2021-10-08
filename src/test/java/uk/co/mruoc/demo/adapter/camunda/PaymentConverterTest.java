package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.spin.json.SpinJsonNode;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentConverterTest {

    private final ApprovalFormFactory formFactory = mock(ApprovalFormFactory.class);

    private final PaymentConverter converter = new PaymentConverter(formFactory);

    @Test
    void shouldPopulateFiveVariables() {
        Payment payment = PaymentMother.build();
        givenConvertsToApprovalForm(payment);

        Map<String, Object> variables = converter.toVariables(payment);

        assertThat(variables).hasSize(5);
    }

    @Test
    void shouldPopulatePaymentId() {
        Payment payment = PaymentMother.build();
        givenConvertsToApprovalForm(payment);

        Map<String, Object> variables = converter.toVariables(payment);

        assertThat(variables).containsEntry("paymentId", payment.getId());
    }

    @Test
    void shouldPopulateProductId() {
        Payment payment = PaymentMother.build();
        givenConvertsToApprovalForm(payment);

        Map<String, Object> variables = converter.toVariables(payment);

        assertThat(variables).containsEntry("productId", payment.getProductId());
    }

    @Test
    void shouldPopulateRiskScore() {
        Payment payment = PaymentMother.build();
        givenConvertsToApprovalForm(payment);

        Map<String, Object> variables = converter.toVariables(payment);

        assertThat(variables).containsEntry("riskScore", payment.getRiskScore());
    }

    @Test
    void shouldPopulateCost() {
        Payment payment = PaymentMother.build();
        givenConvertsToApprovalForm(payment);

        Map<String, Object> variables = converter.toVariables(payment);

        assertThat(variables).containsEntry("cost", payment.getCost());
    }

    @Test
    void shouldPopulateApprovalForm() {
        Payment payment = PaymentMother.build();
        SpinJsonNode approvalForm = givenConvertsToApprovalForm(payment);

        Map<String, Object> variables = converter.toVariables(payment);

        assertThat(variables).containsEntry("approvalForm", approvalForm);
    }

    private SpinJsonNode givenConvertsToApprovalForm(Payment payment) {
        SpinJsonNode form = mock(SpinJsonNode.class);
        when(formFactory.toApprovalForm(payment)).thenReturn(form);
        return form;
    }

}
