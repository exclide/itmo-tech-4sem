package org.xcvtt.entities;

import lombok.Data;
import lombok.NonNull;
import org.xcvtt.exceptions.BankException;
import org.xcvtt.models.DepositAccountInterestRates;

/**
 * Конфиг банка
 * Содержит годовые проценты для дебетовых, депозитных аккаунтов, коммиссии для кредитных аккаунтов, пределы транзацкий
 */
@Data
public class BankConfig {
    private final long unverifiedClientTransactionLimit;
    private final long creditAccountCommission;
    private final double debitAccountRate;
    @NonNull
    private final DepositAccountInterestRates depositAccountInterestRates;

    public BankConfig(
            long unverifiedClientTransactionLimit,
            long creditAccountCommission,
            double debitAccountRate,
            DepositAccountInterestRates depositAccountInterestRates) {

        if (unverifiedClientTransactionLimit < 0 || creditAccountCommission < 0 || debitAccountRate < 0)
        {
            throw new BankException("Invalid bank limits");
        }

        this.unverifiedClientTransactionLimit = unverifiedClientTransactionLimit;
        this.creditAccountCommission = creditAccountCommission;
        this.debitAccountRate = debitAccountRate;
        this.depositAccountInterestRates = depositAccountInterestRates;
    }
}
