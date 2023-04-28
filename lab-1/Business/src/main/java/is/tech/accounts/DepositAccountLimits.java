package is.tech.accounts;

/**
 * Пределы депозитных аккаунтов
 */
public class DepositAccountLimits extends AccountLimits {
    public DepositAccountLimits(Account account, long initialDeposit) {
        super(account, initialDeposit);
    }

    @Override
    public boolean balanceCanGoNegative() {
        return false;
    }

    @Override
    public long transactionCommission() {
        return 0;
    }

    @Override
    public double annualInterestRate() {
        if (getInitialDeposit() < getAccount().getBank().getBankConfig().getDepositAccountInterestRates().getFirstRange())
        {
            return getAccount().getBank().getBankConfig().getDepositAccountInterestRates().getFirstPercent();
        }

        if (getInitialDeposit() < getAccount().getBank().getBankConfig().getDepositAccountInterestRates().getSecondRange())
        {
            return getAccount().getBank().getBankConfig().getDepositAccountInterestRates().getSecondPercent();
        }

        return getAccount().getBank().getBankConfig().getDepositAccountInterestRates().getThirdPercent();
    }

    @Override
    public long withdrawTransferLimit() {
        return getAccount().isExpired() ? Long.MAX_VALUE : 0;
    }
}