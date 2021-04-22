package command;

import element.Worker;
import java.util.TreeSet;
import tools.Speaker;

/**
 * Класс-команда show. Выводит коллекцию.
 *
 * @author mike
 */
public class CommandShow extends Command {
    
    public CommandShow(String ... args) {
        ready = true;
    }
    
    @Override
    public Speaker event(TreeSet<Worker> collection) {
        String result = "---\n";
        for(Worker iter:collection){
            result+=iter.toString()+"\n---\n";
        }
        result = result.trim();
        return new Speaker(result);
    }
}
