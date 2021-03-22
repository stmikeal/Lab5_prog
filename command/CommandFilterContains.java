/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import tools.Speaker;
import window.Console;

/**
 *
 * @author mike
 */
public class CommandFilterContains {
    public static void event(Console console, String[] args){
        try{
            String name = args[1];
            console.filterName(name);
        }catch(Exception e){
            Speaker.println("Не удалось конкретно считать шаблон.");
        }
    }
}
