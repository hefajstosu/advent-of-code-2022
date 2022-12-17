import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    // ksztalty skal
    static final int[][][] SHAPES = {
        {{0, 0}, {1, 0}, {2, 0}, {3, 0}},
        {{1, 0}, {0, 1}, {1, 1}, {2, 1}, {1, 2}},
        {{0, 0}, {1, 0}, {2, 0}, {2, 1}, {2, 2}},
        {{0, 0}, {0, 1}, {0, 2}, {0, 3}},
        {{0, 0}, {1, 0}, {0, 1}, {1, 1}}
    };
    // profil do cyklu
    static final int PROFILE_ROWS = 30;

    // wiersz jako maska
    static List<Integer> rows;

    static boolean fits(int[][] shape, int x, int y) {
        for (int[] c : shape) {
            int nx = x + c[0], ny = y + c[1];
            if (nx < 0 || nx > 6 || ny < 0) return false;
            if (ny < rows.size() && ((rows.get(ny) >> nx) & 1) != 0) return false;
        }
        return true;
    }

    // liczymy wysokosc
    static long simulate(String jets, long total) {
        rows = new ArrayList<>();
        Map<String, long[]> seen = new HashMap<>();
        int jet = 0;
        long n = 0, extra = 0;
        boolean skipped = false;
        while (n < total) {
            int rock = (int) (n % 5);
            if (!skipped && rows.size() >= PROFILE_ROWS) {
                StringBuilder key = new StringBuilder();
                key.append(rock).append(',').append(jet);
                for (int i = rows.size() - PROFILE_ROWS; i < rows.size(); i++) {
                    key.append(',').append(rows.get(i));
                }
                String k = key.toString();
                long[] prev = seen.get(k);
                if (prev != null) {
                    // przeskakujemy cykle
                    long cycleLen = n - prev[0];
                    long cycleHeight = rows.size() - prev[1];
                    long cycles = (total - n) / cycleLen;
                    n += cycles * cycleLen;
                    extra = cycles * cycleHeight;
                    skipped = true;
                    if (n >= total) break;
                } else {
                    seen.put(k, new long[]{n, rows.size()});
                }
            }
            int[][] shape = SHAPES[rock];
            int x = 2, y = rows.size() + 3;
            while (true) {
                int nx = x + (jets.charAt(jet) == '>' ? 1 : -1);
                jet = (jet + 1) % jets.length();
                if (fits(shape, nx, y)) x = nx;
                if (fits(shape, x, y - 1)) y--;
                else break;
            }
            for (int[] c : shape) {
                int cx = x + c[0], cy = y + c[1];
                while (rows.size() <= cy) rows.add(0);
                rows.set(cy, rows.get(cy) | (1 << cx));
            }
            n++;
        }
        return rows.size() + extra;
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        String jets = Files.readString(Path.of(path)).trim();

        System.out.println("Czesc 1: " + simulate(jets, 2022));
        System.out.println("Czesc 2: " + simulate(jets, 1000000000000L));
    }
}
