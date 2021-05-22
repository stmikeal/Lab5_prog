
package server;

import command.Command;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import tools.*;

/**
 * Класс реализующий связь между исполнителем и сервером, создавая новый поток.
 * @author mike
 */
public class SocketAdapter implements Runnable{
    
    private final Socket socket;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private Thread thread;
    private final String clientAddress;
    
    static {
        System.setProperty("logback.xml", "../logback.xml");
    }
    
    public SocketAdapter(Socket socket) throws IOException{ 
        /*
        Запоминаем сокет, открываем потоки ввода-вывода, запускаем новый поток.
        */
        this.socket = socket;
        clientAddress = socket.getInetAddress().toString();
        thread = new Thread(this);
        thread.start();
    }
    
    @Override
    public void run() {
        /*
        Получаем команду от пользователя.
        Пытаемся ее исполнить и отправить ответ в виде текста.
        Иначе отправляем стандартный ответ об ошибке.
        */
        Speaker message;
        try {
            outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.flush();
            inStream = new ObjectInputStream(socket.getInputStream());
            Command command = (Command) inStream.readObject();
            Executor executor = new Executor(command);
            message = executor.execute();
            if (message.getMessage().equals("exit\n")) {
                ServerLogger.logger.info("Пользователь ввел команду на отключение");
                thread = null;
            }
        } catch(IOException | ClassNotFoundException e){
            ServerLogger.logger.log(Level.WARNING,"Мы не смогли отправить команду", e);
            message = new Speaker("Извините, но у нас возникла проблема с отправкой команды.");
            message.error();
        }
        try {
            outStream.writeObject((Object)message);
        } catch(IOException e) {
            ServerLogger.logger.log(Level.WARNING,"Поток был прерван", e);
            System.out.println("Поток " + clientAddress + ": не удалось отправить ответ пользователю");
        }
    }
}
