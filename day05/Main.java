import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Main {
    // parsujemy stosy
    static List<Deque<Character>> parseStacks(List<String> drawing) {
        String labels = drawing.get(drawing.size() - 1);
        String[] nums = labels.trim().split("\\s+");
        int n = nums.length;
        List<Deque<Character>> stacks = new ArrayList<>();
        for (int i = 0; i < n; i++) stacks.add(new ArrayDeque<>());
        for (int row = drawing.size() - 2; row >= 0; row--) {
            String line = drawing.get(row);
            for (int i = 0; i < n; i++) {
                int col = 1 + 4 * i;
                if (col < line.length() && line.charAt(col) != ' ') {
                    stacks.get(i).addLast(line.charAt(col));
                }
            }
        }
        return stacks;
    }

    static String tops(List<Deque<Character>> stacks) {
        StringBuilder sb = new StringBuilder();
        for (Deque<Character> st : stacks) {
            if (!st.isEmpty()) sb.append(st.peekLast());
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<String> lines = Files.readAllLines(Path.of(path));

        // szukamy pustej
        int split = 0;
        while (split < lines.size() && !lines.get(split).trim().isEmpty()) split++;
        List<String> drawing = lines.subList(0, split);

        List<Deque<Character>> s1 = parseStacks(drawing);
        List<Deque<Character>> s2 = parseStacks(drawing);

        for (int i = split + 1; i < lines.size(); i++) {
            String s = lines.get(i).trim();
            if (s.isEmpty()) continue;
            String[] p = s.split("\\s+"); // parsujemy ruch
            int count = Integer.parseInt(p[1]);
            int from = Integer.parseInt(p[3]) - 1;
            int to = Integer.parseInt(p[5]) - 1;

            // po jednej
            for (int k = 0; k < count; k++) s1.get(to).addLast(s1.get(from).removeLast());

            // wszystkie naraz
            Deque<Character> tmp = new ArrayDeque<>();
            for (int k = 0; k < count; k++) tmp.addFirst(s2.get(from).removeLast());
            for (char c : tmp) s2.get(to).addLast(c);
        }

        System.out.println("Czesc 1: " + tops(s1));
        System.out.println("Czesc 2: " + tops(s2));
    }
}
