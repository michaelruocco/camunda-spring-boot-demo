package uk.co.mruoc.demo.adapter.camunda;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.domain.entity.Payment;

import java.util.Map;

@RequiredArgsConstructor
public class PaymentConverter {

    private final ApprovalFormFactory approvalFormFactory;

    public Map<String, Object> toVariables(Payment payment) {
        return Map.of(
                "paymentId", payment.getId(),
                "productId", payment.getProductId(),
                "riskScore", payment.getRiskScore(),
                "cost", payment.getCost(),
                "approvalForm", approvalFormFactory.toApprovalForm(payment)
        );
    }

}
