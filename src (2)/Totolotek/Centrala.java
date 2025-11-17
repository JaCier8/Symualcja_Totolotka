package Totolotek;

/* __NAGŁÓWEK__
Autor: Jan Ciecierski
Projekt: Symulacja losowań totolotka na potrzeby
         projektu zaliczeniowego z Programowania
         obiektowego na WMIM UW
Schemat działania poniżej
 */

import Totolotek.Wyjatki.BrakKolekturWyjatek;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

// Centrala zajmuje się tworzeniem kolektur, zbieraniem od nich pieniędzy, wypłacaniem nagród, przeprowadza losowania

/* Sam schemat losowania odbywa się poprzez: gracz kupuje kupon w kolekturze, którego każdy zakład jest dodawany do
   listy zakładów dla poszególnego losowania, od razu przy kupnie kolektura wysyła pieniądze do centrali, która rozdziela
   pieniądze na: podatek, nagrody i zysk, po uzbiraniu się kuponów w kolekturach, centrala losuje szóstkę, którą wysyła
   kolekturom, które liczą ile osób wygrało poszczególne nagrody, centrala to podlicza i liczy ile przypada na nagrodę
   n-tego stopnia. Gracz po losowaniu ma możliwość odebrania nagrody w swojej kolekturze.
 */
public class Centrala implements  WidokNaCentrale {

    private final long minimalnaNagrodaGlowna = 2_000_000__00L;
    private final long minimalnaNagrodaTrzeciegoStopnia = 36__00L;
    private final long nagordaCzwartegoStopnia = 24__00L;
    private final double mnoznikPierwszegoStopnia = 0.44;
    private final double mnoznikDrugiegoStopnia = 0.08;

    private final long cenaZakladu = 3__00L;
    private final double mnoznikPodatku = 0.2;
    private final double mnoznikZysku = 0.51;

    private final long minimalnaOpodatkowanaNagroda = 2280__00L;
    private final double podatekOdNagrody = 0.1;


    private Waluta budzet;
    private ArrayList<Waluta> budzetNaLosowanie;
    private Waluta kumulacja;
    private ArrayList<Losowanie> losowania;
    private ArrayList<Kolektura> kolektury;
    private int numerOstatniegoLosowania;
    private int iloscKuponow;
    private final Panstwo panstwo;

    private final Random rand;

    private Waluta[] TrafionePieniadzeWygranePoszczegolnychStopni;
    private int[] iloscTrafionychWygranychPoszczegolnychStopni;

    public Centrala(Panstwo panstwo, Waluta budzetPoczatkowy) {
        this.budzet = budzetPoczatkowy;
        this.panstwo = panstwo;
        this.kumulacja = new Waluta();
        this.rand = new Random();
        this.numerOstatniegoLosowania = 0;
        this.iloscKuponow = 0;
        this.kolektury = new ArrayList<>();
        this.losowania = new ArrayList<>();
        this.budzetNaLosowanie = new ArrayList<>();
        TrafionePieniadzeWygranePoszczegolnychStopni = new Waluta[4];
        for (int i = 0; i < 4; i++) TrafionePieniadzeWygranePoszczegolnychStopni[i] = new Waluta();
        iloscTrafionychWygranychPoszczegolnychStopni = new int[4];
    }
    // Jeśli brakuje w budzecie na losowania to siegamy do zysków z wcześniejkszych losowań,
    // jeśli to też zawiedzie to bierzemy subwencje od państwa
    // Gracz płaci podatek dopiero od pewnej kwoty
    public void wydajNagrode(int stopien, Waluta portfel, int numerLosowania) {
        Waluta wartosc = new Waluta(losowania.get(numerLosowania-1).wielkosciWygranych()[stopien-1]);
        iloscTrafionychWygranychPoszczegolnychStopni[stopien-1]++;
        TrafionePieniadzeWygranePoszczegolnychStopni[stopien-1].dodaj(wartosc);
        if (wartosc.grosze() >= minimalnaOpodatkowanaNagroda) {
            Waluta podatek = wartosc.iloczyn(podatekOdNagrody);
            panstwo.pobierzPodatek(podatek, portfel);
            wartosc.odejmij(podatek);
        }
        Waluta aktBudzet = budzetNaLosowanie.get(numerLosowania-1);
        if (aktBudzet.grosze() < wartosc.grosze()) {
            wartosc.odejmij(aktBudzet);
            portfel.dodaj(aktBudzet);
            aktBudzet.odejmij(aktBudzet);
            if (budzet.grosze() < wartosc.grosze()) {
                wartosc.odejmij(budzet);
                portfel.dodaj(budzet);
                budzet.odejmij(budzet);
                panstwo.wydajSubwencje(wartosc, portfel);
            }else {
                budzet.odejmij(wartosc);
                portfel.dodaj(wartosc);
            }
        }
        else {
            aktBudzet.odejmij(wartosc);
            portfel.dodaj(wartosc);
        }
    }


    public Kolektura stworzKolekture() {
        Kolektura nowa = new Kolektura(this);
        kolektury.add(nowa);
        return nowa;
    }

    public void przeprowadzLosowanie() {
        // Jeśli nie został wpłacony żaden kupon to budżet na losowanie może się nie zainicjalizować
        while (budzetNaLosowanie.size() < numerOstatniegoLosowania) budzetNaLosowanie.add(new Waluta(0));
        TreeSet<Integer> wygrLiczby = losujSzostke();
        int[] wygrane = new int[4];
        for (Kolektura kolektura : kolektury) {
            int[] wygraneWKolekturze = kolektura.sprawdzIleTrafienWLosowaniu(numerOstatniegoLosowania + 1, wygrLiczby);
        }
        // Tworzymy losowanie, które już zna ile osób wygrało, więc rozdziela pule, niżej dodajemy wszstkie rzeczy do naszych statystyk.
        Losowanie losowanie = new Losowanie(this, wygrLiczby, sprawdzWygrane(wygrLiczby), budzetNaLosowanie.get(numerOstatniegoLosowania), kumulacja);
        for (int i = 0; i < 4; i++) {
            TrafionePieniadzeWygranePoszczegolnychStopni[i].dodaj(losowanie.wielkosciWygranych()[i].iloczyn(losowanie.iloscWygranych()[i]));
            iloscTrafionychWygranychPoszczegolnychStopni[i] += losowanie.iloscWygranych()[i];
        }
        losowania.add(losowanie);
        numerOstatniegoLosowania++;
    }

    // Podlicza wygrane ze wszystkich kolektur
    private int[] sprawdzWygrane(TreeSet<Integer> wygrLiczby) {
        int[] wygrane = new int[4];
        for (Kolektura kolektura : kolektury) {
            int[] wygraneWKolekturze = kolektura.sprawdzIleTrafienWLosowaniu(numerOstatniegoLosowania + 1, wygrLiczby);
            for (int i = 0; i < 4; i++) {
                wygrane[i] += wygraneWKolekturze[i];
            }
        }
        return wygrane;
    }

    // Pobiera zyski kolektur i odprowadza od nich podatek
    public void pobierzKase(Waluta ilosc, Waluta portfel, int numerLosowania) {
        while (budzetNaLosowanie.size() < numerLosowania) budzetNaLosowanie.add(new Waluta(0));
        Waluta podatek = ilosc.iloczyn(mnoznikPodatku);
        panstwo.pobierzPodatek(podatek, portfel);
        ilosc.odejmij(podatek);

        Waluta zysk = new Waluta(ilosc.iloczyn(mnoznikZysku));
        portfel.odejmij(zysk);
        budzet.dodaj(zysk);
        ilosc.odejmij(zysk);

        budzetNaLosowanie.get(numerLosowania - 1).dodaj(ilosc);
        portfel.odejmij(ilosc);
    }


    public TreeSet<Integer> dajWygrywajaceZLosowania(int i) {
        return losowania.get(i-1).wygrywajaceLiczby();
    }

    @Override
    public ArrayList<Kolektura> pokazKolektury() {
        return kolektury;
    }

    @Override
    public Kolektura wylosujKolekture() throws BrakKolekturWyjatek {
        if (kolektury.isEmpty()) throw new BrakKolekturWyjatek();
        return kolektury.get(rand.nextInt(0, kolektury.size()));
    }

    @Override
    public int losujLiczbe(int od, int koniec) {
        return rand.nextInt(od,koniec);
    }

    @Override
    public int losujIdentyfikator() {
        return rand.nextInt(1_000_000_000);
    }

    @Override
    public ArrayList<TreeSet<Integer>> losujNLiczb(int n) {
        ArrayList<TreeSet<Integer>> wynik = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            TreeSet<Integer> liczby = losujSzostke();
            wynik.add(liczby);
        }
        while (wynik.size() < 8) wynik.add(new TreeSet<>());
        return wynik;
    }

    @Override
    public TreeSet<Integer> losujSzostke() {
        TreeSet<Integer> liczby = new TreeSet<>();
        for (int j = 0; j < 6; j++) {
            int x = rand.nextInt(1, 50);
            while (liczby.contains(x)) x = rand.nextInt(1, 50);
            liczby.add(x);
        }
        return liczby;
    }

    @Override
    public int iloscKolektur() {
        return kolektury.size();
    }

    @Override
    public int iloscKuponow() {
        return iloscKuponow;
    }

    @Override
    public void ZwiekszIloscKuponow() {
        iloscKuponow++;
    }

    @Override
    public double mnoznikDrugiegoStopnia() {
        return mnoznikDrugiegoStopnia;
    }

    @Override
    public double mnoznikPierwszegoStopnia() {
        return mnoznikPierwszegoStopnia;
    }

    @Override
    public long minimalnaNagrodaTrzeciegoStopnia() {
        return minimalnaNagrodaTrzeciegoStopnia;
    }

    @Override
    public long nagrodaCzwartegoStopnia() {
        return nagordaCzwartegoStopnia;
    }

    @Override
    public long minimalnaNagrodaGlowna() {
        return minimalnaNagrodaGlowna;
    }

    @Override
    public int numerOstatniegoLosowania() {
        return numerOstatniegoLosowania;
    }
    @Override
    public long cenaZakladu() {
        return cenaZakladu;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Losowanie losowanie = losowania.get(numerOstatniegoLosowania-1);
        sb.append(losowanie);
        sb.append("\nPULE NAGRÓD:\n");
        for (int i = 0; i < 4; i++) {
            sb.append("Stopień ");
            sb.append(i + 1);
            sb.append(". ");
            sb.append(losowanie.wielkosciWygranych()[i]);
            sb.append("\n");
        }
        sb.append("\nILOSC TRAFIEN:\n");
        for (int i = 0; i < 4; i++) {
            sb.append("Stopień ");
            sb.append(i + 1);
            sb.append(". ");
            sb.append(losowanie.iloscWygranych()[i]);
            sb.append("\n");
        }
        sb.append("\n");
        sb.append(statystykiCentrali());
        sb.append("\n\n\n");
        return sb.toString();
    }

    public String statystykiCentrali() {
        StringBuilder sb = new StringBuilder();
        sb.append("----STATYSTYKI-CENTRALI----\n");
        sb.append("LICZBA I KWOTA TRAFIONYCH NAGROD N-TEGO STOPNIA: \n");
        for (int i = 0; i < 4; i++) {
            sb.append("Stopień ");
            sb.append(i+1);
            sb.append(": ");
            sb.append(iloscTrafionychWygranychPoszczegolnychStopni[i]);
            sb.append(" ,");
            sb.append(TrafionePieniadzeWygranePoszczegolnychStopni[i]);
            sb.append("\n");
        }
        sb.append(panstwo);
        return sb.toString();

    }

}
