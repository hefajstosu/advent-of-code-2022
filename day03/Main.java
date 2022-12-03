import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // liczymy priorytet
    static int priority(char c) {
        if (c >= 'a' && c <= 'z') return c - 'a' + 1;
        return c - 'A' + 27;
    }

    // maska bitowa
    static long mask(String s) {
        long m = 0;
        for (char c : s.toCharArray()) m |= 1L << priority(c);
        return m;
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<String> lines = new ArrayList<>();
        for (String line : Files.readAllLines(Path.of(path))) {
            String s = line.trim();
            if (!s.isEmpty()) lines.add(s);
        }

        long part1 = 0;
        for (String s : lines) {
            int half = s.length() / 2;
            long common = mask(s.substring(0, half)) & mask(s.substring(half));
            part1 += Long.numberOfTrailingZeros(common);
        }

        long part2 = 0;
        for (int i = 0; i + 2 < lines.size(); i += 3) {
            long common = mask(lines.get(i)) & mask(lines.get(i + 1)) & mask(lines.get(i + 2));
            part2 += Long.numberOfTrailingZeros(common);
        }

        System.out.println("Czesc 1: " + part1);
        System.out.println("Czesc 2: " + part2);
    }
}
