package command;

import element.Worker;
import java.util.TreeSet;
import tools.Speaker;

/**
 * Класс-команда remove_lower
 *
 * @author mike
 */
public class CommandRemoveLower extends Command{

    /**
     * Удаляет элемент.Удаляет наименьший элемент коллекции.
     *
     * @param args
     */
    
    public CommandRemoveLower(String ... args) {
        ready = true;
    }
    
    @Override
    public Speaker event(TreeSet<Worker> collection) {
        try {
            collection.remove(collection.first());
            speaker = new Speaker("Наименьший элемент удачно удалён.");
            speaker.success();
            return speaker;
        } catch(Exception e) {
            speaker = new Speaker("Коллекция пуста.");
            speaker.success();
            return speaker;
        }
    }
}
