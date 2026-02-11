# How to Implement a Scheduled Engine

## What is a Scheduled Engine?

A Scheduled Engine is a component that calculates payment schedules for loans and credits. It determines when payments are due, how much to pay, and how interest accrues over time.

**Why do we need this?** Different loan products have different repayment structures:
- Fixed monthly payments (consumer loans, auto loans)
- Interest-only payments with balloon payment
- Graduated payment schedules
- Different interest calculation methods

Each loan product needs its own calculation logic!

---

## Step-by-Step Guide: Creating Your Own Scheduled Engine

### Prerequisites

Before you begin, make sure you understand:
- How your loan product works (amortization method)
- Payment frequency and schedule
- Interest calculation rules
- How payments are distributed between principal and interest

---

## Step 1: Understand the Interface

All scheduled engines implement the `ScheduledEngine<T>` interface:

```java
public interface ScheduledEngine<T extends CreditCondition> {
    Class<T> getConditionClass();
    List<PaymentSegment> payments(T condition, LocalDate interestStart,
        LocalDate paymentStart, MonetaryAmount principal, MonetaryAmount interest);
}
```

### What does each method do?

| Method | What | Why | Returns |
|--------|------|-----|---------|
| `getConditionClass()` | Returns condition class type | For Spring automatic wiring | `Class<T>` - your condition class |
| `payments()` | Calculates payment schedule | Core logic for generating payments | `List<PaymentSegment>` - all payments |

### Parameters explanation:

- **`condition`** - loan terms (amount, rate, term, frequency, etc.)
- **`interestStart`** - date when interest accrual begins
- **`paymentStart`** - date of first payment
- **`principal`** - outstanding loan amount
- **`interest`** - accrued interest not yet paid

---

## Step 2: Create Your Condition Class

Create a class that holds all loan terms:

```java
@Entity
@Table(name = "your_credit_condition")
public class YourCreditCondition extends UUIDPersistable implements CreditCondition {

    @Embedded
    private MonetaryAmount principal;        // Loan amount

    @Column(nullable = false)
    private String engineName;               // Your engine's NAME

    @Column(nullable = false)
    private BigDecimal interestRate;         // Annual rate (0.12 = 12%)

    @Column(nullable = false)
    private Period period;                   // Payment frequency

    @Column(nullable = false)
    private Integer term;                    // Number of payments

    @Embedded
    private MonetaryAmount regularPayment;   // Fixed payment amount

    // Getters and setters...
}
```

### Key Fields:

- **`principal`** - Initial loan amount
- **`engineName`** - Must match your engine's NAME constant
- **`interestRate`** - Annual rate as decimal (12% = 0.12)
- **`period`** - `Period.ofMonths(1)` for monthly payments
- **`term`** - Total number of payments
- **`regularPayment`** - Fixed payment amount per period

---

## Step 3: Create Your Engine Class

Create a new class implementing `ScheduledEngine`:

```java
package com.timvero.example.admin.product.engine;

import com.timvero.loan.engine.scheduled.ScheduledEngine;
import com.timvero.scheduled.day_count.DayCountMethod;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(YourScheduledEngine.NAME)   // ← needed for Spring (automatic registration)
public class YourScheduledEngine implements ScheduledEngine<YourCreditCondition> {

    public static final String NAME = "YourScheduledEngine"; // ← unique name for your engine

    @Autowired
    protected Map<String, DayCountMethod> dayCountMethods;

    @Override
    public Class<YourCreditCondition> getConditionClass() {
        return YourCreditCondition.class;
    }

    @Override
    public List<PaymentSegment> payments(YourCreditCondition condition,
        LocalDate interestStart, LocalDate paymentStart,
        MonetaryAmount principal, MonetaryAmount interest) {

        // Get day count method from credit product
        DayCountMethod dayCountMethod = dayCountMethods.get(
            condition.getSecuredOffer()
                     .getOriginalOffer()
                     .getCreditProduct()
                     .getDayCountMethod()
        );

        // Calculate payment schedule
        payments(condition.getRegularPayment(), condition.getInterestRate(),
            condition.getPeriod(), principal, interest, interestStart, paymentStart,
            condition.getTerm(), dayCountMethod);
    }
}
```

### Naming Convention:

- **Class name**: Descriptive name + `ScheduledEngine` (e.g., `SimpleScheduledEngine`)
- **NAME constant**: Unique identifier for Spring
- **Service annotation**: `@Service(NAME)` registers as Spring bean

---

## Step 4: Implement Payment Calculation Logic

This is where you write the payment schedule generation.

### Example: SimpleScheduledEngine

Let's break down a real example — **SimpleScheduledEngine** (equal payment loan):

```java
private List<PaymentSegment> payments(
    MonetaryAmount regularPayment,     // Fixed payment amount
    BigDecimal annualInterest,         // Annual interest rate
    Period period,                     // Payment frequency
    MonetaryAmount principal,          // Loan amount
    MonetaryAmount interest,           // Accrued interest
    LocalDate startInterest,           // Interest start date
    LocalDate payStarting,             // Payment start date
    int length,                        // Number of payments
    DayCountMethod dayCountMethod      // Day count method
) {

    // STEP 1: Calculate maturity date
    LocalDate maturityDate = startInterest.plus(period.multipliedBy(length));

    // STEP 2: Create payment grid and day counter
    PaymentGrid paymentGrid = new PaymentGrid(
        maturityDate,
        startInterest.getDayOfMonth(),
        period
    );
    DayCounter dayCounter = new DayCounter(paymentGrid, dayCountMethod);

    // STEP 3: Generate payments
    List<PaymentSegment> payments = new ArrayList<>();
    LocalDate from = startInterest, to;

    for (int order = 1; order <= length; order++) {

        // Calculate payment date
        to = startInterest.plus(period.multipliedBy(order));

        // STEP 4: Accrue interest for period
        if (from.isBefore(to)) {
            BigFraction fraction = dayCounter.count(from, to);
            MonetaryAmount increment = calcRangeAccural(
                principal, annualInterest, fraction
            );
            interest = interest.add(increment);
        }

        // STEP 5: Distribute payment
        MonetaryAmount principalPayment;
        MonetaryAmount interestPayment;

        if (order < length) {
            // Regular payment: interest first, then principal
            interestPayment = Lang.min(regularPayment, interest);
            principalPayment = Lang.min(
                principal,
                regularPayment.subtract(interestPayment)
            );
        } else {
            // Final payment: pay everything
            interestPayment = interest;
            principalPayment = principal;
        }

        // STEP 6: Update balances
        principal = principal.subtract(principalPayment);
        interest = interest.subtract(interestPayment);

        // STEP 7: Create payment segment
        PaymentSegment payment = new PaymentSegment(
            to, principalPayment, interestPayment, principal, interest
        );
        payments.add(payment);

        from = Lang.max(to, from);

        // Stop if loan is paid off
        if (principal.isNegativeOrZero()) {
            break;
        }
    }

    return payments;
}
```

### Breaking Down the Algorithm:

**Step 1: Calculate Maturity Date**
```java
LocalDate maturityDate = startInterest.plus(period.multipliedBy(length));
```
- Example: Start = 2024-01-01, Period = 1 month, Length = 12
- Maturity = 2025-01-01

**Step 2: Create Payment Grid**
```java
PaymentGrid paymentGrid = new PaymentGrid(maturityDate, startDay, period);
DayCounter dayCounter = new DayCounter(paymentGrid, dayCountMethod);
```
- PaymentGrid: Defines regular payment dates
- DayCounter: Calculates time fractions for interest

**Step 4: Accrue Interest**
```java
BigFraction fraction = dayCounter.count(from, to);  // e.g., 30/360
MonetaryAmount increment = calcRangeAccural(principal, annualInterest, fraction);
interest = interest.add(increment);
```
- Formula: `Interest = Principal × AnnualRate × TimeFraction`
- Example: $10,000 × 12% × (30/360) = $100

**Step 5: Distribute Payment**
```java
// Interest paid first (standard amortization)
interestPayment = min(regularPayment, interest);
principalPayment = min(principal, regularPayment - interestPayment);
```

---

## Step 5: Add Spring Service Annotation

```java
@Service(SimpleScheduledEngine.NAME)  // ← registers bean with name
public class SimpleScheduledEngine implements ScheduledEngine<ExampleCreditCondition> {
    public static final String NAME = "SimpleScheduledEngine";
```

### Why do we need this?

1. **Automatic registration** — Spring finds and registers your engine
2. **Dependency Injection** — engine can be retrieved by name
3. **Runtime selection** — engine selected based on credit product configuration

### How to use it later:

```java
@Autowired
@Qualifier("SimpleScheduledEngine")  // ← use the NAME constant
private ScheduledEngine<ExampleCreditCondition> scheduledEngine;
```

---

## Step 6: Add Internationalization (i18n)

Add a human-readable name in `common_messages_en.properties`:

**Location:** `web/src/main/resources/common_messages_en.properties`

```properties
#------------------------------- ScheduledEngine -------------------------------------
scheduled.engine.SimpleScheduledEngine=Simple Equal Payment Schedule
scheduled.engine.YourScheduledEngine=Your Engine Display Name
```

### Pattern:

```
scheduled.engine.{NAME}={Display Name}
```

Where:
- `{NAME}` — your NAME constant from the engine class
- `{Display Name}` — user-friendly name shown in the UI

---

## Step 7: Verify Your Implementation in UI

After implementing your Scheduled Engine, verify it appears in the Credit Product creation interface.

### Where to Find It

Navigate to the Credit Product creation page:
```
https://your-domain.com/product/new
```

### What to Look For

Your newly created Scheduled Engine should appear in the "Scheduled Engine" dropdown selector, displaying the localized name you defined in the `common_messages_en.properties` file.

**Screenshot:**

![Engine Name Selector](./scheduleEngine.png)


*The screenshot above shows the Engine name dropdown in the Credit Product creation form. Your method will appear alongside existing options like "Simple Equal Payment Schedule", etc.*

---

## Complete Example: SimpleScheduledEngine

Here's the complete, annotated implementation:

```java
package com.timvero.example.admin.product.engine;

import static com.timvero.servicing.engine.CreditCalculatorUtils.calcRangeAccural;

import com.timvero.example.admin.scheduled.ExampleCreditCondition;
import com.timvero.ground.util.Lang;
import com.timvero.loan.engine.scheduled.ScheduledEngine;
import com.timvero.scheduled.day_count.DayCountMethod;
import com.timvero.scheduled.day_count.DayCounter;
import com.timvero.scheduled.day_count.PaymentGrid;
import com.timvero.scheduled.entity.PaymentSegment;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.money.MonetaryAmount;
import org.apache.commons.math3.fraction.BigFraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Simple Scheduled Engine for equal payment loans.
 *
 * This engine calculates payment schedules for loans with:
 * - Fixed regular payment amount
 * - Interest paid first, then principal (standard amortization)
 * - Early payoff if payments exceed remaining balance
 *
 * Key characteristics:
 * - Each payment has the same amount (regularPayment)
 * - Interest accrues based on current principal balance
 * - Remaining payment after interest goes to principal
 * - Loan pays off early if regularPayment is large enough
 *
 * Commonly used for:
 * - Consumer loans
 * - Personal loans
 * - Auto loans with fixed payments
 */
@Service(SimpleScheduledEngine.NAME)
public class SimpleScheduledEngine implements ScheduledEngine<ExampleCreditCondition> {

    public static final String NAME = "SimpleScheduledEngine";

    @Autowired
    protected Map<String, DayCountMethod> dayCountMethods;

    @Override
    public Class<ExampleCreditCondition> getConditionClass() {
        return ExampleCreditCondition.class;
    }

    /**
     * Generates payment schedule for the loan.
     *
     * @param condition Credit terms and conditions
     * @param interestStart Date when interest accrual begins
     * @param paymentStart Date of first payment
     * @param principal Initial loan amount
     * @param interest Previously accrued unpaid interest
     * @return List of payment segments representing the full schedule
     */
    @Override
    public List<PaymentSegment> payments(ExampleCreditCondition condition, LocalDate interestStart,
        LocalDate paymentStart, MonetaryAmount principal, MonetaryAmount interest) {

        // Get the day count method from credit product configuration
        DayCountMethod dayCountMethod = dayCountMethods
            .get(condition.getSecuredOffer().getOriginalOffer().getCreditProduct().getDayCountMethod());

        return payments(
            condition.getRegularPayment(),
            condition.getInterestRate(),
            condition.getPeriod(),
            principal,
            interest,
            interestStart,
            paymentStart,
            condition.getTerm(),
            dayCountMethod
        );
    }

    /**
     * Core payment calculation algorithm.
     *
     * This method implements simple equal payment amortization:
     * 1. Calculate interest for each period based on current principal
     * 2. Apply payment: interest first, then principal
     * 3. Track remaining balances
     * 4. Stop early if loan is paid off
     *
     * @param regularPayment Fixed payment amount per period
     * @param annualInterest Annual interest rate as decimal (e.g., 0.12 for 12%)
     * @param period Payment frequency (monthly, bi-weekly, etc.)
     * @param principal Remaining principal balance
     * @param interest Accrued unpaid interest
     * @param startInterest Interest accrual start date
     * @param payStarting First payment date (currently unused)
     * @param length Number of scheduled payments
     * @param dayCountMethod Day count convention for interest calculation
     * @return List of payment segments
     */
    private List<PaymentSegment> payments(MonetaryAmount regularPayment, BigDecimal annualInterest,
        Period period, MonetaryAmount principal, MonetaryAmount interest,
        LocalDate startInterest, LocalDate payStarting, int length, DayCountMethod dayCountMethod) {

        // Calculate final payment date (maturity)
        LocalDate maturityDate = startInterest.plus(period.multipliedBy(length));

        // Create payment grid to manage regular payment dates
        final PaymentGrid paymentGrid = new PaymentGrid(maturityDate, startInterest.getDayOfMonth(), period);

        // Create day counter for interest accrual calculation
        final DayCounter dayCounter = new DayCounter(paymentGrid, dayCountMethod);

        List<PaymentSegment> payments = new ArrayList<>();

        // Track period boundaries for interest calculation
        LocalDate from = startInterest, to;

        // Generate each payment
        for (int order = 1; order <= length; order++) {

            MonetaryAmount principalPayment;
            MonetaryAmount interestPayment;

            // Calculate this payment's due date
            to = startInterest.plus(period.multipliedBy(order));

            // Accrue interest for the period between payments
            if (from.isBefore(to)) {
                // Calculate time fraction for this period
                BigFraction fraction = dayCounter.count(from, to);

                // Calculate interest: Principal × AnnualRate × TimeFraction
                MonetaryAmount increment = calcRangeAccural(principal, annualInterest, fraction);

                // Add accrued interest to total unpaid interest
                interest = interest.add(increment);
            }

            // Distribute the regular payment between interest and principal
            if (order < length) {
                // Regular payment: interest first, remainder to principal
                interestPayment = Lang.min(regularPayment, interest);
                principalPayment = Lang.min(principal, regularPayment.subtract(interestPayment));
            } else {
                // Final payment: pay all remaining balances
                interestPayment = interest;
                principalPayment = principal;
            }

            // Update remaining balances
            principal = principal.subtract(principalPayment);
            interest = interest.subtract(interestPayment);

            // Create payment segment with all payment details
            PaymentSegment payment = new PaymentSegment(to, principalPayment, interestPayment, principal, interest);
            payments.add(payment);

            // Move to next period
            from = Lang.max(to, from);

            // Stop generating payments if loan is fully paid off
            if (principal.isNegativeOrZero()) {
                break;
            }
        }

        return payments;
    }
}
```

---

## Checklist: Before You Submit

- [ ] Created condition class implementing `CreditCondition`
- [ ] Created engine class implementing `ScheduledEngine<T>`
- [ ] Added `@Service` annotation with unique NAME
- [ ] Implemented `getConditionClass()` method
- [ ] Implemented `payments()` method with calculation logic
- [ ] Added i18n entry in `common_messages_en.properties`
- [ ] Tested with various scenarios (different terms, rates, amounts)
- [ ] Tested edge cases (early payoff, zero interest, large payments)

---

## Common Pitfalls

### ❌ Mistake 1: Forgetting to inject DayCountMethods

```java
// WRONG - DayCountMethods not available
public class YourScheduledEngine implements ScheduledEngine<YourCondition> {
    // No @Autowired Map<String, DayCountMethod>
}

// CORRECT
@Autowired
protected Map<String, DayCountMethod> dayCountMethods;
```

### ❌ Mistake 2: Not handling early payoff

```java
// WRONG - continues generating payments even when balance is zero
for (int order = 1; order <= length; order++) {
    // ... payment logic
}

// CORRECT - stops when loan is paid off
for (int order = 1; order <= length; order++) {
    // ... payment logic
    if (principal.isNegativeOrZero()) {
        break;
    }
}
```

### ❌ Mistake 3: Incorrect payment distribution

```java
// WRONG - principal paid before interest
principalPayment = min(regularPayment, principal);
interestPayment = min(interest, regularPayment.subtract(principalPayment));

// CORRECT - interest paid first (standard amortization)
interestPayment = min(regularPayment, interest);
principalPayment = min(principal, regularPayment.subtract(interestPayment));
```

### ❌ Mistake 4: Not accruing interest

```java
// WRONG - no interest accrual between payments
MonetaryAmount interestPayment = interest;

// CORRECT - accrue interest for the period
BigFraction fraction = dayCounter.count(from, to);
MonetaryAmount increment = calcRangeAccural(principal, annualInterest, fraction);
interest = interest.add(increment);
```

---

## Where to Find More Information

- **Existing engines**: `src/main/java/com/timvero/example/admin/product/engine/`
- **Interface definition**: Search for `ScheduledEngine` interface
- **Payment segment**: `com.timvero.scheduled.entity.PaymentSegment`
- **Day count methods**: See `day-count-methods-implementation-guide.md`
- **Credit calculation**: `CreditCalculatorUtils.calcRangeAccural()`

---

## Need Help?

If you're stuck:
1. Look at `SimpleScheduledEngine` for a working example
2. Check the unit tests to see how engines are used
3. Review the `DayCountMethod` documentation for interest calculations
4. Examine `PaymentGrid` and `DayCounter` classes for date/time handling

---

*Good luck with your implementation! 🚀*

*Last Updated: 2026-02-11*
