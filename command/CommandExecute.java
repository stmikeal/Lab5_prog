/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import tools.FileReader;
import tools.Speaker;
import window.Console;

/**
 *
 * @author mike
 */
public class CommandExecute {
    public static void event(Console console, String[] args){
        String path;
        try{
            path = args[1];
            console.listen(FileReader.getStream(path));
        }catch(Exception e){
            Speaker.println("Не выполнить команду.");
        }
    }
}
