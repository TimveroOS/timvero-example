package com.timvero.example.admin.notification;

import com.timvero.structure.notification.execution.NotificationModel;
import java.time.LocalDate;

public class PaymentReminderTemplateModel extends NotificationModel {

    private PaymentReminderType reminderType;
    private int daysCount;
    private LocalDate expectedPaymentDate;

    public PaymentReminderTemplateModel() {
        this.reminderType = PaymentReminderType.UPCOMING;
        this.daysCount = 3;
    }

    public PaymentReminderType getReminderType() {
        return reminderType;
    }

    public void setReminderType(PaymentReminderType reminderType) {
        this.reminderType = reminderType != null ? reminderType : PaymentReminderType.UPCOMING;
    }

    public int getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
    }

    public LocalDate getExpectedPaymentDate() {
        return expectedPaymentDate;
    }

    public void setExpectedPaymentDate(LocalDate expectedPaymentDate) {
        this.expectedPaymentDate = expectedPaymentDate;
    }
}

/**
 * Enum representing different types of payment reminders.
 */
enum PaymentReminderType {
    UPCOMING,     // Payment due soon
    OVERDUE;      // Payment past due
}