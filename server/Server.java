package server;

import element.*;
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
import java.util.logging.Level;
import tools.*;

/**
 * Класс сервера.
 * Добавляет новых пользователей.
 * @author mike
 */
public class Server {
    
    private static int PORT;
    private static String PATH;
    static TreeSet<Worker> collection = new TreeSet<Worker>();
    private static LocalDate createDate = LocalDate.now();
    
    static {
        Comparator<Worker> comparator = (o1,o2)->
               ((Integer)o1.getId()).compareTo((Integer)o2.getId());
        collection = new TreeSet(comparator);
    }
    
    
    
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        
        Runtime.getRuntime().addShutdownHook(
                new Thread(
                        () -> {
                            try {
                                Thread.sleep(200);
                                ServerLogger.logger.log(Level.INFO, "Выключение сервера");
                                System.out.println("\nВыключаем сервер ...");
                                save();
                            } catch(InterruptedException e) {
                                ServerLogger.logger.log(Level.INFO, "Мы даже выключить нормально не смогли");
                                System.out.println("\nНе удалось сохранить коллекцию");
                            }
                        }
                )
        );
        
        Class.forName("org.postgresql.Driver");
        /*
        Блок чтения из переменной окружения.
        При неудачной попытке выставляем значение по умолчанию.
        Читаем коллекцию из файла.0
        */
        try {
            PATH = EnvReader.getenv("LAB_PATH");
        } catch(EnvException e) {
            ServerLogger.logger.log(Level.WARNING,"Ошибка чтения переменной среды", e);
            System.out.println("Неудачно загружена переменная среды, загружаем значение по умолчанию.");
            PATH = "data";
        }
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
            ServerLogger.logger.log(Level.WARNING,"Введено некорректное значение порта", e);
            System.out.println("Ошибка чтение порта, загружено значение по умолчанию - 4242.");
        } catch(NoSuchElementException e) {
            ServerLogger.logger.log(Level.WARNING,"Введен некорректный символ при чтении порта", e);
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
            ServerLogger.logger.log(Level.WARNING,"Сокет не был запущен", e);
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
                    ServerLogger.logger.log(Level.INFO, "Запущен адаптер для " + 
                            socket.getInetAddress().toString());
                } catch(IOException e) {
                    ServerLogger.logger.log(Level.WARNING,"Не был запущен адаптер", e);
                    System.out.println("Соединение было прервано. Пользователь: " 
                            + socket.getInetAddress().toString());
                    socket.close();
                }
            }
        } catch(IOException e) {
            System.out.println("Сервер умер, сори");
            ServerLogger.logger.log(Level.WARNING,"Прервано прослушивание подключений", e);
        } finally {
            server.close();
        }
    }
    
    public static LocalDate getDate() {
        return createDate;
    }
    
    private static void load(){
        Deque<String> text = new ArrayDeque<>();
        String[] elements;
        int size = 0;
        try(BufferedInputStream reader = FileReader.getStream(PATH)) {
            if (PATH.length() > 4) {
                if (PATH.substring(0, 4).equals("/dev")) { 
                    ServerLogger.logger.log(Level.WARNING,"Файл из переменной окружения - устройство");
                    throw new NullPointerException();
                }
            }
            Scanner scanner = new Scanner(reader);
            
            while(scanner.hasNext()) {
                elements = scanner.nextLine().trim().split(",");
                for(String iter:elements) {
                    String newElem = iter.trim();
                    if (!newElem.equals("")) text.add(newElem);
                }
            }
            size = text.size();
            
            int year = Integer.parseInt(text.pop());
            int month = Integer.parseInt(text.pop());
            int day = Integer.parseInt(text.pop());
            createDate = LocalDate.of(year, month, day);
            int elem = Integer.parseInt(text.pop());
            Boolean isValid = true;
            
            for(int i=0; i<elem; i++) {
                try{
                    String name = text.pop();
                    Coordinates coordinates = new Coordinates(Double.parseDouble(text.pop()),
                        Double.parseDouble(text.pop()));
                    if (coordinates.getX() < -622) throw new NumberFormatException();
                    Double salary = Double.parseDouble(text.pop());
                    if (salary < 0) throw new NumberFormatException();
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
                } catch(NumberFormatException e) {
                    ServerLogger.logger.log(Level.WARNING,"Число не верно прочитано в строке" + (size-text.size()), e);
                    System.out.println("Не удалось прочитать число в строке "+ (size-text.size()) + ".");
                } catch (java.time.DateTimeException e) {
                    ServerLogger.logger.log(Level.WARNING,"Неверный формат даты", e);
                    System.out.println("Неверный формат даты создания коллекции в строке " + (size-text.size()));
                } catch(NoSuchElementException | NullPointerException e) {
                    ServerLogger.logger.log(Level.WARNING,"Не удалось создать объект в строке "+(size-text.size()) , e);
                    System.out.println("Не удалось создать объект в строке "+(size-text.size()));
                }
                
            }
            ServerLogger.logger.log(Level.INFO, "Смогли прочитать коллекцию из файла");
            System.out.println("Коллекция прочитана корректно");
            reader.close();
        } catch(IOException | NoSuchElementException | NullPointerException e) {
            ServerLogger.logger.log(Level.WARNING,"Не удалось открыть или создать файл", e);
            System.out.println("Не удалось открыть или создать файл.\n" +
                   "Попробуйте изменить путь в переменной окружения.");
        } catch (NumberFormatException e) {
            ServerLogger.logger.log(Level.WARNING,"Число не верно прочитано в строке " + (size-text.size()), e);
            System.out.println("Не удалось прочитать число в строке "+ (size-text.size()) + ".");
        } catch (java.time.DateTimeException e) {
            ServerLogger.logger.log(Level.WARNING,"Неверный формат даты", e);
            System.out.println("Неверный формат даты создания коллекции.");
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
            ServerLogger.logger.log(Level.WARNING,"Не удалось сохранить коллекцию", e);
            Speaker.println("Не удалось корректно сохранить коллекцию.");
        }
    }
}
