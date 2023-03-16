package org.xcvtt.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import org.xcvtt.accounts.Account;
import org.xcvtt.interfaces.Observer;
import org.xcvtt.interfaces.Subject;
import org.xcvtt.models.ClientAddress;
import org.xcvtt.models.ClientName;
import org.xcvtt.models.ClientPassportId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс клиента
 * Содержит в себе аккаунты, принадлежащие клиенту
 * Имя, адрес и паспорт клиента
 * Адрес и паспорт могут быть null, тогда клиент считается не verified
 */
@Data
public class Client implements Observer<BankConfig> {
    private final int id;
    private final List<Account> accounts = new ArrayList<>();
    @NonNull
    private ClientName clientName;
    private ClientAddress clientAddress;
    private ClientPassportId clientPassportId;

    public Client(ClientName clientName, ClientAddress clientAddress, ClientPassportId clientPassportId, int id)
    {
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.clientPassportId = clientPassportId;
        this.id = id;
    }

    /**
     * Возвращает подтвержден ли клиент или нет, исходя из наличия адреса и паспорта
     * @return
     */
    public boolean isVerified() {
        return clientAddress != null && clientPassportId != null;
    }

    /**
     * Прикрепляет аккаунт к клиенту
     * @param account
     */
    public void addAccount(Account account) {
        Objects.requireNonNull(account);
        accounts.add(account);
    }

    /**
     * Оповещает клиента об изменениях в конфиге банка
     * @param context
     */
    @Override
    public void update(BankConfig context) {
        System.out.println("Bank has changed its limits:");
        System.out.println(context);
    }
}
