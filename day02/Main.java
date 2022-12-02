import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<String> lines = Files.readAllLines(Path.of(path));

        long part1 = 0, part2 = 0;
        for (String line : lines) {
            String s = line.trim();
            if (s.isEmpty()) continue;
            int opp = s.charAt(0) - 'A';   // kamien papier nozyce
            int me = s.charAt(2) - 'X';

            // czesc 1
            int outcome = ((me - opp) % 3 + 3) % 3; // liczymy wynik
            int score = me + 1;
            if (outcome == 0) score += 3;
            else if (outcome == 1) score += 6;
            part1 += score;

            // czesc 2
            int want = me; // czego chcemy
            int move = ((opp + want - 1) % 3 + 3) % 3;
            part2 += move + 1 + want * 3;
        }

        System.out.println("Czesc 1: " + part1);
        System.out.println("Czesc 2: " + part2);
    }
}
