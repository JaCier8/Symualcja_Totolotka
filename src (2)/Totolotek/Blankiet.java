package Totolotek;

import java.util.*;

public class Blankiet {
    private boolean[] anuluj;
    private ArrayList<TreeSet<Integer>> zawartosc;
    private TreeSet<Integer> iloscLosowan;

    public Blankiet() {
        anuluj = new boolean[8];
        iloscLosowan = new TreeSet<>();
        zawartosc = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            zawartosc.add(new TreeSet<>());
        }
    }

    public Blankiet(ArrayList<TreeSet<Integer>> liczby, boolean[] czyAnuluj, int[] iloscLosowan) {
        zawartosc = new ArrayList<>(8);
        this.iloscLosowan = new TreeSet<>();
        for (int i = 0; i < 8; i++) {
            TreeSet<Integer> akt;
            if (liczby.size() <= i || liczby.get(i) == null) {
                akt = new TreeSet<>();
            }
            else {
                akt = new TreeSet<>(liczby.get(i));
            }
            zawartosc.add(akt);
        }
        anuluj = czyAnuluj;
        for (int x : iloscLosowan) this.iloscLosowan.add(x);
    }

    public Blankiet(ArrayList<TreeSet<Integer>> liczby, boolean[] czyAnuluj, TreeSet<Integer> drzewoLosowan) {
        zawartosc = new ArrayList<>(8);
        this.iloscLosowan = new TreeSet<>(drzewoLosowan);
        for (int i = 0; i < 8; i++) {
            TreeSet<Integer> akt = new TreeSet<>(liczby.get(i));
            zawartosc.add(akt);
        }
        anuluj = czyAnuluj;
    }

    public void zakreslLiczbe(int numerZakladu, int x) throws IndexOutOfBoundsException {
        if (numerZakladu > 8 || numerZakladu <= 0) throw new IndexOutOfBoundsException("Zły indeks numeru zakładu: " + numerZakladu);
        if (x > 49 || x <= 0) throw new IndexOutOfBoundsException("Nie można zakreślić liczby: " + x);
        zawartosc.get(numerZakladu - 1).add(x);
    }

    public void zakreslAnulujZakladu(int numerZakladu) throws IndexOutOfBoundsException {
        if (numerZakladu > 8 || numerZakladu <= 0) throw new IndexOutOfBoundsException("Zły indeks numeru zakładu: " + numerZakladu);
        anuluj[numerZakladu - 1] = true;
    }

    public void zakreslLiczbeLosowan(int x) throws IndexOutOfBoundsException {
        if (x > 10 || x <= 0) throw new IndexOutOfBoundsException("Nie można postawić zakładu" +
                " na więcej niż 10 losowań, postawiono na: " + x);
        iloscLosowan.add(x);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 1; j < 50; j++) {
                sb.append("[ ");
                if (j < 10 && !zawartosc.get(i).contains(j)) sb.append(" ");
                sb.append(zawartosc.get(i).contains(j) ? "--" : j);
                sb.append(" ]");
                if (j % 10 == 0) sb.append("\n");
                else sb.append(" ");
            }
            sb.append("\n[ ");
            sb.append(anuluj[i] ? "--" : "  ");
            sb.append(" ] anuluj\n");
        }
        return sb.toString();
    }

    public int najIloscLosowan() {
        if (iloscLosowan.isEmpty()) return 1;
        return iloscLosowan.getLast();
    }
    public boolean czyAnulowany(int i) {
        return anuluj[i];
    }

    public ArrayList<TreeSet<Integer>> zawartosc() {
        return zawartosc;
    }

    public TreeSet<Integer> iloscLosowan() {
        return iloscLosowan;
    }
}
