import java.nio.file.*;
import java.util.*;

public class Main {
    static int pos;

    // int albo lista
    static Object parse(String s) {
        pos = 0;
        return parseValue(s);
    }

    static Object parseValue(String s) {
        if (s.charAt(pos) == '[') {
            pos++;
            List<Object> list = new ArrayList<>();
            if (s.charAt(pos) == ']') {
                pos++;
                return list;
            }
            while (true) {
                list.add(parseValue(s));
                char c = s.charAt(pos++);
                if (c == ']') return list;
            }
        }
        int start = pos;
        while (pos < s.length() && Character.isDigit(s.charAt(pos))) pos++;
        return Integer.parseInt(s.substring(start, pos));
    }

    @SuppressWarnings("unchecked")
    static int compare(Object a, Object b) {
        if (a instanceof Integer && b instanceof Integer) {
            return Integer.compare((Integer) a, (Integer) b);
        }
        List<Object> la = a instanceof Integer ? List.of(a) : (List<Object>) a;
        List<Object> lb = b instanceof Integer ? List.of(b) : (List<Object>) b;
        int n = Math.min(la.size(), lb.size());
        for (int i = 0; i < n; i++) {
            int c = compare(la.get(i), lb.get(i));
            if (c != 0) return c;
        }
        return Integer.compare(la.size(), lb.size());
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<Object> packets = new ArrayList<>();
        for (String line : Files.readAllLines(Path.of(path))) {
            if (!line.isBlank()) packets.add(parse(line.trim()));
        }

        // sumujemy indeksy
        int sum = 0;
        for (int i = 0; i + 1 < packets.size(); i += 2) {
            if (compare(packets.get(i), packets.get(i + 1)) < 0) sum += i / 2 + 1;
        }

        // sortujemy pakiety
        Object d1 = parse("[[2]]");
        Object d2 = parse("[[6]]");
        int pos1 = 1, pos2 = 2;
        for (Object p : packets) {
            if (compare(p, d1) < 0) pos1++;
            if (compare(p, d2) < 0) pos2++;
        }

        System.out.println("Czesc 1: " + sum);
        System.out.println("Czesc 2: " + (pos1 * pos2));
    }
}
