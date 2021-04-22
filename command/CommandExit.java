package command;

import element.Worker;
import java.util.TreeSet;
import tools.Speaker;

/**
 * Класс-команда exit.
 *
 * @author mike
 */
public class CommandExit extends Command{

    /**
     * Выходит из программы.
     *
     * @param collection
     * @return 
     */
    public CommandExit(String ... args){
        this.ready = true;
    }
    
    @Override
    public Speaker event(TreeSet<Worker> collection) {
        return new Speaker("exit");
    }
}
