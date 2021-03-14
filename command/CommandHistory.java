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
public class CommandHistory {
    public static void event(Console console, String[] args){
        Speaker.println(console.history());
    }
}
