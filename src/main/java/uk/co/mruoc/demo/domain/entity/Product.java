package uk.co.mruoc.demo.domain.entity;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class Product {

    private final String id;
    private final String description;
    private final double cost;

}
