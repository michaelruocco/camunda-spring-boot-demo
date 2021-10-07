package uk.co.mruoc.demo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class ApplicationTest {

    @Test
    void applicationShouldStart() {
        assertThatCode(Application::main).doesNotThrowAnyException();
    }

}
