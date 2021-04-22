package client;

import tools.*;
import command.Command;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Main-class консоли.
 * Реализует управление консолью, хранит все глобальные данные.
 * @author Mike Stepanov P3130
 */
public class Client {
    
    private Socket clientSocket;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private BufferedInputStream consoleStream;
    private int PORT;
    private Speaker speaker;
    
    public static void main(String[] args) {
        Client client = new Client();
    }
    
    public Client() {
        try {
            System.out.println("Введите порт для подключения:");
            try {
                PORT = Integer.parseInt(new Scanner(System.in).nextLine());
            } catch(NumberFormatException e) {
                PORT = 4242;
                System.out.println("Введен неправильный формат порта, устанавливаем стандартное значение " + PORT);
            }
            clientSocket = new Socket("localhost", PORT);
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
            command = cp.choice(inputString);
            if (command != null) {
                try {
                    outStream.writeObject(command);
                    speaker = (Speaker)inStream.readObject();
                    speaker.println();
                } catch(IOException e) {
                    System.out.println("Не удалось отправить команду.");
                } catch(ClassNotFoundException e) {
                    System.out.println("Ответ пришел в некорректном формате!");
                }
            }
        }
        
    }
}