package org.xcvtt.consoleui;

import org.xcvtt.entities.BankConfig;
import org.xcvtt.entities.BankConfigBuilder;
import org.xcvtt.entities.ClientBuilder;
import org.xcvtt.models.ClientAddress;
import org.xcvtt.models.ClientName;
import org.xcvtt.models.ClientPassportId;
import org.xcvtt.services.CentralBankService;

import java.util.Objects;
import java.util.Scanner;

/**
 * Класс консольного интерфейса
 * Каждый метод - отдельное меню
 */
public class ConsoleCommands {
    private final CentralBankService cb;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleCommands(CentralBankService cb)
    {
        this.cb = cb;
        MainMenu();
    }

    public void MainMenu()
    {
        System.out.println("""
                1. Manage banks
                2. Manage clients
                3. Manage accounts
                4. Manage time
                Type number to continue...""");
        String input = scanner.next();
        switch (input) {
            case "1" -> ManageBanks();
            case "2" -> ManageClients();
            case "3" -> ManageAccounts();
            case "4" -> ManageTime();
            default -> MainMenu();
        }
    }

    public void ManageBanks()
    {
        System.out.println("""
                1. Create bank
                2. Edit banks
                3. List banks
                4. Back to menu
                Type number to continue...""");
        String input = scanner.next();
        switch (input) {
            case "1" -> {
                System.out.println("Enter bank name");
                String bankName = scanner.next();
                cb.registerNewBank(bankName, GetBankConfig());
                ManageBanks();
            }
            case "2" -> {
                
                var banks = cb.getBanks();
                for (var bank : banks) {
                    System.out.println(bank.getId() + ". " + bank.getBankName() + " config: " + bank.getBankConfig());
                }

                System.out.println("\nEnter bank number to edit Bank Config\n");
                int num = scanner.nextInt();
                var bankToEdit = banks.stream().filter(x -> x.getId() == num).findFirst().get();
                var newBankCfg = GetBankConfig();
                bankToEdit.setBankConfig(newBankCfg);
                ManageBanks();
            }
            case "3" -> {
                
                var banks = cb.getBanks();
                for (var bank : banks)
                {
                    System.out.println(bank.getId() + ". " + bank.getBankName() + "Clients: " +
                            bank.getClients().size() + " Accounts: " +  bank.getAccounts().size() +
                            " Config: " + bank.getBankConfig());
                }

                System.out.println("\nPress any key to go back\n");
                scanner.next();
                ManageBanks();
            }
            case "4" -> MainMenu();
            default -> ManageBanks();
        }
    }

    public BankConfig GetBankConfig()
    {
        
        var bankCfgBuilder = new BankConfigBuilder();
        System.out.println("Enter Unverified Transaction Limit");
        long val = scanner.nextLong();
        bankCfgBuilder.SetUnverifiedTransactionLimit(val);
        System.out.println("Enter Credit Account Commission");
        val = scanner.nextLong();
        bankCfgBuilder.SetCreditAccountCommission(val);
        System.out.println("Enter Debit Account Rate");
        double rate = scanner.nextDouble();
        bankCfgBuilder.SetDebitAccountRate(rate);
        System.out.println("Enter Deposit Account Ranges (two values, from lowest to highest)");
        long[] ranges = new long[2];
        ranges[0] = scanner.nextLong();
        ranges[1] = scanner.nextLong();
        bankCfgBuilder.SetDepositAccountRanges(ranges[0], ranges[1]);
        System.out.println("Enter Deposit Account Rates (three values, from lowest to highest)");
        double[] rates = new double[3];
        rates[0] = scanner.nextDouble();
        rates[1] = scanner.nextDouble();
        rates[2] = scanner.nextDouble();
        bankCfgBuilder.SetDepositAccountRates(rates[0], rates[1], rates[2]);

        return bankCfgBuilder.GetBankConfig();
    }

    public void ManageClients()
    {
        
        System.out.println("""
                1. Register client
                2. List clients
                3. Edit clients
                4. Back to menu
                Type number to continue...""");
        String input = scanner.next();
        switch (input) {
            case "1" -> {
                for (var bank : cb.getBanks()) {
                    System.out.println(bank.getId() + ". " + bank.getBankName() + " config: " + bank.getBankConfig());
                }

                System.out.println("Choose bank ID to register in");
                int bankId = scanner.nextInt();
                var choosenBank = cb.getBanks().stream().filter(x -> x.getId() == bankId).findFirst().get();

                var clientBuilder = GetClientBuilder();
                var client = cb.registerNewClient(choosenBank, clientBuilder);
                ManageClients();
            }
            case "2" -> {

                for (var client : cb.getClients()) {
                    System.out.println(client.getId() + ". " +  client.getClientName());
                }

                System.out.println("\nPress any key to go back\n");
                scanner.next();
                ManageClients();
            }
            case "3" -> {

                for (var client : cb.getClients()) {
                    System.out.println(client.getId() + ". " +  client.getClientName());
                }

                System.out.println("\nType client ID to change name/adr/passport\n");
                int clientId = scanner.nextInt();
                var choosenClient = cb.getClients().stream().filter(x -> x.getId() == clientId).findFirst().get();


                System.out.println("""
                        1. Change name
                        2. Change adr
                        3. Change passport
                        4. Back
                        Type number to continue...""");
                String inp = scanner.next();
                switch (inp) {
                    case "1" -> {
                        System.out.println("Enter new first name");
                        String firstName = scanner.next();
                        System.out.println("Enter new second name");
                        String secondName = scanner.next();
                        choosenClient.setClientName(new ClientName(firstName, secondName));
                        ManageClients();
                    }
                    case "2" -> {
                        var clientAdrBuilder = ClientAddress.builder();
                        System.out.println("Enter city");
                        String city = scanner.next();
                        clientAdrBuilder.city(city);
                        System.out.println("Enter street name");
                        String streetName = scanner.next();
                        clientAdrBuilder.streetName(streetName);
                        System.out.println("Enter postal code");
                        int postalCode = scanner.nextInt();
                        clientAdrBuilder.postalCode(postalCode);
                        System.out.println("Enter house number");
                        int houseNumber = scanner.nextInt();
                        clientAdrBuilder.houseNumber(houseNumber);
                        System.out.println("Enter apartment number");
                        int apartmentNumber = scanner.nextInt();
                        clientAdrBuilder.apartmentNumber(apartmentNumber);
                        choosenClient.setClientAddress(clientAdrBuilder.build());
                        ManageClients();
                    }
                    case "3" -> {
                        System.out.println("Enter new passport ID");
                        String passportId = scanner.next();
                        choosenClient.setClientPassportId(new ClientPassportId(passportId));
                        ManageClients();
                    }
                    case "4" -> ManageClients();
                    default -> ManageClients();
                }
            }
            case "4" -> MainMenu();
            default -> ManageClients();
        }
    }

    public ClientBuilder GetClientBuilder()
    {
        System.out.println("Enter client's first name");
        String firstName = scanner.next();
        System.out.println("Enter client's second name");
        String secondName = scanner.next();
        var clientBuilder = new ClientBuilder();
        clientBuilder.SetClientName(new ClientName(firstName, secondName));
        System.out.println("Would you like to verify your registration? [y/n]");
        String ans = scanner.next();
        if (Objects.equals(ans, "n"))
        {
            return clientBuilder;
        }

        var clientAdrBuilder = ClientAddress.builder();
        System.out.println("Enter passport ID\n");
        String passportId = scanner.next();
        clientBuilder.SetClientPassportId(new ClientPassportId(passportId));
        System.out.println("Enter city\n");
        String city = scanner.next();
        clientAdrBuilder.city(city);
        System.out.println("Enter street name\n");
        String streetName = scanner.next();
        clientAdrBuilder.streetName(streetName);
        System.out.println("Enter postal code\n");
        int postalCode = scanner.nextInt();
        clientAdrBuilder.postalCode(postalCode);
        System.out.println("Enter house number\n");
        int houseNumber = scanner.nextInt();
        clientAdrBuilder.houseNumber(houseNumber);
        System.out.println("Enter apartment number\n");
        int apartmentNumber = scanner.nextInt();
        clientAdrBuilder.apartmentNumber(apartmentNumber);
        clientBuilder.SetClientAddress(clientAdrBuilder.build());
        return clientBuilder;
    }

    public void ManageAccounts()
    {
        
        System.out.println("""
                1. Register account
                2. List accounts
                3. Edit accounts
                4. Back to menu
                Type number to continue...""");
        String input = scanner.next();
        switch (input) {
            case "1" -> {

                for (var bank : cb.getBanks()) {
                    System.out.println(bank.getId() + ". " + bank.getBankName() + " config: " + bank.getBankConfig());
                }

                System.out.println("Choose bank ID to register in\n");
                int bankId = scanner.nextInt();
                var choosenBank = cb.getBanks().stream().filter(x -> x.getId() == bankId).findFirst().get();


                for (var client : choosenBank.getClients()) {
                    System.out.println(client.getId() + ". " +  client.getClientName());
                }

                System.out.println("Choose client ID to register on\n");
                int clientId = scanner.nextInt();
                var choosenClient = cb.getClients().stream().filter(x -> x.getId() == clientId).findFirst().get();


                System.out.println("""
                        1. Debit
                        2. Deposit
                        3. Credit
                        Choose account type...""");
                String ans = scanner.next();
                String accountType = switch (ans) {
                    case "1" -> "debit";
                    case "2" -> "deposit";
                    case "3" -> "credit";
                    default -> " ";
                };


                System.out.println("Type in deposit amount\n");
                long depositAmount = scanner.nextLong();
                cb.registerNewAccount(choosenBank, choosenClient, accountType, depositAmount);
                ManageAccounts();
            }
            case "2" -> {

                for(var acc : cb.getAccounts()) {
                    System.out.println(acc);
                }

                System.out.println("\nPress any key to go back\n");
                scanner.next();
                ManageAccounts();

            }
            case "3" -> {

                for (var acc : cb.getAccounts()) {
                    System.out.println(acc);
                }

                System.out.println("\nChoose account ID to edit\n");
                int accountId = scanner.nextInt();
                var account = cb.getAccounts().stream().filter(x -> x.getId() == accountId).findFirst().get();


                for (var trans : account.getTransactionHistory()) {
                    System.out.println(trans);
                }

                System.out.println("""
                        Type transaction ID to cancel
                        or -1 to go back
                        """);
                int transId = scanner.nextInt();

                if (transId >= 0) {
                    account.revertTransaction(transId);
                }

                ManageAccounts();
            }
            case "4" -> {
                MainMenu();
            }
            default -> ManageAccounts();
        }
    }

    public void ManageTime()
    {
        
        System.out.println("""
                1. Get time
                2. Add days
                3. Add months
                4. Add years
                5. Back to menu
                Type number to continue...""");
        String input = scanner.next();
        switch (input) {
            case "1" -> {
                System.out.println("Current time is " + cb.getTimeMachine().getDate() + "\nPress any key to go back");
                scanner.next();
                ManageTime();
            } case "2" -> {
                System.out.println("Type the number of days to add\n");
                int days = scanner.nextInt();
                cb.getTimeMachine().addDays(days);
                ManageTime();
            }
            case "3" -> {
                System.out.println("Type the number of months to add\n");
                int months = scanner.nextInt();
                cb.getTimeMachine().addMonths(months);
                ManageTime();
            }
            case "4" -> {
                System.out.println("Type the number of years to add\n");
                int years = scanner.nextInt();
                cb.getTimeMachine().addYears(years);
                ManageTime();
            }
            case "5" -> MainMenu();
            default -> ManageTime();
        }
    }
}
