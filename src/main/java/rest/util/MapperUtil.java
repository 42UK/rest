package rest.util;

public class MapperUtil {

    public static int parseInteger(String input) {
        Integer id = 0;
        if (input != null) {
            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                return id;
            }
        }
        return id;
    }
}
