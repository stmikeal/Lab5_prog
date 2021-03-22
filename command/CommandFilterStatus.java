/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import element.Worker;
import tools.Speaker;
import window.Console;

/**
 *
 * @author mike
 */
public class CommandFilterStatus {
    public static void event(Console console, String[] args){
        try{
            int index = Worker.statusToInt(args[1]);
            if(index!=-1)console.filterStatus(index);
            else Speaker.println("Некорректный статус.");
        }catch(Exception e){
            Speaker.println("Не можем корректно считать статус.");
        }
    }
}
