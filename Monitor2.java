import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Monitor2 {
    private final Object lock = new Object();
    private final List<HashMap<String, Subsekvens>> hashMapList = new ArrayList<>();

    public void settInnHashMap(HashMap<String, Subsekvens> hashMap) {
        synchronized (lock) {
            hashMapList.add(hashMap);
            lock.notifyAll(); // Vekker trådene som venter på tilgjengelige HashMap-er
        }
    }
 
    public HashMap<String, Subsekvens>[] hentToHashMaper() {
        synchronized (lock) {
            // Venter til det er minst to HashMap-er tilgjengelig
            while (hashMapList.size() < 2) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            // Henter ut to HashMap-er og fjerner dem fra beholderen
            HashMap<String, Subsekvens>[] maps = new HashMap[2];
            maps[0] = hashMapList.remove(0);
            maps[1] = hashMapList.remove(0);
            return maps;
        }
    }

    public int antallHashMaper() {
        synchronized (lock) {
            return hashMapList.size();
        }
    }


    // henter ut et vilkårlig HashMap
    public HashMap<String, Subsekvens> taUtVilkårligHashMap() {
        synchronized (lock) {
            if (hashMapList.isEmpty()) {
                return null;
            }
            return hashMapList.remove(0);  // Returnerer og fjerner det første elementet
        }
    }
}