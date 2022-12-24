import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    // wnetrze doliny
    static char[][] inner;
    static int H, W;
    static int startC, endC;
    static final int[] DR = {0, 0, 1, 0, -1};
    static final int[] DC = {0, 1, 0, -1, 0};

    // czy wolne
    static boolean free(int r, int c, int t) {
        if (r == -1 && c == startC) return true;
        if (r == H && c == endC) return true;
        if (r < 0 || r >= H || c < 0 || c >= W) return false;
        if (inner[r][Math.floorMod(c - t, W)] == '>') return false;
        if (inner[r][Math.floorMod(c + t, W)] == '<') return false;
        if (inner[Math.floorMod(r - t, H)][c] == 'v') return false;
        if (inner[Math.floorMod(r + t, H)][c] == '^') return false;
        return true;
    }

    static long key(int r, int c) {
        return (long) (r + 1) * 1000L + c;
    }

    // BFS po czasie
    static int travel(int sr, int sc, int er, int ec, int t0) {
        Set<Long> cur = new HashSet<>();
        cur.add(key(sr, sc));
        int t = t0;
        while (true) {
            t++;
            Set<Long> next = new HashSet<>();
            for (long k : cur) {
                int r = (int) (k / 1000) - 1, c = (int) (k % 1000);
                for (int d = 0; d < 5; d++) {
                    int nr = r + DR[d], nc = c + DC[d];
                    if (!free(nr, nc, t)) continue;
                    if (nr == er && nc == ec) return t;
                    next.add(key(nr, nc));
                }
            }
            cur = next;
        }
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<String> lines = new ArrayList<>();
        for (String line : Files.readAllLines(Path.of(path))) {
            if (!line.trim().isEmpty()) lines.add(line.trim());
        }
        H = lines.size() - 2;
        W = lines.get(0).length() - 2;
        inner = new char[H][W];
        for (int r = 0; r < H; r++) {
            for (int c = 0; c < W; c++) inner[r][c] = lines.get(r + 1).charAt(c + 1);
        }
        startC = lines.get(0).indexOf('.') - 1;
        endC = lines.get(H + 1).indexOf('.') - 1;

        int t1 = travel(-1, startC, H, endC, 0);
        int t2 = travel(H, endC, -1, startC, t1);
        int t3 = travel(-1, startC, H, endC, t2);

        System.out.println("Czesc 1: " + t1);
        System.out.println("Czesc 2: " + t3);
    }
}
