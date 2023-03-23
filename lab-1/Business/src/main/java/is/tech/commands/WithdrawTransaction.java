package is.tech.commands;

import lombok.Data;
import lombok.NonNull;
import is.tech.accounts.Account;
import is.tech.accounts.AccountLimits;
import is.tech.exceptions.BankException;
import is.tech.util.Counters;

@Data
public class WithdrawTransaction implements Transaction {
    @NonNull
    private final Account account;
    private final long withdrawAmount;
    private final AccountLimits accountLimits;
    private final long beforeBalance;
    public final int id;
    private boolean hasRun;

    public WithdrawTransaction(Account account, long withdrawAmount)
    {
        if (withdrawAmount < 0)
        {
            throw new BankException("Withdraw amount can't be negative");
        }

        this.account = account;
        this.withdrawAmount = withdrawAmount;
        this.accountLimits = account.getAccountLimits();
        this.beforeBalance = account.getBalance();
        this.hasRun = false;
        this.id = Counters.transactionIdCounter++;
    }

    public void run()
    {
        if (hasRun)
        {
            throw new BankException("Transaction was already executed");
        }

        if (accountLimits.withdrawTransferLimit() < withdrawAmount)
        {
            throw new BankException("Withdraw amount exceeds limit");
        }

        long newBalance = beforeBalance - withdrawAmount;
        if (accountLimits.transactionCommission() > 0 && beforeBalance < 0)
        {
            newBalance -= accountLimits.transactionCommission();
        }

        if (!accountLimits.balanceCanGoNegative() && newBalance < 0)
        {
            throw new BankException("Balance can't go negative for this account");
        }

        account.setBalance(newBalance);
        hasRun = true;
    }

    public void revert()
    {
        if (!hasRun)
        {
            throw new BankException("Transaction wasn't executed or was already reverted");
        }

        long currentBalance = account.getBalance();
        long newBalance = currentBalance + withdrawAmount;
        if (accountLimits.transactionCommission() > 0 && beforeBalance < 0)
        {
            newBalance += accountLimits.transactionCommission();
        }

        account.setBalance(newBalance);
        hasRun = false;
    }
}
