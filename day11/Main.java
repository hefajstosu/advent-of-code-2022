import java.nio.file.*;
import java.util.*;

public class Main {
    static class Monkey {
        List<Long> items = new ArrayList<>();
        char op;
        long operand;      // minus to old
        long div;
        int ifTrue;
        int ifFalse;
        long inspected;
    }

    static String last(String line) {
        String[] p = line.trim().split(" ");
        return p[p.length - 1];
    }

    static List<Monkey> parse(List<String> lines) {
        List<Monkey> ms = new ArrayList<>();
        Monkey cur = null;
        for (String raw : lines) {
            String line = raw.trim();
            if (line.startsWith("Monkey")) {
                cur = new Monkey();
                ms.add(cur);
            } else if (line.startsWith("Starting items:")) {
                String rest = line.substring("Starting items:".length()).trim();
                if (!rest.isEmpty()) {
                    for (String s : rest.split(",")) cur.items.add(Long.parseLong(s.trim()));
                }
            } else if (line.startsWith("Operation:")) {
                // parsujemy operacje
                String[] p = line.split(" ");
                cur.op = p[p.length - 2].charAt(0);
                String o = p[p.length - 1];
                cur.operand = o.equals("old") ? -1 : Long.parseLong(o);
            } else if (line.startsWith("Test:")) {
                cur.div = Long.parseLong(last(line));
            } else if (line.startsWith("If true:")) {
                cur.ifTrue = Integer.parseInt(last(line));
            } else if (line.startsWith("If false:")) {
                cur.ifFalse = Integer.parseInt(last(line));
            }
        }
        return ms;
    }

    static long run(List<String> lines, int rounds, boolean relief) {
        List<Monkey> ms = parse(lines);
        // modulo, nie puchnie
        long mod = 1;
        for (Monkey m : ms) mod *= m.div;

        for (int r = 0; r < rounds; r++) {
            for (Monkey m : ms) {
                List<Long> items = new ArrayList<>(m.items);
                m.items.clear();
                for (long item : items) {
                    m.inspected++;
                    long o = m.operand < 0 ? item : m.operand;
                    long w = m.op == '*' ? item * o : item + o;
                    if (relief) w /= 3;
                    else w %= mod;
                    int target = w % m.div == 0 ? m.ifTrue : m.ifFalse;
                    ms.get(target).items.add(w);
                }
            }
        }

        List<Long> counts = new ArrayList<>();
        for (Monkey m : ms) counts.add(m.inspected);
        counts.sort(Collections.reverseOrder());
        return counts.get(0) * counts.get(1);
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<String> lines = Files.readAllLines(Path.of(path));
        System.out.println("Czesc 1: " + run(lines, 20, true));
        System.out.println("Czesc 2: " + run(lines, 10000, false));
    }
}
