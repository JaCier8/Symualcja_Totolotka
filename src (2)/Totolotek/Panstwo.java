package Totolotek;

public class Panstwo {
    private Waluta pobranePodatki;
    private Waluta wydaneSubwencje;

    public Panstwo() {
        pobranePodatki = new Waluta();
        wydaneSubwencje = new Waluta();
    }

    public void wydajSubwencje(Waluta ilosc, Waluta portfel) {
        portfel.dodaj(ilosc);
        wydaneSubwencje.dodaj(ilosc);
    }

    public void pobierzPodatek(Waluta ilosc, Waluta portfel) {
        portfel.odejmij(ilosc);
        pobranePodatki.dodaj(ilosc);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Wydano subwencji: ").append(wydaneSubwencje).append("\n");
        sb.append("Pobrano podatków: ").append(pobranePodatki);
        return sb.toString();
    }
}
