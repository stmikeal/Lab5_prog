/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
/**
 *
 * @author mike
 */
public class ConsoleStream extends InputStream{
    private char[] command = {};
    private boolean ret = false;
    private int counter = 0;

    @Override
    public int read() throws IOException {
        if (counter >= command.length){
            if (ret){
                ret = false;
                return -1;
            }else {
                counter=0;
                String s = Speaker.scanString();
                System.out.println(s);
                command = s.toCharArray();
                ret = true;
            }
        }
        char result = (char)-1;
        if (command.length>0) result = command[counter];
        System.out.println(result);
        counter++;
        return result;
    }
    
}
