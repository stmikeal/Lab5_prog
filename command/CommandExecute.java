package command;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import tools.FileReader;
import tools.Speaker;
import client.Client;

/**
 * Класс-команда execute_script.
 *
 * @author mike
 */
public class CommandExecute {

    static Deque<String> stack = new ArrayDeque<>();

    /**
     * Выполняет скрипт из переданного файла.
     *
     * @param console
     * @param args
     */
    public static void event(Client console, String[] args) {
        String path;
        if (stack.contains(args[1])) {
            System.err.println("Ошибка. Один или несколько скриптов зациклены.");
            return;
        }
        try {
            path = args[1];
            if (path.substring(0, 4).equals("/dev")) throw new NullPointerException();
            stack.add(path);
            console.listen(FileReader.getStream(path));
            stack.removeLast();
        } catch (ArrayIndexOutOfBoundsException e) {
            Speaker.println("Не указан путь к файлу.");
        } catch (NullPointerException e) {
            Speaker.println("Введите нормальный файл!");
        } catch (Exception e) {
        }

    }
}
