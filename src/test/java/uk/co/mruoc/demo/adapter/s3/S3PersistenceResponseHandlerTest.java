package uk.co.mruoc.demo.adapter.s3;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class S3PersistenceResponseHandlerTest {

    private final S3PersistenceResponseHandler handler = new S3PersistenceResponseHandler();

    @Test
    void shouldThrowExceptionWhenHandlingError() {
        Throwable cause = new Exception("test-error");

        Throwable error = catchThrowable(() -> handler.handle(cause));

        assertThat(error)
                .isInstanceOf(S3PersistenceException.class)
                .hasCause(cause);
    }

    @Test
    void shouldThrowExceptionWhenHandlingUnsuccessfulResponse() {
        String responseString = "response-string";
        SdkHttpResponse httpResponse = givenHttpResponse(false);
        PutObjectResponse response = givenPutObjectResponse(httpResponse);
        when(response.toString()).thenReturn(responseString);

        Throwable error = catchThrowable(() -> handler.handle(response));

        assertThat(error)
                .isInstanceOf(S3PersistenceException.class)
                .hasMessage(responseString);
    }

    @Test
    void shouldNotThrowExceptionWhenHandlingSuccessfulResponse() {
        SdkHttpResponse httpResponse = givenHttpResponse(true);
        PutObjectResponse response = givenPutObjectResponse(httpResponse);

        ThrowingCallable call = (() -> handler.handle(response));

        assertThatCode(call).doesNotThrowAnyException();
    }

    private PutObjectResponse givenPutObjectResponse(SdkHttpResponse httpResponse) {
        PutObjectResponse response = mock(PutObjectResponse.class);
        when(response.sdkHttpResponse()).thenReturn(httpResponse);
        return response;
    }

    private SdkHttpResponse givenHttpResponse(boolean successful) {
        SdkHttpResponse httpResponse = mock(SdkHttpResponse.class);
        when(httpResponse.isSuccessful()).thenReturn(successful);
        return httpResponse;
    }

}
