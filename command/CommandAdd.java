/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;
import window.Console;
import tools.Speaker;
import tools.ReadWorker;
import java.io.InputStream;
/**
 *
 * @author mike
 */
public class CommandAdd {
    public static void event(Console console, String[] args, InputStream stream){
        if (args.length>1){
            Speaker.println("Add не имеет дополнительных аругментов.");
        } else {
            try{
                console.addToCol(ReadWorker.read(stream));
                Speaker.println("Мы успешно добавили элемент в коллекцию!");
                Speaker.hr();
            }catch(Exception e){
                Speaker.println("Не удалось добавить работника в коллекцию.");
            }//TODO
        }
    }
}
