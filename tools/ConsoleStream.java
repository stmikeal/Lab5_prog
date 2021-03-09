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
    private char[] command = null;
    private boolean ret = false;

    @Override
    public int read() throws IOException {
        if (command==null){
            if (ret){
                ret = false;
                return -1;
            }else {
                command = Speaker.scanString().toCharArray();
                ret = true;
            }
        }
        char result = command[0];
        command = Arrays.copyOfRange(command, 1, command.length);
        return result;
    }
    
}
