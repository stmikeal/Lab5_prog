
package server;

import command.Command;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import tools.Speaker;

/**
 * Класс реализующий связь между исполнителем и сервером, создавая новый поток.
 * @author mike
 */
public class SocketAdapter implements Runnable{
    
    private Socket socket;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private Thread thread;
    private String[] history = new String[14];
    
    public SocketAdapter(Socket socket) throws IOException{ 
        /*
        Запоминаем сокет, открываем потоки ввода-вывода, запускаем новый поток.
        */
        this.socket = socket;
        inStream = new ObjectInputStream(socket.getInputStream());
        outStream = new ObjectOutputStream(socket.getOutputStream());
        thread = new Thread(this);
    }
    
    @Override
    public void run() {
        /*
        Получаем команду от пользователя.
        Пытаемся ее исполнить и отправить ответ в виде текста.
        Иначе отправляем стандартный ответ об ошибке.
        */
        Speaker message;
        while (true){
            try {
                Command command = (Command) inStream.readObject();
                Executor executor = new Executor(command);
                message = executor.execute();
            } catch(IOException | ClassNotFoundException e){
                message = new Speaker("Извините, но у нас возникла проблема с отправкой команды.");
                message.error();
            }
            try {
                outStream.writeObject((Object)message);
            } catch(IOException e) {
                System.out.printf("Поток {}: не удалось отправить ответ пользователю\n", thread.getName());
            }
        }
    }
}
