package test;
import Totolotek.*;

import Totolotek.Gracze.Losowy;
import Totolotek.Gracze.Minimalista;
import Totolotek.Gracze.StaloBlankietowy;
import Totolotek.Gracze.StaloLiczbowy;
import Totolotek.Wyjatki.BrakKolekturWyjatek;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class Testy {
    static Random rand = new Random();
    private Panstwo panstwo;
    private Centrala centrala;

    @BeforeEach
    void init() {
        panstwo = new Panstwo();
        centrala = new Centrala(panstwo, new Waluta(20_000_000__00L));
    }

    Blankiet stworzBlankietZPolecenia() {
        int[][] dane = new int[][] {
                {},
                {11,12,23,33,43,19},
                {11,12,23,33,43,19},
                {1,11,21,31,41,3,13,23,33,43},
                {1,2,3,4,5,6,7,8,9,10},
                {21,22,23,24,25,26,27,28,29,30},
                {4,15,24,33,35,44},
                {1,2,3}
        };
        ArrayList<TreeSet<Integer>> daneLista = new ArrayList<>();
        for (int[] arr : dane) {
            TreeSet<Integer> list = new TreeSet<>();
            for (int num : arr) {
                list.add(num);
            }
            daneLista.add(list);
        }
        return new Blankiet(daneLista ,new boolean[]{false,false,true,false,false,true,false,false},
                new int[]{4});
    }
    @Test
    void losowaniaWCentrali() {
        for (int i = 0; i < 1000; i++) {
            assertEquals(6,centrala.losujSzostke().size());
        }
        for (int i = 1; i < 8; i++) {
            assertEquals(8, centrala.losujNLiczb(i).size());
        }

    }
    // Tworzy po 10 graczy każdego rodzaju i sprawdza ich podstawowe cechy
    @Test
    void Gracze() throws BrakKolekturWyjatek {
        Panstwo panstwo = new Panstwo();
        Centrala centrala = new Centrala(panstwo, new Waluta());
        for (int i = 0; i < 10; i++) {
            centrala.stworzKolekture();
        }

        ArrayList<Losowy> losowi = new ArrayList<>(10);
        ArrayList<Minimalista> minimalisci = new ArrayList<>(10);
        ArrayList<StaloBlankietowy> staloBlankietowi = new ArrayList<>(10);
        ArrayList<StaloLiczbowy> staloLiczbowi = new ArrayList<>(10);
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {

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

        // LOSOWI, trudno tu coś testować, można sprawdzić toStringiem, czy się sensownie inicjalizują
        for (Losowy losowy : losowi) {
            losowy.kupJesliChcesz();
//            System.out.println(losowy);
        }

        // Minimalisci, sprawdzamy, czy dorbze kolektury się dobieraja i czy kupują po jednym bilecie
        for (Minimalista minimalista : minimalisci) {
            minimalista.kupJesliChcesz();
            assertEquals(1, minimalista.iloscKuponow());
//            System.out.println(minimalista);
        }
        for (int i = 0; i < 10; i++) {
            assertEquals(i+1, minimalisci.get(i).ulubionaKolektura().numerKolektury());
        }
        // StałoLiczbowy i StałoBlankietowy różnią się kupowanie kuponów co załatwiliśmy w innym teście, więc też tylko
        // sprawdzam toStringiem czy mają sens
        for (StaloLiczbowy staloLiczbowy : staloLiczbowi) {
            staloLiczbowy.kupJesliChcesz();
//            System.out.println(staloLiczbowy);
        }

        for (StaloBlankietowy staloBlankietowy : staloBlankietowi) {
            staloBlankietowy.kupJesliChcesz();
//            System.out.println(staloBlankietowy);
        }
    }

    @Test
    void Panstwo() {
        // Pobieranie podatkow
        Waluta portfel = new Waluta(100_000__00L);
        Waluta podatek = new Waluta(100__00L);
        panstwo.pobierzPodatek(podatek, portfel);
        assertEquals(new Waluta(99_900_00L).toString(),portfel.toString());
//        System.out.println(panstwo);
        // Wydawanie subwencji
        Waluta subwencja = new Waluta(1L);
        panstwo.wydajSubwencje(subwencja,portfel);
        assertEquals(new Waluta(99_900_01L), portfel);
//        System.out.println(panstwo);

    }

    @Test
    void Blankiet() {
        Blankiet blankietOczekiwany = stworzBlankietZPolecenia();
//        System.out.println(blankietOczekiwany);
        Blankiet blankietUzytkownika = new Blankiet();
        blankietUzytkownika.zakreslLiczbeLosowan(4);

        blankietUzytkownika.zakreslLiczbe(2,11);
        blankietUzytkownika.zakreslLiczbe(2,12);
        blankietUzytkownika.zakreslLiczbe(2,19);
        blankietUzytkownika.zakreslLiczbe(2,23);
        blankietUzytkownika.zakreslLiczbe(2,33);
        blankietUzytkownika.zakreslLiczbe(2,43);

        blankietUzytkownika.zakreslLiczbe(3,11);
        blankietUzytkownika.zakreslLiczbe(3,12);
        blankietUzytkownika.zakreslLiczbe(3,19);
        blankietUzytkownika.zakreslLiczbe(3,23);
        blankietUzytkownika.zakreslLiczbe(3,33);
        blankietUzytkownika.zakreslLiczbe(3,43);
        blankietUzytkownika.zakreslAnulujZakladu(3);

        for (int liczba : new int[] {1,11,21,31,41,3,13,23,33,43}) {
            blankietUzytkownika.zakreslLiczbe(4,liczba);
        }
        for (int i = 1; i < 11; i++) {
            blankietUzytkownika.zakreslLiczbe(5,i);
        }
        for (int i = 21; i < 31; i++) {
            blankietUzytkownika.zakreslLiczbe(6,i);
        }
        blankietUzytkownika.zakreslAnulujZakladu(6);
        for (int liczba : new int[] {4,15,24,33,35,44}) {
            blankietUzytkownika.zakreslLiczbe(7,liczba);
        }

        blankietUzytkownika.zakreslLiczbe(8,1);
        blankietUzytkownika.zakreslLiczbe(8,2);
        blankietUzytkownika.zakreslLiczbe(8,3);
        System.out.println(blankietUzytkownika);
        assertEquals(blankietOczekiwany.toString(), blankietUzytkownika.toString());
    }

    @Test
    void TestKuponu() {
        // Zakłady losowe, pobieranie pieniędzy
        Kolektura kolektura = centrala.stworzKolekture();
        Waluta portfel = new Waluta(1_000_000__00L);
        ArrayList<Kupon> kupony = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            kupony.add(kolektura.kupKupon(10, 8, portfel));
            // Czy tworzy 8 zakładów
            assertEquals(8, kupony.get(i).liczby().size());
            // Czy przypisuje 8 losowań
            assertEquals(10, kupony.get(i).losowania().size());
            // Czy zakłady mają 6 liczb
            assertEquals(6, kupony.get(i).liczby().getFirst().size());
        }
        // Czy dobrze ściąga kasę
        Waluta oczekiwanyStanPoKupnie = new Waluta(1_000_000__00L - (long)(100*3*8*10*100));
        assertEquals(oczekiwanyStanPoKupnie, portfel);
//        System.out.println(panstwo);

        // Kupowanie Z Blankiety
        Blankiet blankiet = stworzBlankietZPolecenia();
        kupony.add(kolektura.kupKupon(blankiet, portfel));
        Kupon kuponBlankietowy = kupony.getLast();
        // Czy tworzy 2 zakłady
        assertEquals(2, kuponBlankietowy.liczby().size());
        // Czy przypisuje 4 losowania
        assertEquals(4, kuponBlankietowy.losowania().size());
        // Czy zakłady mają 6 liczb
        assertEquals(6, kuponBlankietowy.liczby().getFirst().size());
    }


    public static String losujPesel() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}
