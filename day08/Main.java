import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<String> g = new ArrayList<>();
        for (String line : Files.readAllLines(Path.of(path))) {
            String s = line.trim();
            if (!s.isEmpty()) g.add(s);
        }
        int h = g.size(), w = g.get(0).length();
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        long visible = 0, best = 0;
        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                char t = g.get(r).charAt(c);
                boolean vis = false;
                long score = 1;
                for (int[] d : dirs) {
                    int rr = r + d[0], cc = c + d[1];
                    long dist = 0;
                    boolean blocked = false;
                    while (rr >= 0 && rr < h && cc >= 0 && cc < w) {
                        dist++;
                        if (g.get(rr).charAt(cc) >= t) {
                            blocked = true;
                            break;
                        }
                        rr += d[0];
                        cc += d[1];
                    }
                    // czy widac
                    if (!blocked) vis = true;
                    score *= dist;
                }
                if (vis) visible++;
                if (score > best) best = score;
            }
        }

        System.out.println("Czesc 1: " + visible);
        System.out.println("Czesc 2: " + best);
    }
}
