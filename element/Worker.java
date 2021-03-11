package element;
import java.time.LocalDate;

public class Worker {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Double salary; //Поле может быть null, Значение поля должно быть больше 0
    private LocalDate startDate; //Поле не может быть null
    private Position position; //Поле может быть null
    private Status status; //Поле может быть null
    private Person person; //Поле может быть null
    
    public Worker(String name, Coordinates coordinates, Double salary, 
            java.time.LocalDate startDate, Position position, Status status,
            Person person){
        this.creationDate = LocalDate.now();
        this.name = name;
        this.coordinates = coordinates;
        this.salary = salary;
        this.startDate = startDate;
        this.position = position;
        this.status = status;
        this.person = person;
        char[] nameArr = name.toCharArray();
        double sum = 0;
        for(char c:nameArr)sum+=((int)c)*Math.random()/1024;
        this.id = (int)Math.round((sum+coordinates.getX()/(coordinates.getX()+3000000)+
                coordinates.getY()/(coordinates.getY()+3000000)+
                salary/(salary+3000000)+
                ((position!=null)?1/256:0)+
                ((status!=null)?1/512:0)+
                ((person!=null)?1/2048:0) )*100000000);
    }
    public int getId(){return id;}
}
