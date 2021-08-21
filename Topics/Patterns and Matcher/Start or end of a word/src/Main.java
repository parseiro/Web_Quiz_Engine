import java.util.*;
import java.util.regex.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String part = scanner.nextLine();
        String line = scanner.nextLine();

        var pattern = Pattern.compile("\\b" + part + "|" + part + "\\b", Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(line).find()) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }
}