package command;

import tools.Speaker;
import element.Worker;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Класс-команда print. Выводит элементы коллекции упорядоченно.
 *
 * @author mike
 */
public class CommandPrint extends Command{
    
    public CommandPrint(String ... args) {
        ready = true;
    }
    
    @Override
    public Speaker event(TreeSet<Worker> collection) {
        String result = "---\n";
        Iterator<Worker> iter = collection.iterator();
        while(iter.hasNext()){
            result+=iter.next().toString()+"\n---\n";
        }
        result = result.trim();
        return new Speaker(result);
    }
}
