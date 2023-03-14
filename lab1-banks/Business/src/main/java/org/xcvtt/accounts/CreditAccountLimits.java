package org.xcvtt.accounts;

/**
 * Пределы кредитных аккаунтов
 */
public class CreditAccountLimits extends AccountLimits {
    public CreditAccountLimits(Account account, long initialDeposit) {
        super(account, initialDeposit);
    }

    @Override
    public boolean balanceCanGoNegative() {
        return true;
    }

    @Override
    public long transactionCommission() {
        return getAccount().getBank().getBankConfig().getCreditAccountCommission();
    }

    @Override
    public double annualInterestRate() {
        return 0;
    }

    @Override
    public long withdrawTransferLimit() {
        return getAccount().getClient().isVerified() ? 0 : getAccount().getBank().getBankConfig().getUnverifiedClientTransactionLimit();
    }
}
