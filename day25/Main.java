import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    static long fromSnafu(String s) {
        long n = 0;
        for (char ch : s.toCharArray()) {
            int d;
            switch (ch) {
                case '=': d = -2; break;
                case '-': d = -1; break;
                default: d = ch - '0';
            }
            n = n * 5 + d;
        }
        return n;
    }

    static String toSnafu(long n) {
        if (n == 0) return "0";
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            int r = (int) (n % 5);
            n /= 5;
            // przenosimy reszte
            if (r <= 2) {
                sb.append((char) ('0' + r));
            } else {
                sb.append(r == 3 ? '=' : '-');
                n++;
            }
        }
        return sb.reverse().toString();
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        long sum = 0;
        for (String line : Files.readAllLines(Path.of(path))) {
            line = line.trim();
            if (!line.isEmpty()) sum += fromSnafu(line);
        }

        System.out.println("Czesc 1: " + toSnafu(sum));
        System.out.println("Czesc 2: -");
    }
}
