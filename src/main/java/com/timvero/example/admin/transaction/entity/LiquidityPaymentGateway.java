package com.timvero.example.admin.transaction.entity;

import com.timvero.transfer.method.entity.PaymentMethod;
import com.timvero.transfer.transaction.gateway.PaymentGateway;
import com.timvero.transfer.transaction.gateway.TransactionResult;
import java.io.IOException;
import javax.money.MonetaryAmount;
import org.springframework.stereotype.Component;

@Component
public class LiquidityPaymentGateway implements PaymentGateway {

    public static final String GATEWAY_TYPE = "XMPL";

    @Override
    public String getMethodType() {
        return GATEWAY_TYPE;
    }

    @Override
    public String getName() {
        return "Example payment gateway";
    }

    @Override
    public boolean verify(PaymentMethod method) throws IOException {
        return true;
    }

    @Override
    public TransactionResult proceedIncoming(String orderId, PaymentMethod method, MonetaryAmount amount)
        throws IOException {
        return process(orderId, amount);
    }

    @Override
    public TransactionResult proceedOutgoing(String orderId, PaymentMethod method, MonetaryAmount amount)
        throws IOException {
        return process(orderId, amount);
    }

    private TransactionResult process(String orderId, MonetaryAmount amount) {
        return new TransactionResult(orderId.toString() + "-" + System.currentTimeMillis(), amount,
            TransactionResult.Status.SUCCESS, false, "xmpl");
    }
}
