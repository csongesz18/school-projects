#include <iostream>
#include <ctime>
#include <cstdlib>
#include <limits>
#include <fstream>
#include <cstdio>
#include <exception>

#include "jegy.h"
#include "jegy_tipusok.h"
#include "menu.h"
#include "vonat.h"
#include "kesesKezelo.h"
#include "FileException.h"
#include "String.h"
#include "gtest_lite.h"
#include "memtrace.h"

using namespace std;

/**
* itt a menu.cpp-ben megirt fuggvenyeket hivjuk meg
* osszeallitott program
*/

///a fajlt valtozoba tesszuk, ez megkonnyiti a munkat
const String filename = "vonatok.txt";

int main() {
    ///peldanyositas
    Menu menu;

    ///gtest_lite tesztek
    TEST(String, konstruktorok_es_operatorok) {
        String s1("teszt");
        String s2 = s1;
        EXPECT_EQ(s1, s2);
        s2 = "masik";
        EXPECT_NE(s1, s2);
    } END

    TEST(Vonat, getter_setter) {
        Vonat v(123, "10:00", "08:00", "Budapest", "Gyor");
        EXPECT_EQ(v.getvonatszam(), 123);
        EXPECT_EQ(v.getindulasiHely(), "Budapest");
    } END

    TEST(Vonat, Setters) {
        Vonat v(300, "10:00", "08:00", "Budapest", "Pecs");
        v.setvonatszam(400);
        v.setindulasiIdo("09:30");
        v.seterkezesiIdo("11:30");
        v.setindulasiHely("Debrecen");
        v.seterkezesiHely("Szeged");

        EXPECT_EQ(400, v.getvonatszam());
        EXPECT_EQ(String("09:30"), v.getindulasiIdo());
        EXPECT_EQ(String("11:30"), v.geterkezesiIdo());
        EXPECT_EQ(String("Debrecen"), v.getindulasiHely());
        EXPECT_EQ(String("Szeged"), v.geterkezesiHely());
    } END

    TEST(JegyTipusok, ar_szamitas) {
        Vonat v(1, "10:00", "08:00", "Bp", "Pecs");
        Felnott felnott;
        felnott.setPrice(1000);
        felnott.setVonat(&v);
        EXPECT_EQ(felnott.getPrice(), 1000);

        Diak diak;
        diak.setPrice(1000);
        diak.setVonat(&v);
        EXPECT_EQ(diak.getPrice(), 500);

        Nyugdijas ny;
        ny.setPrice(1000);
        ny.setVonat(&v);
        EXPECT_EQ(ny.getPrice(), 300);

        Gyerek gy;
        gy.setPrice(1000);
        gy.setVonat(&v);
        EXPECT_EQ(gy.getPrice(), 250);
    } END

    TEST(Jegy, DiakKedvezmeny) {
        Diak d;
        d.setPrice(1000);
        EXPECT_EQ(500, d.getPrice()); /// 50% kedvezmeny
    } END

    TEST(KesesKezelo, KesesBeallitas) {
        Vonat v(123, "10:00", "08:00", "Gyõr", "Veszprem");
        KesesKezelo kk;
        kk.keses(&v, "10:45");
        EXPECT_EQ(String("10:45"), v.geterkezesiIdo());
    } END

    cout << endl << "Udvozollek a Vonatjegy kezelo rendszerben!" << endl << endl;

    ///beolvassuk a fajl tartalmat, figyeljuk a kivetelek dobasat
    try {
        menu.beolvasVonatok(filename);
    } catch (EmptyFileException e) {
        cout << e.what();
    } catch (FileNotFoundException e) {
        cout << e.what();
    }

    ///menu hivasa ahol az egyes esetek a menu.cpp-ben meg vannak irva
    while (true) {
        menu.menuMegjelenitese();
        int valasztottOpcio = menu.opcioValasztas();
        switch (valasztottOpcio) {
            case 1:
                menu.egyesOpcio();
                break;
            case 2:
                menu.kettesOpcio();
                break;
            case 3:
                menu.harmasOpcio();
                break;
            case 4:
                menu.jegyekListazasa();
                break;
            case 5:
                menu.kilepesOpcio(filename);
                cout << endl << "Viszlat!" << endl;
                return 0;
            default:
                cout << endl << "Ervenytelen opcio! Kerem valasszon ujra." << endl << endl;
                break;
        }
        cout << endl;
    }
}
