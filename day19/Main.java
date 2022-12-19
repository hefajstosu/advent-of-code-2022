import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static int oreOre, clayOre, obsOre, obsClay, geoOre, geoObs, maxOre, best;

    // ile czekac
    static int waitFor(int cost, int have, int rate) {
        if (have >= cost) return 0;
        return (cost - have + rate - 1) / rate;
    }

    // DFS po robotach
    // geody od razu
    static void dfs(int time, int ore, int clay, int obs, int rOre, int rClay, int rObs, int geodes) {
        if (geodes > best) best = geodes;
        // obcinamy galezie
        if (geodes + time * (time - 1) / 2 <= best) return;

        if (rObs > 0) {
            int wait = Math.max(waitFor(geoOre, ore, rOre), waitFor(geoObs, obs, rObs));
            int t = time - wait - 1;
            if (t > 0) {
                dfs(t, ore + rOre * (wait + 1) - geoOre, clay + rClay * (wait + 1), obs + rObs * (wait + 1) - geoObs,
                    rOre, rClay, rObs, geodes + t);
            }
        }
        if (rClay > 0 && rObs < geoObs) {
            int wait = Math.max(waitFor(obsOre, ore, rOre), waitFor(obsClay, clay, rClay));
            int t = time - wait - 1;
            if (t > 0) {
                dfs(t, ore + rOre * (wait + 1) - obsOre, clay + rClay * (wait + 1) - obsClay, obs + rObs * (wait + 1),
                    rOre, rClay, rObs + 1, geodes);
            }
        }
        if (rClay < obsClay) {
            int wait = waitFor(clayOre, ore, rOre);
            int t = time - wait - 1;
            if (t > 0) {
                dfs(t, ore + rOre * (wait + 1) - clayOre, clay + rClay * (wait + 1), obs + rObs * (wait + 1),
                    rOre, rClay + 1, rObs, geodes);
            }
        }
        if (rOre < maxOre) {
            int wait = waitFor(oreOre, ore, rOre);
            int t = time - wait - 1;
            if (t > 0) {
                dfs(t, ore + rOre * (wait + 1) - oreOre, clay + rClay * (wait + 1), obs + rObs * (wait + 1),
                    rOre + 1, rClay, rObs, geodes);
            }
        }
    }

    static int maxGeodes(int[] bp, int time) {
        oreOre = bp[1];
        clayOre = bp[2];
        obsOre = bp[3];
        obsClay = bp[4];
        geoOre = bp[5];
        geoObs = bp[6];
        maxOre = Math.max(Math.max(oreOre, clayOre), Math.max(obsOre, geoOre));
        best = 0;
        dfs(time, 0, 0, 0, 1, 0, 0, 0);
        return best;
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data.txt";
        String text = Files.readString(Path.of(path));

        // parsujemy koszty
        List<Integer> nums = new ArrayList<>();
        Matcher m = Pattern.compile("\\d+").matcher(text);
        while (m.find()) nums.add(Integer.parseInt(m.group()));
        List<int[]> blueprints = new ArrayList<>();
        for (int i = 0; i + 6 < nums.size(); i += 7) {
            int[] bp = new int[7];
            for (int j = 0; j < 7; j++) bp[j] = nums.get(i + j);
            blueprints.add(bp);
        }

        long part1 = 0;
        for (int[] bp : blueprints) part1 += (long) bp[0] * maxGeodes(bp, 24);

        long part2 = 1;
        for (int i = 0; i < Math.min(3, blueprints.size()); i++) part2 *= maxGeodes(blueprints.get(i), 32);

        System.out.println("Czesc 1: " + part1);
        System.out.println("Czesc 2: " + part2);
    }
}
