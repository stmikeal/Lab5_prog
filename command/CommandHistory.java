package command;

import tools.Speaker;
import client.Client;

/**
 * Класс-команда history. Выводит историю.
 *
 * @author mike
 */
public class CommandHistory {

    public static void event(Client console, String[] args) {
        Speaker.println(console.history());
    }
}
