package uk.co.mruoc.demo.domain.entity;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class Payment {

    private final String id;
    private final Status status;
    private final double riskScore;
    private final Account account;
    private final Product product;
    private final String quote;

}
