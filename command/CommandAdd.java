package command;

import element.Worker;
import java.io.IOException;
import tools.Speaker;
import tools.ReadWorker;
import java.io.InputStream;
import java.util.TreeSet;

/**
 * Класс-команда add.
 *
 * @author mike
 */
public class CommandAdd extends Command{

    /**
     * Интерактивный метод добавления. Получает на вход поток, с которого
     * подаются данные работика, обрабатывает их и добавляет в коллекцию.
     *
     */
    
    private Worker worker;
    
    public CommandAdd(){
        try {
            this.worker = ReadWorker.read(System.in);
            this.ready = true;
        } catch(IOException e) {
            Speaker.println(Speaker.FontColor.RED, "Не удалось добавить работника. "
                    + "IOException thrown from <Command Add>.");
        }
    }
    
    @Override
    public boolean event(TreeSet<Worker> collection) {
        try {
            collection.add(worker);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
