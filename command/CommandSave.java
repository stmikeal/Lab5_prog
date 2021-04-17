package command;

import client.Client;

/**
 * Класс-команда save. Сохраняет коллекцию.
 *
 * @author mike
 */
public class CommandSave {

    public static void event(Client console, String[] args) {
        console.save();
    }
}
