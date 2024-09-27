import java.util.HashMap;
import java.util.Map;

public class FletteTrad extends Thread {
    private final Monitor2 monitor;

    public FletteTrad(Monitor2 monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            HashMap<String, Subsekvens>[] maps = monitor.hentToHashMaper();
            HashMap<String, Subsekvens> map1 = maps[0];
            HashMap<String, Subsekvens> map2 = maps[1];

            if (map1 == null || map2 == null) {
                if (map1 != null) {
                    monitor.settInnHashMap(map1);
                }
                if (map2 != null) {
                    monitor.settInnHashMap(map2);
                }
                break;
            }

            HashMap<String, Subsekvens> flettetMap = slåSammenHashMaper(map1, map2);
            monitor.settInnHashMap(flettetMap);
        }
    }

    private HashMap<String, Subsekvens> slåSammenHashMaper(HashMap<String, Subsekvens> map1, HashMap<String, Subsekvens> map2) {
        HashMap<String, Subsekvens> sammenslåttMap = new HashMap<>(map1);

        for (Map.Entry<String, Subsekvens> entry : map2.entrySet()) {
            String subsekvens = entry.getKey();
            Subsekvens subsekvens2 = entry.getValue();

            sammenslåttMap.merge(subsekvens, subsekvens2, (sub1, sub2) -> 
                new Subsekvens(sub1.hentSubsekvens(), sub1.hentAntallForekomster() + sub2.hentAntallForekomster())
            );
        }

        return sammenslåttMap;
    }
}
