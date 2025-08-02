package com.timvero.example.admin.notification;

import com.timvero.loan.decline_reason.entity.DeclineReason;
import com.timvero.structure.notification.execution.NotificationModel;

public class LoanDeclineTemplateModel extends NotificationModel {

    private DeclineReason declineReason;
    private boolean reapplicationEligible;

    public DeclineReason getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(DeclineReason declineReason) {
        this.declineReason = declineReason;
    }

    public boolean isReapplicationEligible() {
        return reapplicationEligible;
    }

    public void setReapplicationEligible(boolean reapplicationEligible) {
        this.reapplicationEligible = reapplicationEligible;
    }

    public Integer getReapplicationWaitDays() {
        return reapplicationEligible ? 30 : 180;
    }
}
