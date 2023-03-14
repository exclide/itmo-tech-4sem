package org.xcvtt;

import org.xcvtt.consoleui.ConsoleCommands;
import org.xcvtt.services.CentralBankService;

public class Main {
    public static void main(String[] args) {
        var cb = new CentralBankService();
        var consoleCommands = new ConsoleCommands(cb);
    }
}