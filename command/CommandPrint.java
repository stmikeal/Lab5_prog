package command;

import tools.Speaker;
import element.Worker;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Класс-команда print. Выводит элементы коллекции упорядоченно.
 *
 * @author mike
 */
public class CommandPrint extends Command{
    private String result = "---\n";
    
    public CommandPrint(String ... args) {
        ready = true;
    }
    
    @Override
    public Speaker event(TreeSet<Worker> collection) {
        collection.stream().sorted(Comparator.comparing(Worker::getId)).forEachOrdered(worker -> result+=worker.toString()+"\n---\n");
        result = result.trim();
        return new Speaker(result);
    }
}
