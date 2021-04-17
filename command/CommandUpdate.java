package command;

import element.Worker;
import java.io.InputStream;
import tools.ReadWorker;
import tools.Speaker;
import client.Client;

/**
 * Класс-команда update_by_id.
 *
 * @author mike
 */
public class CommandUpdate {

    /**
     * Обновляет работника по id. Принимает как параметр, консоль, аргументы и
     * поток, из которого получаем данные о работнике.
     *
     * @param console
     * @param args
     * @param stream
     */
    public static void event(Client console, String[] args, InputStream stream) {
        try {
            int id = Integer.parseInt(args[1]);
            try {
                console.remove(id);
                Worker worker = ReadWorker.read(stream);
                worker.setId(id);
                console.addToCol(worker);
                Speaker.println("Мы успешно добавили элемент в коллекцию!");
                Speaker.hr();
            } catch (Exception e) {
                Speaker.println("Не удалось добавить работника в коллекцию.");
            }
        } catch (Exception e) {
        }
    }
}
