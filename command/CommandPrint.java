package command;

import tools.Speaker;
import client.Client;

/**
 * Класс-команда print. Выводит элементы коллекции упорядоченно.
 *
 * @author mike
 */
public class CommandPrint {

    public static void event(Client console, String[] args) {
        Speaker.println(console.print());
    }
}
