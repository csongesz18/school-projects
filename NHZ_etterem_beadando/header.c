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

int beolvas_menu_meret()
{
    FILE *f;
    f=fopen("strukturamenu.txt","r");
    if(f==NULL)
        printf("\nHiba a fajl megnyitasakor!\n");
    char s[50];
    int meret = 0;
    while(fgets(s,50,f)!=NULL)
    {
        meret++;
    }
    return meret;
}

void beolvas_menu(menu *menutomb,int menu_meret)
{
    FILE *f;
    f=fopen("strukturamenu.txt","r");
    if(f==NULL)
        printf("\nHiba a fajl megnyitasakor!\n");
    for(int i=0; i<menu_meret; i++)
    {
        menu sor;
        fscanf(f,"%d %s %d",&sor.sorszam,sor.etel,&sor.ar);
        menutomb[i]=sor;
    }
    fclose(f);
}

void matrmegj()
{
    FILE *f;
    char s;
    f=fopen("asztalok.txt","r");
    if(f==NULL)
        printf("\nHiba a fajl megnyitasakor!\n");
    else
    {
    while((s=fgetc(f))!=EOF)
    {
        if(s=='0')
            printf(" ");
        else
            printf("%c",s);
    }}
    fclose(f);
    printf("\n");
}

void menumegj()
{
    FILE *f;
    char s;
    f=fopen("menu.txt","r");
    if(f==NULL)
        printf("\nHiba a fajl megnyitasakor!\n");
    while((s=fgetc(f))!=EOF)
    {
        printf("%c",s);
    }
    fclose(f);
    printf("\n");
}

void beolvas_asztalok(asztal **asztalokfej, int asztalszam)
{
    FILE *f;
    f = fopen("foglalt_asztalok.txt", "r");
    if (f == NULL)
        printf("\nHiba a fajl megnyitasakor!\n");

    for (int i = 0; i < asztalszam; i++)
    {
        asztal *sor = (asztal *)malloc(sizeof(asztal));
        fscanf(f, "%d %d %d", &sor->sorszam, &sor->ferohely, &sor->foglalt);
        sor->kov = *asztalokfej;
        *asztalokfej = sor;
    }
    fclose(f);
}

void asztalfoglalas(asztal *asztalokfej,int asztalszam)
{
    FILE *f;
    f = fopen("foglalt_asztalok.txt", "r");
    if (f == NULL)
        printf("\nHiba a fajl megnyitasakor!\n");
    fclose(f);

    int foglalas;
    printf("Melyik asztalt szeretned lefoglalni: ");
    scanf("%d", &foglalas);

    asztal *current = asztalokfej;

    while (current != NULL && current->sorszam != foglalas)
    {
        current = current->kov;
    }

    if (current == NULL)
    {
        printf("\nHibas asztalszam!\n");
        return;
    }

    if (current->foglalt == 1)
        printf("\nEz az asztal mar foglalt!\n");
    else
    {
        current->foglalt = 1;
        printf("\nA %d asztal le lett foglalva.\n", foglalas);
    }

    f = fopen("foglalt_asztalok.txt", "w");
    current = asztalokfej;

    while (current != NULL)
    {
        fprintf(f, "%d %d %d\n", current->sorszam, current->ferohely, current->foglalt);
        current = current->kov;
    }

    fclose(f);
}

void asztalkilistazas(asztal *asztalokfej,int asztalszam)
{
    asztal *current = asztalokfej;
    for (int i = asztalszam - 1; i >= 0; i--)
    {
        current = asztalokfej;
        for (int j = 0; j < i; j++)
        {
            current = current->kov;
        }
        if (current->foglalt == 0)
            printf("%d. asztal: %d ferohely, nem foglalt\n", current->sorszam, current->ferohely);
        if (current->foglalt == 1)
            printf("%d. asztal: %d ferohely, foglalt\n", current->sorszam, current->ferohely);
    }
}

void asztal_felszabaditas(asztal *asztalokfej)
{
    FILE *f;
    f = fopen("foglalt_asztalok.txt", "r");
    if (f == NULL)
        printf("Hiba a fajl megnyitasakor!\n");
    fclose(f);
    int foglalas;
    printf("\nMelyik asztalt szeretned felszabaditani (ha mindegyiket, nyomj 0-t): ");
    scanf("%d", &foglalas);
    if (foglalas < 0 || foglalas > 7)
        printf("\nHibas bemenet!\n");
    else
    {
        if (foglalas == 0)
        {
            asztal *current = asztalokfej;
            while (current != NULL)
            {
                if (current->foglalt == 1)
                    current->foglalt = 0;
                current = current->kov;
            }
            printf("\nMindegyik asztal fel lett szabaditva.\n");
        }
        else
        {
            asztal *current = asztalokfej;
            while (current != NULL && current->sorszam != foglalas)
            {
                current = current->kov;
            }

            if (current == NULL)
            {
                printf("\nHibas asztalszam!\n");
                return;
            }

            if (current->foglalt == 1)
                current->foglalt = 0;
            printf("\nA %d asztal fel lett szabaditva.\n", foglalas);
        }

        f = fopen("foglalt_asztalok.txt", "w");
        asztal *current = asztalokfej;

        while (current != NULL)
        {
            fprintf(f, "%d %d %d\n", current->sorszam, current->ferohely, current->foglalt);
            current = current->kov;
        }
        fclose(f);
    }
}

void rendelesfelvetel(menu *rendelestomb, int asztalszam, asztal *asztalokfej)
{
    int asztal_szam;
    printf("Melyik asztalhoz veszel fel rendelest: ");
    scanf("%d", &asztal_szam);
    asztal *aktualis_asztal = asztalokfej;
    while (aktualis_asztal != NULL && aktualis_asztal->sorszam != asztal_szam)
    {
        aktualis_asztal = aktualis_asztal->kov;
    }
    if (aktualis_asztal == NULL)
    {
        printf("\nHibas asztalszam!\n");
        return;
    }
    if (aktualis_asztal->foglalt == 0)
    {
        printf("\nEz az asztal meg nincs lefoglalva, igy nem veheto fel rendeles!\n");
    }
    else
    {
        printf("\nAdd meg a rendeles szamat, ha befejezned a rendelsfelvetelt, nyomj 0-t!\n");
        int i = 0;
        while (1)
        {
            int rsz;
            rendelestomb[asztal_szam - 1].voltrendeles = 1; ///< értékváltoztatás történik
            printf("\nAdd meg a rendeles szamat: ");
            scanf("%d", &rsz);
            if (rsz < 0 || rsz > 45)
                printf("\nHibas bemenet!\n");
            else
            {
                rendelestomb[asztal_szam - 1].rendeles[i] = rsz;
                i++;
            }
            if (rsz == 0)
                break;
        }
    }
}

void szamlanyomtatas(menu *rendelestomb, int asztalszam, asztal *asztalokfej, menu *menutomb)
{
    int asztal_szam;
    printf("Melyik asztalhoz szeretnel szamlat nyomtatni: ");
    scanf("%d", &asztal_szam);
    asztal *aktualis_asztal = asztalokfej;
    while (aktualis_asztal != NULL && aktualis_asztal->sorszam != asztal_szam)
    {
        aktualis_asztal = aktualis_asztal->kov;
    }

    if (aktualis_asztal == NULL)
    {
        printf("\nHibas asztalszam!\n");
        return;
    }
    if (rendelestomb[asztal_szam - 1].voltrendeles == 1)
    {
        int i = 0, osszeg = 0; ///< az osszeg változóba összeadjuk a megrendelt ételek árait
        while (rendelestomb[asztal_szam - 1].rendeles[i] != 0)
        {
            printf("%d.  %s %d.-HUF\n", menutomb[rendelestomb[asztal_szam - 1].rendeles[i] - 1].sorszam, menutomb[rendelestomb[asztal_szam - 1].rendeles[i] - 1].etel, menutomb[rendelestomb[asztal_szam - 1].rendeles[i] - 1].ar);
            osszeg += menutomb[rendelestomb[asztal_szam - 1].rendeles[i] - 1].ar;
            i++;
        }
        printf("_______________________________________________________________________\n");
        printf("Osszeg:                                                      %d.-HUF\n", osszeg);
    }
    else
        printf("\nEloszor vegyel fel rendelest!\n");

    rendelestomb[asztal_szam - 1].voltrendeles = 0;
    FILE *f;
    f = fopen("foglalt_asztalok.txt", "w");
    aktualis_asztal->foglalt = 0;
    while (aktualis_asztal != NULL)
    {
        fprintf(f, "%d %d %d\n", aktualis_asztal->sorszam, aktualis_asztal->ferohely, aktualis_asztal->foglalt);
        aktualis_asztal = aktualis_asztal->kov;
    }

    fclose(f);
}
void asztal_free(asztal *asztalokfej)
{
    asztal *current = asztalokfej;
    asztal *next;
    while (current != NULL)
    {
        next = current->kov;
        free(current);
        current = next;
    }
}
