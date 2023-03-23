package is.tech.accounts;

import is.tech.entities.Bank;
import is.tech.entities.Client;

import java.time.LocalDate;

/**
 * Депозитный аккаунт расширает базовый
 */
public class DepositAccount extends Account {
    public DepositAccount(Client client, Bank bank, int id, LocalDate creationDate, long depositAmount) {
        super(client, bank, id, creationDate);
        setAccountLimits(new DepositAccountLimits(this, depositAmount));
        setBalance(depositAmount);
    }
}