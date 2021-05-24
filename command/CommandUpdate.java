package command;

import element.Worker;
import java.io.IOException;
import java.util.TreeSet;
import tools.ReadWorker;
import tools.Speaker;

/**
 * Класс-команда update_by_id.
 *
 * @author mike
 */
public class CommandUpdate extends Command{

    /**
     * Обновляет работника по id. Принимает как параметр, консоль, аргументы и
     * поток, из которого получаем данные о работнике.
     *
     * @param args
     */
    
    private int id;
    private Worker worker;
    
    public CommandUpdate(String ... args) {
        ready = true;
        try {
            id = Integer.parseInt(args[1]);
            worker = ReadWorker.read(System.in);
        } catch(NumberFormatException e) {
            ready = false;
        } catch(IOException e) {
            System.out.println("Не удалось считать работника.");
        }
    }
    
    @Override
    public Speaker event(TreeSet<Worker> collection) {
        worker.setId(id);
        Worker compared = collection.floor(new Worker(id)); 
        if(id == compared.getId()) {
            collection.add(worker);
            collection.remove(compared);
            speaker = new Speaker("Удачно заменили элемент.");
            speaker.success();
            return speaker;
        } else {
            speaker = new Speaker("Не смогли найти такой элемент.");
            speaker.error();
            return speaker;
        }
    }
}
