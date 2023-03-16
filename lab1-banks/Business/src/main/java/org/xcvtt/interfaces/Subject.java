package org.xcvtt.interfaces;

/**
 * Реализация паттерна наблюдатель
 * @param <T> - контекст, о которым хотим быть оповощены
 */
public interface Subject<T> {
    void subscribe(Observer<T> observer);
    void unsubscribe(Observer<T> observer);
    void notifySubs();
}
