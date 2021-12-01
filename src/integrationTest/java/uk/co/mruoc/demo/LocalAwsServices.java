package uk.co.mruoc.demo;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;

import static org.testcontainers.utility.MountableFile.forHostPath;

@Slf4j
public class LocalAwsServices extends GenericContainer<LocalAwsServices> {

    private static final String ACCESS_KEY_ID = "abc";
    private static final String SECRET_ACCESS_KEY= "123";
    private static final String REGION = "eu-west-2";

    private static final int PORT = 4566;

    public LocalAwsServices() {
        super("localstack/localstack:latest");
        withEnv("AWS_ACCESS_KEY_ID", ACCESS_KEY_ID);
        withEnv("AWS_SECRET_ACCESS_KEY", SECRET_ACCESS_KEY);
        withEnv("DEFAULT_REGION", REGION);
        withEnv("SERVICES", "s3");
        withExposedPorts(PORT);
        withCopyFileToContainer(forHostPath("localstack/init-s3.sh"), "/docker-entrypoint-initaws.d/init-s3.sh");
        withLogConsumer(new LogConsumer());
    }

    public String getEndpointUri() {
        String ip = getContainerIpAddress();
        int port = getMappedPort(PORT);
        return String.format("http://%s:%s", ip, port);
    }

    public String getAccessKeyId() {
        return ACCESS_KEY_ID;
    }

    public String getSecretAccessKey() {
        return SECRET_ACCESS_KEY;
    }

    public String getRegion() {
        return REGION;
    }

}
