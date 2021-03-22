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
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;


public class Console {
    private static final String ENV_PATH = "LAB_PATH"; //there're names of env. var.'s
    private static final String DEF_PATH = "/home/mike/Рабочий стол/data/"
            + "programm/Lab5/Lab5/src/main/java/data";
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
           load();
       }catch(Exception e){
           System.out.println("Не удалось до конца считать файл.");
       }
    }
    
    public final void load() throws Exception{
        
        try{
            BufferedInputStream reader = FileReader.getStream(path);
            InputStream stream = reader;
            Scanner scanner = new Scanner(reader);
            int year = Integer.parseInt(scanner.nextLine());
            int month = Integer.parseInt(scanner.nextLine());
            int day = Integer.parseInt(scanner.nextLine());
            this.createDate = LocalDate.of(year, month, day);
            int elem = Integer.parseInt(scanner.nextLine());
            for(int i=0; i<elem;i++){
                this.addToCol(ReadWorker.read(stream));
            }
            reader.close();
        } catch(IOException e){
            Speaker.println(Speaker.FontColor.RED, 
                   "Не удалось открыть или создать файл.",
                   "Попробуйте изменить путь в переменной окружения.");
        }
    }
    
    public void save(){
        try{
            OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(new File(path)));
            writer.write(Integer.toString(createDate.getYear())+"\n");
            writer.write(Integer.toString(createDate.getMonthValue())+"\n");
            writer.write(Integer.toString(createDate.getDayOfMonth())+"\n");
            writer.write(Integer.toString(collection.size())+"\n");
            for(Worker elem:collection){
                writer.write(elem.toStringSave()+"\n");
            }
            writer.close();
        }catch(Exception e){
            Speaker.println("Не удалось корректно сохранить коллекцию.");
        }
    }
    
    public void remove(int id){
        Worker compared = collection.floor(new Worker(id)); 
        if(id==compared.getId())collection.remove(compared);
    }
    
    public void removeLower(){
        try{
            collection.remove(collection.first());
        }catch(Exception e){
            Speaker.println("Коллекция пуста.");
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
    
    public String print(){
        String result = "---\n";
        Iterator<Worker> iter = collection.iterator();
        while(iter.hasNext()){
            result+=iter.next().toString()+"---";
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
    
    public int first(){
        return collection.first().getId();
    }
    
    public void filterName(String name){
        Speaker.println("---");
        for(Worker elem:collection){
            if(Pattern.matches(".*"+name+".*",elem.getName())){
                Speaker.println(elem.toString());
                Speaker.println("---");
            }
        }
    }
    
    public void filterStatus(int status){
        Speaker.println("---");
        for(Worker elem:collection){
            if(elem.statusToInt()<status){
                Speaker.println(elem.toString());
                Speaker.println("---");
            }
        }
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
            case "update": CommandUpdate.event(this, commandArr,stream); break;
            case "remove_by_id": CommandRemove.event(this, commandArr); break;
            case "clear": CommandClear.event(this, commandArr); break;
            case "save": CommandSave.event(this, commandArr); break;
            case "execute_script": CommandExecute.event(this, commandArr); break;
            case "exit": CommandExit.event(this, commandArr); break;
            case "add_if_min": CommandAddIfMin.event(this, commandArr, stream); break;
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
        try{
            while(scanner.hasNext()){
                command = scanner.nextLine();
                choice(command,stream);
            }
        }catch(NoSuchElementException e){
            Speaker.println("Выходим из консоли...");
            CommandExit.event(this, history);
        }
        
    }
    
    public static void main(String[] args){
        Console console = new Console();
        console.listen(System.in);
    }
    
    public TreeSet<Worker> getCollection(){return this.collection;}
    public void addToCol(Worker arg){this.collection.add(arg);}
}
