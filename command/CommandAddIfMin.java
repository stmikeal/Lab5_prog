package command;

import element.Worker;
import java.io.InputStream;
import tools.ReadWorker;
import tools.Speaker;
import java.io.IOException;
import java.util.TreeSet;

/**
 * Класс-команда add_if_min.
 *
 * @author mike
 */
public class CommandAddIfMin extends Command{

    /**
     * Интерактивный метод добавления. Добавляет полученного из потока
     * работника, если его id меньше всех в коллекции.
     *
     * @param console
     * @param args
     * @param stream
     */
    
    private Worker worker;
    
    public CommandAddIfMin(InputStream stream){
        try {
            this.worker = ReadWorker.read(stream);
            this.ready = true;
        } catch(IOException e) {
            Speaker.println(Speaker.FontColor.RED, "Не удалось добавить работника. "
                    + "IOException thrown from <Command Add If Min>.");
        }
    }
    
    @Override
    public boolean event(TreeSet<Worker> collection) {
        try {
            Integer first;
            if (collection.isEmpty()) {
                first = -2147483648;
            }
            else {
                first = collection.first().getId();
            }
            if (worker.getId() < first) {
                collection.add(worker);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
