package uk.co.mruoc.demo.adapter.camunda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import uk.co.mruoc.demo.domain.service.SendExternalNotification;

@RequiredArgsConstructor
@Slf4j
public class SendExternalNotificationDelegate implements JavaDelegate {

    private final VariableExtractor extractor;
    private final SendExternalNotification sendExternalNotification;

    @Override
    public void execute(DelegateExecution execution) {
        String id = extractor.extractPaymentId(execution);
        sendExternalNotification.send(id);
    }

}
