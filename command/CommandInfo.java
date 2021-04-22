package command;

import element.Worker;
import java.util.TreeSet;
import tools.Speaker;

/**
 * Класс-команда info. Выводит информацию о коллекции.
 *
 * @author mike
 */
public class CommandInfo extends Command{
    
    public CommandInfo(String ... args) {
        ready = true;
    }
    
    @Override
    public Speaker event(TreeSet<Worker> collection) {
        return new Speaker("TreeSet<Worker> collection, "+server.Server.getDate().toString()+", "+
                Integer.toString(collection.size())+" elements.");
    }
}
