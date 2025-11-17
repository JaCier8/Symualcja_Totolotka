package Totolotek;

import java.util.Arrays;
import java.util.TreeSet;

public class Losowanie {
    private final int numerLosowania;
    private TreeSet<Integer> wygrywajaceLiczby;
    private final WidokNaCentrale centrala;
    private final int[] iloscWygranych;
    private final Waluta[] pulaNagrod;
    private final Waluta[] wielkosciWygrancyh;


    public Losowanie(WidokNaCentrale centrala, TreeSet<Integer> wygrywajaceLiczby,
                     int[] iloscWygranych, Waluta budzetNaLosowanie, Waluta kumulacja) {
        this.centrala = centrala;
        this.numerLosowania = centrala.numerOstatniegoLosowania();
        this.wygrywajaceLiczby = wygrywajaceLiczby;
        this.iloscWygranych = iloscWygranych;
        pulaNagrod = obliczPuleNagrod(budzetNaLosowanie, kumulacja, iloscWygranych);
        pulaNagrod[0].dodaj(kumulacja);
        if (iloscWygranych[0] == 0)
            kumulacja.dodaj(((long) (budzetNaLosowanie.grosze() * centrala.mnoznikPierwszegoStopnia())));
        else kumulacja.odejmij(kumulacja);
        wielkosciWygrancyh = obliczWielkosciWygrancyh(iloscWygranych);
    }


    // Zlicza, jakie są nagrody, z zysków z kupionych kuponów
    private Waluta[] obliczPuleNagrod(Waluta budzetNaLosowanie, Waluta kumulacja, int[] iloscWygranych) {
        Waluta[] pula = new Waluta[4];
        long stp1 = ((long)(budzetNaLosowanie.grosze() * centrala.mnoznikPierwszegoStopnia()));
        if (stp1 < centrala.minimalnaNagrodaGlowna()) {
            pula[0] = new Waluta(centrala.minimalnaNagrodaGlowna());
        }
        else {
            pula[0] = new Waluta(stp1);
        }
        pula[1] = new Waluta(budzetNaLosowanie.iloczyn(centrala.mnoznikDrugiegoStopnia()));
        pula[3] = new Waluta(centrala.nagrodaCzwartegoStopnia()*iloscWygranych[3]);
        pula[2] = new Waluta(budzetNaLosowanie);
        pula[2].odejmij(pula[0]);
        pula[2].odejmij(pula[1]);
        pula[2].odejmij(pula[3]);
        if (pula[2].grosze() < centrala.minimalnaNagrodaTrzeciegoStopnia()) {
            pula[2] = new Waluta(centrala.minimalnaNagrodaTrzeciegoStopnia());
        }
        return pula;
    }

    // Dzieli wygrane na ilość sczęśliwców
    private Waluta[] obliczWielkosciWygrancyh(int[] iloscWygranych) {
        Waluta[] wynik = new Waluta[4];
        for (int i = 0; i < 4; i++) {
            wynik[i] = pulaNagrod[i].iloraz(iloscWygranych[i]);
        }
        return wynik;
    }


    public TreeSet<Integer> wygrywajaceLiczby() {
        return new TreeSet<>(wygrywajaceLiczby);
    }

    public int[] iloscWygranych() {
        return Arrays.copyOf(iloscWygranych, iloscWygranych.length);
    }

    public Waluta[] wielkosciWygranych() {
        Waluta[] wynik = new Waluta[4];
        for (int  i = 0; i < wielkosciWygrancyh.length; i++) {
            wynik[i] = new Waluta(wielkosciWygrancyh[i]);
        }
        return wynik;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LOSOWANIE NR ");
        sb.append(numerLosowania);
        sb.append("\nWyniki: ");
        for (int liczba : wygrywajaceLiczby) {
            sb.append(liczba > 9 ? "" : " ");
            sb.append(liczba);
            sb.append(" ");
        }
        return sb.toString();
    }
}
