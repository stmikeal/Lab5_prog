/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import java.util.ArrayDeque;
import java.util.Deque;
import tools.FileReader;
import tools.Speaker;
import window.Console;

/**
 *
 * @author mike
 */
public class CommandExecute {
    static Deque<String> stack = new ArrayDeque<>();
    public static void event(Console console, String[] args){
        String path;
        if (stack.contains(args[1])){
            System.err.println("Ошибка. Один или несколько скриптов зациклены.");
            return;
        } 
        try{
            path = args[1];
            if (path.equals("/dev/zero"))throw new NullPointerException(); 
            stack.add(path);
            console.listen(FileReader.getStream(path));
            stack.removeLast();
        }catch(ArrayIndexOutOfBoundsException e){
            Speaker.println("Не указан путь к файлу.");
        }catch(NullPointerException e){
            Speaker.println("Ай-Ай-Ай, не балуйтесь с дев зиро!");
        }catch(Exception e){}
        
    }
}
