package command;

import client.Client;

/**
 * Класс-команда info. Выводит информацию о коллекции.
 *
 * @author mike
 */
public class CommandInfo {

    public static void event(Client console, String[] args) {
        System.out.println(console.info());
    }
}
