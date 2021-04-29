package element;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Класс из задачи.
 * Класс работника, составлен из варианта лабораторной работы.
 * @author mike
 */
public class Worker implements Serializable {
    private Integer id;
    private String name;
    private Coordinates coordinates;
    private LocalDate creationDate;
    private Double salary; 
    private LocalDate startDate;
    private Position position;
    private Status status;
    private Person person; 
    
    public Worker(String name, Coordinates coordinates, Double salary, 
            java.time.LocalDate startDate, Position position, Status status,
            Person person) {
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
    
    public Worker(int id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * Getter для id.
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Getter для name.
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Приведение статуса.
     * Приводит статус к числу.
     * @param status
     * @return int
     */
    public static int statusToInt(String status) {
        switch(status) {
            case "RECOMMENDED_FOR_PROMOTION" : return 2;
            case "FIRED" : return 1;
            case "REGULAR" : return 3;
            case "null": return 0;
            default: return -1;
        }
    }
    public static int statusToInt(Status status) {
        if (status==null) return 0;
        switch(status) {
            case RECOMMENDED_FOR_PROMOTION : return 2;
            case FIRED : return 1;
            case REGULAR : return 3;
            default: return -1;
        }
    }
    public int statusToInt() {
        status = this.status;
        return statusToInt(status);
    }
    
    @Override
    public String toString() {
        String result = "";           
        result += "id, "+id+"\n";
        result += "name, "+name+"\n";
        result += "coordinates, "+coordinates.getX()+", "+coordinates.getY()+"\n";
        result += "salary, "+Double.toString(salary)+"\n";
        result += "startDate, "+startDate.toString()+"\n";
        if (position==null) result += "position, null\n";
        else result += "position, "+position.toString()+"\n";
        if (status==null) result += "status, null\n";
        else result += "status, "+status.toString()+"\n";
        if (person==null) result += "person, null\n";
        else result += "person, "+person.toString()+"\n";
        return result;
    }
    
    public String toStringSave() {
        String result = "";
        result += name+"\n";
        result += coordinates.getX()+"\n"+coordinates.getY()+"\n";
        result += Double.toString(salary)+"\n";
        result += startDate.getYear()+"\n"+startDate.getMonthValue()+"\n"+startDate.getDayOfMonth()+"\n";
        if (position==null) result += "null\n";
        else result += position.toString()+"\n";
        if (status==null) result += "null\n";
        else result += status.toString()+"\n";
        if (person==null) result += "n\n";
        else result += "y\n"+person.toStringSave();
        return result;
    }
}
