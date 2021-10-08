package uk.co.mruoc.demo.adapter.camunda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.service.UpdateApproval;

@RequiredArgsConstructor
@Slf4j
public class CamundaUpdateApproval implements UpdateApproval {

    @Override
    public void update(Payment payment) {
        log.info("updating approval for payment {}", payment);
    }

}
