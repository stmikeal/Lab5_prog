package command;

import server.DataManager;
import tools.ClientLogger;
import tools.Encoder;
import tools.Speaker;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;

public class CommandRegister extends Command{

    public CommandRegister(String ... args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите логин:");
            username = scanner.nextLine();
            System.out.println("Введите пароль:");
            password = scanner.nextLine();
            password = Encoder.encrypt(password);
            ready = true;
        } catch (NoSuchElementException e) {
            System.out.println("Поздравляю, вы сломали ввод. Можете так больше не делать?");
            ClientLogger.logger.log(Level.WARNING, "Во время ввода команды Login ошибка ввода.");
        }
    }

    @Override
    public Speaker event(DataManager collection) {
        boolean result = false;
        try {
            result = collection.register(username, password);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            return new Speaker("Ошибка доступа к базе данных пользователей.");
        }
        if (result) {
            Speaker speaker = new Speaker("Успешная регистрация.");
            speaker.setPrivateMessage1(username);
            speaker.setPrivateMessage2(password);
            return speaker;
        } else {
            return new Speaker("Логин уже занят.");
        }
    }
}
