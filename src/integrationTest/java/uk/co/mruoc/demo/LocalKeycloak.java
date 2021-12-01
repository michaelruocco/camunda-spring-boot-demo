package uk.co.mruoc.demo;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;

import static org.testcontainers.utility.MountableFile.forHostPath;

@Slf4j
public class LocalKeycloak extends GenericContainer<LocalKeycloak> {

    private static final int HTTP_PORT = 8080;
    private static final int HTTPS_PORT = 8443;

    public LocalKeycloak() {
        super("quay.io/keycloak/keycloak:15.0.2");
        withExposedPorts(HTTP_PORT, HTTPS_PORT);
        withEnv("KEYCLOAK_USER", "admin");
        withEnv("KEYCLOAK_PASSWORD", "admin");
        withEnv("KEYCLOAK_IMPORT", "/tmp/demo-local-realm.json");
        withCopyFileToContainer(forHostPath("keycloak/demo-local-realm.json"), "/tmp/demo-local-realm.json");
        withLogConsumer(new LogConsumer());
    }

    public String getHttpAuthUri() {
        String ip = getContainerIpAddress();
        int port = getMappedPort(HTTP_PORT);
        return String.format("http://%s:%d", ip, port);
    }

    public String getHttpsAuthUri() {
        String ip = getContainerIpAddress();
        int port = getMappedPort(HTTPS_PORT);
        return String.format("https://%s:%d", ip, port);
    }

}
