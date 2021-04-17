package command;

import tools.Speaker;
import client.Client;

/**
 * Класс-команда filter_contains_name
 *
 * @author mike
 */
public class CommandFilterContains {

    /**
     * Фильтр имени. Выводит все элементы, имя которых содержит подстроку.
     *
     * @param console
     * @param args
     */
    public static void event(Client console, String[] args) {
        try {
            String name = args[1];
            console.filterName(name);
        } catch (Exception e) {
            Speaker.println("Не удалось конкретно считать шаблон.");
        }
    }
}
