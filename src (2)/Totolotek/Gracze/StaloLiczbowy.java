package Totolotek.Gracze;

import Totolotek.*;
import Totolotek.Wyjatki.BrakKolekturWyjatek;

import java.util.TreeSet;

public class StaloLiczbowy extends GraczStaly {

    private final TreeSet<Integer> ulubioneLiczby;

    public StaloLiczbowy(String imie, String nazwisko, String pesel,
                         Waluta budzetStartowy, WidokNaCentrale centrala,
                         TreeSet<Integer> ulubioneLiczby, Kolektura kolektura) {
        super(imie, nazwisko, pesel, budzetStartowy, centrala, kolektura);
        this.ulubioneLiczby = ulubioneLiczby;
    }

    private Blankiet wypelnijBlankietUlubionymiLiczbami() {

        Blankiet ulubionyBlankiet = new Blankiet();
        for (int liczba : ulubioneLiczby) {
            ulubionyBlankiet.zakreslLiczbe(1, liczba);
        }
        ulubionyBlankiet.zakreslLiczbeLosowan(10);
        return ulubionyBlankiet;
    }

    @Override
    public void kupJesliChcesz() throws BrakKolekturWyjatek {
        if (centrala.numerOstatniegoLosowania() >= numerOstatniegoLosowania + 9) kupKupon();
    }

    // Gracz stołoliczbowy wypełnia swój ulubiony blankiet i kupuje go w wybranej kolekturze.
    // Jeśli go nie stać, funkcja przekazuje false, wpp przekazuje true i daje graczowi kupon
    @Override
    public boolean kupKupon() {
        Blankiet blankiet = wypelnijBlankietUlubionymiLiczbami();
        Kolektura kolektura = wybierzKolekture();
        Kupon nowy = kolektura.kupKupon(blankiet, kasa());
        if (nowy == null) return false;
        dodajKupon(nowy);
        numerOstatniegoLosowania = centrala.numerOstatniegoLosowania() + 1;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TYP GRACZA: STAŁOLICZBOWY\n");
        sb.append(super.toString());
        sb.append("ULUBIONE LICZBY:\n");
        sb.append(ulubioneLiczby);
        return sb.toString();
    }


}
