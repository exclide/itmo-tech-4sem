package is.tech.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import is.tech.accounts.Account;
import is.tech.commands.DepositTransaction;
import is.tech.commands.WithdrawTransaction;

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
