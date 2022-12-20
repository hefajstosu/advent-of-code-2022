import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static long mix(long[] vals, int rounds) {
        int n = vals.length;
        // pozycje liczb
        int[] order = new int[n];
        for (int i = 0; i < n; i++) order[i] = i;
        for (int r = 0; r < rounds; r++) {
            for (int i = 0; i < n; i++) {
                int p = 0;
                while (order[p] != i) p++;
                // lista bez jednego
                int q = (int) Math.floorMod(p + vals[i], (long) (n - 1));
                if (q > p) System.arraycopy(order, p + 1, order, p, q - p);
                else if (q < p) System.arraycopy(order, q, order, q + 1, p - q);
                order[q] = i;
            }
        }
        int zero = 0;
        while (vals[order[zero]] != 0) zero++;
        long sum = 0;
        for (int k = 1; k <= 3; k++) sum += vals[order[(zero + 1000 * k) % n]];
        return sum;
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<Long> list = new ArrayList<>();
        for (String line : Files.readAllLines(Path.of(path))) {
            line = line.trim();
            if (!line.isEmpty()) list.add(Long.parseLong(line));
        }
        long[] vals = new long[list.size()];
        for (int i = 0; i < vals.length; i++) vals[i] = list.get(i);

        long[] keyed = new long[vals.length];
        for (int i = 0; i < vals.length; i++) keyed[i] = vals[i] * 811589153L;

        System.out.println("Czesc 1: " + mix(vals, 1));
        System.out.println("Czesc 2: " + mix(keyed, 10));
    }
}
