import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    // kierunki i przesuniecia
    static final int[][][] CHECK = {
        {{-1, -1}, {-1, 0}, {-1, 1}},
        {{1, -1}, {1, 0}, {1, 1}},
        {{-1, -1}, {0, -1}, {1, -1}},
        {{-1, 1}, {0, 1}, {1, 1}}
    };
    static final int[][] MOVE = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    static long key(int r, int c) {
        return (long) (r + 50000) * 100000L + (c + 50000);
    }

    // jedna runda
    static boolean round(List<int[]> elves, int first) {
        Set<Long> occ = new HashSet<>();
        for (int[] e : elves) occ.add(key(e[0], e[1]));
        int n = elves.size();
        int[][] prop = new int[n][];
        Map<Long, Integer> count = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int[] e = elves.get(i);
            boolean alone = true;
            for (int dr = -1; dr <= 1 && alone; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if ((dr != 0 || dc != 0) && occ.contains(key(e[0] + dr, e[1] + dc))) {
                        alone = false;
                        break;
                    }
                }
            }
            if (alone) continue;
            for (int k = 0; k < 4; k++) {
                int dir = (first + k) % 4;
                boolean free = true;
                for (int[] ch : CHECK[dir]) {
                    if (occ.contains(key(e[0] + ch[0], e[1] + ch[1]))) {
                        free = false;
                        break;
                    }
                }
                if (free) {
                    prop[i] = new int[]{e[0] + MOVE[dir][0], e[1] + MOVE[dir][1]};
                    count.merge(key(prop[i][0], prop[i][1]), 1, Integer::sum);
                    break;
                }
            }
        }
        boolean moved = false;
        for (int i = 0; i < n; i++) {
            if (prop[i] != null && count.get(key(prop[i][0], prop[i][1])) == 1) {
                elves.set(i, prop[i]);
                moved = true;
            }
        }
        return moved;
    }

    static int emptyTiles(List<int[]> elves) {
        int minR = Integer.MAX_VALUE, maxR = Integer.MIN_VALUE, minC = Integer.MAX_VALUE, maxC = Integer.MIN_VALUE;
        for (int[] e : elves) {
            minR = Math.min(minR, e[0]);
            maxR = Math.max(maxR, e[0]);
            minC = Math.min(minC, e[1]);
            maxC = Math.max(maxC, e[1]);
        }
        return (maxR - minR + 1) * (maxC - minC + 1) - elves.size();
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<String> lines = Files.readAllLines(Path.of(path));
        List<int[]> elves = new ArrayList<>();
        for (int r = 0; r < lines.size(); r++) {
            String s = lines.get(r);
            for (int c = 0; c < s.length(); c++) {
                if (s.charAt(c) == '#') elves.add(new int[]{r, c});
            }
        }

        int part1 = -1, part2 = -1;
        for (int r = 0; part1 < 0 || part2 < 0; r++) {
            boolean moved = round(elves, r % 4);
            if (!moved && part2 < 0) part2 = r + 1;
            if (r == 9) part1 = emptyTiles(elves);
        }

        System.out.println("Czesc 1: " + part1);
        System.out.println("Czesc 2: " + part2);
    }
}
