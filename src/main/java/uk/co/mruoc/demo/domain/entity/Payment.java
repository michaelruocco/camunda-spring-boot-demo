package uk.co.mruoc.demo.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Builder(toBuilder = true)
@Data
public class Payment {

    private final String id;
    @With
    private final Status status;
    private final double riskScore;
    private final Account account;
    private final Product product;
    private final String quote;

    public Payment accept() {
        return withStatus(Status.ACCEPTED);
    }

    public Payment reject() {
        return withStatus(Status.REJECTED);
    }

    @JsonIgnore
    public String getProductId() {
        return product.getId();
    }

    @JsonIgnore
    public double getCost() {
        return product.getCost();
    }

    @JsonIgnore
    public boolean isPending() {
        return status == Status.PENDING;
    }

}
