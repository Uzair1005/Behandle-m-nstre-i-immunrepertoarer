import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Oblig2Hele {
    private static final int ANTALL_FLETTE_TRAADER = 8;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Oblig2Hele <mappe_navn> <metadata_fil>");
            return;
        }

        String mappeNavn = args[0];
        String metadataFil = args[1];
        Monitor2 smittetMonitor = new Monitor2();
        Monitor2 friskMonitor = new Monitor2();

        // Leser metadata for å bestemme om filer skal til smittet eller frisk monitor
        lesOgSortereFiler(mappeNavn, metadataFil, smittetMonitor, friskMonitor);

        // Starter flettetråder for begge monitorene
        startFletteTraader(smittetMonitor);
        startFletteTraader(friskMonitor);

        // Venter på at begge flettetrådene er ferdige
        ventPåFletteTraader(smittetMonitor);
        ventPåFletteTraader(friskMonitor);

        // Henter ut HashMap fra hver monitor
        HashMap<String, Subsekvens> smittetMap = smittetMonitor.taUtVilkårligHashMap();
        HashMap<String, Subsekvens> friskMap = friskMonitor.taUtVilkårligHashMap();

        // Skriv ut dominante subsekvenser
        skrivUtDominanteSubsekvenser(smittetMap, friskMap);
    }

    private static void lesOgSortereFiler(String mappeNavn, String metadataFil, Monitor2 smittetMonitor, Monitor2 friskMonitor) {
        try (BufferedReader reader = new BufferedReader(new FileReader(metadataFil))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    continue;
                }
                String filnavn = parts[0];
                boolean harVirus = Boolean.parseBoolean(parts[1]);

                // Leser inn filen og legg til i riktig monitor
                File file = new File(mappeNavn, filnavn);
                if (file.isFile()) {
                    Monitor2 monitor = harVirus ? smittetMonitor : friskMonitor;
                    LeseTrad leseTrad = new LeseTrad(file.getAbsolutePath(), monitor);
                    leseTrad.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startFletteTraader(Monitor2 monitor) {
        for (int i = 0; i < ANTALL_FLETTE_TRAADER; i++) {
            FletteTrad fletteTrad = new FletteTrad(monitor);
            fletteTrad.start();
        }
    }

    private static void ventPåFletteTraader(Monitor2 monitor) {
        while (monitor.antallHashMaper() > 1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void skrivUtDominanteSubsekvenser(HashMap<String, Subsekvens> smittetMap, HashMap<String, Subsekvens> friskMap) {
        System.out.println("Dominante subsekvenser (de som er mye hyppigere hos smittede personer):");

        for (Map.Entry<String, Subsekvens> entry : smittetMap.entrySet()) {
            String subsekvens = entry.getKey();
            int smittetForekomster = entry.getValue().hentAntallForekomster();
            int friskForekomster = friskMap.getOrDefault(subsekvens, new Subsekvens(subsekvens, 0)).hentAntallForekomster();

            // velger at antallet må være minst 10 ganger høyere hos smittede
            if (smittetForekomster > friskForekomster * 10) {
                System.out.println(subsekvens + ": " + smittetForekomster + " forekomster hos smittede, " + friskForekomster + " hos friske.");
            }
        }
    }
}
