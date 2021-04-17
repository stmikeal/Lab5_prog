package command;

import client.Client;

/**
 * Класс-команда show. Выводит коллекцию.
 *
 * @author mike
 */
public class CommandShow {

    public static void event(Client console, String[] args) {
        System.out.print(console.show());
    }
}
