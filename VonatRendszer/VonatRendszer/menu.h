#ifndef MENU_H
#define MENU_H

#include <iostream>
#include <ctime>
#include <cstdlib>
#include <limits>
#include <fstream>
#include <cstdio>

#include "vonat.h"
#include "jegy.h"
#include "String.h"
#include "memtrace.h"

/**
* statikus adattagok es fuggvenyek
* peldanyositas nelkul hivhatoak
* a main.cpp-ben hivodnak meg, mindegyik opcionak mas funkcionalitasa van
*/
class Menu {
private:
    Jegy* jegy;
    Jegy** jegyek;
    int jegyCount;
    int jegyKapacitas;
    Vonat** vonatok;
    int vonatCount;

    void waitForKeyPress();
    void clearScreen();
    /**
    * kimentjuk egy fajlba a valtoztatott adatokat, parameterkent egy fajlnevet kapunk
    */
    void mentesVonatok(const String& filename);
    /**
    * felszabaditja a vonatok dinamikusan tarolt tombjet
    */
    void deleteVonatok();
    /**
    * ez a fuggveny segit, hogy az elso opcio kettes opciojanak ne legyen nagyon bonyolult a szerkezete
    */
    void switchcase_vonatadatok(int adat, int vonatszam);
    /**
    * az elso opcio harom lehetseges valasztasi lehetosegei, amelyek kulon fuggvenyekre vannak szedve
    */
    void VonatHozzaadas();
    void VonatAdatModositas();
    void VonatTorles(bool &talalt);

public:
    /**
    * parameter nelkuli konstruktor
    */
    Menu();
    /**
    * destruktor
    */
    ~Menu();
    /**
    * kiirja egy a benne osszeirt valasztasi lehetosegeket
    */
    void menuMegjelenitese();
    /**
    * beker egy int tipusu szamot ami le van vedve
    */
    int opcioValasztas();
    /**
    * az egyes opcioban valaszthatunk harom ujabb lehetoseg kozul, amelyeket egy switch ertekel ki
    */
    void egyesOpcio();
    /**
    * utas tipust es arat kerunk be, emelett random sorsolunk helyszamot es kocsiszamot, ezutan jegyet iratunk ki
    */
    void kettesOpcio();
    /**
    * kesest allitunk be egy adott vonatnak, amelyet vonatszam alapjan hatarozunk meg
    */
    void harmasOpcio();
    /**
    * kilepunk a programbol es felszabaditjuk a dinamikusa foglalt teruleteket
    */
    void kilepesOpcio(const String& filename);
    /**
    * beolvassuk a vonatokat egy file-bol, parameterkent a fajl nevet kapjuk
    */
    void beolvasVonatok(const String& filename);
    /**
    * kilistazzuk a letrehozott jegyeket a program kezdetetol
    */
    void jegyekListazasa();
    /**
    * felszabaditjuk a jegyeket
    */
    void jegyekFelszabaditasa();
};

#endif // MENU_H
