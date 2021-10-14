package uk.co.mruoc.demo.adapter.camunda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@RequiredArgsConstructor
@Slf4j
public class SendExternalNotification implements JavaDelegate {

    private final VariableExtractor extractor;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String id = extractor.extractPaymentId(execution);
        log.info("sending external notification {}", id);
        if (id.endsWith("9")) {
            throw new RuntimeException(String.format("failed to send external notification for %s", id));
        }
    }

}
