package utils;

public class IsNumeric {
    public static boolean isNumeric(String str) {
        boolean res;
        try {
            Integer.parseInt(str);
            res = true;
        } catch (NumberFormatException e) {
            res = false;
        }
        return res;
    }
}
