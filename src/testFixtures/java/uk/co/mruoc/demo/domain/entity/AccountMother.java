package uk.co.mruoc.demo.domain.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMother {

    public static Account build() {
        return Account.builder()
                .id("aaa-bbb-ccc")
                .owner("Joe Bloggs")
                .build();
    }

}
