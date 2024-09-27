public class Subsekvens {
    public final String subsekvens;
    private int antallForekomster;

    public Subsekvens(String subsekvens, int antallForekomster) {
        this.subsekvens = subsekvens;
        this.antallForekomster = antallForekomster;
    }

    public int hentAntallForekomster() {
        return antallForekomster;
    }

    public void økAntallForekomster() {
        antallForekomster++;
    }

    public String hentSubsekvens() {
        return this.subsekvens;
    }

    @Override
    public String toString() {
        return "(" + subsekvens + "," + antallForekomster + ")";
    }
}
