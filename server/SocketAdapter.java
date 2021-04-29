
package server;

import command.Command;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import tools.Speaker;

/**
 * Класс реализующий связь между исполнителем и сервером, создавая новый поток.
 * @author mike
 */
public class SocketAdapter implements Runnable{
    
    private Socket socket;
    private ObjectInputStream inStream;
    OutputStream clientOutStream;
    private ObjectOutputStream outStream;
    private Thread thread;
    private String clientAddress;
    private String[] history = new String[14];
    
    public SocketAdapter(Socket socket) throws IOException{ 
        /*
        Запоминаем сокет, открываем потоки ввода-вывода, запускаем новый поток.
        */
        this.socket = socket;
        clientAddress = socket.getInetAddress().toString(); 
        clientOutStream = socket.getOutputStream();
        clientOutStream.flush();
        outStream = new ObjectOutputStream(clientOutStream);
        outStream.flush();
        inStream = new ObjectInputStream(socket.getInputStream());
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
        while (thread != null){
            try {
                Command command = (Command) inStream.readObject();
                Executor executor = new Executor(command);
                message = executor.execute();
                if (message.getMessage().equals("exit\n")) {
                    System.out.println("Пользователь " + clientAddress +" отключился.");
                    thread = null;
                }
            } catch(IOException | ClassNotFoundException e){
                message = new Speaker("Извините, но у нас возникла проблема с отправкой команды.");
                message.error();
            }
            /*try {
                outStream.writeObject((Object)message);
            } catch(IOException e) {
                System.out.println("Поток " + clientAddress + ": не удалось отправить ответ пользователю");
            }*/
            try {
                clientOutStream.flush();
                outStream = new ObjectOutputStream(this.clientOutStream);
                outStream.flush();
                outStream.writeObject((Object)message);
            } catch(IOException e) {
                System.out.println("Пользователь " + clientAddress + " отключился вне штатного режима.");
                thread = null;
            }
        }
    }
}
