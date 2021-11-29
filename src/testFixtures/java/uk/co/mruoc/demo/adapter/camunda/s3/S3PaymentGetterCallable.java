package uk.co.mruoc.demo.adapter.camunda.s3;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import uk.co.mruoc.demo.adapter.s3.S3Config;

import java.util.Optional;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class S3PaymentGetterCallable implements Callable<Boolean> {

    private final S3PaymentGetter getter;
    private final String paymentId;
    private String content;

    public S3PaymentGetterCallable(S3Config config, S3Client client, String paymentId) {
        this(new S3PaymentGetter(config, client), paymentId);
    }

    @Override
    public Boolean call() {
        Optional<String> payment = getter.getPaymentContent(paymentId);
        if (payment.isPresent()) {
            this.content = payment.get();
            return true;
        }
        return false;
    }

    public String getContent() {
        return content;
    }

}
