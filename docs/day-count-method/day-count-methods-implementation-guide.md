# How to Implement a Day Count Method

## Interface

```java
public interface DayCountMethod {
    long countDays(LocalDate from, LocalDate to, PaymentGrid grid);
    BigFraction fractionOfYear(LocalDate from, LocalDate to, PaymentGrid grid);
}
```

- `from` — start date (inclusive), `to` — end date (exclusive)
- `grid` — payment schedule (see [PaymentGrid](#paymentgrid) below)

## How It's Used

Your method is not called directly. It's wrapped in `DayCounter`, which holds both the method and the grid:

```java
public class DayCounter {
    private final PaymentGrid grid;
    private final DayCountMethod method;

    public BigFraction count(LocalDate from, LocalDate to) {
        return method.fractionOfYear(from, to, grid);
    }
}
```

During accrual calculation, `DayCounter` is resolved per date range. Both `fractionOfYear()` (for interest amount) and `countDays()` (for reporting) end up in `AccrualRecord`.

## PaymentGrid

`PaymentGrid` represents the payment schedule — a series of equally spaced dates based on a period (monthly, quarterly, etc.) anchored to a maturity date.

**What it provides:**

| Method | What it does |
|--------|-------------|
| `get(n)` | Returns the n-th payment date (0 = basis date, negative = past) |
| `flooring(date)` | Index of the last payment date <= given date |
| `ceiling(date)` | Index of the first payment date >= given date |
| `lower(date)` / `higher(date)` | Same but strictly less / strictly greater |
| `getYearPeriodCount()` | How many periods per year (12 for monthly, 4 for quarterly, etc.) |
| `getPeriod()` | The period itself (`Period.ofMonths(1)`, etc.) |

**Who needs it:**

- **30/360 family, ACT/360, ACT/365F, ACT/366, NL/365** — don't use grid at all. The parameter is there but ignored.
- **ACT/ACT ICMA, ACT/ACT AFB** — heavily depend on grid. They use `flooring()`, `ceiling()`, `getYearPeriodCount()` to find coupon period boundaries and compute the fraction relative to the actual period length.
- **ACT/ACT ISDA** — doesn't use grid (computes based on calendar year boundaries).

## Implementation

**Package:** `com.timvero.scheduled.day_count`

**Naming:** `Method_` + name (e.g. `Method_30E_360`), with a `NAME` constant matching the Spring bean name.

### Example without grid: Method_30E_360

```java
@Component(Method_30E_360.NAME)
public class Method_30E_360 implements DayCountMethod {
    public static final String NAME = "30E_360";

    @Override
    public long countDays(LocalDate from, LocalDate to, PaymentGrid grid) {
        int y1 = from.getYear(), y2 = to.getYear();
        int m1 = from.getMonthValue(), m2 = to.getMonthValue();
        int d1 = from.getDayOfMonth(), d2 = to.getDayOfMonth();

        if (d1 == 31) d1 = 30;
        if (d2 == 31) d2 = 30;

        return (y2 - y1) * 360 + (m2 - m1) * 30 + (d2 - d1);
    }

    @Override
    public BigFraction fractionOfYear(LocalDate from, LocalDate to, PaymentGrid grid) {
        return new BigFraction(countDays(from, to, grid), 360L);
    }
}
```

### Example with grid: Method_ACT_ACT_ICMA

This method computes the year fraction relative to the actual coupon period length, so it needs the grid to find period boundaries.

```java
@Component(Method_ACT_ACT_ICMA.NAME)
public class Method_ACT_ACT_ICMA implements DayCountMethod {
    public static final String NAME = "ACT_ACT_ICMA";

    @Override
    public BigFraction fractionOfYear(LocalDate from, LocalDate to, PaymentGrid grid) {
        int startPayment = grid.flooring(from);
        int endPayment = grid.ceiling(to);
        LocalDate startDate = grid.get(startPayment);
        LocalDate endDate = grid.get(endPayment);

        return new BigFraction(endPayment - startPayment)
            .subtract(new BigFraction(
                DAYS.between(startDate, from),
                DAYS.between(startDate, grid.get(startPayment + 1))))
            .subtract(new BigFraction(
                DAYS.between(to, endDate),
                DAYS.between(grid.get(endPayment - 1), endDate)))
            .divide(grid.getYearPeriodCount());
    }

    @Override
    public long countDays(LocalDate from, LocalDate to, PaymentGrid grid) {
        return DAYS.between(from, to);
    }
}
```

## fractionOfYear denominator

| Family | Denominator |
|--------|-------------|
| 30/360 | 360 |
| ACT/360 | 360 |
| ACT/365F | 365 |
| ACT/ACT | varies (actual year length or period-based) |

## i18n

Add a display name in `web/src/main/resources/common_messages_en.properties`:

```properties
day.count.method.{NAME}={Display Name}
```

## Existing Methods

```
30_360_BB       — 30/360 Bond Basis
30E_360         — 30E/360
30E_360_ISDA    — 30E/360 ISDA
30EPlus_360     — 30E+/360
30U_360         — 30/360 US
ACT_360         — Actual/360
ACT_364         — Actual/364
ACT_365F        — Actual/365
ACT_365L        — Actual/365L
ACT_366         — Actual/366
ACT_ACT_AFB     — Actual/Actual AFB
ACT_ACT_ICMA    — Actual/Actual ICMA
ACT_ACT_ISDA    — Actual/Actual ISDA
NL_365          — No Leap/365 No Leap
```

## Verification

After implementation, your method appears in the Day Count Method dropdown on the Credit Product creation page (`/product/new`):

![Day Count Method Selector](./dayCountView.png)

