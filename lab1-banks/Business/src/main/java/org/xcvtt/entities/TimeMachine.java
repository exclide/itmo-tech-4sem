package org.xcvtt.entities;

import lombok.Data;
import lombok.NonNull;
import org.xcvtt.exceptions.BankException;
import org.xcvtt.interfaces.Observer;
import org.xcvtt.interfaces.Subject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Машина времени, используется для проверки начисления процентов
 * При смене времени в машине, оповещает подписчиков (счета), те начисляют проценты
 */
@Data
public class TimeMachine implements Subject<LocalDate> {
    private final List<Observer<LocalDate>> observers = new ArrayList<>();
    private LocalDate date;

    public TimeMachine(LocalDate date) {
        this.date = date;
    }

    public void setDate(@NonNull LocalDate date) {
        if (date.compareTo(this.date) < 0) {
            throw new BankException("Can't travel to past");
        }

        this.date = date;
        notifySubs();
    }

    public void addDays(int days) {
        setDate(date.plusDays(days));
    }

    public void addMonths(int months) {
        setDate(date.plusMonths(months));
    }

    public void addYears(int years) {
        setDate(date.plusYears(years));
    }

    @Override
    public void subscribe(@NonNull Observer<LocalDate> observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(@NonNull Observer<LocalDate> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifySubs() {
        for(var observer : observers) {
            observer.update(date);
        }
    }
}
