package Totolotek;

import java.util.*;

public class Kupon {
    private boolean czyPodarty;
    private final String identyfikator;
    private ArrayList<TreeSet<Integer>> liczby;
    private final TreeSet<Integer> losowania;
    private final WidokNaCentrale centrala;
    private final Kolektura kolektura;

    // Inicjalizacja kuponu na chybił trafił
    public Kupon(int numerKolektury, int numerKuponu, WidokNaCentrale centrala, int iloscLosowan, int iloscZakladow) {
        this(
                numerKolektury,
                numerKuponu,
                centrala,
                new Blankiet(
                        centrala.losujNLiczb(iloscZakladow),
                        new boolean[8],
                        treeSetZElemntem(iloscLosowan))
                        );
    }

    // Inicjalizacja kuponu z blankietem
    public Kupon(int numerKolektury, int numerKuponu, WidokNaCentrale centrala, Blankiet blankiet) {
        czyPodarty = false;
        this.centrala = centrala;
        identyfikator = zbudujIdentyfikator(numerKolektury, numerKuponu);
        liczby = new ArrayList<>(8);
        kolektura = centrala.pokazKolektury().get(numerKolektury-1);
        int  iloscLosowan = blankiet.najIloscLosowan();
        losowania = new TreeSet<>();
        for (int i = 0; i < iloscLosowan; i++) {
            losowania.add(centrala.numerOstatniegoLosowania()+1+i);
        }
        ArrayList<TreeSet<Integer>> zaznaczone = blankiet.zawartosc();
        for (int i = 0; i < zaznaczone.size(); i++) {
            TreeSet<Integer> zaklad = zaznaczone.get(i);
            if (zaklad.size() == 6 && !blankiet.czyAnulowany(i)) {
                liczby.add(new TreeSet<>(zaklad));
            }
        }
    }

    public void podrzyjKupon() {
        czyPodarty = true;
    }

    private static TreeSet<Integer> treeSetZElemntem(int x) {
        TreeSet<Integer> drzewo = new TreeSet<>();
        drzewo.add(x);
        return drzewo;
    }
    //
    private String zbudujIdentyfikator(int numerKolektury, int numerKuponu) {
        StringBuilder sb = new StringBuilder();
        sb.append(numerKuponu);
        sb.append("-");
        sb.append(numerKolektury);
        sb.append("-");
        int losowyFragment = centrala.losujIdentyfikator();
        sb.append(losowyFragment);
        int suma = sumaCyfr(numerKolektury) + sumaCyfr(numerKuponu) + sumaCyfr(losowyFragment);
        sb.append("-");
        sb.append(suma%100);
        return sb.toString();
    }


    private static int sumaCyfr(int x) {
        int suma = 0;
        while (x > 0) {
            suma += x % 10;
            x /= 10;
        }
        return suma;
    }

    public ArrayList<TreeSet<Integer>> liczby() {
        ArrayList<TreeSet<Integer>> kopia = new ArrayList<>();
        for (TreeSet<Integer> drzewo : this.liczby) {
            kopia.add(new TreeSet<>(drzewo));
        }
        return kopia;
    }

    public TreeSet<Integer> losowania() {
        return new TreeSet<>(losowania);
    }

    public boolean czyPodarty() {
        return czyPodarty;
    }

    public String identyfikator() {
        return identyfikator;
    }

    public Kolektura kolektura() {
        return kolektura;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("KUPON NR ").append(identyfikator).append("\n");
        for (int i = 0; i < liczby.size(); i++) {
            TreeSet<Integer> zaklad = liczby.get(i);
            sb.append(i+1).append(": ");
            for (int liczba : zaklad) {
                if (liczba < 10) sb.append(" ");
                sb.append(liczba).append(" ");
            }
            sb.append("\n");
        }
        sb.append("LICZBA LOSOWAŃ: ").append(losowania.size()).append("\n");
        sb.append("NUMERY LOSOWAŃ: \n");
        for (int liczba : losowania) {
            if (liczba < 10) sb.append(" ");
            sb.append(liczba).append(" ");
        }
        Waluta cena = new Waluta(centrala.cenaZakladu());
        cena.przemnoz(losowania.size());
        cena.przemnoz(liczby.size());
        sb.append("\nCENA: ");
        sb.append(cena);
        return sb.toString();
    }
}
