/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
/**
 *
 * @author mike
 */
public class FileReader {
    private static File purpose;
    //private FileInputStream inputContainer;
    private BufferedInputStream inputer;
    
    public static BufferedInputStream getStream(String path) throws FileNotFoundException, IOException{
        
        purpose = new File(path);
        if (!purpose.exists()){
            Speaker.println("Файла по указанному пути не сущетсвует.");
            throw new FileNotFoundException("");
        }
        
        if (purpose.isDirectory()){
            Speaker.println(Speaker.FontColor.RED, "Указанный файл - директория");
            throw new FileNotFoundException("Не удалось получить доступ к файлу "
                    +path);
        }
        
        FileInputStream inputContainer = new FileInputStream(purpose);
        return new BufferedInputStream(inputContainer);
        
    }
    
    public String nextLine(){
        
        int i;
        try{
            
        } catch(Exception e){
            System.out.println("oops");
            return "";
        }
        return "s";
    }
}
