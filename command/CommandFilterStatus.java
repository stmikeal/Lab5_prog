package command;

import element.Worker;
import tools.Speaker;
import java.util.TreeSet;

/**
 * Класс-команда filter_status.
 *
 * @author mike
 */
public class CommandFilterStatus extends Command{

    /**
     * Фильтр по статусу. Выводит все элементы, у которых статус меньше
     * заданного.
     *
     * @param console
     * @param args
     */
    
    private int status;
    private String result = "---";
    
    public CommandFilterStatus(String ... args) {
        try {
            status = Integer.parseInt(args[1]);
            ready = true;
        } catch(NumberFormatException e) {
            ready = false;
        }
    }
    
    @Override
    public Speaker event(TreeSet<Worker> collection) {
        collection.stream().filter(worker -> worker.statusToInt()<status).forEach(worker -> result+=worker.toString()+"\n---\n");
        result = result.trim();
        speaker = new Speaker(result);
        return speaker;
    }
}
