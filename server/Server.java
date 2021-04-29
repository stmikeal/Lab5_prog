package server;

import element.Color;
import element.Coordinates;
import element.Country;
import element.Person;
import element.Position;
import element.Status;
import element.Worker;
import exception.EnvException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeSet;
import tools.EnvReader;
import tools.FileReader;
import tools.Speaker;

/**
 * Класс сервера.
 * Добавляет новых пользователей.
 * @author mike
 */
public class Server {
    
    private static int PORT;
    private static String PATH;
    static TreeSet<Worker> collection;
    private static LocalDate createDate = LocalDate.now();
    
    static {
        Comparator<Worker> comparator = (o1,o2)->
               ((Integer)o1.getId()).compareTo((Integer)o2.getId());
        collection = new TreeSet(comparator);
    }
    
    public static void main(String[] args) throws IOException{
        
        Runtime.getRuntime().addShutdownHook(
                new Thread(
                        () -> {
                            try {
                                Thread.sleep(200);
                                System.out.println("Выключаем сервер ...");
                                save();
                            } catch(InterruptedException e) {
                                System.out.println("Не удалось сохранить коллекцию");
                            }
                        }
                )
        );
        
        /*
        Блок чтения из переменной окружения.
        При неудачной попытке выставляем значение по умолчанию.
        Читаем коллекцию из файла.0
        */
        try {
            PATH = EnvReader.getenv("LAB_PATH");
        } catch(EnvException e) {
            System.out.println("Неудачно загружена переменная среды, загружаем значение по умолчанию.");
            PATH = "data";
        }
        System.out.println(PATH);
        load();
        
        /*
        Блок чтения порта.
        При неудачном чтении выставляется значение по умолчанию.
        */
        try {
            PORT = 4242;
            System.out.println("Введите порт для подключения: ");
            PORT = Integer.parseInt(new Scanner(System.in).nextLine());
            if (PORT<=1024){
                throw new NumberFormatException();
            }
        } catch(NumberFormatException e) {
            System.out.println("Ошибка чтение порта, загружено значение по умолчанию - 4242.");
        } catch(NoSuchElementException e) {
            System.out.println("Зачем вы ломаете программу?! Ни мучий, апути.");
            System.out.println("Устанавливаем значение по умолчанию - 4242.");
        }
        
        
        /*
        Открываем сервер на прослушку на полученном порте
        */
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT);
        } catch(IOException e) {
            System.out.println("Проблема с запуском сервера, проверьте настройки...");
            System.exit(1337);
        }
        
        /*
        Ждем пока поступит запрос на подключение.
        Когда приходит подключение, запускаем поток, начинаем работу с юзером.
        */
        try {
            while(true) {
                Socket socket = server.accept();
            
                try {
                    SocketAdapter adapter = new SocketAdapter(socket);
                    System.out.println("Мы начали общение с пользователем: " + socket.getInetAddress().toString());
                } catch(IOException e) {
                    System.out.println("Мы не смогли корректно начать общение с пользователем.");
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
    
    public static LocalDate getDate() {
        return createDate;
    }
    
    
    
    
    private static void load(){
        
        try(BufferedInputStream reader = FileReader.getStream(PATH)) {
            if (PATH.length() > 4) {
                if (PATH.substring(0, 4).equals("/dev")) { 
                    throw new NullPointerException();
                }
            }
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
            createDate = LocalDate.of(year, month, day);
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
                
                if (isValid) {
                    collection.add(new Worker(name, coordinates, salary, startDate,
                position, status, person));
                }
                
            }
            System.out.println("Коллекция прочитана корректно");
            reader.close();
        } catch(IOException | NoSuchElementException e) {
            System.out.println("Не удалось открыть или создать файл.\n" +
                   "Попробуйте изменить путь в переменной окружения.");
        } catch (NumberFormatException e) {
            System.out.println("Не удалось прочитать число.");
        } catch (java.time.DateTimeException e) {
            System.out.println("Неверный формат даты");
        }
    }
    
    /**
     * Метод сохраняющий коллекцию в файл.
     */
    public static void save(){ 
        try(OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(new File(PATH)))) {
            if (PATH.substring(0, 4).equals("/dev")) throw new NoSuchElementException();
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
}
