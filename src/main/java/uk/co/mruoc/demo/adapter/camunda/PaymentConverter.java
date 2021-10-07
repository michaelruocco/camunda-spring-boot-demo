package uk.co.mruoc.demo.adapter.camunda;

import uk.co.mruoc.demo.domain.entity.Payment;

import java.util.Map;

public class PaymentConverter {

    public Map<String, Object> toVariables(Payment payment) {
        return Map.of(
                "paymentId", payment.getId(),
                "productId", payment.getProductId(),
                "riskScore", payment.getRiskScore(),
                "cost", payment.getCost()
        );
    }

}
