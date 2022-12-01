import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<String> lines = Files.readAllLines(Path.of(path));

        // sumujemy kalorie
        List<Long> sums = new ArrayList<>();
        long current = 0;
        boolean any = false;
        for (String line : lines) {
            String s = line.trim();
            if (s.isEmpty()) {
                if (any) sums.add(current);
                current = 0;
                any = false;
            } else {
                current += Long.parseLong(s);
                any = true;
            }
        }
        if (any) sums.add(current);

        sums.sort(Collections.reverseOrder());
        long part1 = sums.isEmpty() ? 0 : sums.get(0);
        long part2 = 0;
        for (int i = 0; i < Math.min(3, sums.size()); i++) part2 += sums.get(i);

        System.out.println("Czesc 1: " + part1);
        System.out.println("Czesc 2: " + part2);
    }
}
