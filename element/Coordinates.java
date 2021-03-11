package element;


public class Coordinates {
    private double x; //Значение поля должно быть больше -623
    private double y;
    public Coordinates(double x, double y){
        this.x = x;
        this.y = y;
    }
    public double getX(){return x;}
    public double getY(){return y;}
}
