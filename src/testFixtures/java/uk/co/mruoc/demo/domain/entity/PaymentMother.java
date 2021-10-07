package uk.co.mruoc.demo.domain.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.co.mruoc.demo.domain.entity.Payment.PaymentBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentMother {

    public static Payment build() {
        return builder().build();
    }

    public static Payment withId(String id) {
        return builder().id(id).build();
    }

    private static PaymentBuilder builder() {
        return Payment.builder()
                .id("1234567890")
                .product(ProductMother.build())
                .account(AccountMother.build())
                .riskScore(100)
                .quote("demo quote")
                .status(Status.ACCEPTED);
    }

}
