package uk.co.mruoc.demo.domain.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMother {

    public static Product build() {
        return Product.builder()
                .id("abc-123")
                .description("demo widget")
                .cost(99.99)
                .build();
    }

}
