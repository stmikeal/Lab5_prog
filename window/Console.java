package window;

public class Console {
    private static final String ENV_INPUT = "LAB_INPUT";
    private static final String ENV_OUTPUT = "LAB_OUTPUT";
    
    public static void main(String[] args){
        System.out.println(System.getenv(ENV_INPUT));
        System.out.println(System.getenv(ENV_OUTPUT));
    }
}
