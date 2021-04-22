package command;

import element.Worker;
import java.util.TreeSet;
import tools.Speaker;

/**
 * Класс-команда clear.
 *
 * @author mike
 */
public class CommandClear extends Command {

    /**
     * Очищает коллекцию.
     */
    public CommandClear(String ... args){
        this.ready = true;
    }
    
    @Override
    public Speaker event(TreeSet<Worker> collection) {
        try {
            collection.clear();
            speaker = new Speaker("Мы очистили коллекцию!");
            speaker.success();
            return speaker;
        } catch (Exception e) {
            speaker = new Speaker("Мы не смогли очистить коллекцию!");
            speaker.error();
            return speaker;
        }
    }
}
