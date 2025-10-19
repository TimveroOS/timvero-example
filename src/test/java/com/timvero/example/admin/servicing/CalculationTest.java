package com.timvero.example.admin.servicing;

import static com.timvero.servicing.engine.CreditCalculatorUtils.periodicInterest;

import com.timvero.base.entity.PumpkinBaseRepository;
import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.credit.CreditCalculationConfiguration;
import com.timvero.example.admin.credit.ExampleCreditType;
import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.offer.entity.ExampleSecuredOffer;
import com.timvero.example.admin.operation.charge.ChargeOperationService;
import com.timvero.example.admin.procuring.ExampleProcuringType;
import com.timvero.example.admin.product.engine.SimpleScheduledEngine;
import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.entity.ExampleCreditProductAdditive;
import com.timvero.example.admin.product.repository.ExampleCreditProductAdditiveRepository;
import com.timvero.example.admin.product.repository.ExampleCreditProductRepository;
import com.timvero.example.admin.scheduled.ExampleCreditCondition;
import com.timvero.ground.hibernate.TransactionTemplateBuilder;
import com.timvero.ground.hibernate.TransactionTemplateBuilderImpl;
import com.timvero.ground.util.MonetaryUtil;
import com.timvero.loan.engine.util.PaymentCalculator;
import com.timvero.scheduled.ScheduledConfiguration;
import com.timvero.scheduled.day_count.Method_30_360_BB;
import com.timvero.servicing.ServicingConfiguration;
import com.timvero.servicing.credit.entity.debt.Debt;
import com.timvero.servicing.credit.entity.operation.CreditPayment;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import com.timvero.servicing.engine.AccrualService;
import com.timvero.servicing.engine.CreditCalculationService;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Disabled
@DataJpaTest
@AutoConfigureEmbeddedDatabase(provider = DatabaseProvider.ZONKY)
@EnableTransactionManagement(proxyTargetClass = true)
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create",
    "spring.jpa.hibernate.naming.implicit-strategy=component-path",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect"})
@ContextConfiguration(classes = {CreditCalculationConfiguration.class, CalculationTest.CalculationTestConfig.class, ServicingConfiguration.class, ScheduledConfiguration.class})
@EnableJpaRepositories(basePackages = {
    "com.timvero.base.entity",
    "com.timvero.structure.calendar",
    "com.timvero.loan",

    "com.timvero.example.admin",
}, repositoryBaseClass = PumpkinBaseRepository.class)
@EnableJpaAuditing
@EntityScan(basePackages = {
    "com.timvero.base",
    "com.timvero.structure",
    "com.timvero.loan",
    "com.timvero.origination",
    "com.timvero.ground",
    "com.timvero.flowable",

    "com.timvero.example.admin",
})
public class CalculationTest {

    private static final LocalDate TODAY = LocalDate.of(2024, 1, 24);
    private static final CurrencyUnit ZWL = MonetaryUtil.getCurrency("ZWL");

    private static final BigDecimal INTEREST_RATE = BigDecimal.valueOf(12);
    private static final BigDecimal LATE_FEE_RATE = BigDecimal.valueOf(24);
    private static final BigDecimal PRINCIPAL = BigDecimal.valueOf(2_000_000);

    @Configuration
    public static class CalculationTestConfig {
        @Bean(name = "transactionManager")
        PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
            JpaTransactionManager transactionManager = new JpaTransactionManager();
            transactionManager.setEntityManagerFactory(emf);
            return transactionManager;
        }

        @Bean
        TransactionTemplateBuilder transactionTemplateBuilder(
            @Lazy PlatformTransactionManager transactionManager) {
            return new TransactionTemplateBuilderImpl(transactionManager) {};
        }
    }

    @Autowired
    private TransactionTemplateBuilder transactionTemplateBuilder;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ExampleCreditProductAdditiveRepository additiveRepository;
    @Autowired
    private ExampleCreditProductRepository productRepository;

    @Autowired
    private CreditCalculationService calculationService;
    @Autowired
    private AccrualService accrualService;
    @Autowired
    private ChargeOperationService chargeOperationService;

    @PostConstruct
    public void init() {
        ExampleCreditProduct creditProduct = new ExampleCreditProduct();
        creditProduct.setCode("1");
        creditProduct.setCreditType(ExampleCreditType.PERSONAL);
        creditProduct.setEngineName(SimpleScheduledEngine.NAME);
        creditProduct.setTitle("Test credit product");
        creditProduct.setUuidContractTemplate(UUID.randomUUID());
        creditProduct.setMinAmount(PRINCIPAL);
        creditProduct.setMaxAmount(LATE_FEE_RATE);
        creditProduct.setMinTerm(1);
        creditProduct.setMaxTerm(12);
        creditProduct.setCurrency(ZWL);
        creditProduct.setLateFeeRate(INTEREST_RATE.multiply(BigDecimal.TWO));
        productRepository.save(creditProduct);

        ExampleCreditProductAdditive productAdditive = new ExampleCreditProductAdditive();
        productAdditive.setProcuringType(ExampleProcuringType.PENALTY);
        productAdditive.setProduct(creditProduct);
        productAdditive.setName("Test additive #1");
        productAdditive.setMaxAmount(creditProduct.getMaxAmount());
        productAdditive.setMinAmount(creditProduct.getMinAmount());
        productAdditive.setMaxTerm(creditProduct.getMaxTerm());
        productAdditive.setMinTerm(creditProduct.getMinTerm());
        productAdditive.setInterestRate(INTEREST_RATE);
        additiveRepository.save(productAdditive);
    }

    @Test
    public void testEmpty() {
        LocalDate startDate = TODAY.minusDays(8);

        UUID creditId = initCredit(startDate, TODAY);
        calculate(creditId, startDate, TODAY);
        ExampleCredit credit = entityManager.find(ExampleCredit.class, creditId);

        Assertions.assertEquals(startDate, credit.getActualSnapshot().getDate());
        Assertions.assertEquals(TODAY, credit.getCalculationDate());
        Assertions.assertTrue(credit.getActualSnapshot().getDebt().getTotal().isEmpty());

    }

    @Test
    public void chargeOperation() {
        LocalDate startDate = TODAY.minusDays(8);
        LocalDate chargeDate = startDate.plusDays(3);
        MonetaryAmount principal = MonetaryUtil.of(PRINCIPAL, ZWL);

        UUID creditId = initCredit(startDate, TODAY);
        charge(creditId, chargeDate, MonetaryUtil.of(PRINCIPAL, ZWL));
        calculate(creditId, startDate, TODAY);
        ExampleCredit credit = entityManager.find(ExampleCredit.class, creditId);

        Assertions.assertEquals(chargeDate, credit.getActualSnapshot().getDate());
        Assertions.assertEquals(TODAY, credit.getCalculationDate());
        Assertions.assertEquals(principal, credit.getActualSnapshot().getDebt().getAccount(CreditCalculationConfiguration.PRINCIPAL).get());

        MonetaryAmount interest = principal.multiply((INTEREST_RATE.doubleValue() / 100d) * (ChronoUnit.DAYS.between(chargeDate, TODAY) / 360d));

        Debt debt = accrualService.calculateCurrentAccurals(credit);
        Assertions.assertEquals(1, debt.getAccounts().keySet().size());
        Assertions.assertEquals(interest, debt.getAccount(CreditCalculationConfiguration.INTEREST).get());
    }

    @Test
    public void paymentOperation1() {
        LocalDate startDate = TODAY.minusDays(8);

        LocalDate chargeDate = startDate.plusDays(3);
        MonetaryAmount principal = MonetaryUtil.of(PRINCIPAL, ZWL);

        LocalDate paymentDate = startDate.plusDays(7);
        MonetaryAmount unpaidInterest = MonetaryUtil.of(BigDecimal.valueOf(1_000), ZWL);
        MonetaryAmount paymentAmount = principal.multiply((INTEREST_RATE.doubleValue() / 100d) * (ChronoUnit.DAYS.between(chargeDate, paymentDate) / 360d))
            .subtract(unpaidInterest);

        UUID creditId = initCredit(startDate, TODAY);
        charge(creditId, chargeDate, MonetaryUtil.of(PRINCIPAL, ZWL));
        registerPayment(creditId, paymentDate, paymentAmount);
        calculate(creditId, startDate, TODAY);
        ExampleCredit credit = entityManager.find(ExampleCredit.class, creditId);

        Assertions.assertEquals(paymentDate, credit.getActualSnapshot().getDate());
        Assertions.assertEquals(TODAY, credit.getCalculationDate());
        Assertions.assertEquals(principal, credit.getActualSnapshot().getDebt().getAccount(CreditCalculationConfiguration.PRINCIPAL).get());

        CreditPayment creditPayment = credit.getOperations(CreditPayment.class, OperationStatus.APPROVED).findAny().get();
        Assertions.assertEquals(paymentAmount.negate(), creditPayment.getFinalDebt().get().getAccount(CreditCalculationConfiguration.INTEREST).get());
        Assertions.assertEquals(unpaidInterest, credit.getActualSnapshot().getDebt().getAccount(CreditCalculationConfiguration.INTEREST).get());

        MonetaryAmount interest = principal
            .multiply((INTEREST_RATE.doubleValue() / 100d) * (ChronoUnit.DAYS.between(chargeDate, TODAY) / 360d))
            .subtract(paymentAmount).subtract(unpaidInterest);

        Debt debt = accrualService.calculateCurrentAccurals(credit);
        Assertions.assertEquals(1, debt.getAccounts().keySet().size());
        Assertions.assertEquals(interest, debt.getAccount(CreditCalculationConfiguration.INTEREST).get());
    }

    @Test
    public void paymentOperation2() {
        LocalDate startDate = TODAY.minusDays(8);

        LocalDate chargeDate = startDate.plusDays(3);
        MonetaryAmount principal = MonetaryUtil.of(PRINCIPAL, ZWL);

        LocalDate paymentDate = startDate.plusDays(7);
        MonetaryAmount paymentInterest = principal.multiply((INTEREST_RATE.doubleValue() / 100d) * (ChronoUnit.DAYS.between(chargeDate, paymentDate) / 360d));
        MonetaryAmount paymentPrincipal = MonetaryUtil.of(BigDecimal.valueOf(2_000), ZWL);
        MonetaryAmount paymentAmount = paymentInterest.add(paymentPrincipal) ;

        UUID creditId = initCredit(startDate, TODAY);
        charge(creditId, chargeDate, MonetaryUtil.of(PRINCIPAL, ZWL));
        registerPayment(creditId, paymentDate, paymentAmount);
        calculate(creditId, startDate, TODAY);
        ExampleCredit credit = entityManager.find(ExampleCredit.class, creditId);

        Assertions.assertEquals(paymentDate, credit.getActualSnapshot().getDate());
        Assertions.assertEquals(TODAY, credit.getCalculationDate());
        Assertions.assertEquals(principal.subtract(paymentPrincipal), credit.getActualSnapshot().getDebt().getAccount(CreditCalculationConfiguration.PRINCIPAL).get());

        CreditPayment creditPayment = credit.getOperations(CreditPayment.class, OperationStatus.APPROVED).findAny().get();
        Assertions.assertEquals(paymentInterest.negate(), creditPayment.getFinalDebt().get().getAccount(CreditCalculationConfiguration.INTEREST).get());
        Assertions.assertEquals(paymentPrincipal.negate(), creditPayment.getFinalDebt().get().getAccount(CreditCalculationConfiguration.PRINCIPAL).get());

        MonetaryAmount interest = principal.subtract(paymentPrincipal).multiply((INTEREST_RATE.doubleValue() / 100d) * (ChronoUnit.DAYS.between(paymentDate, TODAY) / 360d));

        Debt debt = accrualService.calculateCurrentAccurals(credit);
        Assertions.assertEquals(1, debt.getAccounts().keySet().size());
        Assertions.assertEquals(interest, debt.getAccount(CreditCalculationConfiguration.INTEREST).get());
    }

    @Test
    public void pastDue1() {
        LocalDate startDate = TODAY.minusDays(8);

        LocalDate chargeDate = startDate.plusDays(3);
        MonetaryAmount principal = MonetaryUtil.of(PRINCIPAL, ZWL);

        LocalDate paymentDate = startDate.plusDays(7);
        MonetaryAmount unpaidInterest = MonetaryUtil.of(BigDecimal.valueOf(1_000), ZWL);
        MonetaryAmount paymentAmount = principal.multiply((INTEREST_RATE.doubleValue() / 100d) * (ChronoUnit.DAYS.between(chargeDate, paymentDate) / 360d))
            .subtract(unpaidInterest);

        UUID creditId = initCredit(startDate, TODAY);
        charge(creditId, chargeDate, MonetaryUtil.of(PRINCIPAL, ZWL));
        registerPayment(creditId, paymentDate, paymentAmount);
        calculate(creditId, startDate, TODAY.plusMonths(1));
        ExampleCredit credit = entityManager.find(ExampleCredit.class, creditId);

        LocalDate expectedPaymentDate = startDate.plusMonths(1);
        MonetaryAmount pastDueAmount = credit.getCondition().getRegularPayment().subtract(paymentAmount);
        MonetaryAmount pastDueInterest = principal.multiply((INTEREST_RATE.doubleValue() / 100d) * ((30 - 3) / 360d)).subtract(paymentAmount);
        MonetaryAmount pastDuePrincipal = pastDueAmount.subtract(pastDueInterest);

        Assertions.assertEquals(expectedPaymentDate, credit.getActualSnapshot().getDate());
        Assertions.assertEquals(TODAY.plusMonths(1), credit.getCalculationDate());
        Assertions.assertEquals(pastDueInterest, credit.getActualSnapshot().getDebt().getAccount(CreditCalculationConfiguration.PAST_DUE_INTEREST).get());
        Assertions.assertEquals(pastDuePrincipal, credit.getActualSnapshot().getDebt().getAccount(CreditCalculationConfiguration.PAST_DUE_PRINCIPAL).get());

        MonetaryAmount lateFeeAmount = pastDueAmount.multiply((LATE_FEE_RATE.doubleValue() / 100d) * (ChronoUnit.DAYS.between(expectedPaymentDate, credit.getCalculationDate()) / 360d));
        Debt debt = accrualService.calculateCurrentAccurals(credit);
        Assertions.assertEquals(2, debt.getAccounts().keySet().size());
        Assertions.assertEquals(lateFeeAmount, debt.getAccount(CreditCalculationConfiguration.LATE_FEE).get());
    }

    public UUID initCredit(LocalDate startDate, LocalDate today) {
        UUID creditId = transactionTemplateBuilder.requiresNew().execute(s -> {

            ExampleCreditProduct product = productRepository.findAll().get(0);
            ExampleCreditProductAdditive additive = additiveRepository.findAll().get(0);

            MonetaryAmount principal = MonetaryUtil.of(additive.getMinAmount(), product.getCurrency());
            String engineName = product.getEngineName();
            BigDecimal interestRate = additive.getInterestRate();
            BigDecimal lateFeeRate = product.getLateFeeRate();
            String dayCountMethod = Method_30_360_BB.NAME;
            Period period = Period.ofMonths(1);
            Integer term = 12;
            MonetaryAmount regularPayment = PaymentCalculator.calcAnnuityPayment(principal,
                MonetaryUtil.zero(principal.getCurrency()), periodicInterest(period, interestRate), term, 0);
            ExampleSecuredOffer securedOffer = null;
            ExampleCreditCondition condition = new ExampleCreditCondition(principal, engineName, interestRate, lateFeeRate, dayCountMethod, period, term, regularPayment, securedOffer);
            entityManager.persist(condition);

            Application application = new Application();
            application.setCondition(condition);
            entityManager.persist(application);

            ExampleCredit credit = new ExampleCredit();
            credit.setApplication(application);
            credit.setCondition(condition);
            credit.setStartDate(startDate);
            entityManager.persist(credit);

            return credit.getId();
        });
        calculate(creditId, startDate, today);
        return creditId;
    }

    public void calculate(UUID creditId, LocalDate from, LocalDate today) {
        transactionTemplateBuilder.requiresNew().executeWithoutResult(status -> {
            calculationService.calculate(creditId, from, today);
        });
    }

    public void charge(UUID creditId, LocalDate operationDate, MonetaryAmount amount) {
        transactionTemplateBuilder.requiresNew().executeWithoutResult(status -> {
            chargeOperationService.createOperation(creditId, operationDate, amount);
        });
    }

    public void registerPayment(UUID creditId, LocalDate paymentDate, MonetaryAmount amount) {
        transactionTemplateBuilder.requiresNew().executeWithoutResult(status -> {
            entityManager.find(ExampleCredit.class, creditId).getOperations().add(new CreditPayment(paymentDate, OperationStatus.APPROVED, amount, CreditCalculationConfiguration.GENERAL));
        });
    }

}
