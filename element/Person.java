package element;

import java.io.Serializable;

/**
 * Класс личных данных. Хранит внешние данные работника.
 *
 * @author mike
 */
public class Person implements Serializable {

    private Integer height; //Поле не может быть null, Значение поля должно быть больше 0
    private Color eyeColor; //Поле не может быть null
    private Color hairColor; //Поле не может быть null
    private Country nationality; //Поле не может быть null

    public Person(Integer height, Color eyeColor, Color hairColor, Country nationality) {
        this.height = height;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        String result = Integer.toString(height);
        if (eyeColor == null) {
            result += ", null";
        } else {
            result += ", " + eyeColor.toString();
        }
        if (hairColor == null) {
            result += ", null";
        } else {
            result += ", " + hairColor.toString();
        }
        if (nationality == null) {
            result += ", null";
        } else {
            result += ", " + nationality.toString();
        }
        return result;
    }

    public String toStringSave() {
        String result = Integer.toString(height) + "\n";
        if (eyeColor == null) {
            result += "null\n";
        } else {
            result += ", " + eyeColor.toString();
        }
        if (hairColor == null) {
            result += ", null\n";
        } else {
            result += ", " + hairColor.toString();
        }
        if (nationality == null) {
            result += ", null\n";
        } else {
            result += ", " + nationality.toString();
        }
        return result;
    }
}
