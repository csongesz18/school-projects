#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <conio.h>
#include <stdbool.h>
#include "debugmalloc.h"
#include "header.h"
#ifdef _MSC_VER
#define getch() _getch()
#endif

int main()
{
    asztal *asztalokfej = NULL;
    int menu_meret = beolvas_menu_meret();
    int asztalszam = 7;
    menu *rendelestomb = (menu*)malloc(asztalszam*sizeof(menu));
    for(int i=0;i<asztalszam;i++)
        rendelestomb[i].voltrendeles=0;
    menu *menutomb = (menu*) malloc(menu_meret * sizeof(menu));
    beolvas_asztalok(&asztalokfej, asztalszam);
    beolvas_menu(menutomb,menu_meret);
    printf("%s\n","Udvozlunk az etterem programban!");
    /// ez a vezérlõ gombom, ez kéri be az opció számát és futtatja le azt a részt amelyiket szükséges
    int gomb;
    /// a program fõciklusa ez a do...while ciklus, amely addig fut, ameddig ki nem választjuk a kilépés opció számát
    do
    {
        /// ezt a változót a rendelés felvételnél használom, ebbe kérem be a rendelések számát
        printf("\nValasztasi lehetosegek:\n");
        /// ez a nyolc választási lehetõséges menürendszerem
        printf("\n 1. Asztalok kilistazasa\n");
        printf(" 2. Asztalok matrixos megjelenitese\n");
        printf(" 3. Asztal foglalas\n");
        printf(" 4. Menu megjelenitese\n");
        printf(" 5. Rendeles felvetel\n");
        printf(" 6. Szamla nyomtatas\n","");
        printf(" 7. Asztal felszabaditas\n");
        printf(" 8. Kilepes\n");
        printf("\nValassz egy opciot: ");
        /// a gomb változónak itt történik meg az értékadása, innentõl kezdve azt figyelem ez a gomb mennyivel egyenlõ, tehát szükségszerûen 7 if íródott
        scanf("%d",&gomb);
        printf("\n");
        ///minden if-ben egy korábban megírt függvény hívódik meg, amik kiegészülnek
        if(gomb < 1 || gomb > 8)
            printf("Rossz bemenetet adtal meg!\n");

        if(gomb == 1)
        {
            asztalkilistazas(asztalokfej,asztalszam);
        }
        /**
        * ebben az if-ben meghívódik a matrmegj() függvény
        * egy fájlból olvasódik be az étterem asztalainak száma helyezkedése
        **/
        if(gomb == 2)
        {
            matrmegj();
        }
        if(gomb == 3)
        {
            asztalfoglalas(asztalokfej,asztalszam);
        }
        if(gomb == 4)
        {
            menumegj();
        }
        /**
        * most használom a do...while elején deklarált tömböt
        * egy while() ciklust használ, aminek a feltétele végig igaz, ezért szükséges egy leállító is
        * break-et használ, ami akkor áll le ha 0-t kap értékül
        **/
        if(gomb == 5)
        {
            menumegj();
            rendelesfelvetel(rendelestomb,asztalszam,asztalokfej);
        }
        if(gomb == 6)
        {
            szamlanyomtatas(rendelestomb,asztalszam,asztalokfej,menutomb);
        }
        /// ez az if két függvényt hív meg
        if(gomb == 7)
        {
            asztalkilistazas(asztalokfej,asztalszam);
            asztal_felszabaditas(asztalokfej);
        }
        printf("\nNyomj ENTER-t!");
        /// a két getchar() vár amíg megnyomunk egy gombot
        getchar();
        getchar();
        system("cls");
    }while(gomb != 8);
    /// a legvégén felszabadítjuk a tömböket
    free(menutomb);
    free(rendelestomb);
    asztal_free(asztalokfej);

    return 0;
}
