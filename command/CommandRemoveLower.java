package command;

import client.Client;

/**
 * Класс-команда remove_lower
 *
 * @author mike
 */
public class CommandRemoveLower {

    /**
     * Удаляет элемент. Удаляет наименьший элемент коллекции.
     *
     * @param console
     * @param args
     */
    public static void event(Client console, String[] args) {
        console.removeLower();
    }
}
