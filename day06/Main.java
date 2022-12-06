import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    // szukamy okna
    static int marker(String s, int len) {
        int[] count = new int[128];
        int distinct = 0;
        for (int i = 0; i < s.length(); i++) {
            if (count[s.charAt(i)]++ == 0) distinct++;
            if (i >= len) {
                if (--count[s.charAt(i - len)] == 0) distinct--;
            }
            if (i >= len - 1 && distinct == len) return i + 1;
        }
        return -1;
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        String s = Files.readString(Path.of(path)).trim();

        System.out.println("Czesc 1: " + marker(s, 4));
        System.out.println("Czesc 2: " + marker(s, 14));
    }
}
