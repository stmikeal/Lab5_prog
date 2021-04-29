package client;

import tools.*;
import command.Command;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Main-class консоли.
 * Реализует управление консолью, хранит все глобальные данные.
 * @author Mike Stepanov P3130
 */
public class Client {
    
    private SocketChannel clientSocket;
    private ObjectInputStream inStream;
    private Socket socket;
    private ObjectOutputStream outStream;
    private int PORT;
    private Speaker speaker;
    private final int REP = 5;
    
    public static void main(String[] args) {
        Client client = new Client();
    }
    
    public Client() {
        try {
            System.out.println("Введите порт для подключения:");
            try {
                PORT = 4242;
                PORT = Integer.parseInt(new Scanner(System.in).nextLine());
                if (PORT<=1024){
                    throw new NumberFormatException();
                }
            } catch(NumberFormatException e) {
                System.out.println("Введен неправильный формат порта, устанавливаем стандартное значение " + PORT);
            } catch(NoSuchElementException e) {
                System.out.println("Зачем вы ломаете программу?! Ни мучий, апути.");
                System.out.println("Устанавливаем значение по умолчанию - 4242.");
            }
            this.connect("127.0.0.1", PORT);
            System.out.println("Мы удачно подключились к серверу!");
        } catch(IOException e) {
            System.out.println("Простите, но сейчас сервер закрыт для посещений!");
            System.exit(1337);
        }
        
        listen();
    }
    
    public final void listen(){
        String inputString;
        Command command;
        CommandParser cp = new CommandParser();
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            inputString = scanner.nextLine();
            command= cp.choice(inputString);
            if (command != null) {
                try {
                    write(command);
                    sleep(1000);
                    speaker = read();
                    if (speaker.getMessage().equals("exit\n")) {
                        System.out.println("Работа завершена. До свидания!");
                        System.exit(0);
                    }
                    speaker.println();
                } catch(IOException e) {
                    e.printStackTrace();
                    System.out.println("Сервер больше не доступен, пытаемся переподключиться ...");
                        int i;
                        for (i = 1; i <= REP; i++) {
                            try {
                                System.out.println("Попытка переподключения " + 
                                        ((Integer)i).toString() + ":");
                                this.connect("127.0.0.1", PORT);
                                System.out.println("Попытка удачная!");
                                break;
                            } catch(IOException ex) {
                                System.out.println("Неудачная попытка...");
                            } finally {
                                try {
                                    sleep(200);
                                } catch(InterruptedException exp) {}

                            }
                        }
                        if (i > REP) {
                            System.out.println("Сервер, видимо, отключен "
                                    + ", завершаем работу");
                            System.exit(404);
                        }
                } catch(ClassNotFoundException e) {
                    System.out.println("Ответ пришел в некорректном формате!");
                } catch(InterruptedException e) {}
            }
        }
        
    }
    
    public void connect(String address, int PORT) throws IOException{
        clientSocket = SocketChannel.open(new InetSocketAddress(address, PORT));
    }
    
    public void write(Command command) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(2048);
        outStream = new ObjectOutputStream(byteStream);
        outStream.writeObject((Object)command);
        outStream.flush();
        ByteBuffer buffer = ByteBuffer.wrap(byteStream.toByteArray());
        clientSocket.write(buffer);
    }
    
    public Speaker read() throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[2048];
        clientSocket.read(ByteBuffer.wrap(buffer));
        inStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
        return (Speaker) inStream.readObject();
    }
}