import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import is.tech.entities.Bank;
import is.tech.entities.BankConfig;
import is.tech.entities.BankConfigBuilder;
import is.tech.exceptions.BankException;
import is.tech.models.ClientName;
import is.tech.services.CentralBankService;
import is.tech.services.ClientAccountController;

public class BankTests {
    private final CentralBankService cb;
    private final Bank sber;
    private final Bank tink;
    private final Bank alpha;

    public BankTests()
    {
        cb = new CentralBankService();

        BankConfig sberConfig = new BankConfigBuilder()
                .SetCreditAccountCommission(10000)
                .SetUnverifiedTransactionLimit(500)
                .SetDebitAccountRate(365)
                .SetDepositAccountRanges(50000, 100000)
                .SetDepositAccountRates(3, 4, 5)
                .GetBankConfig();

        sber = cb.registerNewBank("Sber", sberConfig);

        BankConfig tinkConfig = new BankConfigBuilder()
                .SetCreditAccountCommission(20000)
                .SetUnverifiedTransactionLimit(1000)
                .SetDebitAccountRate(10)
                .SetDepositAccountRanges(500000, 1000000)
                .SetDepositAccountRates(6, 8, 10)
                .GetBankConfig();

        tink = cb.registerNewBank("Tink", tinkConfig);

        BankConfig alphaConfig = new BankConfigBuilder()
                .SetCreditAccountCommission(100000)
                .SetUnverifiedTransactionLimit(500000)
                .SetDebitAccountRate(5)
                .SetDepositAccountRanges(5000, 10000)
                .SetDepositAccountRates(365, 730, 1095)
                .GetBankConfig();

        alpha = cb.registerNewBank("Alpha", alphaConfig);
    }

    @Test
    public void CreateDebitAccount_CheckRates()
    {
        var client = cb.registerNewClient(sber, new ClientName("Ivan", "Govnov"), null, null);
        var account = cb.registerNewAccount(sber, client, "debit", 10000);
        cb.getTimeMachine().addMonths(1);
        Assertions.assertEquals(13100, account.getBalance());
        cb.getTimeMachine().addMonths(1);
        Assertions.assertEquals(16768, account.getBalance());
    }

    @Test
    public void CreateDepositAccount_Transfer_ChangeBankConfig_CheckRates()
    {
        var client = cb.registerNewClient(alpha, new ClientName("Ivan", "Govnov"), null, null);
        var account = cb.registerNewAccount(alpha, client, "deposit", 10000);
        cb.getTimeMachine().addMonths(1);
        Assertions.assertEquals(19300, account.getBalance());
        var client2 = cb.registerNewClient(alpha, new ClientName("Oleg", "Govnov"), null, null);
        var account2 = cb.registerNewAccount(alpha, client, "debit", 10000);
        cb.transferFromAccountTo(account2.getId(), account.getId(), 700);
        Assertions.assertEquals(20000, account.getBalance());
        Assertions.assertEquals(9300, account2.getBalance());

        alpha.setBankConfig(new BankConfigBuilder()
                .SetCreditAccountCommission(2000)
                .SetDebitAccountRate(730)
                .SetUnverifiedTransactionLimit(2000)
                .SetDepositAccountRanges(100, 200)
                .SetDepositAccountRates(1, 2, 3650)
                .GetBankConfig());

        cb.getTimeMachine().addMonths(1);
        Assertions.assertEquals(76000, account.getBalance());
        Assertions.assertEquals(14508, account2.getBalance());
    }

    @Test
    public void CreateAccountController_TestAccountLimits()
    {
        var client = cb.registerNewClient(alpha, new ClientName("Ivan", "Govnov"), null, null);
        var account = cb.registerNewAccount(alpha, client, "deposit", 10000);
        var accountController = new ClientAccountController(account);
        var throwed = Assertions.assertThrows(BankException.class, () -> accountController.withdrawFromAccount(5000));
        cb.getTimeMachine().addYears(3);
        accountController.withdrawFromAccount(5000);
    }
}
