package command;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import server.DataManager;
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

    public Speaker event(DataManager collection) throws ExecutionException {
        return null;
    }

    public boolean isReady() {
        return ready;
    }
    
    public void setDone() {
        this.ready = false;
    }
}
