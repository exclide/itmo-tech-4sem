package org.xcvtt.commands;

/**
 * Базовый класс транзакции, паттерн команда
 * Передается в метод аккаунта runTransaction для выполнения различных транзацкий, поддерживает отмену транзацкий
 */
public interface Transaction {
    /**
     * Выполняет транзацкию
     */
    void run();

    /**
     * Отменяет транзакцию
     */
    void revert();

    /**
     *
     * @return идентиф. транзацкии
     */
    int getId();
}
