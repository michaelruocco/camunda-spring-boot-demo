package uk.co.mruoc.demo;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;

import static org.assertj.core.api.Assertions.assertThatCode;

class ApplicationTest {

    @Test
    @SetSystemProperty(key = "server.port", value = "0")
    void applicationShouldStart() {
        assertThatCode(Application::main).doesNotThrowAnyException();
    }

}
