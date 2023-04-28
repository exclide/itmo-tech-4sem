package is.tech.entities;

import is.tech.exceptions.BankException;
import is.tech.models.DepositAccountInterestRates;

public class BankConfigBuilder {
    private long _unverifiedTransactionLimit;
    private long _creditAccountCommission;
    private double _debitAccountRate;
    private long _depositFirstRange;
    private long _depositSecondRange;
    private double _depositAccountFirstRate;
    private double _depositAccountSecondRate;
    private double _depositAccountThirdRate;
    private int bitSet;

    public BankConfigBuilder SetUnverifiedTransactionLimit(long limit)
    {
        if (limit < 0)
        {
            throw new BankException("Limit can't be negative");
        }

        _unverifiedTransactionLimit = limit;
        bitSet |= 1 << 0;
        return this;
    }

    public BankConfigBuilder SetCreditAccountCommission(long commission)
    {
        if (commission < 0)
        {
            throw new BankException("Commission can't be negative");
        }

        _creditAccountCommission = commission;
        bitSet |= 1 << 1;
        return this;
    }

    public BankConfigBuilder SetDebitAccountRate(double rate)
    {
        if (rate < 0)
        {
            throw new BankException("Rate can't be negative");
        }

        _debitAccountRate = rate;
        bitSet |= 1 << 2;
        return this;
    }

    public BankConfigBuilder SetDepositAccountRanges(long firstRange, long secondRange)
    {
        _depositFirstRange = firstRange;
        _depositSecondRange = secondRange;
        bitSet |= 1 << 3;
        return this;
    }

    public BankConfigBuilder SetDepositAccountRates(double firstRate, double secondRate, double thirdRate)
    {
        _depositAccountFirstRate = firstRate;
        _depositAccountSecondRate = secondRate;
        _depositAccountThirdRate = thirdRate;
        bitSet |= 1 << 4;
        return this;
    }

    public BankConfig GetBankConfig()
    {
        if (bitSet != 31)
        {
            throw new BankException("Some fields weren't set");
        }

        return new BankConfig(
                _unverifiedTransactionLimit,
                _creditAccountCommission,
                _debitAccountRate,
                new DepositAccountInterestRates(
                        _depositFirstRange,
                        _depositSecondRange,
                        _depositAccountFirstRate,
                        _depositAccountSecondRate,
                        _depositAccountThirdRate));
    }
}
