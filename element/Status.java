package element;

import java.io.Serializable;

/**
 * Перечисление статус.
 *
 * @author mike
 */
public enum Status implements Serializable {
    FIRED,
    RECOMMENDED_FOR_PROMOTION,
    REGULAR;
    public static Status fromString(String arg) {
        switch (arg) {
            case "FIRED": return Status.FIRED;
            case "RECOMMENDED_FOR_PROMOTION": return Status.RECOMMENDED_FOR_PROMOTION;
            case "REGULAR": return Status.REGULAR;
            default: return null;
        }
    }
}
