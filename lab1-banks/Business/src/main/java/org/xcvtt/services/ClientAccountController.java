package org.xcvtt.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.xcvtt.accounts.Account;
import org.xcvtt.commands.DepositTransaction;
import org.xcvtt.commands.WithdrawTransaction;

/**
 * Контроллер аккаунта
 * Типа фасада? Хз зачем сделал
 * Можно выводить деньги и начислять
 */
@Value
@AllArgsConstructor
public class ClientAccountController {
    @NonNull
    Account account;

    public void withdrawFromAccount(long withdrawAmount) {
        var withdrawTransaction = new WithdrawTransaction(account, withdrawAmount);
        account.makeTransaction(withdrawTransaction);
    }

    public void depositToAccount(long depositAmount) {
        var depositTransaction = new DepositTransaction(account, depositAmount);
        account.makeTransaction(depositTransaction);
    }
}
