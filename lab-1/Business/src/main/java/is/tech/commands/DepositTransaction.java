package is.tech.commands;

import lombok.Data;
import lombok.NonNull;
import is.tech.accounts.Account;
import is.tech.accounts.AccountLimits;
import is.tech.exceptions.BankException;
import is.tech.util.Counters;

@Data
public class DepositTransaction implements Transaction {
    public final int id;
    @NonNull
    private final Account account;
    private final long depositAmount;
    private final AccountLimits accountLimits;
    private final long beforeBalance;
    private boolean hasRun;

    public DepositTransaction(Account account, long depositAmount)
    {
        if (depositAmount < 0)
        {
            throw new BankException("Deposit amount can't be negative");
        }

        this.account = account;
        this.depositAmount = depositAmount;
        this.accountLimits = account.getAccountLimits();
        this.beforeBalance = account.getBalance();
        hasRun = false;
        id = Counters.transactionIdCounter++;
    }

    public void run()
    {
        if (hasRun)
        {
            throw new BankException("Transaction was already executed");
        }

        long newBalance = beforeBalance + depositAmount;

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
        long newBalance = currentBalance - depositAmount;

        account.setBalance(newBalance);
        hasRun = false;
    }
}
