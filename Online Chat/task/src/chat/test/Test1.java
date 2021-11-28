package chat.test;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Test1 {
    public static void test() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String  line = scanner.nextLine();
            if (line.contains("sent")){
                List<String > lineComponents = Arrays.asList(line.split(" "));
                lineComponents.set(1,":");
                StringBuilder sb = new StringBuilder();
                lineComponents.forEach(str -> {
                    sb.append(str);
                    if (lineComponents.indexOf(str) != 0)
                        sb.append(" ");
                });
                System.out.println(sb);

            }
        }

    }
}
