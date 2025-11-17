package Totolotek.Gracze;

import Totolotek.Kupon;
import Totolotek.Waluta;
import Totolotek.WidokNaCentrale;
import Totolotek.Wyjatki.BrakKolekturWyjatek;

import java.util.ArrayList;

public abstract class Gracz {
    private final String imie;
    private final String nazwisko;
    private final String pesel;
    final WidokNaCentrale centrala;
    int iloscSprawdzonchKuponow;
    private Waluta kasa;
    int numerOstatniegoLosowania;
    private ArrayList<Kupon> kupony;

    public Gracz(String imie, String nazwisko, String pesel, Waluta budzetStartowy, WidokNaCentrale centrala) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.pesel = pesel;
        this.kasa = budzetStartowy;
        this.centrala = centrala;
        this.kupony = new ArrayList<>();
    }

    abstract boolean kupKupon() throws BrakKolekturWyjatek;

    public abstract void kupJesliChcesz() throws BrakKolekturWyjatek;

    public void odbierzNagordySkonczonychKuponow() {
        for (Kupon kupon : kupony) {
            if (kupon.losowania().getLast() <= centrala.numerOstatniegoLosowania()) {
                kupon.kolektura().wydajNagrode(kupon, kasa);
            }
        }
    }

    public void dodajKupon(Kupon kupon) {
        kupony.add(kupon);
    }

    Waluta kasa() {
        return kasa;
    }

    public int iloscKuponow() {
        return kupony.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Imię: ");
        sb.append(imie);
        sb.append("\nNazwisko: ");
        sb.append(nazwisko);
        sb.append("\nPESEL: ");
        sb.append(pesel);
        sb.append("\nPOSIADANE KUPONY: ");
        if (kupony.isEmpty()) return sb.append("\nNIE POSIADA KUPONÓW").toString();
        for (Kupon kupon : kupony) sb.append("\n").append(kupon.identyfikator());
        return sb.toString();
    }
}
