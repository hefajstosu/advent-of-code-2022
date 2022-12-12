import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<String> grid = new ArrayList<>();
        for (String line : Files.readAllLines(Path.of(path))) {
            if (!line.isBlank()) grid.add(line.trim());
        }
        int h = grid.size();
        int w = grid.get(0).length();

        int[][] height = new int[h][w];
        int sr = 0, sc = 0, er = 0, ec = 0;
        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                char ch = grid.get(r).charAt(c);
                if (ch == 'S') { sr = r; sc = c; ch = 'a'; }
                else if (ch == 'E') { er = r; ec = c; ch = 'z'; }
                height[r][c] = ch - 'a';
            }
        }

        // BFS od tylu
        int[][] dist = new int[h][w];
        for (int[] row : dist) Arrays.fill(row, -1);
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        dist[er][ec] = 0;
        queue.add(new int[]{er, ec});
        int[] dr = {1, -1, 0, 0};
        int[] dc = {0, 0, 1, -1};
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int r = cur[0], c = cur[1];
            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d], nc = c + dc[d];
                if (nr < 0 || nr >= h || nc < 0 || nc >= w || dist[nr][nc] >= 0) continue;
                if (height[r][c] - height[nr][nc] > 1) continue;
                dist[nr][nc] = dist[r][c] + 1;
                queue.add(new int[]{nr, nc});
            }
        }

        // najblizsze a
        int best = Integer.MAX_VALUE;
        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                if (height[r][c] == 0 && dist[r][c] >= 0) best = Math.min(best, dist[r][c]);
            }
        }

        System.out.println("Czesc 1: " + dist[sr][sc]);
        System.out.println("Czesc 2: " + best);
    }
}
