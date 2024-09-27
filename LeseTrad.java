import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class LeseTrad extends Thread {
    private String filnavn;
    private Monitor2 monitor;

    public LeseTrad(String filnavn, Monitor2 monitor) {
        this.filnavn = filnavn;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        HashMap<String, Subsekvens> map = lesFilOgLagHashMap(filnavn);
        monitor.settInnHashMap(map);
    }

    private HashMap<String, Subsekvens> lesFilOgLagHashMap(String filnavn) {
        HashMap<String, Subsekvens> hashMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filnavn))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() < 3) {
                    continue;
                }

                for (int i = 0; i <= line.length() - 3; i++) {
                    String subsekvens = line.substring(i, i + 3);
                    hashMap.put(subsekvens, new Subsekvens(subsekvens, 1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hashMap;
    }
}
