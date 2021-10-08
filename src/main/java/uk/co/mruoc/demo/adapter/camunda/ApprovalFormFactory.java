package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.spin.Spin;
import org.camunda.spin.json.SpinJsonNode;
import uk.co.mruoc.demo.domain.entity.Account;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.Product;

public class ApprovalFormFactory {

    public SpinJsonNode toApprovalForm(Payment payment) {
        SpinJsonNode json = Spin.JSON("{}");
        json.prop("container", toPayment(payment));
        return json;
    }

    private SpinJsonNode toPayment(Payment payment) {
        SpinJsonNode json = Spin.JSON("{}");
        json.prop("id", payment.getId());
        json.prop("product", toProduct(payment.getProduct()));
        json.prop("account", toAccount(payment.getAccount()));
        json.prop("riskScore", payment.getRiskScore());
        json.prop("quote", payment.getQuote());
        return json;
    }

    private SpinJsonNode toAccount(Account account) {
        SpinJsonNode form = Spin.JSON("{}");
        form.prop("id", account.getId());
        form.prop("owner", account.getOwner());
        return form;
    }

    private SpinJsonNode toProduct(Product product) {
        SpinJsonNode form = Spin.JSON("{}");
        form.prop("id", product.getId());
        form.prop("description", product.getDescription());
        form.prop("cost", product.getCost());
        return form;
    }

}
