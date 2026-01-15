package com.timvero.example.admin.payment.label;

import com.timvero.servicing.credit.entity.operation.CreditPayment;
import com.timvero.servicing.credit.label.OperationPaymentStatusLabel;
import org.springframework.stereotype.Component;

@Component
public class CreditPaymentStatusLabel extends OperationPaymentStatusLabel<CreditPayment>{

}
