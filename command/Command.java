package command;

import element.Worker;
import java.io.Serializable;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import tools.Speaker;

/**
 * Абстрактный класс команды.
 *
 * @author mike
 */
public abstract class Command  implements Serializable{

    boolean ready = false;
    Speaker speaker;
    
    public Command(String... args) {
        ready = true;
    }

    public Speaker event(TreeSet<Worker> collection) throws ExecutionException {
        return null;
    }

    public boolean isReady() {
        return ready;
    }
    
    public void setDone() {
        this.ready = false;
    }
}
