package Totolotek.Gracze;

import Totolotek.Kolektura;
import Totolotek.Kupon;
import Totolotek.Waluta;
import Totolotek.WidokNaCentrale;

public class Minimalista extends Gracz {

    private final Kolektura ulubionaKolektura;

    // Minimalista kupuje jeden kupon w swojej ulubionej kolekturze z jednym zakladem na jedno losowanie
    // Jeśli użytkownika stać to funkcja przekaże true i da graczowi ten kupon, jeśli nie to przekaże false


    public Minimalista(String imie, String nazwisko, String pesel, Waluta budzetStartowy, WidokNaCentrale centrala, Kolektura kolektura) {
        super(imie, nazwisko, pesel, budzetStartowy ,centrala);
        this.ulubionaKolektura = kolektura;
    }

    @Override
    public void kupJesliChcesz() {
        if (centrala.numerOstatniegoLosowania() >= numerOstatniegoLosowania) kupKupon();
    }
    @Override
    public boolean kupKupon() {
        Kupon kupon = ulubionaKolektura.kupKupon(1, 1, super.kasa());
        if (kupon != null) {
            dodajKupon(kupon);
            numerOstatniegoLosowania = centrala.numerOstatniegoLosowania() + 1;
            return true;
        }
        return false;
    }

    public Kolektura ulubionaKolektura() {
        return ulubionaKolektura;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nTYP GRACZA: MINIMALISTA\n");
        sb.append("ULUBIONA KOLEKTURA: ");
        sb.append(ulubionaKolektura.numerKolektury());
        sb.append("\n");
        sb.append(super.toString());
        return sb.toString();
    }
}
