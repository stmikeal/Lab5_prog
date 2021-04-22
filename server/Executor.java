package server;

import command.Command;
import java.util.concurrent.ExecutionException;
import tools.Speaker;

/**
 * Класс, выполняющий команды.
 *
 * @author mike
 */
public class Executor {
    
    private Command command;
    private Speaker result;
    
    public Executor(Command command) {
        this.command = command;
    }

    public Speaker execute() {
        /*
        Выполняем команду, возвращаем сообщение.
        */
        try {
            if (command.isReady() == false) {
                throw new NullPointerException();
            }
            result = command.event(Server.collection);
            if (result == null) {
                throw new NullPointerException();
            }
        } catch(ExecutionException | NullPointerException e) {
            result = new Speaker("Не смогли корректно исполнить команду.");
        }
        return result;
    }

}
