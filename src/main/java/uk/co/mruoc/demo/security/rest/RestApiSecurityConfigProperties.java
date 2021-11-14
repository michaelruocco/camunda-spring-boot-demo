package uk.co.mruoc.demo.security.rest;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Component
@ConfigurationProperties(prefix = "rest.security")
@Validated
@Data
public class RestApiSecurityConfigProperties {

    private Boolean enabled = true;

    @NotEmpty
    private String provider;

    @NotEmpty
    private String requiredAudience;

}
