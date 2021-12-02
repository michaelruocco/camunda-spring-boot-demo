package uk.co.mruoc.demo.adapter.s3;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.json.JsonConverter;
import uk.org.webcompere.systemstubs.ThrowingRunnable;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.org.webcompere.systemstubs.SystemStubs.tapSystemOut;

class LoggingPaymentPersistorTest {

    private final JsonConverter jsonConverter = mock(JsonConverter.class);

    private final LoggingPaymentPersistor persistor = new LoggingPaymentPersistor(jsonConverter);

    @Test
    void shouldLogPayment() throws Exception {
        Payment payment = mock(Payment.class);
        String json = givenPaymentConvertsToJson(payment);

        ThrowingRunnable call = () -> persistor.persist(payment);

        assertThat(captureLogLines(call))
                .hasSize(1)
                .allMatch(line -> line.endsWith(String.format("would persist payment %s", json)));
    }

    private String givenPaymentConvertsToJson(Payment payment) {
        String json = "{\"id\":\"12345678\"}";
        when(jsonConverter.toJson(payment)).thenReturn(json);
        return json;
    }

    public static List<String> captureLogLines(ThrowingRunnable statement) throws Exception {
        return tapSystemOut(statement)
                .lines()
                .collect(Collectors.toList());
    }

}
