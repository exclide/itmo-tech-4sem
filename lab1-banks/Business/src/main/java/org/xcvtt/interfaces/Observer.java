package org.xcvtt.interfaces;

/**
 * Реализация паттерна наблюдатель
 * @param <T> - контекст, о которым хотим быть оповощены
 */
public interface Observer<T> {
    void update(T context);
}
