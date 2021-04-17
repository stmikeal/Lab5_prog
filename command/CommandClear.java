package command;

import element.Worker;
import java.util.TreeSet;

/**
 * Класс-команда clear.
 *
 * @author mike
 */
public class CommandClear extends Command {

    /**
     * Очищает коллекцию.
     */
    public CommandClear(){
        
    }
    
    @Override
    public boolean event(TreeSet<Worker> collection) {
        try {
            collection.clear();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
