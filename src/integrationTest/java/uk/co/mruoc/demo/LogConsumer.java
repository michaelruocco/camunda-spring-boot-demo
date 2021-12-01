package uk.co.mruoc.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.testcontainers.containers.output.OutputFrame;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public class LogConsumer implements Consumer<OutputFrame> {

    private final String prefix;

    public LogConsumer() {
        this("");
    }

    @Override
    public void accept(OutputFrame frame) {
        log.info(prefix(removeNewline(frame.getUtf8String())));
    }

    private String prefix(String value) {
        if (StringUtils.isEmpty(prefix)) {
            return value;
        }
        return String.format("%s: %s", prefix, value);
    }

    private static String removeNewline(String value) {
        return value.replace("\n", "")
                .replace("\r", "");
    }

}
