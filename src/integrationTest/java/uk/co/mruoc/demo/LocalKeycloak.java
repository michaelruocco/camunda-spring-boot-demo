package uk.co.mruoc.demo;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.utility.MountableFile.forHostPath;

public class LocalKeycloak extends GenericContainer<LocalKeycloak> {

    private static final DockerImageName KEYCLOAK_IMAGE = DockerImageName.parse("quay.io/keycloak/keycloak:15.0.2");

    private static final int HTTP_PORT = 8080;
    private static final int HTTPS_PORT = 8443;

    public LocalKeycloak() {
        super(KEYCLOAK_IMAGE);
        withExposedPorts(HTTP_PORT, HTTPS_PORT);
        withEnv("KEYCLOAK_USER", "admin");
        withEnv("KEYCLOAK_PASSWORD", "admin");
        withEnv("KEYCLOAK_IMPORT", "/tmp/demo-local-realm.json");
        withCopyFileToContainer(forHostPath("keycloak/demo-local-realm.json"), "/tmp/demo-local-realm.json");
    }

    public String getHttpAuthUri() {
        String ip = getContainerIpAddress();
        int port = getMappedPort(HTTP_PORT);
        return String.format("http://%s:%d", ip, port);
    }

    public String getHttpsAuthUri() {
        String ip = getContainerIpAddress();
        int port = getMappedPort(HTTPS_PORT);
        return String.format("httpS://%s:%d", ip, port);
    }

}
