package command;

import client.Client;

/**
 * Класс-команда exit.
 *
 * @author mike
 */
public class CommandExit {

    /**
     * Выходит из программы.
     *
     * @param console
     * @param args
     */
    public static void event(Client console, String[] args) {
        System.exit(0);
    }
}
