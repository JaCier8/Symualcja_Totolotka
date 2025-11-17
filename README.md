Symulacja Totolotka
Projekt w języku Java symulujący działanie systemu loterii "Totolotek". Został zrealizowany w ramach kursu Programowanie Obiektowe na MIMUW.

Projekt implementuje logikę tworzenia centrali, kolektur, różnych typów graczy oraz przeprowadzania cyklicznych losowań, obliczania wygranych i zarządzania finansami.

📍 Główne Założenia
System został oparty na szczegółowej specyfikacji, a jego kluczowe elementy to:

Centrala: Główny organ zarządzający. Przeprowadza losowania, oblicza pule nagród (dla 4 stopni), zarządza budżetem, obsługuje kumulacje (gdy nie padnie "szóstka") i wypłaca nagrody.

Kolektury: Punkty sprzedaży (10 w symulacji). Gracze mogą w nich kupować kupony i odbierać wygrane. Każda kolektura ma unikalny numer i bazę sprzedanych kuponów.

Kupony: Dowód udziału w grze. Mogą zawierać od 1 do 8 zakładów i obejmować do 10 kolejnych losowań. Generowane są na podstawie Blankietu lub losowo (chybił-trafił).

Finanse: System precyzyjnie zarządza finansami (za pomocą klasy Waluta opartej na typie long do reprezentowania groszy).

Cena zakładu: 3.00 zł.

Podatek od zakładu: 0.60 zł (20%) trafia do państwa.

Zysk centrali: 51% z pozostałej kwoty (reszta idzie na nagrody).

Opodatkowanie wygranych: 10% od nagród powyżej 2280 zł.

Państwo: Oddzielna klasa Panstwo monitoruje sumę pobranych podatków oraz kwotę udzielonych subwencji (gdy centrali zabraknie środków na wypłatę gwarantowanej puli nagród).

🧩 Struktura Projektu
Projekt jest zorganizowany obiektowo, a kluczowe klasy znajdują się w pakiecie Totolotek (oraz podpakietach Gracze i Wyjatki).

Main.java: Zawiera główną metodę main, która uruchamia całą symulację.

Totolotek.Centrala: Sercem systemu, zarządza losowaniami i finansami. Implementuje interfejs WidokNaCentrale.

Totolotek.Kolektura: Reprezentuje punkt sprzedaży. Obsługuje kupno kuponów i wypłatę nagród.

Totolotek.Kupon: Reprezentuje kupon na okaziciela, z unikalnym identyfikatorem.

Totolotek.Blankiet: Formularz do typowania zakładów przez gracza.

Totolotek.Losowanie: Reprezentuje pojedyncze losowanie z 6 wylosowanymi liczbami.

Totolotek.Waluta: Klasa pomocnicza do bezpiecznych operacji na finansach (przechowuje kwoty jako grosze w typie long).

Totolotek.Panstwo: Klasa monitorująca podatki i subwencje.

Rodzaje Graczy
System modeluje różne typy graczy dziedziczące po abstrakcyjnej klasie Totolotek.Gracze.Gracz:

Minimalista: Zawsze kupuje jeden zakład "chybił-trafił" na jedno losowanie w swojej ulubionej kolekturze.

Losowy: Kupuje losową liczbę kuponów (1-100) w losowej kolekturze, każdy na losową liczbę zakładów i losowań.

StaloLiczbowy: Zawsze gra na te same 6 ulubionych liczb. Kupuje kupon na 10 losowań i robi to ponownie, dopiero gdy poprzedni kupon się skończy.

StaloBlankietowy: Gra według z góry ustalonego blankietu, kupując nowy kupon co określoną liczbę losowań.

🚀 Uruchomienie Symulacji
Główna metoda programu znajduje się w pliku src/Main.java. Jej uruchomienie przeprowadza pełną symulację zgodnie z wymogami zadania:

Tworzona jest 1 Centrala i 10 Kolektur.

Tworzonych jest po 200 graczy każdego z czterech typów (Minimalista, Losowy, StaloLiczbowy, StaloBlankietowy), którzy są przydzielani do kolektur.

W pętli przeprowadzanych jest 20 losowań.

Przed każdym losowaniem, gracze decydują o zakupie kuponów zgodnie ze swoją strategią.

Po każdym losowaniu, gracze sprawdzają swoje kupony i odbierają nagrody za te, które zakończyły już wszystkie swoje losowania.

Na końcu symulacji, na standardowe wyjście wypisywane są pełne statystyki Centrali (wyniki wszystkich losowań, pule nagród) oraz statystyki Panstwa (łączne podatki i subwencje).

🧪 Testy
Projekt zawiera testy jednostkowe JUnit 5, znajdujące się w pliku src/test/Testy.java. Testy weryfikują poprawność działania kluczowych komponentów systemu, m.in.:

Poprawne tworzenie i parsowanie Blankietu.

Logikę finansową w klasie Panstwo.

Tworzenie graczy i ich podstawowe zachowania.

Poprawne generowanie Kuponu i obliczanie jego ceny.
