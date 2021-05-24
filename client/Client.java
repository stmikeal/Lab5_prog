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
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Main-class консоли.
 * Реализует управление консолью, хранит все глобальные данные.
 * @author Mike Stepanov P3130
 */
public class Client {
    
    private SocketChannel clientSocket;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private int PORT;
    private Speaker speaker;
    private final int REP = 20;
    private final InetSocketAddress address;
    
    static {
        System.setProperty("logback.xml", "../logback.xml");
    }
    
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(
                new Thread(
                        () -> {
                            ClientLogger.logger.log(Level.INFO, "Выключение клиента");
                            System.out.println();
                            Client.printCat();
                        }
                )
        );
        ClientLogger.logger.log(Level.INFO, "Клиент запущен");
        Client client = new Client();
    }
    
    public Client() {
        System.out.println("Введите порт для подключения:");
        try {
            PORT = 4242;
            PORT = Integer.parseInt(new Scanner(System.in).nextLine());
            if (PORT<=1024||PORT>=65536){
                throw new NumberFormatException();
            }
        } catch(NumberFormatException e) {
            ClientLogger.logger.log(Level.WARNING, "Введен неправильный формат порта", e);
            PORT = 4242;
            System.out.println("Введен неправильный формат порта, устанавливаем стандартное значение " + PORT);
        } catch(NoSuchElementException e) {
            ClientLogger.logger.log(Level.WARNING,"Введен некорректный символ при чтении порта", e);
            PORT = 4242;
            System.out.println("Зачем вы ломаете программу?! Ни мучий, апути.");
            System.out.println("Устанавливаем значение по умолчанию - 4242.");
        }
        ClientLogger.logger.log(Level.INFO, "Клиент работает на порту: " + PORT);
        address = new InetSocketAddress("127.0.0.1", PORT);
        
        System.out.println("Добрый день, мы рады вас приветствовать в этой программе,"
                + "\nДля справки введите help.");
        
        listen();
    }
    
    public final void listen() {
        
        String inputString;
        Command command;
        CommandParser cp = new CommandParser();
        Scanner scanner = new Scanner(System.in);
        
        while(scanner.hasNext()) {
            
            inputString = scanner.nextLine();
            command= cp.choice(inputString);
            if (!inputString.equals("")) {
                ClientLogger.logger.log(Level.INFO, "Введена команда " + inputString);
            }
            
            if (command != null) {
                try {
                    speaker = this.execute(command, 0);
                    
                    if (speaker.getMessage().equals("exit\n")) {
                        ClientLogger.logger.log(Level.INFO, "Введено exit, выходим");
                        System.out.println("Работа завершена. До свидания!");
                        System.exit(0);
                    }
                    
                    if (speaker.getMessage().equals("unconnected\n")) {
                        ClientLogger.logger.log(Level.WARNING, "Ошибка подключения к серверу");
                        System.out.println("Сервер не доступен, извините:( Выходим из программы...");
                        System.exit(0);
                    }
                    
                    speaker.println();
                } catch(ClassNotFoundException e){
                    ClientLogger.logger.log(Level.INFO, "Ответ от сервера в неккоректном формате", e);
                    System.out.println("Ответ пришел в некорректном формате!");
                } catch(InterruptedException e) {
                    ClientLogger.logger.log(Level.INFO, "Прерван поток во время чтения", e);
                    System.out.println("О нет! Кажется, я умираю...");
                }
            }
        }
        
    }
    
    public Speaker execute(Command command, int stack) throws InterruptedException, ClassNotFoundException{
        Speaker tempSpeaker;
        if (stack > REP) {
            return new Speaker("unconnected");
        }
        if (stack > 0) {
            ClientLogger.logger.log(Level.INFO, "Попытка подключения номер " + stack);
        }
        try {
            this.connect();
            write(command);
            sleep(100);
            tempSpeaker = read();
        } catch(IOException e) {
            ClientLogger.logger.log(Level.INFO, "Неудачная попытка подключения номер " + stack);
            return execute(command, stack + 1);
        }
        return tempSpeaker;
    }
    
    public void connect() throws IOException{
        clientSocket = SocketChannel.open(address);
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
    
    public static void printCat() {
        System.out.println("########################%+::*****++*+=++****+*+*::+++*+::--./&!!&&/\\-.-@#%%%%%%%%%%%%%%%%%%@@@@@@@@@\n" +
"#######################%\\|||||&!!&|/-*****++*+**-::::*+:::-.\\||!&///\\\\\\=###%%=%%%%%%%%%%%%%%@@@@@@@@\n" +
"#######################=/|&&|||||//\\-*++***:--::---.\\.---:-.//||||\\\\|!|-@##@===%%%%%%%%%%%%%%%@@%%@@\n" +
"########################=||||//\\\\.\\.:++*:---.\\\\\\..\\\\-\\\\.---.\\\\\\/&&\\/|&&:####%===%%%%%%%%%%%%%%%%%%@@\n" +
"########################@*||//\\--...:*++:-:--.\\||||&&|/\\..--\\\\\\\\//||/&!-@###@=%===%%=%%%%%%%%%%%%%@@\n" +
"#########################@\\||/\\.----::*:---.|!&!;$;;№!||/\\.-./\\/\\.\\///.%#####=====%=====%%%%%%%%%%%%\n" +
"#########################@+||/\\.*::------\\.-|!&&$;;;;^;№&/.-.\\///\\...\\-*%@###%=========%%%%%%%%%%%%%\n" +
"###########################=&&|-*:-..\\-:-&№.|!|/$'\"\"''^№&|//.\\-.-**:.-:=%@###@=========%==%=%%%%%%%%\n" +
"############################%:*+**+:-\\\\..:--!;!№^]]\"''!+%:+@%*/.-:*::-:*=@####@#####%========%%%%%%%\n" +
"#############################%===++*:\\:@@=.**!;;^\"\"'';/\\/*.=.&/-\\/\\.-::*+%@##########@======%=%%%%%%\n" +
"##############################%+*:--:/|*%@=:*|$$$$^^;;№№$$;!/..\\\\\\\\//\\.:%################%==%%%%%%%%\n" +
"##############################@=*.\\\\\\-.&;^'';^^№;$^^$;'\"]]]'^!||!&||//-+@####################%%%%%%%\n" +
"##############################@+:-.\\|&№$'\"\"\"\"';&№;$;№^\"]'\"\"\";$;;№&&&|.:+%######################@%%%%\n" +
"###############################=*-\\|&!!;^;^^$;;!&№№№$;^^^^;;$^^^^$;!&/:%%@#######################%%%\n" +
"###############################%:.\\\\|!!!!№$^^^^$№!&!№;;;;$^^;;^^$;$;!|.*=%@@######################%%\n" +
"################################%**.\\|!№№!&!№;;№!&&!&!№№;;$^^^$$^;;^№&\\-+%@@#######################%\n" +
"#################################@=://\\|!№№!!!№№;;$$^$^^^^$^^;''''';$№!/:*%@@@@#####################\n" +
"###############################@@@+:*:./||&&!№№;;;$$$$$$$^^;''\"\"\"\"\";$;!/-*=%@@@@@@#############%==%%\n" +
"##############################@@%=%=:./&№;^^$$^$$;;$^^;'''\"\"\"\"\"\"\"\"\";$№&/.*+%%@@@@############@+++===\n" +
"#############################@%=%%%=*:./|№$^;'\"\"'''\"\"\"\"]]]]]]\"]\"\"\"';$!&/.:+%@@@@@@@#########========\n" +
"#############################@%=+==+*:-\\|!;^;']\"\"]]]]]]]]]]\"\"\"\"\"\"';^$!&/.:*=%@@@@@@#######%*++++++==\n" +
"#############################@@=+==++:-/|&!№;;''\"\"\"]\"]]\"\"\"\"\"\"\"'';;^$!&|\\-:+%@@@#@@@#####@*******++++\n" +
"############################@%=+*++**:-../&!&!;^;''\"\"\"\"\"''''';;;^$№!|||/-*+%@@@@@@@@#@@==+****+*++++\n" +
"##################@#########@%=+*++*:--.\\/\\/|&!№;$^;;;;'\"'';;;^$№&&||///.:+%@@@@@@##%+++=+++++*****+\n" +
"############################@%%=*++****--.\\/|!!!!!№;;$^;;;;^^^$;!&|/|//\\.:+%@@@@@#@%+++++++++*+***++\n" +
"############################@@%==+*::::-../\\.\\|!!№!!!№;$$$$^$;;$;№!&||/\\.-:*=@####%+++++++++******++\n" +
"############################@%%=+*++***=*::::./&&!;№№!№№№!&!&&&&№;№!!||///\\.=@#####%=++++++++++++++*\n" +
"#############################%=++=+***+==++:./&&!!№№!!№;$!&&&&&&;^^$$№&||//:=@#######@=*+++++=++++++\n" +
"##############################%++==+++*+++*:..\\/|&&!&!;^$№|//\\&№;$$$$;!&&|-+%@#@@@@@@##=**++*+%=++*+\n" +
"###############################@%++**++++*:-.-\\/\\/\\&№$^^$№/\\:+/;;№$$$;;;№!\\+======%%%%@@#=**+**=%+*+\n" +
"#################################@=+*::*+=+-.//|!№$$$;$$$;$$;№№№№;;;;;&№№№&\\*==++++==%%%@##=*=+*+%=+\n" +
"###########################@@@@###@@=.\\.-*%=+\\&!&!!&|!!!№;$$;№&//\\.---::***++++=+=====%%%###%==++++=\n" +
"#################@@@@@@@@@%%%%%%===++**:::::------::-.\\\\\\\\\\.....--:****+++++++++++=+==%%%@###@===+++\n" +
"##########@@@@@@@@@@%%%==+++****:::::::----:::-------.--::-:::-:-:--::::****++++++++++====%%@@======\n" +
"######@@@@@@@%%%=+++************:********:::::*:::----..-.......-----:::::*************++=%%@##@####\n" +
"#@@@@%%%======++++++++++++==++************:::::--...\\.\\.\\\\\\\\\\/\\\\.....----------::::-:::::*+=%@######\n" +
"%%%%%%%%%%%=====%===+++*****:::----::--:--.....\\\\\\//\\/////|/||||///////\\\\\\\\\\\\\\\\\\..\\....---::*=@#####\n" +
"%%%%%%%%==++++++*****:**::------.-.\\\\\\.\\\\\\\\\\\\\\////////|||/|/|||||||||||||||||//////\\\\\\\\...-:**===%=%\n" +
"");
    }
}