package window;

import tools.*;
import exception.EnvException;
import java.io.IOException;
import java.io.InputStream;
import command.*;
import element.*;
import java.util.TreeSet;
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
import java.util.Deque;
import java.util.ArrayDeque;

/**
 *
 * @author mike
 */
public class Console {
    private static final String ENV_PATH = "LAB_PATH"; //there're names of env. var.'s
    private static final String DEF_PATH = "/home/mike/Рабочий стол/data/"
            + "programm/Lab5/Lab5/src/main/java/data";
    private String path;
    private TreeSet<Worker> collection;
    private LocalDate createDate = LocalDate.now();
    private String[] history = new String[14];
    
    public Console(){
       Speaker.hr();
       Speaker.println("Доброго дня, СЭР!", 
               "Приветствую вас в программе SuperSlamMegaTerminalEmployee.",
               "Если не знаете, что делать введите help.");
       Speaker.hr();
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
           if (path.equals("/dev/zero")) throw new NoSuchElementException();
           load();
       }catch(NoSuchElementException e){
           System.out.println("Не удалось до конца считать файл.");
       }
    }
    
    /**
     *
     * @throws NoSuchElementException
     */
    public final void load() throws NoSuchElementException{
        
        try(BufferedInputStream reader = FileReader.getStream(path)){
            Scanner scanner = new Scanner(reader);
            Deque<String> text = new ArrayDeque<>();
            String[] elements;
            while(scanner.hasNext()){
                elements = scanner.nextLine().trim().split(",");
                for(String iter:elements){
                    String newElem = iter.trim();
                    if (!newElem.equals(""))text.add(newElem);
                }
            }
            int year = Integer.parseInt(text.pop());
            int month = Integer.parseInt(text.pop());
            int day = Integer.parseInt(text.pop());
            this.createDate = LocalDate.of(year, month, day);
            int elem = Integer.parseInt(text.pop());
            for(int i=0; i<elem;i++){
                String name = text.pop();
                Coordinates coordinates = new Coordinates(Double.parseDouble(text.pop()),
                    Double.parseDouble(text.pop()));
                Double salary = Double.parseDouble(text.pop());
                LocalDate startDate = LocalDate.of(Integer.parseInt(text.pop()), 
                        Integer.parseInt(text.pop()), Integer.parseInt(text.pop()));
                Position position;
                switch(text.pop()){
                    case "DIRECTOR": position = Position.DIRECTOR; break;
                    case "ENGINEER": position = Position.ENGINEER; break;
                    case "HEAD_OF_DIVISION": position = Position.HEAD_OF_DIVISION; break;
                    default: position = null;
                }
                Status status;
                switch(text.pop()){
                    case "FIRED": status = Status.FIRED; break;
                    case "RECOMMENDED_FOR_PROMOTION": status = Status.RECOMMENDED_FOR_PROMOTION; break;
                    case "REGULAR": status = Status.REGULAR; break;
                    default: status = null;
                }
                Person person;
                if (text.pop().equals("y")){
                    int height = Integer.parseInt(text.pop());
                    Color eye;
                    switch(text.pop()){
                        case "BLUE": eye = Color.BLUE; break;
                        case "GREEN": eye = Color.GREEN; break;
                        case "ORANGE": eye = Color.ORANGE; break;
                        case "WHITE": eye = Color.WHITE; break;
                        default: eye = null;
                    }
                    Color hair;
                    switch(text.pop()){
                        case "YELLOW": hair = Color.YELLOW; break;
                        case "BROWN": hair = Color.BROWN; break;
                        case "WHITE": hair = Color.WHITE; break;
                        default: hair = null;
                    }
                    Country nation;
                    switch(text.pop()){
                        case "RUSSIA": nation = Country.RUSSIA; break;
                        case "UNITED_KINGDOM": nation = Country.UNITED_KINGDOM; break;
                        case "GERMANY": nation = Country.GERMANY; break;
                        case "ITALY": nation = Country.ITALY; break;
                        default: nation = null;
                    }
                    person = new Person(height, eye, hair, nation);
                }else{ 
                    person = null;
                }
                this.addToCol(new Worker(name, coordinates, salary, startDate,
                position, status, person));
                
            }
            reader.close();
        } catch(IOException e){
            Speaker.println(Speaker.FontColor.RED, 
                   "Не удалось открыть или создать файл.",
                   "Попробуйте изменить путь в переменной окружения.");
        } catch (NumberFormatException e){
            Speaker.println("Не удалось прочитать число.");
        } catch (java.time.DateTimeException e) {
            Speaker.println("Неверный формат даты");
        }
    }
    
    public void save(){
        try(OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(new File(path)))){
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
            result+=iter.toString()+"---\n";
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
            if (elem==null)return result;
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
        if (collection.size()==0)return -2147483648;
        else return collection.first().getId();
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
    
    private void addToHistory(String command){
        try{
            for(int i=history.length-2; i>=0; i--){
                history[i+1]=history[i];
            }
            history[0] = command;
        }catch(Exception e){return;}
    }
    
    private void choice(String command, InputStream stream){
        String[] commandArr = command.trim().split(" ");
        Boolean historicalString = false;
        try{
            command = commandArr[0];
        }catch(Exception e){return;}
        switch(command){
            case "help": CommandHelp.event(this, commandArr); historicalString = true;break;
            case "info": CommandInfo.event(this, commandArr); historicalString = true;break;
            case "show": CommandShow.event(this, commandArr); historicalString = true;break;
            case "add": CommandAdd.event(this, commandArr, stream); historicalString = true;break;
            case "update": CommandUpdate.event(this, commandArr,stream); historicalString = true;break;
            case "remove_by_id": CommandRemove.event(this, commandArr); historicalString = true;break;
            case "clear": CommandClear.event(this, commandArr); historicalString = true;break;
            case "save": CommandSave.event(this, commandArr); historicalString = true;break;
            case "execute_script": CommandExecute.event(this, commandArr); historicalString = true;break;
            case "exit": CommandExit.event(this, commandArr); historicalString = true;break;
            case "add_if_min": CommandAddIfMin.event(this, commandArr, stream); historicalString = true;break;
            case "remove_lower": CommandRemoveLower.event(this, commandArr); historicalString = true;break;
            case "history": CommandHistory.event(this, commandArr); historicalString = true;break;
            case "filter_contains_name": CommandFilterContains.event(this, commandArr); historicalString = true;break;
            case "filter_less_than_status": CommandFilterStatus.event(this, commandArr); historicalString = true;break;
            case "print_ascending": CommandPrint.event(this, commandArr); historicalString = true;break;
            case "": break;
            default: Speaker.println("Кажется вы ввели неверную команду, введите help для справки");break;
        }
        if (historicalString)addToHistory(command);
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
