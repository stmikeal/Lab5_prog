package window;

import tools.*;
import exception.EnvException;
import java.io.IOException;
import java.io.InputStream;
import command.*;
import java.util.TreeSet;
import element.Worker;
import java.util.Comparator;


public class Console {
    private static final String ENV_PATH = "LAB_PATH"; //there're names of env. var.'s
    private static final String DEF_PATH = "/home/mike/Рабочий стол/data/"
            + "programm/Lab5/Lab5/src/main/java/data";
    private FileReader reader;
    private String path;
    private TreeSet<Worker> collection;
    
    public Console(){
       Comparator<Worker> comparator = (o1,o2)->
               ((Integer)o1.getId()).compareTo((Integer)o2.getId());
       collection = new TreeSet(comparator);
       
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
