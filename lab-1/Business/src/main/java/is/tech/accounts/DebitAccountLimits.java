package is.tech.accounts;


/**
 * Пределы дебетовых аккаунтов
 */
public class DebitAccountLimits extends AccountLimits {
    public DebitAccountLimits(Account account, long initialDeposit) {
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
        return getAccount().getBank().getBankConfig().getDebitAccountRate();
    }

    @Override
    public long withdrawTransferLimit() {
        return getAccount().getClient().isVerified() ? 0 : getAccount().getBank().getBankConfig().getUnverifiedClientTransactionLimit();
    }
}
