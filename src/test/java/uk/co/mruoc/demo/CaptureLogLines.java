package uk.co.mruoc.demo;

import uk.org.webcompere.systemstubs.ThrowingRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static uk.org.webcompere.systemstubs.SystemStubs.tapSystemOut;

public class CaptureLogLines {

    private CaptureLogLines() {
        // utility class
    }

    public static List<String> captureLogLines(ThrowingRunnable statement) {
        try {
            String[] logLines = tapSystemOut(statement).split(System.lineSeparator());
            return Arrays.stream(logLines).collect(Collectors.toList());
        } catch (Exception e) {
            throw new LogOutputException(e);
        }
    }

    public static class LogOutputException extends RuntimeException {

        public LogOutputException(Throwable cause) {
            super(cause);
        }

    }

}
