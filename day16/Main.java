import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Main {
    static int[][] dist;
    static int[] useful;     // zawory z przeplywem
    static int[] flow;
    static int[] best;       // najlepsze na maske

    // przeszukujemy rekurencyjnie
    static void dfs(int pos, int time, int mask, int pressure) {
        if (pressure > best[mask]) best[mask] = pressure;
        for (int j = 0; j < useful.length; j++) {
            if ((mask & (1 << j)) != 0) continue;
            int t = time - dist[pos][useful[j]] - 1;
            if (t <= 0) continue;
            dfs(useful[j], t, mask | (1 << j), pressure + t * flow[useful[j]]);
        }
    }

    static int[] solve(int start, int time) {
        best = new int[1 << useful.length];
        dfs(start, time, 0, 0);
        return best;
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        Pattern p = Pattern.compile("Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? (.*)");
        List<String> names = new ArrayList<>();
        List<Integer> rates = new ArrayList<>();
        List<String[]> edges = new ArrayList<>();
        for (String line : Files.readAllLines(Path.of(path))) {
            if (line.isBlank()) continue;
            Matcher m = p.matcher(line.trim());
            if (!m.matches()) continue;
            names.add(m.group(1));
            rates.add(Integer.parseInt(m.group(2)));
            edges.add(m.group(3).split(",\\s*"));
        }
        int n = names.size();
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < n; i++) idx.put(names.get(i), i);

        // Floyd-Warshall
        final int INF = 1_000_000;
        dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;
            for (String e : edges.get(i)) dist[i][idx.get(e.trim())] = 1;
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) dist[i][j] = dist[i][k] + dist[k][j];
                }
            }
        }

        flow = new int[n];
        List<Integer> u = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            flow[i] = rates.get(i);
            if (flow[i] > 0) u.add(i);
        }
        useful = new int[u.size()];
        for (int i = 0; i < u.size(); i++) useful[i] = u.get(i);
        int start = idx.get("AA");

        // solo 30 minut
        int part1 = 0;
        for (int v : solve(start, 30)) part1 = Math.max(part1, v);

        // we dwoch
        int[] f = solve(start, 26).clone();
        int full = (1 << useful.length) - 1;
        // max po podzbiorach
        for (int b = 0; b < useful.length; b++) {
            for (int mask = 0; mask <= full; mask++) {
                if ((mask & (1 << b)) != 0) f[mask] = Math.max(f[mask], f[mask ^ (1 << b)]);
            }
        }
        int part2 = 0;
        for (int mask = 0; mask <= full; mask++) {
            part2 = Math.max(part2, f[mask] + f[full ^ mask]);
        }

        System.out.println("Czesc 1: " + part1);
        System.out.println("Czesc 2: " + part2);
    }
}
