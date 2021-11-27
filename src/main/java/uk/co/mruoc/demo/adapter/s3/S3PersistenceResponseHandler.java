package uk.co.mruoc.demo.adapter.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@RequiredArgsConstructor
@Slf4j
public class S3PersistenceResponseHandler {

    public void handle(Throwable error) {
        throw new S3PersistenceException(error);
    }

    public void handle(PutObjectResponse response) {
        SdkHttpResponse httpResponse = response.sdkHttpResponse();
        log.info("s3 put http response {}", response);
        if (!httpResponse.isSuccessful()) {
            throw new S3PersistenceException(response.toString());
        }
    }

}
