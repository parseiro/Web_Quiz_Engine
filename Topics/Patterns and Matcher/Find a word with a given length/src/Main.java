import java.util.*;
import java.util.regex.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int size = Integer.parseInt(scanner.nextLine());
        String line = scanner.nextLine();

        var pattern = Pattern.compile("\\b[a-zA-Z]{" + size + "}\\b");
        if (pattern.matcher(line).find()) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }
}