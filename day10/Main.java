import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<String> lines = Files.readAllLines(Path.of(path));

        // wartosci rejestru
        List<Integer> xs = new ArrayList<>();
        int x = 1;
        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty()) continue;
            if (line.equals("noop")) {
                xs.add(x);
            } else {
                int v = Integer.parseInt(line.split(" ")[1]);
                xs.add(x);
                xs.add(x);
                x += v;
            }
        }

        // sumujemy sygnal
        long sum = 0;
        for (int c = 20; c <= 220; c += 40) {
            if (c <= xs.size()) sum += (long) c * xs.get(c - 1);
        }

        // rysujemy ekran
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < xs.size() && i < 240; i++) {
            int col = i % 40;
            sb.append(Math.abs(xs.get(i) - col) <= 1 ? '#' : '.');
            if (col == 39) sb.append('\n');
        }

        System.out.println("Czesc 1: " + sum);
        System.out.println("Czesc 2:");
        System.out.print(sb);
    }
}
