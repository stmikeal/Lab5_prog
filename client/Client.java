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
    private ByteArrayOutputStream byteStream;
    private Socket socket;
    private ObjectOutputStream outStream;
    private int PORT;
    private Speaker speaker;
    private final int REP = 5;
    private ByteArrayInputStream stream;
    private ByteArrayOutputStream passiveStream;
    
    public static void main(String[] args) {
        Client client = new Client();
    }
    
    public Client() {
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
        listen();
    }
    
    public final void listen() {
        String inputString;
        Command command;
        CommandParser cp = new CommandParser();
        Scanner scanner = new Scanner(System.in);
        while(scanner       .hasNext()) {
            inputString = scanner.nextLine();
            command= cp.choice(inputString);
            if (command != null) {
                try {
                    speaker = this.execute(command, 0);
                    if (speaker.getMessage().equals("exit\n")) {
                        System.out.println("Работа завершена. До свидания!");
                        System.exit(0);
                    }
                    if (speaker.getMessage().equals("unconnected\n")) {
                        System.out.println("Сервер не доступен, извините:( Выходим из программы...");
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
                } catch(ClassNotFoundException e){
                    System.out.println("Ответ пришел в некорректном формате!");
                } catch(InterruptedException e) {}
            }
        }
        
    }
    
    public Speaker execute(Command command, int stack) throws IOException, InterruptedException, ClassNotFoundException{
        Speaker speaker = null;
        if (stack > 5) {
            return new Speaker("unconnected");
        }
        if (stack > 0) {
            System.out.println("Попытка подключения номер " + stack);
        }
        try {
            this.connect("127.0.0.1", PORT);
            write(command);
            sleep(100);
            speaker = read();
        } catch(IOException e) {
            System.out.println("Неудачная попытка подключения...");
            return execute(command, stack + 1);
        }
        return speaker;
    }
    
    public void connect(String address, int PORT) throws IOException{
        clientSocket = SocketChannel.open(new InetSocketAddress(address, PORT));
        /*outStream = new ObjectOutputStream(clientSocket.socket().getOutputStream());
        outStream.flush();
        inStream = new ObjectInputStream(clientSocket.socket().getInputStream());*/
    }
    
    public void write(Command command) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(2048);
        outStream = new ObjectOutputStream(buffer);
        outStream.flush();
        outStream.writeObject((Object)command);
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer.toByteArray());
        clientSocket.write(byteBuffer);
    }
    
    public Speaker read() throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[2048];
        clientSocket.read(ByteBuffer.wrap(buffer));
        inStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
        return (Speaker) inStream.readObject();
    }
}