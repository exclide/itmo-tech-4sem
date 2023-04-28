package is.tech.accounts;

import lombok.Data;
import is.tech.commands.DepositTransaction;
import is.tech.commands.Transaction;
import is.tech.entities.Bank;
import is.tech.entities.Client;
import is.tech.exceptions.BankException;
import is.tech.interfaces.Observer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Базовый класс аккаунта, есть методы для транзацкий и начисления процентов
 */
@Data
public abstract class Account implements Observer<LocalDate> {
    /** Поле клиента, прикрепленного к счету */
    private final Client client;
    /** Поле банка, прикрепленного к счету */
    private final Bank bank;
    private final int id;
    /** Список транзацкий для возможной отмены */
    private List<Transaction> transactionHistory = new ArrayList<>();
    /** Баланс аккаунта */
    private long balance;
    /** Количество накопленных денег после процентов, начисляется в конце месяца уже на баланс */
    private long interestAmount;
    /** Проценты и ограничения на аккаунт в зависимости от его вида */
    private AccountLimits accountLimits;
    /** Срок годности аккаунта */
    private static final int yearsTillValid = 2;
    /** Дата создания */
    private LocalDate creationDate;
    /** Дата последнего начисления interestAmount */
    private LocalDate lastInterest;

    /**
     * Конструктор класса
     * @param client клиент счета
     * @param bank банк счета
     * @param id идентиф.
     * @param creationDate дата создания
     */
    public Account(Client client, Bank bank, int id, LocalDate creationDate) {
        balance = 0;
        interestAmount = 0;
        this.client = client;
        this.bank = bank;
        this.id = id;
        this.creationDate = creationDate;
        this.lastInterest = creationDate;


    }

    /**
     * Считает, истек ли срок действия счета
     * @return счет истек/нет
     */
    public boolean isExpired() {
        long daysSinceCreation = DAYS.between(creationDate, lastInterest);
        return daysSinceCreation > 365 * yearsTillValid;
    }

    /**
     * Выполняет переданную транзакцию
     * @param transaction любой тип транзакции
     */
    public void makeTransaction(Transaction transaction) {
        transaction.run();
        transactionHistory.add(transaction);
    }

    /**
     * Отменяет выбранную транзакцию
     * @param transaction любой тип транзакции (должен быть в истории)
     */
    public void revertTransaction(Transaction transaction) {
        if (!transactionHistory.contains(transaction)) {
            throw new BankException("Transaction not found");
        }

        transaction.revert();
    }

    /**
     * Отменяет выбранную транзакцию по идентиф.
     * @param transactionId идентиф. транзакции
     */
    public void revertTransaction(int transactionId) {
        var transaction = transactionHistory.stream().filter(x -> x.getId() == transactionId).findFirst();
        if (transaction.isEmpty()) {
            throw new BankException("Transaction not found in history");
        }

        transaction.get().revert();
    }

    /**
     * Добавляет начисленные проценты с interestAmount на счет balance
     * Вызывается в начале месяца
     */
    public void addMonthlyInterestToBalance() {
        var depositTransaction = new DepositTransaction(this, interestAmount);
        makeTransaction(depositTransaction);
        interestAmount = 0;
    }

    /**
     * Добавляет ежедневные проценты на interestAmount
     */
    public void addDailyInterest() {
        double dailyInterestRate = accountLimits.annualInterestRate() / 365 / 100;
        interestAmount += balance * dailyInterestRate;
    }

    /**
     * Вызывается при изменении времени в машине времени TimeMachine
     * Начисляет проценты, в соответствии с прошедшим временем
     * @param context текущая дата
     */
    @Override
    public void update(LocalDate context) {
        if (context.compareTo(lastInterest) < 0) {
            throw new BankException("Can't time travel to past");
        }

        while (context.compareTo(lastInterest) != 0) {
            addDailyInterest();

            LocalDate nextDay = lastInterest.plusDays(1);
            if (lastInterest.getMonth() != nextDay.getMonth()) {
                addMonthlyInterestToBalance();
            }

            lastInterest = nextDay;
        }
    }
}
