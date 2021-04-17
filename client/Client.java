package client;

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
 * Main-class консоли.
 * Реализует управление консолью, хранит все глобальные данные.
 * @author Mike Stepanov P3130
 */
public class Client {
    private static final String ENV_PATH = "LAB_PATH";
    private static final String DEF_PATH = "data";
    private String path;
    private TreeSet<Worker> collection;
    private LocalDate createDate = LocalDate.now();
    private String[] history = new String[14];
    
    /**
     * Конструктор Main-class.
     * Создаёт объект консоли и заполняет все необходимые
     * переменные для корректной работы
     */
    public Client() {
       Speaker.hr();
       Speaker.println("Доброго дня, СЭР!", 
               "Приветствую вас в программе SuperSlamMegaTerminalEmployee.",
               "Если не знаете, что делать введите help.");
       Speaker.hr();
       Comparator<Worker> comparator = (o1,o2)->
               ((Integer)o1.getId()).compareTo((Integer)o2.getId());
       collection = new TreeSet(comparator);
       
       try  {
           path = EnvReader.getenv(ENV_PATH);
       } catch(EnvException e) {
           Speaker.println(e.getMessage(),"Устанавливаем значение по умолчанию...");
           path = DEF_PATH;
       }
       
       
       try {
           if (path.substring(0, 4).equals("/dev")) throw new NoSuchElementException();
           load();
       } catch(Exception e) {
           System.out.println("Не удалось до конца считать файл.");
       }
    }
    
    /**
     * Метод загрузки входных данных.
     * Обращается к пути, полученному в конструкторе и читает данные,
     * пока не кончится файл или не встретит некорректные данные.
     * @throws NoSuchElementException
     */
    public final void load() throws NoSuchElementException {
        
        try(BufferedInputStream reader = FileReader.getStream(path)) {
            Scanner scanner = new Scanner(reader);
            Deque<String> text = new ArrayDeque<>();
            String[] elements;
            while(scanner.hasNext()) {
                elements = scanner.nextLine().trim().split(",");
                for(String iter:elements) {
                    String newElem = iter.trim();
                    if (!newElem.equals("")) text.add(newElem);
                }
            }
            int year = Integer.parseInt(text.pop());
            int month = Integer.parseInt(text.pop());
            int day = Integer.parseInt(text.pop());
            this.createDate = LocalDate.of(year, month, day);
            int elem = Integer.parseInt(text.pop());
            Boolean isValid = true;
            for(int i=0; i<elem;i++) {
                String name = text.pop();
                Coordinates coordinates = new Coordinates(Double.parseDouble(text.pop()),
                    Double.parseDouble(text.pop()));
                if (coordinates.getX()<622) isValid=false;
                Double salary = Double.parseDouble(text.pop());
                if (coordinates.getX()<0) isValid=false;
                LocalDate startDate = LocalDate.of(Integer.parseInt(text.pop()), 
                        Integer.parseInt(text.pop()), Integer.parseInt(text.pop()));
                Position position;
                switch(text.pop()) {
                    case "DIRECTOR": position = Position.DIRECTOR; break;
                    case "ENGINEER": position = Position.ENGINEER; break;
                    case "HEAD_OF_DIVISION": position = Position.HEAD_OF_DIVISION; break;
                    default: position = null;
                }
                Status status;
                switch(text.pop()) {
                    case "FIRED": status = Status.FIRED; break;
                    case "RECOMMENDED_FOR_PROMOTION": status = Status.RECOMMENDED_FOR_PROMOTION; break;
                    case "REGULAR": status = Status.REGULAR; break;
                    default: status = null;
                }
                Person person;
                if (text.pop().equals("y")) {
                    int height = Integer.parseInt(text.pop());
                    Color eye;
                    switch(text.pop()) {
                        case "BLUE": eye = Color.BLUE; break;
                        case "GREEN": eye = Color.GREEN; break;
                        case "ORANGE": eye = Color.ORANGE; break;
                        case "WHITE": eye = Color.WHITE; break;
                        default: eye = null;
                    }
                    Color hair;
                    switch(text.pop()) {
                        case "YELLOW": hair = Color.YELLOW; break;
                        case "BROWN": hair = Color.BROWN; break;
                        case "WHITE": hair = Color.WHITE; break;
                        default: hair = null;
                    }
                    Country nation;
                    switch(text.pop()) {
                        case "RUSSIA": nation = Country.RUSSIA; break;
                        case "UNITED_KINGDOM": nation = Country.UNITED_KINGDOM; break;
                        case "GERMANY": nation = Country.GERMANY; break;
                        case "ITALY": nation = Country.ITALY; break;
                        default: nation = null;
                    }
                    person = new Person(height, eye, hair, nation);
                } else { 
                    person = null;
                }
                if (isValid)this.addToCol(new Worker(name, coordinates, salary, startDate,
                position, status, person));
                
            }
            reader.close();
        } catch(IOException e) {
            Speaker.println(Speaker.FontColor.RED, 
                   "Не удалось открыть или создать файл.",
                   "Попробуйте изменить путь в переменной окружения.");
        } catch (NumberFormatException e) {
            Speaker.println("Не удалось прочитать число.");
        } catch (java.time.DateTimeException e) {
            Speaker.println("Неверный формат даты");
        }
    }
    
    /**
     * Метод сохраняющий коллекцию в файл.
     */
    public void save(){ 
        try(OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(new File(path)))) {
            if (path.substring(0, 4).equals("/dev")) throw new NoSuchElementException();
            writer.write(Integer.toString(createDate.getYear())+"\n");
            writer.write(Integer.toString(createDate.getMonthValue())+"\n");
            writer.write(Integer.toString(createDate.getDayOfMonth())+"\n");
            writer.write(Integer.toString(collection.size())+"\n");
            for(Worker elem:collection){
                writer.write(elem.toStringSave()+"\n");
            }
            writer.close();
        } catch(Exception e) {
            Speaker.println("Не удалось корректно сохранить коллекцию.");
        }
    }
    
    /**
     * Удаление элемента.
     * Удаляет элемент по id, переданному в параметре.
     * @param id
     */
    public void remove(int id) {
        Worker compared = collection.floor(new Worker(id)); 
        if(id==compared.getId())collection.remove(compared);
    }
    
    /**
     * Удаление наименьшего элемента.
     * Удаляет элемент с наименьшим id, если коллекция не пуста.
     */
    public void removeLower() {
        try {
            collection.remove(collection.first());
        } 
        catch(Exception e) {
            Speaker.println("Коллекция пуста.");
        }
    }
    
    /**
     * Метод очищающий коллекцию.
     * Удаляет все элементы в коллекции.
     */
    public void clear() {
        this.collection.clear();
    }
    
    /**
     * Метод отображения коллекции.
     * Возвращает строку, в которой описаны элементы коллекции.
     * @return String
     */
    public String show() {
        String result = "---\n";
        for(Worker iter:collection){
            result+=iter.toString()+"---\n";
        }
        return result;
    }
    
    /**
     * Метод отображения коллекции.
     * Возвращает строку, в которой описаны элементы в упорядоченном виде.
     * @return String
     */
    public String print() {
        String result = "---\n";
        Iterator<Worker> iter = collection.iterator();
        while(iter.hasNext()){
            result+=iter.next().toString()+"---";
        }
        return result;
    }
    
    /**
     * Метод отображения истории.
     * Возвращает строку, содержащую историю команд.
     * @return String
     */
    public String history() {
        String result = "";
        for(String elem:this.history){
            if (elem==null)return result;
            result+=elem+", ";
        }
        result = result.substring(0, result.length()-2);
        return result;
    }
    
    /**
     * Метод отображения информации.
     * Возвращает строку с информацией о коллекции.
     * @return String
     */
    public String info() {
        return"TreeSet<Worker> collection, "+this.createDate.toString()+", "+
                Integer.toString(collection.size())+" elements.";
    }
    
    /**
     * Выбирает поиск наименьшего элемента в коллекции.
     * Возвращает id наименьшего элемента или, если коллекция пуста, то
     * минимальное значение целочисленного типа.
     * @return int
     */
    public int first() {
        if (collection.size()==0)return -2147483648;
        else return collection.first().getId();
    }
    
    /**
     * Фильтр по имени.
     * Выводит все элементы, которые содержат подстроку name в имени.
     * @param name
     */
    public void filterName(String name) {
        Speaker.println("---");
        for(Worker elem:collection){
            if(Pattern.matches(".*"+name+".*",elem.getName())){
                Speaker.println(elem.toString());
                Speaker.println("---");
            }
        }
    }
    
    /**
     * Фильтр по статусу.
     * Выводит все элементы со статусом status.
     * @param status
     */
    public void filterStatus(int status) {
        Speaker.println("---");
        for(Worker elem:collection){
            if(elem.statusToInt()<status){
                Speaker.println(elem.toString());
                Speaker.println("---");
            }
        }
    }
    
    private void addToHistory(String command) {
        try {
            for(int i=history.length-2; i>=0; i--) {
                history[i+1]=history[i];
            }
            history[0] = command;
        }
        catch(Exception e) {
            return;
        }
    }
    
    private Command choice(String command, InputStream stream){
        String[] commandArr = command.trim().split(" ");
        Boolean historicalString = false;
        try{
            command = commandArr[0];
        }
        catch(Exception e) {
            return null;
        }
        switch(command){
            case "help": new CommandHelp(); break;
            case "info": new CommandInfo(); break;
            case "show": new CommandShow(); break;
            case "add": new CommandAdd(stream); break;
            case "update": new CommandUpdate(commandArr, stream); break;
            case "remove_by_id": new CommandRemove(commandArr); break;
            case "clear": new CommandClear(commandArr); break;
            case "save": new CommandSave(commandArr); break;
            case "execute_script": new CommandExecute(commandArr); break;
            case "exit": new CommandExit(commandArr); historicalString = true;break;
            case "add_if_min": new CommandAddIfMin.event(this, commandArr, stream); historicalString = true;break;
            case "remove_lower": new CommandRemoveLower.event(this, commandArr); historicalString = true;break;
            case "history": new CommandHistory.event(this, commandArr); historicalString = true;break;
            case "filter_contains_name": new CommandFilterContains.event(this, commandArr); historicalString = true;break;
            case "filter_less_than_status": new CommandFilterStatus.event(this, commandArr); historicalString = true;break;
            case "print_ascending": new CommandPrint.event(this, commandArr); historicalString = true;break;
            case "": break;
            default: Speaker.println("Кажется вы ввели неверную команду, введите help для справки");break;
        }
        if (historicalString) addToHistory(command);
    }
    
    /**
     * Реализация поведения консоли.
     * Выполняет команды, которые получает из stream.
     * @param stream
     */
    public void listen(InputStream stream) {
        String inputString;
        Command command;
        Scanner scanner = new Scanner(stream);
        try {
            while(scanner.hasNext()) {
                inputString = scanner.nextLine();
                command = choice(inputString,stream);
            }
        }
        catch(NoSuchElementException e) {
            Speaker.println("Выходим из консоли...");
            CommandExit.event(this, history);
        }
        
    }
    
    public static void main(String[] args) {
        Client console = new Client();
        console.listen(System.in);
    }
    
    /**
     * Getter коллекции.
     * @return
     */
    public TreeSet<Worker> getCollection() {
        return this.collection;
    }

    /**
     * Добавляет arg в коллекцию.
     * @param arg
     */
    public void addToCol(Worker arg) {
        this.collection.add(arg);
    }
}
