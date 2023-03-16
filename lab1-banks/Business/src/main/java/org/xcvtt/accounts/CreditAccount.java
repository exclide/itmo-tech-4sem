package org.xcvtt.accounts;

import org.xcvtt.entities.Bank;
import org.xcvtt.entities.Client;

import java.time.LocalDate;

/**
 * Кредитный аккаунт расширает базовый
 */
public class CreditAccount extends Account {
    public CreditAccount(Client client, Bank bank, int id, LocalDate creationDate, long depositAmount) {
        super(client, bank, id, creationDate);
        setAccountLimits(new CreditAccountLimits(this, depositAmount));
        setBalance(depositAmount);
    }
}
