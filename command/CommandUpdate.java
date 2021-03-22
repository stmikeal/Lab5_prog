/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import element.Worker;
import java.io.InputStream;
import tools.ReadWorker;
import tools.Speaker;
import window.Console;

/**
 *
 * @author mike
 */
public class CommandUpdate {
    public static void event(Console console, String[] args, InputStream stream){
        try{
            int id = Integer.parseInt(args[1]);
            try{
                Worker worker = ReadWorker.read(stream);
                console.remove(id);
                worker.setId(id);
                console.addToCol(worker);
                Speaker.println("Мы успешно добавили элемент в коллекцию!");
                Speaker.hr();
            }catch(Exception e){
                Speaker.println("Не удалось добавить работника в коллекцию.");
            }
        } catch(Exception e){}
    }
}
