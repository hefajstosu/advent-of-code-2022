import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    // kierunki
    static final int[] DR = {0, 1, 0, -1};
    static final int[] DC = {1, 0, -1, 0};

    static char[][] grid;
    static int H, W, N;

    // sciany kostki
    static int[][] faceAt;
    static int[] faceTy = new int[6], faceTx = new int[6];
    static int[][] fR = new int[6][], fD = new int[6][], fN = new int[6][];

    static boolean inMap(int r, int c) {
        return r >= 0 && r < H && c >= 0 && c < W && grid[r][c] != ' ';
    }

    // zawijamy plasko
    static int[] stepFlat(int r, int c, int d) {
        int nr = r + DR[d], nc = c + DC[d];
        if (!inMap(nr, nc)) {
            nr = r;
            nc = c;
            while (inMap(nr - DR[d], nc - DC[d])) {
                nr -= DR[d];
                nc -= DC[d];
            }
        }
        return new int[]{nr, nc, d};
    }

    static int[] neg(int[] v) {
        return new int[]{-v[0], -v[1], -v[2]};
    }

    static int dot(int[] a, int[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    // kierunek w 3D
    static int[] dirVec(int f, int d) {
        switch (d) {
            case 0: return fR[f].clone();
            case 1: return fD[f].clone();
            case 2: return neg(fR[f]);
            default: return neg(fD[f]);
        }
    }

    // skladamy kostke
    static void foldCube() {
        int tilesY = (H + N - 1) / N, tilesX = (W + N - 1) / N;
        faceAt = new int[tilesY][tilesX];
        for (int[] row : faceAt) Arrays.fill(row, -1);
        int count = 0;
        for (int ty = 0; ty < tilesY; ty++) {
            for (int tx = 0; tx < tilesX; tx++) {
                if (ty * N < H && tx * N < W && grid[ty * N][tx * N] != ' ' && count < 6) {
                    faceAt[ty][tx] = count;
                    faceTy[count] = ty;
                    faceTx[count] = tx;
                    count++;
                }
            }
        }
        fR[0] = new int[]{1, 0, 0};
        fD[0] = new int[]{0, 1, 0};
        fN[0] = new int[]{0, 0, 1};
        boolean[] done = new boolean[6];
        done[0] = true;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.add(0);
        while (!queue.isEmpty()) {
            int f = queue.poll();
            for (int d = 0; d < 4; d++) {
                int ty = faceTy[f] + DR[d], tx = faceTx[f] + DC[d];
                if (ty < 0 || tx < 0 || ty >= tilesY || tx >= tilesX) continue;
                int g = faceAt[ty][tx];
                if (g < 0 || done[g]) continue;
                // zaginamy sciane
                if (d == 0) {
                    fN[g] = fR[f].clone(); fR[g] = neg(fN[f]); fD[g] = fD[f].clone();
                } else if (d == 1) {
                    fN[g] = fD[f].clone(); fD[g] = neg(fN[f]); fR[g] = fR[f].clone();
                } else if (d == 2) {
                    fN[g] = neg(fR[f]); fR[g] = fN[f].clone(); fD[g] = fD[f].clone();
                } else {
                    fN[g] = neg(fD[f]); fD[g] = fN[f].clone(); fR[g] = fR[f].clone();
                }
                done[g] = true;
                queue.add(g);
            }
        }
    }

    // chodzimy po kostce
    static int[] stepCube(int r, int c, int d) {
        int ty = r / N, tx = c / N;
        int lr = r - ty * N, lc = c - tx * N;
        int nlr = lr + DR[d], nlc = lc + DC[d];
        if (nlr >= 0 && nlr < N && nlc >= 0 && nlc < N) return new int[]{r + DR[d], c + DC[d], d};

        int f = faceAt[ty][tx];
        // srodek w 3D
        int[] p = new int[3];
        for (int i = 0; i < 3; i++) {
            p[i] = fN[f][i] * N + fR[f][i] * (2 * lc + 1 - N) + fD[f][i] * (2 * lr + 1 - N);
        }
        // przechodzimy krawedz
        int[] d3 = dirVec(f, d);
        for (int i = 0; i < 3; i++) p[i] += d3[i] - fN[f][i];
        int g = -1;
        for (int k = 0; k < 6; k++) if (Arrays.equals(fN[k], d3)) g = k;
        int gc = (dot(p, fR[g]) + N - 1) / 2;
        int gr = (dot(p, fD[g]) + N - 1) / 2;
        // nowy kierunek
        int[] enter = neg(fN[f]);
        int nd = -1;
        for (int k = 0; k < 4; k++) if (Arrays.equals(dirVec(g, k), enter)) nd = k;
        return new int[]{faceTy[g] * N + gr, faceTx[g] * N + gc, nd};
    }

    static long walk(String path, boolean cube) {
        int r = 0, c = 0, d = 0;
        while (grid[0][c] != '.') c++;
        int i = 0;
        while (i < path.length()) {
            char ch = path.charAt(i);
            if (Character.isDigit(ch)) {
                int j = i;
                while (j < path.length() && Character.isDigit(path.charAt(j))) j++;
                int steps = Integer.parseInt(path.substring(i, j));
                i = j;
                for (int s = 0; s < steps; s++) {
                    int[] nx = cube ? stepCube(r, c, d) : stepFlat(r, c, d);
                    if (grid[nx[0]][nx[1]] == '#') break;
                    r = nx[0];
                    c = nx[1];
                    d = nx[2];
                }
            } else {
                if (ch == 'R') d = (d + 1) % 4;
                else if (ch == 'L') d = (d + 3) % 4;
                i++;
            }
        }
        return 1000L * (r + 1) + 4L * (c + 1) + d;
    }

    public static void main(String[] args) throws Exception {
        String file = args.length > 0 ? args[0] : "data.txt";
        List<String> lines = Files.readAllLines(Path.of(file));
        List<String> map = new ArrayList<>();
        String path = "";
        int idx = 0;
        while (idx < lines.size() && !lines.get(idx).trim().isEmpty()) map.add(lines.get(idx++));
        while (idx < lines.size()) {
            if (!lines.get(idx).trim().isEmpty()) path = lines.get(idx).trim();
            idx++;
        }

        H = map.size();
        W = 0;
        for (String s : map) W = Math.max(W, s.length());
        grid = new char[H][W];
        int cells = 0;
        for (int r = 0; r < H; r++) {
            Arrays.fill(grid[r], ' ');
            String s = map.get(r);
            for (int c = 0; c < s.length(); c++) {
                grid[r][c] = s.charAt(c);
                if (s.charAt(c) != ' ') cells++;
            }
        }
        // bok kostki
        N = (int) Math.round(Math.sqrt(cells / 6.0));
        foldCube();

        System.out.println("Czesc 1: " + walk(path, false));
        System.out.println("Czesc 2: " + walk(path, true));
    }
}
