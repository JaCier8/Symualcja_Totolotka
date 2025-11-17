package Totolotek;

import Totolotek.Wyjatki.BrakKolekturWyjatek;

import java.util.ArrayList;
import java.util.TreeSet;

public interface WidokNaCentrale {
    double mnoznikDrugiegoStopnia();

    double mnoznikPierwszegoStopnia();

    long minimalnaNagrodaTrzeciegoStopnia();

    long nagrodaCzwartegoStopnia();

    long minimalnaNagrodaGlowna();

    long cenaZakladu();

    int numerOstatniegoLosowania();

    int iloscKolektur();

    int iloscKuponow();

    void ZwiekszIloscKuponow();

    void pobierzKase(Waluta ilosc, Waluta portfel, int numerLosowania);

    ArrayList<Kolektura> pokazKolektury();

    int losujLiczbe(int od, int koniec);

    void wydajNagrode(int stopien, Waluta portfel, int numerLosowania);

    TreeSet<Integer> dajWygrywajaceZLosowania(int i);

    Kolektura wylosujKolekture() throws BrakKolekturWyjatek;
    int losujIdentyfikator();
    ArrayList<TreeSet<Integer>> losujNLiczb(int n);
    TreeSet<Integer> losujSzostke();

}