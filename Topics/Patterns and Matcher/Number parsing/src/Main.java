import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String number = scanner.nextLine();

            if (isNumber2(number)) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }
        }
    }

    private static boolean isNumber2(String number) {
        final Pattern WHOLE_NUMBER = Pattern.compile("^[-+]?([1-9][0-9]*|0)([,.](0|[0-9]*[1-9]))?");
//        final Pattern SEPARATOR = Pattern.compile("[,.]");
//        final Pattern RIGHT_NUMBER = Pattern.compile("0|[0-9]*[1-9]");

        if (number == null
                || !WHOLE_NUMBER.matcher(number).matches()) {
            return false;
        }

        return true;
    }

    private static boolean isNumber(String number) {
        final Pattern LEFT_NUMBER = Pattern.compile("^[-+]?([1-9][0-9]*|0)");
        final Pattern SEPARATOR = Pattern.compile("[,.]");
        final Pattern RIGHT_NUMBER = Pattern.compile("0|[0-9]*[1-9]");

        var parts = SEPARATOR.split(number);

        if (parts == null || parts.length > 2) {
            return false;
        }

        if (!LEFT_NUMBER.matcher(parts[0]).matches()) {
            return false;
        }

        if (parts.length == 2) {
//                    System.out.println("Analisando a segunda parte: " + parts[1]);
            if (!RIGHT_NUMBER.matcher(parts[1]).matches()) {
                return false;
            }
        }

        return true;
    }
}