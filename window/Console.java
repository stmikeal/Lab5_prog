package window;

import tools.*;
import exception.EnvException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import command.*;

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
    
    private void choise(String command){
        String[] commandArr = command.split(" ");
        command = commandArr[0];
        switch(command){
            case "help": CommandHelp.event(this, commandArr); break;
            case "info": CommandInfo.event(this, commandArr); break;
            case "show": CommandShow.event(this, commandArr); break;
            case "add": CommandAdd.event(this, commandArr); break;
            case "update": CommandUpdate.event(this, commandArr); break;
            case "remove_by_id": CommandRemove.event(this, commandArr); break;
            case "clear": CommandClear.event(this, commandArr); break;
            case "save": CommandSave.event(this, commandArr); break;
            case "execute_script": CommandExecute.event(this, commandArr); break;
            case "exit": CommandExit.event(this, commandArr); break;
            case "add_if_min": CommandAddIfMin.event(this, commandArr); break;
            case "remove_lower": CommandRemoveLower.event(this, commandArr); break;
            case "history": CommandHistory.event(this, commandArr); break;
            case "filter_contains_name": CommandFilterContains.event(this, commandArr); break;
            case "filter_less_than_status": CommandFilterStatus.event(this, commandArr); break;
            case "print_ascending": CommandPrint.event(this, commandArr); break;
        }
    }
    
    public void listen(InputStream stream){
        String command = "";
        while(true){
            command = Speaker.scanStream(stream);
            choise(command);
        }
    }
    
    public static void main(String[] args){
        Console console = new Console();
        console.listen(new ConsoleStream());
    }
}
