package window;

import tools.*;
import exception.EnvException;
import java.io.IOException;
import java.io.InputStream;
import command.*;
import java.util.TreeSet;
import element.Worker;
import java.util.Comparator;
import java.util.Scanner;
import java.io.BufferedInputStream;
import java.time.LocalDate;


public class Console {
    private static final String ENV_PATH = "LAB_PATH"; //there're names of env. var.'s
    private static final String DEF_PATH = "/home/mike/Рабочий стол/data/"
            + "programm/Lab5/Lab5/src/main/java/data";
    private BufferedInputStream reader;
    private String path;
    private TreeSet<Worker> collection;
    private LocalDate createDate = LocalDate.now();
    private String[] history = new String[14];
    
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
           reader = FileReader.getStream(path);
       } catch(IOException e){
           Speaker.println(Speaker.FontColor.RED, 
                   "Не удалось открыть или создать файл.",
                   "Попробуйте изменить путь в переменной окружения.");
       }
    }
    
    public void clear(){
        this.collection.clear();
    }
    
    public String show(){
        String result = "---\n";
        for(Worker iter:collection){
            result+=iter.toString()+"---";
        }
        return result;
    }
    
    public String history(){
        String result = "";
        for(String elem:this.history){
            if (elem==null)break;
            result+=elem+", ";
        }
        result = result.substring(0, result.length()-2);
        return result;
    }
    
    public String info(){
        return"TreeSet<Worker> collection, "+this.createDate.toString()+", "+
                Integer.toString(collection.size())+" elements.";
    }
    
    private void choice(String command, InputStream stream){
        String[] commandArr = command.trim().split(" ");
        try{
            command = commandArr[0];
            for(int i=history.length-2; i>=0; i--){
                history[i+1]=history[i];
            }
            history[0] = command;
        }catch(Exception e){return;}
        switch(command){
            case "help": CommandHelp.event(this, commandArr); break;
            case "info": CommandInfo.event(this, commandArr); break;
            case "show": CommandShow.event(this, commandArr); break;
            case "add": CommandAdd.event(this, commandArr, stream); break;
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
        String command;
        Scanner scanner = new Scanner(stream);
        while(true){
            command = scanner.nextLine();
            choice(command,stream);
        }
    }
    
    public static void main(String[] args){
        Console console = new Console();
        console.listen(System.in);
    }
    
    public TreeSet<Worker> getCollection(){return this.collection;}
    public void addToCol(Worker arg){this.collection.add(arg);}
}
