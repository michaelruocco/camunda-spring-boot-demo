package uk.co.mruoc.demo;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import java.net.URI;

import static org.testcontainers.utility.MountableFile.forHostPath;

@Slf4j
public class LocalAws extends LocalStackContainer {

    private static final String ACCESS_KEY_ID = "abc";
    private static final String SECRET_ACCESS_KEY= "123";
    private static final String REGION = "eu-west-2";

    private static final int PORT = 4566;

    public LocalAws() {
        super(DockerImageName.parse("localstack/localstack:latest"));
        withServices(Service.S3);
        withEnv("AWS_ACCESS_KEY_ID", ACCESS_KEY_ID);
        withEnv("AWS_SECRET_ACCESS_KEY", SECRET_ACCESS_KEY);
        withEnv("DEFAULT_REGION", REGION);
        withExposedPorts(PORT);
        withCopyFileToContainer(forHostPath("localstack/init-s3.sh"), "/etc/localstack/init/ready.d/init-s3.sh");
        withLogConsumer(new LogConsumer());
    }

    public String getS3EndpointOverrideAsString() {
        return getS3EndpointOverride().toASCIIString();
    }

    public URI getS3EndpointOverride() {
        return getEndpointOverride(Service.S3);
    }
}
