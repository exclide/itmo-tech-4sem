package is.tech.models;

import lombok.NonNull;
import lombok.Value;
import is.tech.exceptions.BankException;

import java.util.regex.Pattern;

/**
 * Модель имени клиента, используется проверка с регуляркой, чтобы в имени не было лишних символов
 */
@Value
public class ClientName {
    /** Прекомпайлнутый паттерн, допустимы только латинские буквы */
    static Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
    @NonNull
    String firstName;
    @NonNull
    String secondName;

    public ClientName(String firstName, String secondName) {
        if (!pattern.matcher(firstName).matches() || !pattern.matcher(secondName).matches()) {
            throw new BankException("Wrong name format");
        }

        this.firstName = firstName;
        this.secondName = secondName;
    }
}
