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
public class CommandAddIfMin {
    public static void event(Console console, String[] args, InputStream stream){
        if (args.length>1){
            Speaker.println("Add не имеет дополнительных аругментов.");
        } else {
            try{
                Worker worker = ReadWorker.read(stream);
                if(worker.getId()<console.first()){
                    console.addToCol(worker);
                    Speaker.println("Мы успешно добавили элемент в коллекцию!");
                }
                else Speaker.println("Элемент больше наименьшего!");
                Speaker.hr();
            }catch(Exception e){
                Speaker.println("Не удалось добавить работника в коллекцию.");
            }//TODO
        }
    }
}
