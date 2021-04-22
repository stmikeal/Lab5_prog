package command;

import element.Worker;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import tools.Speaker;

/**
 * Абстрактный класс команды.
 *
 * @author mike
 */
public abstract class Command {

    boolean ready;
    Speaker speaker;
    
    public Command(String... args) {
        ready = false;
    }

    public Speaker event(TreeSet<Worker> collection) throws ExecutionException {
        return null;
    }

    public boolean isReady() {
        return ready;
    }
}
