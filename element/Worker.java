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
    
    public Worker(int id){
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public int getId(){return id;}
    public String getName(){return name;}
    public static int statusToInt(String status){
        switch(status){
            case "RECOMMENDED_FOR_PROMOTION" : return 2;
            case "FIRED" : return 1;
            case "REGULAR" : return 3;
            case "null": return 0;
            default: return -1;
        }
    }
    public static int statusToInt(Status status){
        if(status==null)return 0;
        switch(status){
            case RECOMMENDED_FOR_PROMOTION : return 2;
            case FIRED : return 1;
            case REGULAR : return 3;
            default: return -1;
        }
    }
    public int statusToInt(){
        status = this.status;
        return statusToInt(status);
    }
    
    @Override
    public String toString(){
        String result = "";
        result += "name, "+name+"\n";
        result += "coordinates, "+coordinates.getX()+", "+coordinates.getY()+"\n";
        result += "salary, "+Double.toString(salary)+"\n";
        result += "startDate, "+startDate.toString()+"\n";
        if(position==null)result += "position, null\n";
        else result += "position, "+position.toString()+"\n";
        if(status==null)result += "status, null\n";
        else result += "status, "+status.toString()+"\n";
        if(person==null)result += "person, null\n";
        else result += "person, "+person.toString();
        return result;
    }
    
    public String toStringSave(){
        String result = "";
        result += name+"\n";
        result += coordinates.getX()+"\n"+coordinates.getY()+"\n";
        result += Double.toString(salary)+"\n";
        result += startDate.getYear()+"\n"+startDate.getMonth()+"\n"+startDate.getDayOfMonth()+"\n";
        if(position==null)result += "null\n";
        else result += position.toString()+"\n";
        if(status==null)result += "null\n";
        else result += status.toString()+"\n";
        if(person==null)result += "n\n";
        else result += "y\n"+person.toStringSave();
        return result;
    }
}
