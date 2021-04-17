package command;

import element.Worker;
import tools.Speaker;
import client.Client;

/**
 * Класс-команда filter_status.
 *
 * @author mike
 */
public class CommandFilterStatus {

    /**
     * Фильтр по статусу. Выводит все элементы, у которых статус меньше
     * заданного.
     *
     * @param console
     * @param args
     */
    public static void event(Client console, String[] args) {
        try {
            int index = Worker.statusToInt(args[1]);
            if (index != -1) {
                console.filterStatus(index);
            } else {
                Speaker.println("Некорректный статус.");
            }
        } catch (Exception e) {
            Speaker.println("Не можем корректно считать статус.");
        }
    }
}
