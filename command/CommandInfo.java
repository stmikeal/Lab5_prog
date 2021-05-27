package command;

import element.Worker;
import java.util.TreeSet;

import server.DataManager;
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
    public Speaker event(DataManager collection) {
        return new Speaker("TreeSet<Worker> collection, "+server.Server.getDate().toString()+", "+
                Integer.toString(collection.size())+" elements.");
    }
}
