import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<String> lines = Files.readAllLines(Path.of(path));

        // rozmiary katalogow
        Map<String, Long> sizes = new HashMap<>();
        List<String> cwd = new ArrayList<>();
        sizes.put("/", 0L);
        // bez dubli
        Set<String> seen = new HashSet<>();

        for (String line : lines) {
            String s = line.trim();
            if (s.isEmpty()) continue;
            String[] p = s.split(" ");
            if (p[0].equals("$")) {
                if (p[1].equals("cd")) {
                    if (p[2].equals("/")) cwd.clear();
                    else if (p[2].equals("..")) {
                        if (!cwd.isEmpty()) cwd.remove(cwd.size() - 1);
                    } else cwd.add(p[2]);
                }
            } else if (!p[0].equals("dir")) {
                long size = Long.parseLong(p[0]);
                if (!seen.add(String.join("/", cwd) + "/" + p[1])) continue;
                StringBuilder key = new StringBuilder("/");
                sizes.merge(key.toString(), size, Long::sum);
                for (String d : cwd) {
                    key.append(d).append('/');
                    sizes.merge(key.toString(), size, Long::sum);
                }
            }
        }

        long part1 = 0;
        for (long v : sizes.values()) if (v <= 100000) part1 += v;

        long total = 70000000L, need = 30000000L;
        long toFree = need - (total - sizes.get("/"));
        long part2 = Long.MAX_VALUE;
        for (long v : sizes.values()) if (v >= toFree && v < part2) part2 = v;

        System.out.println("Czesc 1: " + part1);
        System.out.println("Czesc 2: " + part2);
    }
}
