package command;

import tools.Speaker;
import client.Client;

/**
 * Класс-команда remove_by_id
 *
 * @author mike
 */
public class CommandRemove {

    /**
     * Удаление элемента. Удаляет элемент по его id.
     *
     * @param console
     * @param args
     */
    public static void event(Client console, String[] args) {
        try {
            int id = Integer.parseInt(args[1]);
            console.remove(id);
        } catch (Exception e) {
            Speaker.println("Не можем считать id.");
        }
    }
}
