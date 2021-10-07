package uk.co.mruoc.demo.adapter.camunda;

import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.service.RequestApproval;

@Slf4j
public class CamundaRequestApproval implements RequestApproval {

    @Override
    public void requestApproval(Payment payment) {
        log.info("requesting approval for payment {}", payment);
    }

}
