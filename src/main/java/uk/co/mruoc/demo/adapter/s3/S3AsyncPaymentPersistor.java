package uk.co.mruoc.demo.adapter.s3;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.service.PaymentPersistor;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Builder
@Slf4j
public class S3AsyncPaymentPersistor implements PaymentPersistor {

    private final S3PutObjectRequestFactory requestFactory;
    private final S3AsyncClient client;
    private final S3PersistenceResponseHandler responseHandler;

    @Override
    public void persist(Payment payment) {
        Instant start = Instant.now();
        try {
            performPersist(payment);
        } finally {
            Duration duration = Duration.between(start, Instant.now());
            log.info("persist payment {} took {}ms", payment.getId(), duration.toMillis());
        }
    }

    private void performPersist(Payment payment) {
        S3PutObjectRequestAdapter adapter = requestFactory.build(payment);
        CompletableFuture<PutObjectResponse> future = client.putObject(adapter.getRequest(), adapter.getBody());
        Mono.fromFuture(future).subscribe(responseHandler::handle, responseHandler::handle);
    }

}
