import  Totolotek.*;
import Totolotek.Gracze.Losowy;
import Totolotek.Gracze.Minimalista;
import Totolotek.Gracze.StaloBlankietowy;
import Totolotek.Gracze.StaloLiczbowy;
import Totolotek.Wyjatki.BrakKolekturWyjatek;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;


public class Main {

    public static Random rand = new Random();

    public static void main(String[] args) throws BrakKolekturWyjatek {
        // Tworzenie centrali i kolektur
        Panstwo panstwo = new Panstwo();
        Centrala centrala = new Centrala(panstwo, new Waluta());
        for (int i = 0; i < 10; i++) {
            centrala.stworzKolekture();
        }
        // Tworzenie graczy
        ArrayList<Losowy> losowi = new ArrayList<>(200);
        ArrayList<Minimalista> minimalisci = new ArrayList<>(200);
        ArrayList<StaloBlankietowy> staloBlankietowi = new ArrayList<>(200);
        ArrayList<StaloLiczbowy> staloLiczbowi = new ArrayList<>(200);
        Random rand = new Random();
        for (int i = 0; i < 200; i++) {

            losowi.add(new Losowy("Losowy" + i, "Kowalski" + i, losujPesel(), centrala));


            minimalisci.add(new Minimalista("Minimalista" + i, "Kowalski" + i,
                    losujPesel(), new Waluta(2_000__00), centrala, centrala.pokazKolektury().get(i % 10)));


            int iloscZakladow = centrala.losujLiczbe(1, 9);
            TreeSet<Integer> drzewo = new TreeSet<>();
            drzewo.add(centrala.losujLiczbe(1, 11));
            Blankiet blankiet = new Blankiet(
                    centrala.losujNLiczb(iloscZakladow),
                    new boolean[8],
                    drzewo);
            staloBlankietowi.add(new StaloBlankietowy("StaloB" + i, "Kowalski" + i, losujPesel(),
                    new Waluta(2_000__00), centrala, blankiet, centrala.pokazKolektury().get(i % 10),
                    centrala.losujLiczbe(1, 5)));


            staloLiczbowi.add(new StaloLiczbowy("StaloL" + i, "Kowalski" + i, losujPesel(),
                    new Waluta(2_000__00), centrala, centrala.losujSzostke(),
                    centrala.pokazKolektury().get(i % 10)));
        }
        for (int j = 0; j < 20; j++) {
            for (int i = 0; i < 200; i++) {
                losowi.get(i).kupJesliChcesz();
                minimalisci.get(i).kupJesliChcesz();
                staloBlankietowi.get(i).kupJesliChcesz();
                staloLiczbowi.get(i).kupJesliChcesz();
            }
            centrala.przeprowadzLosowanie();
            for (int i = 0; i < 200; i++) {
                losowi.get(i).odbierzNagordySkonczonychKuponow();
                minimalisci.get(i).odbierzNagordySkonczonychKuponow();
                staloBlankietowi.get(i).odbierzNagordySkonczonychKuponow();
                staloLiczbowi.get(i).odbierzNagordySkonczonychKuponow();
            }
//            System.out.println(centrala); // Do podejrzenia sytuacji między poszególnymi losowaniami
        }
        System.out.println(centrala.statystykiCentrali());
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static String losujPesel() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}