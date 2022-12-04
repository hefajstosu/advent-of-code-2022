import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";

        long part1 = 0, part2 = 0;
        for (String line : Files.readAllLines(Path.of(path))) {
            String s = line.trim();
            if (s.isEmpty()) continue;
            String[] p = s.split("[-,]");
            long a = Long.parseLong(p[0]), b = Long.parseLong(p[1]);
            long c = Long.parseLong(p[2]), d = Long.parseLong(p[3]);

            // czy zawiera
            if ((a <= c && d <= b) || (c <= a && b <= d)) part1++;
            // czy nachodza
            if (a <= d && c <= b) part2++;
        }

        System.out.println("Czesc 1: " + part1);
        System.out.println("Czesc 2: " + part2);
    }
}
