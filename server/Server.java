package server;

import element.*;
import exception.EnvException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
    private static String username;
    private static String password;

    static {
        Comparator<Worker> comparator = (o1, o2) ->
                ((Integer) o1.getId()).compareTo((Integer) o2.getId());
        collection = new TreeSet(comparator);
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Runtime.getRuntime().addShutdownHook(
                new Thread(
                        () -> {
                            try {
                                Thread.sleep(200);
                                ServerLogger.logger.log(Level.INFO, "Выключение сервера");
                                System.out.println("\nВыключаем сервер ...");
                            } catch (InterruptedException e) {
                                ServerLogger.logger.log(Level.INFO, "Мы даже выключить нормально не смогли");
                                System.out.println("\nНе удалось сохранить коллекцию");
                            }
                        }
                )
        );


        try {
            Scanner filereader = new Scanner(FileReader.getStream("../userdata"));
            username = filereader.nextLine().trim();
            password = filereader.nextLine().trim();
        } catch (FileNotFoundException e) {
            System.out.println("Файл с данными для входа не найден.");
            ServerLogger.logger.log(Level.WARNING, "Не найден userdata.");
            System.exit(122);
        } catch (NoSuchElementException e) {
            System.out.println("В файле не найдены данные для входа.");
            ServerLogger.logger.log(Level.WARNING, "userdata не содержит данных.");
            System.exit(221);
        }
        Class.forName("org.postgresql.Driver");
        DatabaseHandler DH = new DatabaseHandler("jdbc:postgresql://localhost:3175/studs", username, password);
        DH.connect();

        
        /*
        Блок чтения порта.
        При неудачном чтении выставляется значение по умолчанию.
        */
        try {
            PORT = 4242;
            System.out.println("Введите порт для подключения: ");
            PORT = Integer.parseInt(new Scanner(System.in).nextLine());
            if (PORT <= 1024) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            ServerLogger.logger.log(Level.WARNING, "Введено некорректное значение порта", e);
            System.out.println("Ошибка чтение порта, загружено значение по умолчанию - 4242.");
        } catch (NoSuchElementException e) {
            ServerLogger.logger.log(Level.WARNING, "Введен некорректный символ при чтении порта", e);
            System.out.println("Зачем вы ломаете программу?! Ни мучий, апути.");
            System.out.println("Устанавливаем значение по умолчанию - 4242.");
        }
        
        
        /*
        Открываем сервер на прослушку на полученном порте
        */
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            ServerLogger.logger.log(Level.WARNING, "Сокет не был запущен", e);
            System.out.println("Проблема с запуском сервера, проверьте настройки...");
            System.exit(1337);
        }
        
        /*
        Ждем пока поступит запрос на подключение.   
        Когда приходит подключение, запускаем поток, начинаем работу с юзером.
        */
        try {
            while (true) {
                Socket socket = server.accept();

                try {
                    SocketAdapter adapter = new SocketAdapter(socket);
                    ServerLogger.logger.log(Level.INFO, "Запущен адаптер для " +
                            socket.getInetAddress().toString());
                } catch (IOException e) {
                    ServerLogger.logger.log(Level.WARNING, "Не был запущен адаптер", e);
                    System.out.println("Соединение было прервано. Пользователь: "
                            + socket.getInetAddress().toString());
                    socket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Сервер умер, сори");
            ServerLogger.logger.log(Level.WARNING, "Прервано прослушивание подключений", e);
        } finally {
            server.close();
        }
    }

    public static LocalDate getDate() {
        return createDate;
    }
}

