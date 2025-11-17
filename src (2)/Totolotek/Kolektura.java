package Totolotek;

import java.util.ArrayList;
import java.util.TreeSet;

public class Kolektura {

    private final int numerKolektury;
    // Kazde losowanie to jedna lista kuponow, które się liczą do tego losowania.
    private ArrayList<ArrayList<Kupon>> kupony;
    private final WidokNaCentrale centrala;


    public Kolektura(WidokNaCentrale centrala) {
        this.centrala = centrala;
        numerKolektury = centrala.iloscKolektur() + 1;
        kupony = new ArrayList<>();
    }
    // Kupowanie kuponu z blankietem, dodawanie go do listy kuponów, wysyłanie pieniędzy od gracza do centrali
    public Kupon kupKupon(Blankiet blankiet, Waluta portfel) {
        Kupon nowy = new Kupon(numerKolektury, centrala.iloscKuponow() + 1, centrala, blankiet);
        int iloscPoprawnychZakladow = 0;
        int iloscLosowan = blankiet.najIloscLosowan();
        for (TreeSet<Integer> zaklad : blankiet.zawartosc()) {
            if (zaklad.size() == 6) {
                iloscPoprawnychZakladow++;
            }
        }
        Waluta nalezne = new Waluta(centrala.cenaZakladu()*iloscPoprawnychZakladow);
        if (portfel.grosze() < nalezne.grosze()) {
            return null;
        }
        for (int  i = 0; i < iloscLosowan; i++)
            centrala.pobierzKase(new Waluta(nalezne), portfel, centrala.numerOstatniegoLosowania() + 1 + i);
        dodajKuponDoListy(nowy, iloscLosowan);
        return nowy;
    }

    // Kupowanie kuponu z losowymi liczbami, dodawanie go do listy kuponów, wysyłanie pieniędzy od gracza do centrali
    public Kupon kupKupon(int iloscLosowan, int iloscZakladow, Waluta portfel) {
        Waluta nalezne = new Waluta(centrala.cenaZakladu() * iloscZakladow);
        if (portfel.grosze() < nalezne.grosze()) {
            return null;
        }
        for (int  i = 0; i < iloscLosowan; i++) {
            centrala.pobierzKase(new Waluta(nalezne), portfel, centrala.numerOstatniegoLosowania() + 1 + i);
        }
        Kupon nowy = new Kupon(numerKolektury,
                centrala.iloscKuponow() + 1,
                centrala, iloscLosowan, iloscZakladow);
        dodajKuponDoListy(nowy, iloscLosowan);
        return nowy;
    }

    private void dodajKuponDoListy(Kupon kupon, int iloscLosowan) {
        int nrOstLosowania = centrala.numerOstatniegoLosowania();
        while (nrOstLosowania + iloscLosowan > kupony.size()) {
            kupony.add(null);
        }
        centrala.ZwiekszIloscKuponow();
        for (int i = nrOstLosowania; i < nrOstLosowania + iloscLosowan; i++){
            if (kupony.get(i) == null) {
                kupony.set(i, new ArrayList<>());
            }
            kupony.get(i).add(kupon);
        }
    }



    public WidokNaCentrale centrala() {
        return centrala;
    }


    // Wydaje nagordę za kupon, zwraca true wtw kupon został kupiony w kolekturze i nie jest podarty
    public boolean wydajNagrode(Kupon kupon, Waluta portfel) {
        int trafienia = 0;
        ArrayList<TreeSet<Integer>> liczby = kupon.liczby();
        if (kupon.czyPodarty()) return false;
        for (int numerLosowania : kupon.losowania()) {
            if (numerLosowania >= kupony.size() ||
                    kupony.get(numerLosowania) == null ||
                    !kupony.get(numerLosowania).contains(kupon)) {
                kupon.podrzyjKupon(); return false;
            }
            TreeSet<Integer> wygrLiczby = centrala.dajWygrywajaceZLosowania(numerLosowania);
            for (TreeSet<Integer> zaznaczone : liczby) {
                TreeSet<Integer> trafione = new TreeSet<>(wygrLiczby);
                trafione.retainAll(zaznaczone);
                trafienia = trafione.size();
                if (trafienia > 2) centrala.wydajNagrode(7-trafienia, portfel, numerLosowania);
            }
        }
        return true;
    }


    public int[] sprawdzIleTrafienWLosowaniu(int losowanie, TreeSet<Integer> wygrLiczby) {
        int[] wygrane = new int[4]; //  1stp,2stp,3stp,4stp
        if(kupony.size() < losowanie || kupony.get(losowanie-1) == null) return wygrane;
        for (Kupon kupon : kupony.get(losowanie-1)) {
            for (TreeSet<Integer> zaklad : kupon.liczby()) {
                int trafienia = 0;
                for (int liczba : wygrLiczby) {
                    if (zaklad.contains(liczba)) trafienia++;
                }
                if (trafienia > 2) {
                    wygrane[6-trafienia]++;
                }
            }
        }
        return wygrane;
    }

    public int numerKolektury() {
        return numerKolektury;
    }
}
