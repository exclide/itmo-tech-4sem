package is.tech.commands;

import lombok.Data;
import lombok.NonNull;
import is.tech.accounts.Account;
import is.tech.accounts.AccountLimits;
import is.tech.exceptions.BankException;
import is.tech.util.Counters;

@Data
public class TransferTransaction implements Transaction {
    @NonNull
    private final Account accountFrom;
    @NonNull
    private final Account accountTo;
    private final long transferAmount;
    private final AccountLimits accountLimitsFrom;
    private final AccountLimits accountLimitsTo;
    private final long beforeBalanceFrom;
    private final long beforeBalanceTo;
    private final int id;
    private boolean hasRun;

    public TransferTransaction(Account accountFrom, Account accountTo, long transferAmount)
    {
        if (transferAmount < 0)
        {
            throw new BankException("Transfer amount can't be negative");
        }

        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.transferAmount = transferAmount;
        this.accountLimitsFrom = accountFrom.getAccountLimits();
        this.accountLimitsTo = accountTo.getAccountLimits();
        this.beforeBalanceFrom = accountFrom.getBalance();
        this.beforeBalanceTo = accountTo.getBalance();
        this.hasRun = false;
        id = Counters.transactionIdCounter++;
    }

    public void run()
    {
        if (hasRun)
        {
            throw new BankException("Transaction was already executed");
        }

        if (accountLimitsFrom.withdrawTransferLimit() < transferAmount)
        {
            throw new BankException("Transfer amount exceeds limit");
        }

        long newBalanceFrom = beforeBalanceFrom - transferAmount;
        if (accountLimitsFrom.transactionCommission() > 0 && beforeBalanceFrom < 0)
        {
            newBalanceFrom -= accountLimitsFrom.transactionCommission();
        }

        if (!accountLimitsFrom.balanceCanGoNegative() && newBalanceFrom < 0)
        {
            throw new BankException("Balance can't go negative for this account");
        }

        long newBalanceTo = beforeBalanceTo + transferAmount;

        accountFrom.setBalance(newBalanceFrom);
        accountTo.setBalance(newBalanceTo);
        hasRun = true;
    }

    public void revert()
    {
        if (!hasRun)
        {
            throw new BankException("Transaction wasn't executed or was already reverted");
        }

        long currentBalanceFrom = accountFrom.getBalance();
        long currentBalanceTo = accountTo.getBalance();
        long newBalanceFrom = currentBalanceFrom + transferAmount;
        long newBalanceTo = currentBalanceTo - transferAmount;
        if (accountLimitsFrom.transactionCommission() > 0 && beforeBalanceFrom < 0)
        {
            newBalanceFrom += accountLimitsFrom.transactionCommission();
        }

        accountFrom.setBalance(newBalanceFrom);
        accountTo.setBalance(newBalanceTo);
        hasRun = false;
    }
}
