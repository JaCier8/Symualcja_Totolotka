package Totolotek.Gracze;

import Totolotek.Kolektura;
import Totolotek.Kupon;
import Totolotek.Waluta;
import Totolotek.WidokNaCentrale;
import Totolotek.Wyjatki.BrakKolekturWyjatek;

public class Losowy extends Gracz {

    private final int maksymalnaLiczbaKuponow = 100;

    public Losowy(String imie, String nazwisko, String pesel, WidokNaCentrale centrala) {
        super(imie, nazwisko, pesel,
                new Waluta(centrala.losujLiczbe(0, 1_000_000__01))
                , centrala);
    }

    @Override
    public void kupJesliChcesz() throws BrakKolekturWyjatek {
        if (centrala.numerOstatniegoLosowania() >= numerOstatniegoLosowania) kupKupon();
    }

    // Gracz Losowy kupuje bilet poprzez wylosowanie kolektury, ilosci kuponow, ilosci zakladow i
    // ilosci losowan

    public boolean kupKupon() throws BrakKolekturWyjatek {
        int ileKuponow = centrala.losujLiczbe(1, maksymalnaLiczbaKuponow+1);
        Kolektura losowaKolektura = centrala.wylosujKolekture();
        Kupon nowy = null;
        for (int i = 0; i < ileKuponow; i++) {
            nowy = (losowaKolektura.kupKupon(
                    centrala.losujLiczbe(1, 11),
                    centrala.losujLiczbe(1,9),
                    super.kasa()));
            if (nowy == null) break;
            dodajKupon(nowy);
        }
        if (nowy == null) return false;
        numerOstatniegoLosowania = centrala.numerOstatniegoLosowania() + 1;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nTYP GRACZA: LOSOWY\n");
        sb.append(super.toString());
        return sb.toString();
    }
}
