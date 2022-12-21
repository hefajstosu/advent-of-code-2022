import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Main {
    // liczba albo dzialanie
    static Map<String, String[]> jobs = new HashMap<>();
    static Map<String, Boolean> humnMemo = new HashMap<>();

    static long eval(String name) {
        String[] j = jobs.get(name);
        if (j.length == 1) return Long.parseLong(j[0]);
        long a = eval(j[0]), b = eval(j[2]);
        switch (j[1]) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            default: return a / b;
        }
    }

    static boolean hasHumn(String name) {
        if (name.equals("humn")) return true;
        Boolean cached = humnMemo.get(name);
        if (cached != null) return cached;
        String[] j = jobs.get(name);
        boolean res = j.length == 3 && (hasHumn(j[0]) || hasHumn(j[2]));
        humnMemo.put(name, res);
        return res;
    }

    // odwracamy dzialania
    static long solve(String name, long target) {
        if (name.equals("humn")) return target;
        String[] j = jobs.get(name);
        String op = j[1];
        if (hasHumn(j[0])) {
            long b = eval(j[2]);
            switch (op) {
                case "+": return solve(j[0], target - b);
                case "-": return solve(j[0], target + b);
                case "*": return solve(j[0], target / b);
                default: return solve(j[0], target * b);
            }
        } else {
            long a = eval(j[0]);
            switch (op) {
                case "+": return solve(j[2], target - a);
                case "-": return solve(j[2], a - target);
                case "*": return solve(j[2], target / a);
                default: return solve(j[2], a / target);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        for (String line : Files.readAllLines(Path.of(path))) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(": ");
            jobs.put(parts[0], parts[1].trim().split(" "));
        }

        long part1 = eval("root");

        // root rowny
        String[] root = jobs.get("root");
        long part2;
        if (hasHumn(root[0])) part2 = solve(root[0], eval(root[2]));
        else part2 = solve(root[2], eval(root[0]));

        System.out.println("Czesc 1: " + part1);
        System.out.println("Czesc 2: " + part2);
    }
}
