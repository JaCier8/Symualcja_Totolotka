package Totolotek.Gracze;

import Totolotek.*;
import Totolotek.Wyjatki.BrakKolekturWyjatek;

public class StaloBlankietowy extends GraczStaly {

    private final Blankiet ulubionyBlankiet;
    private final int coIleKupuje;

    public StaloBlankietowy(String imie, String nazwisko, String pesel, Waluta budzetStartowy,
                            WidokNaCentrale centrala, Blankiet ulubionyBlankiet, Kolektura kolektura, int coIleKupuje) {
        super(imie, nazwisko, pesel, budzetStartowy, centrala, kolektura);
        this.coIleKupuje = coIleKupuje;
        this.ulubionyBlankiet = ulubionyBlankiet;
    }

    @Override
    public void kupJesliChcesz() throws BrakKolekturWyjatek {
        if (centrala.numerOstatniegoLosowania() >= numerOstatniegoLosowania + coIleKupuje-1) kupKupon();
    }
    // Gracz stałoblankietowy kupuje kupon w jednej z ulubionych kolektur przy pomocy ulubionego blankietu.
    // Jeśli go nie stać, funkcja przekazuje false, wpp przekazuje true i daje graczowi kupon.
    @Override
    public boolean kupKupon() {
        Kolektura kolektura = wybierzKolekture();
        Kupon nowy = kolektura.kupKupon(ulubionyBlankiet, kasa());
        if (nowy == null) return false;
        dodajKupon(nowy);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TYP GRACZA: STAŁOBLANKIETOWY\n");
        sb.append(super.toString());
        sb.append("ULUBIONY BLANKIET:\n");
        sb.append(ulubionyBlankiet);
        sb.append("\nCoIleKupuje: ");
        sb.append(coIleKupuje);
        return sb.toString();
    }
}
