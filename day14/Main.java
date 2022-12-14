import java.nio.file.*;
import java.util.*;

public class Main {
    static final int W = 2000;

    // sypiemy piasek
    static int simulate(boolean[][] rock, int maxY, boolean floor) {
        boolean[][] g = new boolean[W][];
        for (int x = 0; x < W; x++) g[x] = rock[x].clone();
        int count = 0;
        while (true) {
            if (g[500][0]) return count;
            int x = 500, y = 0;
            while (true) {
                if (!floor && y > maxY) return count;   // leci w otchlan
                if (floor && y + 1 == maxY + 2) break;  // lezy na podlodze
                if (!g[x][y + 1]) {
                    y++;
                } else if (!g[x - 1][y + 1]) {
                    x--;
                    y++;
                } else if (!g[x + 1][y + 1]) {
                    x++;
                    y++;
                } else {
                    break;
                }
            }
            g[x][y] = true;
            count++;
        }
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<int[][]> paths = new ArrayList<>();
        int maxY = 0;
        for (String line : Files.readAllLines(Path.of(path))) {
            if (line.isBlank()) continue;
            String[] pts = line.trim().split(" -> ");
            int[][] p = new int[pts.length][2];
            for (int i = 0; i < pts.length; i++) {
                String[] xy = pts[i].split(",");
                p[i][0] = Integer.parseInt(xy[0].trim());
                p[i][1] = Integer.parseInt(xy[1].trim());
                maxY = Math.max(maxY, p[i][1]);
            }
            paths.add(p);
        }

        // rysujemy skaly
        boolean[][] rock = new boolean[W][maxY + 3];
        for (int[][] p : paths) {
            for (int i = 0; i + 1 < p.length; i++) {
                int x1 = Math.min(p[i][0], p[i + 1][0]), x2 = Math.max(p[i][0], p[i + 1][0]);
                int y1 = Math.min(p[i][1], p[i + 1][1]), y2 = Math.max(p[i][1], p[i + 1][1]);
                for (int x = x1; x <= x2; x++) {
                    for (int y = y1; y <= y2; y++) rock[x][y] = true;
                }
            }
        }

        System.out.println("Czesc 1: " + simulate(rock, maxY, false));
        System.out.println("Czesc 2: " + simulate(rock, maxY, true));
    }
}
