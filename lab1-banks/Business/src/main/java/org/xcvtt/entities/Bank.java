package org.xcvtt.entities;

import lombok.Data;
import lombok.NonNull;
import org.xcvtt.accounts.Account;
import org.xcvtt.exceptions.BankException;
import org.xcvtt.interfaces.Observer;
import org.xcvtt.interfaces.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс банка
 * Содержит в себе клиентов, аккаунты, прикрепленные к банку, методы для добавления клиентов и аккаунтов
 * Клиенты могут подписываться на изменения банк конфига
 */
@Data
public class Bank implements Subject<BankConfig> {
    private final int id;
    private String bankName;
    @NonNull
    private BankConfig bankConfig;
    private final List<Client> clients = new ArrayList<>();
    private final List<Account> accounts = new ArrayList<>();
    private final List<Observer<BankConfig>> observers = new ArrayList<>();

    public Bank(String bankName, BankConfig bankConfig, int id) {
        this.id = id;
        this.bankName = bankName;
        this.bankConfig = bankConfig;
    }

    /**
     * Об изменениях конфига банка будут оповощены все подписчики
     * @param bankConfig
     */
    public void setBankConfig(@NonNull BankConfig bankConfig) {
        this.bankConfig = bankConfig;
        notifySubs();
    }

    public void subscribe(@NonNull Observer<BankConfig> observer) {
        observers.add(observer);
    }

    public void unsubscribe(@NonNull Observer<BankConfig> observer) {
        observers.remove(observer);
    }

    public void notifySubs() {
        for (var observer : observers) {
            observer.update(bankConfig);
        }
    }

    public void addClient(@NonNull Client client) {
        clients.add(client);
    }

    /**
     * Прикрепляет аккаунт к банку
     * Клиент, прикрепленный к аккаунту, должен быть прикреплен к банку, иначе бросает исключение
     * @param account
     * @param client
     */
    public void addAccount(@NonNull Account account, @NonNull Client client) {
        var targetClient = clients.stream()
                .filter(client1 -> client1.equals(client))
                .findFirst()
                .orElse(null);

        if (targetClient == null) {
            throw new BankException("Client wasn't found in the bank");
        }

        accounts.add(account);
        client.addAccount(account);
    }
}
