package org.xcvtt.services;

import lombok.Data;
import lombok.NonNull;
import org.xcvtt.accounts.Account;
import org.xcvtt.accounts.CreditAccount;
import org.xcvtt.accounts.DebitAccount;
import org.xcvtt.accounts.DepositAccount;
import org.xcvtt.commands.TransferTransaction;
import org.xcvtt.entities.*;
import org.xcvtt.exceptions.BankException;
import org.xcvtt.models.ClientAddress;
import org.xcvtt.models.ClientName;
import org.xcvtt.models.ClientPassportId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис центрального банка
 * Обобщает в себе банки, клиентов, аккаунты
 * Имеет методы для добавления банков, клиентов, аккаунтов
 * А также метод для совершения транзацкий между аккаунтами
 */
@Data
public class CentralBankService {
    private final List<Bank> banks = new ArrayList<>();
    private final List<Client> clients = new ArrayList<>();
    private final List<Account> accounts = new ArrayList<>();
    /**
     * Машина времени, используется для проверки корректности начисления процентов
     */
    private final TimeMachine timeMachine;

    public CentralBankService() {
        timeMachine = new TimeMachine(LocalDate.of(2023, 1, 1));
    }

    /**
     * Регистрирует новый банк и добавляет в список банков
     * @param bankName название банка
     * @param bankConfig конфиг банка
     * @return банк
     */
    public Bank registerNewBank(String bankName, BankConfig bankConfig) {
        Bank bank = new Bank(bankName, bankConfig, banks.size());
        banks.add(bank);
        return bank;
    }

    /**
     * Регистрирует нового клиента в указанном банке
     * @param bank банк, в котором регистрируем клиента
     * @param clientName имя клиента, не может быть null
     * @param clientAddress адрес клиента, может быть null
     * @param clientPassportId паспорт клиента, может быть null
     * @return экземпляр добавленного клиента
     */
    public Client registerNewClient(
            Bank bank,
            @NonNull ClientName clientName,
            ClientAddress clientAddress,
            ClientPassportId clientPassportId) {
        var targetBank = banks.stream().filter(b -> b.equals(bank)).findFirst();
        if (targetBank.isEmpty()) {
            throw new BankException("Bank isn't registered in the central bank");
        }

        ClientBuilder clientBuilder = new ClientBuilder().SetClientName(clientName);

        if (clientAddress != null) {
            clientBuilder = clientBuilder.SetClientAddress(clientAddress);
        }

        if (clientPassportId != null) {
            clientBuilder = clientBuilder.SetClientPassportId(clientPassportId);
        }

        Client client = clientBuilder.GetClient(clients.size());

        bank.addClient(client);
        clients.add(client);
        return client;
    }

    public Client registerNewClient(Bank bank, ClientBuilder clientBuilder) {
        var targetBank = banks.stream().filter(b -> b.equals(bank)).findFirst();
        if (targetBank.isEmpty()) {
            throw new BankException("Bank isn't registered in the central bank");
        }

        Client client = clientBuilder.GetClient(clients.size());

        bank.addClient(client);
        clients.add(client);
        return client;
    }

    /**
     * Регистрирует новый аккаунт в указанном банке, прикрепленный к указанному клиенту
     * @param bank - банк, в котором открывает счет
     * @param client - клиент, которому принадлежит счет
     * @param accountType - тип счета, дебетовый, кредитный, депозит
     * @param depositAmount - первоначальный взнос
     * @return экземпляр аккаунта
     */
    public Account registerNewAccount(Bank bank, Client client, String accountType, long depositAmount) {
        var targetBank = banks.stream().filter(b -> b.equals(bank)).findFirst();
        if (targetBank.isEmpty()) {
            throw new BankException("Bank isn't registered in the central bank");
        }

        var targetClient = clients.stream().filter(c -> c.equals(client)).findFirst();
        if (targetClient.isEmpty()) {
            throw new BankException("Bank isn't registered in the central bank");
        }

        Account account = switch (accountType) {
            case "credit" -> new CreditAccount(client, bank, accounts.size(), timeMachine.getDate(), depositAmount);
            case "debit" -> new DebitAccount(client, bank, accounts.size(), timeMachine.getDate(), depositAmount);
            case "deposit" -> new DepositAccount(client, bank, accounts.size(), timeMachine.getDate(), depositAmount);
            default -> throw new BankException("Account type not implemented");
        };

        bank.addAccount(account, client);
        accounts.add(account);
        timeMachine.subscribe(account);

        return account;
    }

    /**
     * Передача денег между счетами
     * @param accountIdFrom аккаунт с которого перечисляем
     * @param accountIdTo аккаунт куда перечисляем
     * @param transferAmount сумма перевода
     */
    public void transferFromAccountTo(int accountIdFrom, int accountIdTo, long transferAmount) {
        var accountFrom = accounts.stream().filter(x -> x.getId() == accountIdFrom).findFirst();
        var accountTo = accounts.stream().filter(x -> x.getId() == accountIdTo).findFirst();

        if (accountFrom.isEmpty() || accountTo.isEmpty()) {
            throw new BankException("Can't find the required accounts");
        }

        var transferTransaction = new TransferTransaction(accountFrom.get(), accountTo.get(), transferAmount);
        accountFrom.get().makeTransaction(transferTransaction);
    }

    public void subscribeClientToBankLimitChanges(Bank bank, Client client) {
        if (!banks.contains(bank) || !clients.contains(client)) {
            throw new BankException("Bank or client isn't registered");
        }

        bank.subscribe(client);
    }

    public void unsubscribeClientFromBankLimitChanges(Bank bank, Client client) {
        if (!banks.contains(bank) || !clients.contains(client)) {
            throw new BankException("Bank or client isn't registered");
        }

        bank.unsubscribe(client);
    }
}
