package Totolotek.Gracze;

import Totolotek.*;

import java.util.ArrayList;

public abstract class GraczStaly extends Gracz {

    private final ArrayList<Kolektura> ulubioneKolektury;
    private int ostatnioOdwiedzonaKolektura;

    public GraczStaly(String imie, String nazwisko, String pesel, Waluta budzetStartowy,
                      WidokNaCentrale centrala, Kolektura kolektura) {
        super(imie, nazwisko, pesel, budzetStartowy, centrala);
        this.ulubioneKolektury = new ArrayList<>();
        ulubioneKolektury.add(kolektura);
        ostatnioOdwiedzonaKolektura = 0;
    }

    public Kolektura wybierzKolekture() {
        ostatnioOdwiedzonaKolektura = ostatnioOdwiedzonaKolektura+1 % ulubioneKolektury.size();
        return ulubioneKolektury.get(ostatnioOdwiedzonaKolektura);
    }


    public void polubKolekture(Kolektura kolektura) {
        ulubioneKolektury.add(kolektura);
    }

}
