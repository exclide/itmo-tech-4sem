package org.xcvtt.models;

import lombok.Value;
import org.xcvtt.exceptions.BankException;

import java.util.regex.Pattern;

/**
 * Номер паспорта клиента
 * Используется проверка регуляркой на соответствие формату паспорта
 */
@Value
public class ClientPassportId {
    String passportId;
    /** Только 4 цифры */
    static Pattern pattern = Pattern.compile("^\\d{4}$");

    public ClientPassportId(String passportId) {
        if (!pattern.matcher(passportId).matches()) {
            throw new BankException("Wrong passport format");
        }

        this.passportId = passportId;
    }
}
