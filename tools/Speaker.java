package tools;

import java.util.Scanner;
import java.io.InputStream;
import java.io.Serializable;
/**
 * Класс "разговорник".
 * Помогает красиво выводить разную информацию.
 * @author mike
 */
public class Speaker implements Serializable {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String hr = "----------------------------------------------------";
    private String message;
    
    
    public String getMessage() {
        return this.message;
    }
    /**
     * Вывод строк.
     * Выводит каждый аргумент с новой строки.
     * @param args
     */
    public static void println(String ... args){
        for(String s:args){
            System.out.println(s);
        }
    }
    
    public void println(){
        hr();
        System.out.print(this.message);
        hr();
    }
    
    public void error(){
        message = ANSI_RED + message + ANSI_RESET;
    }
    
    public void success() {
        message = ANSI_GREEN + message + ANSI_RESET;
    }
    
    public Speaker(String ... args){
        this.message = "";
        for(String arg:args){
            message += arg + "\n";
        }
    }
   
    /**
     * Вывод строк.
     * Выводит все аргументы с новой строки, с заданным цветом.
     * @param color
     * @param args
     */
    public static void println(FontColor color, String ... args){
        System.out.print(color.toString());
        println(args);
        System.out.print(ANSI_RESET);
    }
    
    public static String scanString(){
        return scanner.nextLine();
    }
    
    public static String scanString(String s){
        System.out.println(s);
        return scanString();
    }
    
    public static String scanStream(InputStream stream){
        Scanner reader = new Scanner(stream);
        return reader.nextLine();
    }
    
    /**
     * Полосочка.
     * Выводит длинную полоску для разделения информации.
     */
    public static void hr(){System.out.println(hr);}
    
    /**
     * Перечисление доступных цветов для текста.
     */
    public static enum FontColor implements Serializable{
        BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE;
        
        @Override
        public String toString(){
            switch(this){
                case BLACK: return ANSI_BLACK;
                case RED: return ANSI_RED;
                case GREEN: return ANSI_GREEN;
                case YELLOW: return ANSI_YELLOW;
                case BLUE: return ANSI_BLUE;
                case PURPLE: return ANSI_PURPLE;
                case CYAN: return ANSI_CYAN;
                case WHITE: return ANSI_WHITE;
                default: return ANSI_RESET;
            }
        }
    }
}
