#ifndef HEADER_H_INCLUDED
#define HEADER_H_INCLUDED

///egy strukúrát hozok létre, amely a menü és a rendelés egyes adatait olvassa be és tárolja
typedef struct menu
{
    int sorszam;
    char etel[50];
    int ar;
    int rendeles[200];
    int voltrendeles;
}menu;

///egy láncolt listában tárolom az asztalok adatait
typedef struct asztal
{
    int sorszam;          ///< minden asztal egy számot kap és ez ezt tárolja
    int ferohely;         ///< ez az asztal férõhelyét tárolja
    int foglalt;          ///< ez tárolja, hogy foglalt vagy sem
    struct asztal *kov;   ///< következő asztalra mutató pointer
}asztal;

int beolvas_menu_meret();
void beolvas_menu(menu *menutomb,int menu_meret);
void menumegj();
void matrmegj();
void beolvas_asztalok(asztal **asztalokfej, int asztalszam);
void asztalfoglalas(asztal *asztalokfej,int asztalszam);
void asztalkilistazas(asztal *asztalokfej,int asztalszam);
void asztal_felszabaditas(asztal *asztalokfej);
void rendelesfelvetel(menu *rendelestomb, int asztalszam, asztal *asztalokfej);
void szamlanyomtatas(menu *rendelestomb, int asztalszam, asztal *asztalokfej, menu *menutomb);
void asztal_free(asztal *asztalokfej);

#endif // HEADER_H_INCLUDED
