package com.timvero.example.admin.credit.filter;

import java.time.LocalDate;

public class CreditDataView implements Comparable<CreditDataView> {

    private final LocalDate date;
    private final LoanEventType type;
    private final Object data;

    public CreditDataView(LocalDate date, LoanEventType type, Object data) {
        super();
        this.date = date;
        this.type = type;
        this.data = data;
    }

    public LocalDate getDate() {
        return date;
    }

    public LoanEventType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    @Override
    public int compareTo(CreditDataView o) {
        int n = date.compareTo(o.date);
        if (n == 0) {
            n = type.compareTo(o.type);
        }
        return n;
    }
}
