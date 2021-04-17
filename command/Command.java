package command;

import element.Worker;
import java.util.TreeSet;

/**
 * Абстрактный класс команды.
 * @author mike
 */
public abstract class Command {
    
    boolean ready;
    
    public Command(){
        ready = false;
    }
    public boolean event(){
        return false;
    }
    public boolean event(TreeSet<Worker> collection){
        return false;
    }
    public boolean isReady(){
        return ready;
    }
}
