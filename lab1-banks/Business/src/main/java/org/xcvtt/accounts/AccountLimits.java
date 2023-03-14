package org.xcvtt.accounts;

import lombok.Data;

/**
 * Базовый класс лимита для аккаунта
 * Содержит в себе методы, необходимые для транзацкий над аккаунтами и начисления процентов
 */
@Data
public abstract class AccountLimits {
    /** Поле аккаунта, необходимо, чтобы брать оттуда банковский конфиг и понимать, истекший ли он */
    private final Account account;
    /** Начальный вклад, необходим для подсчета процентов на депозитный счет */
    private final long initialDeposit;

    public AccountLimits(Account account, long initialDeposit) {
        this.account = account;
        this.initialDeposit = initialDeposit;
    }

    /** Может ли баланс был отрицательным, в зависимости от типа аккаунта */
    public abstract boolean balanceCanGoNegative();
    /** Годовые проценты, зависит от типа аккаунта и конфига банка */
    public abstract double annualInterestRate();
    /** Комиссия на транзакцию, только для кредитного */
    public abstract long transactionCommission();
    /** Ограничение на снятие, зависит от типа счета и подтвержден ли он */
    public abstract long withdrawTransferLimit();
}
