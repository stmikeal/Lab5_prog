package element;

import java.io.IOException;
import java.io.Serializable;

/**
 * Перечисление должностей.
 *
 * @author mike
 */
public enum Position implements Serializable {
    DIRECTOR,
    ENGINEER,
    HEAD_OF_DIVISION;
    public static Position fromString(String arg) {
        switch (arg) {
            case "DIRECTOR": return Position.DIRECTOR;
            case "ENGINEER": return Position.ENGINEER;
            case "HEAD_OF_DIVISION": return Position.HEAD_OF_DIVISION;
            default: return null;
        }
    }
}
