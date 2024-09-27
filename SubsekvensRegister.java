import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class SubsekvensRegister {
    private ArrayList<HashMap<String, Subsekvens>> register;

    public SubsekvensRegister() {
        register = new ArrayList<>();
    }

    public void settInnHashMap(HashMap<String, Subsekvens> hashMap) {
        register.add(hashMap);
    }

    public HashMap<String, Subsekvens> taUtVilkårligHashMap() {
        if (register.isEmpty()) {
            return null;
        }
        return register.remove(0);
    }

    public int antallHashMaper() {
        return register.size();
    }



    public static HashMap<String, Subsekvens> lagHashMapFraFil(String filnavn) {
        HashMap<String, Subsekvens> subsekvensMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filnavn))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() < 3) {
                    System.out.println("Linjen er for kort. Stoppet programmet.");
                    System.exit(1);
                }

                for (int i = 0; i <= line.length() - 3; i++) {
                    String subsekvens = line.substring(i, i + 3);
                    if (!subsekvensMap.containsKey(subsekvens)) {
                        subsekvensMap.put(subsekvens, new Subsekvens(subsekvens, 1));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Feil ved lesing av fil: " + e.getMessage());
        }

        return subsekvensMap;
    }




    public static HashMap<String, Subsekvens> slåSammenHashMaper(HashMap<String, Subsekvens> map1, HashMap<String, Subsekvens> map2) {
        HashMap<String, Subsekvens> sammenslåttMap = new HashMap<>();

        for (Map.Entry<String, Subsekvens> entry : map1.entrySet()) {
            String subsekvens = entry.getKey();
            Subsekvens subsekvens1 = entry.getValue();

            if (map2.containsKey(subsekvens)) {
                Subsekvens subsekvens2 = map2.get(subsekvens);
                int totaltAntallForekomster = subsekvens1.hentAntallForekomster() + subsekvens2.hentAntallForekomster();
                sammenslåttMap.put(subsekvens, new Subsekvens(subsekvens, totaltAntallForekomster));
            } else {
                sammenslåttMap.put(subsekvens, subsekvens1);
            }
        }

        for (Map.Entry<String, Subsekvens> entry : map2.entrySet()) {
            String subsekvens = entry.getKey();
            if (!map1.containsKey(subsekvens)) {
                sammenslåttMap.put(subsekvens, entry.getValue());
            }
        }

        return sammenslåttMap;
    }
}




