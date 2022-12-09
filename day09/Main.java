import java.nio.file.*;
import java.util.*;

public class Main {
    // symulujemy line
    static int simulate(List<String> lines, int knots) {
        int[] x = new int[knots];
        int[] y = new int[knots];
        Set<Long> visited = new HashSet<>();
        visited.add(key(0, 0));
        for (String line : lines) {
            if (line.isBlank()) continue;
            String[] p = line.trim().split(" ");
            char dir = p[0].charAt(0);
            int n = Integer.parseInt(p[1]);
            for (int s = 0; s < n; s++) {
                switch (dir) {
                    case 'R': x[0]++; break;
                    case 'L': x[0]--; break;
                    case 'U': y[0]++; break;
                    case 'D': y[0]--; break;
                    default: break;
                }
                // ogon goni
                for (int k = 1; k < knots; k++) {
                    int dx = x[k - 1] - x[k];
                    int dy = y[k - 1] - y[k];
                    if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
                        x[k] += Integer.signum(dx);
                        y[k] += Integer.signum(dy);
                    }
                }
                visited.add(key(x[knots - 1], y[knots - 1]));
            }
        }
        return visited.size();
    }

    static long key(int x, int y) {
        return ((long) x << 32) ^ (y & 0xffffffffL);
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<String> lines = Files.readAllLines(Path.of(path));
        System.out.println("Czesc 1: " + simulate(lines, 2));
        System.out.println("Czesc 2: " + simulate(lines, 10));
    }
}
