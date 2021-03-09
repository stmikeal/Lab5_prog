package window;

import tools.*;
import exception.EnvException;
import java.io.IOException;
import java.io.InputStream;

public class Console {
    private static final String ENV_PATH = "LAB_PATH"; //there're names of env. var.'s
    private static final String DEF_PATH = "/home/mike/Рабочий стол/data/"
            + "programm/Lab5/Lab5/src/main/java/data";
    private FileReader reader;
    private String path;
    
    public Console(){
       try {
           path = EnvReader.getenv(ENV_PATH);
       } catch(EnvException e) {
           Speaker.println(e.getMessage(),"Устанавливаем значение по умолчанию...");
           path = DEF_PATH;
       }
       try{
           reader = new FileReader(path);
       } catch(IOException e){
           Speaker.println(Speaker.FontColor.RED, 
                   "Не удалось открыть или создать файл.",
                   "Попробуйте изменить путь в переменной окружения.");
       }
    }
    
    public void listen(InputStream stream){
        int c;
        String s = "";
        while(true){
            try{
                while((c=stream.read())!=-1){
                    s+=(char)c;
                }
            } catch(IOException e){}//TODO
        }
    }
    
    public static void main(String[] args){
        Console console = new Console();
        console.listen(new ConsoleStream());
    }
}
