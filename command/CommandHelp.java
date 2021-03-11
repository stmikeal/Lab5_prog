/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;
import window.Console;
import tools.Speaker;
/**
 *
 * @author mike
 */
public class CommandHelp {
    public static void event(Console console, String[] args){
        Speaker.hr();
        Speaker.println("help - справка по командам.",
                "info - вывести информацию о коллекции.",
                "show - вывести все элементы коллекции.",
                "add - добавляет элемент в коллекцию.",
                "update <id> - обновить элемент коллекции по id.",
                "remove_by_id <id> - удаляет элемент коллекции по id.",
                "clear - очищает коллекцию.",
                "save - сохранить коллекцию в файл.",
                "execute_script <path> - выполняет скрипт в файле.",
                "exit - завершает программу(без сохранения)",
                "add_if_min - добавляет элемент, если его значение наименьшее.",
                "remove_lower - удаляет из коллекции элементы меньше заданного.",
                "history - выводит последние 14 команд.",
                "filter_contains_name <name>- вывести элементы, имена которых"
                        + "содержат подстроку.",
                "filter_less_than_status <status>- вывести элементы, статус которых "
                        + "меньше аргумента.",
                "print_ascending - вывести элементы в порядке возрастания.");
        Speaker.hr();
    }
}
