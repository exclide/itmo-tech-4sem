package is.tech.models;

import lombok.Data;
import is.tech.exceptions.BankException;

/**
 * Проценты для депозитного аккаунта
 * Чем выше изначальный взнос, тем выше под него процент
 * Например, firstRange - 5, secondRange - 10
 * Тогда firstPercent - все что меньше 5
 * secondPercent - все что между 5 и 10
 * thirdPercent - все что больше 10
 */
@Data
public class DepositAccountInterestRates {
    private long firstRange;
    private long secondRange;
    private double firstPercent;
    private double secondPercent;
    private double thirdPercent;

    public DepositAccountInterestRates(
            long firstRange,
            long secondRange,
            double firstPercent,
            double secondPercent,
            double thirdPercent)
    {
        if (firstRange < 0 || secondRange < 0 || firstRange >= secondRange)
        {
            throw new BankException("Invalid deposit account deposit ranges");
        }

        if (firstPercent < 0 || secondPercent < 0 || thirdPercent < 0
                || firstPercent > secondPercent || secondPercent > thirdPercent)
        {
            throw new BankException("Invalid deposit account interest rates");
        }

        this.firstRange = firstRange;
        this.secondRange = secondRange;
        this.firstPercent = firstPercent;
        this.secondPercent = secondPercent;
        this.thirdPercent = thirdPercent;
    }
}
