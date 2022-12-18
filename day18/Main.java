import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static final int[][] DIRS = {{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}};

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<int[]> cubes = new ArrayList<>();
        int max = 0;
        for (String line : Files.readAllLines(Path.of(path))) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] p = line.split(",");
            // przesuwamy o 1
            int[] c = {Integer.parseInt(p[0].trim()) + 1, Integer.parseInt(p[1].trim()) + 1, Integer.parseInt(p[2].trim()) + 1};
            cubes.add(c);
            max = Math.max(max, Math.max(c[0], Math.max(c[1], c[2])));
        }
        int size = max + 2;
        boolean[][][] solid = new boolean[size][size][size];
        for (int[] c : cubes) solid[c[0]][c[1]][c[2]] = true;

        // liczymy sciany
        int part1 = 0;
        for (int[] c : cubes) {
            for (int[] d : DIRS) {
                int x = c[0] + d[0], y = c[1] + d[1], z = c[2] + d[2];
                if (!solid[x][y][z]) part1++;
            }
        }

        // zalewamy z zewnatrz
        boolean[][][] outside = new boolean[size][size][size];
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        outside[0][0][0] = true;
        queue.add(new int[]{0, 0, 0});
        int part2 = 0;
        while (!queue.isEmpty()) {
            int[] c = queue.poll();
            for (int[] d : DIRS) {
                int x = c[0] + d[0], y = c[1] + d[1], z = c[2] + d[2];
                if (x < 0 || y < 0 || z < 0 || x >= size || y >= size || z >= size) continue;
                if (solid[x][y][z]) {
                    part2++;
                } else if (!outside[x][y][z]) {
                    outside[x][y][z] = true;
                    queue.add(new int[]{x, y, z});
                }
            }
        }

        System.out.println("Czesc 1: " + part1);
        System.out.println("Czesc 2: " + part2);
    }
}
