import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Main {
    static long[] sx, sy, bx, by, rad;
    static int n;

    // czy w zasiegu
    static boolean covered(long x, long y) {
        for (int i = 0; i < n; i++) {
            if (Math.abs(x - sx[i]) + Math.abs(y - sy[i]) <= rad[i]) return true;
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        List<long[]> rows = new ArrayList<>();
        Pattern num = Pattern.compile("-?\\d+");
        long maxCoord = 0;
        for (String line : Files.readAllLines(Path.of(path))) {
            if (line.isBlank()) continue;
            Matcher m = num.matcher(line);
            long[] v = new long[4];
            for (int i = 0; i < 4 && m.find(); i++) {
                v[i] = Long.parseLong(m.group());
                maxCoord = Math.max(maxCoord, Math.abs(v[i]));
            }
            rows.add(v);
        }
        n = rows.size();
        sx = new long[n]; sy = new long[n]; bx = new long[n]; by = new long[n]; rad = new long[n];
        for (int i = 0; i < n; i++) {
            long[] v = rows.get(i);
            sx[i] = v[0]; sy[i] = v[1]; bx[i] = v[2]; by[i] = v[3];
            rad[i] = Math.abs(sx[i] - bx[i]) + Math.abs(sy[i] - by[i]);
        }

        // przyklad czy prawdziwe
        boolean example = maxCoord < 1000;
        long targetY = example ? 10 : 2000000;
        long limit = example ? 20 : 4000000;

        // scalamy przedzialy
        List<long[]> iv = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            long d = rad[i] - Math.abs(sy[i] - targetY);
            if (d >= 0) iv.add(new long[]{sx[i] - d, sx[i] + d});
        }
        iv.sort((a, b) -> Long.compare(a[0], b[0]));
        long total = 0;
        long curS = 0, curE = 0;
        boolean open = false;
        for (long[] p : iv) {
            if (!open) {
                curS = p[0]; curE = p[1]; open = true;
            } else if (p[0] <= curE + 1) {
                curE = Math.max(curE, p[1]);
            } else {
                total += curE - curS + 1;
                curS = p[0]; curE = p[1];
            }
        }
        if (open) total += curE - curS + 1;
        Set<Long> beaconsOnRow = new HashSet<>();
        for (int i = 0; i < n; i++) {
            if (by[i] == targetY) beaconsOnRow.add(bx[i]);
        }
        total -= beaconsOnRow.size();

        // sprawdzamy przeciecia
        List<Long> aLines = new ArrayList<>();
        List<Long> bLines = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            long r = rad[i] + 1;
            aLines.add(sy[i] - sx[i] + r);
            aLines.add(sy[i] - sx[i] - r);
            bLines.add(sy[i] + sx[i] + r);
            bLines.add(sy[i] + sx[i] - r);
        }
        List<long[]> candidates = new ArrayList<>();
        for (long a : aLines) {
            for (long b : bLines) {
                if (((b - a) & 1) != 0) continue;
                candidates.add(new long[]{(b - a) / 2, (a + b) / 2});
            }
        }
        // rogi na wszelki
        candidates.add(new long[]{0, 0});
        candidates.add(new long[]{0, limit});
        candidates.add(new long[]{limit, 0});
        candidates.add(new long[]{limit, limit});

        long freq = -1;
        for (long[] c : candidates) {
            if (c[0] < 0 || c[0] > limit || c[1] < 0 || c[1] > limit) continue;
            if (!covered(c[0], c[1])) {
                freq = c[0] * 4000000L + c[1];
                break;
            }
        }

        System.out.println("Czesc 1: " + total);
        System.out.println("Czesc 2: " + freq);
    }
}
