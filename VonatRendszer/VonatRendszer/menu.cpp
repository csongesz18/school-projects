#include <iostream>
#include <ctime>
#include <cstdlib>
#include <limits>
#include <fstream>
#include <cstdio>

#include "jegy.h"
#include "jegy_tipusok.h"
#include "menu.h"
#include "vonat.h"
#include "kesesKezelo.h"
#include "FileException.h"
#include "String.h"
#include "memtrace.h"

using namespace std;

///alapertelmezett konstruktor
Menu::Menu() : jegy(NULL), jegyek(NULL), jegyCount(0), jegyKapacitas(0), vonatok(NULL), vonatCount(0) {}

///destruktor
///felszabaditja a dinamikusan foglalt memoriakat
Menu::~Menu() {
    ///jegyek felszabaditasa
    for (int i = 0; i < jegyCount; ++i) {
        delete jegyek[i];
    }
    delete[] jegyek;
    jegyek = NULL;
    jegyCount = 0;
    jegyKapacitas = 0;

    ///vonatok felszabaditasa
    for (int i = 0; i < vonatCount; ++i) {
        delete vonatok[i];
    }
    delete[] vonatok;
    vonatok = NULL;
}

///kepernyo torlese, platformfuggoen cls vagy clear
void Menu::clearScreen() {
#ifdef _WIN32
    system("CLS");
#else
    system("clear");
#endif
}

///enter lenyomasara var, hogy a felhasznalo folytassa a programot
void Menu::waitForKeyPress() {
    cout << endl << "Nyomj Enter-t a folytatashoz!";
    cin.ignore();
    cin.get();
}

///beolvassa a vonatokat fajlbol es dinamikusan letrehozza oket
void Menu::beolvasVonatok(const String& filename) {
    ifstream file(filename.c_str());
    if (!file.is_open()) {
        throw FileNotFoundException();
    }

    if (file.peek() == ifstream::traits_type::eof()) {
        file.close();
        throw EmptyFileException();
    }

    file >> vonatCount;
    file.ignore();
    vonatok = new Vonat*[vonatCount];

    for (int i = 0; i < vonatCount; ++i) {
        int vonatszam;
        String indulasiIdo, erkezesiIdo, indulasiHely, erkezesiHely;
        file >> vonatszam >> indulasiHely >> indulasiIdo >> erkezesiHely >> erkezesiIdo;
        vonatok[i] = new Vonat(vonatszam, erkezesiIdo, indulasiIdo, indulasiHely, erkezesiHely);
    }

    file.close();
}

///elmenti a vonatokat egy megadott fajlba
void Menu::mentesVonatok(const String& filename) {
    ofstream file(filename.c_str());
    if (!file.is_open()) {
        throw FileNotFoundException();
    }

    file << vonatCount << endl;
    for (int i = 0; i < vonatCount; ++i) {
        file << vonatok[i]->getvonatszam() << " "
             << vonatok[i]->getindulasiHely() << " "
             << vonatok[i]->getindulasiIdo() << " "
             << vonatok[i]->geterkezesiHely() << " "
             << vonatok[i]->geterkezesiIdo() << endl;
    }

    file.close();
}

///megkeresi a megadott vonatszamhoz tartozo objektumot es modositja a megfelelo adattagot
void Menu::switchcase_vonatadatok(int adat, int vonatszam) {
    for (int i = 0; i < vonatCount; ++i) {
        if (vonatok[i]->getvonatszam() == vonatszam) {
            switch(adat){
                case 1: {
                    int vonatSzamuj;
                    cout << "Add meg az uj vonat szamot: ";
                    while (!(cin >> vonatSzamuj)) {
                        cout << endl << "Hibas bemenet! Kerlek, adj meg egy szamot: ";
                        cin.clear();
                        cin.ignore(numeric_limits<int>::max(), '\n');
                    }
                    vonatok[i]->setvonatszam(vonatSzamuj);
                    break;
                }
                case 2: {
                    String indulasiIdouj;
                    cout << "Add meg az uj indulasi idot: ";
                    cin >> indulasiIdouj;
                    vonatok[i]->setindulasiIdo(indulasiIdouj);
                    break;
                }
                case 3: {
                    String erkezesiIdouj;
                    cout << "Add meg az uj erkezesi idot: ";
                    cin >> erkezesiIdouj;
                    vonatok[i]->seterkezesiIdo(erkezesiIdouj);
                    break;
                }
                case 4: {
                    String indulasiHelyuj;
                    cout << "Add meg az uj indulasi helyet: ";
                    cin >> indulasiHelyuj;
                    vonatok[i]->setindulasiHely(indulasiHelyuj);
                    break;
                }
                case 5: {
                    String erkezesiHelyuj;
                    cout << "Add meg az uj erkezesi helyet: ";
                    cin >> erkezesiHelyuj;
                    vonatok[i]->seterkezesiHely(erkezesiHelyuj);
                    break;
                }
                default: {
                    cout << endl << "Hibas bemenet!" << endl << endl;
                    break;
                }
            }
            break;
        }
    }
}

///uj vonatot ad hozza a rendszerhez
void Menu::VonatHozzaadas() {
    int vonatSzam;
    String indulasiIdo, erkezesiIdo, indulasiHely, erkezesiHely;

    cout << endl << "Add meg a vonat szamat: ";
    while (!(cin >> vonatSzam)) {
        cout << endl << "Hibas bemenet! Kerlek, adj meg egy szamot: ";
        cin.clear();
        cin.ignore(numeric_limits<int>::max(), '\n');
    }

    bool vane = false;
    for(int i = 0; i < vonatCount; i++) {
        if(vonatSzam == vonatok[i]->getvonatszam()) {
            vane = true;
            break;
        }
    }

    if(vane) {
        cout << endl << "Mar van ilyen vonat!" << endl;
    } else {
        cout << endl << "Add meg a vonat indulasi idejet: ";
        cin >> indulasiIdo;
        cout << endl << "Add meg a vonat erkezesi idejet: ";
        cin >> erkezesiIdo;
        cout << endl << "Add meg a vonat indulasi helyet: ";
        cin >> indulasiHely;
        cout << endl << "Add meg a vonat erkezesi helyet: ";
        cin >> erkezesiHely;

        Vonat** newVonatok = new Vonat*[vonatCount + 1];
        for (int i = 0; i < vonatCount; ++i) {
            newVonatok[i] = vonatok[i];
        }

        newVonatok[vonatCount] = new Vonat(vonatSzam, erkezesiIdo, indulasiIdo, indulasiHely, erkezesiHely);
        delete[] vonatok;
        vonatok = newVonatok;
        vonatCount++;
    }
}

///lekerdezi, melyik vonat adatat akarjuk modositani, es meghivja a modosito fuggvenyt
void Menu::VonatAdatModositas() {
    int adat, vonatszam;
    cout << endl << "Add meg a vonat szamat, amiben adatot akarsz modositani: ";
    while (!(cin >> vonatszam)) {
        cout << endl << "Hibas bemenet! Kerlek, adj meg egy szamot: ";
        cin.clear();
        cin.ignore(numeric_limits<int>::max(), '\n');
    }

    bool vane = false;
    for (int i = 0; i < vonatCount; ++i) {
        if (vonatok[i]->getvonatszam() == vonatszam) {
            cout << endl << "1. Vonatszam" << endl
                 << "2. Indulasi ido" << endl
                 << "3. Erkezesi ido" << endl
                 << "4. Indulasi hely" << endl
                 << "5. Erkezesi hely" << endl;
            cout << "Adja meg hogy melyik adatot szeretne modositani! ";
            adat = Menu::opcioValasztas();
            vane = true;
            break;
        }
    }

    if (!vane)
        cout << endl << "Nincs ilyen vonat a rendszerben!" << endl;
    else
        switchcase_vonatadatok(adat, vonatszam);
}

///torli a megadott vonatszamu vonatot a rendszerbol
void Menu::VonatTorles(bool &talalt) {
    int torlendoVonatszam;
    cout << endl << "Add meg a torlendo vonat szamat: ";
    cin >> torlendoVonatszam;

    for (int i = 0; i < vonatCount; ++i) {
        if (vonatok[i]->getvonatszam() == torlendoVonatszam) {
            for (int j = 0; j < jegyCount; ++j) {
                if (jegyek[j]->getVonat() == vonatok[i]) {
                    jegyek[j]->torolVonat();
                }
            }

            delete vonatok[i];
            vonatok[i] = vonatok[vonatCount - 1];
            --vonatCount;

            cout << "A(z) " << torlendoVonatszam << " vonat sikeresen torolve!" << endl;
            talalt = true;
            break;
        }
    }
}

///megjeleniti a fo menut a felhasznalo szamara
void Menu::menuMegjelenitese() {
    cout << "Vonatjegy kezelo rendszer" << endl << endl;
    cout << "1. Vonatok kezelese" << endl;
    cout << "2. Jegyek kezelese" << endl;
    cout << "3. Kesesek kezelese" << endl;
    cout << "4. Kiadott jegyek listazasa" << endl;
    cout << "5. Kilepes" << endl;
}

///bekeri a felhasznalotol a kivalasztott menupont sorszamot
int Menu::opcioValasztas() {
    int opcio;
    cout << endl << "Valassz egy opcio szamot: ";
    while (!(cin >> opcio)) {
        cout << endl << "Hibas bemenet! Kerlek, adj meg egy szamot: ";
        cin.clear();
        cin.ignore(numeric_limits<int>::max(), '\n');
    }
    return opcio;
}

///a vonatokkal kapcsolatos opciokat kezeli: hozzaadas, modositas, torles
void Menu::egyesOpcio() {
    cout << endl << "1. Uj vonat hozzaadasa" << endl;
    cout << "2. Vonat adatainak modositasa" << endl;
    cout << "3. Vonat torlese" << endl;

    int opcio = Menu::opcioValasztas();

    switch (opcio) {
    case 1:
        Menu::VonatHozzaadas();
        break;
    case 2:
        if (vonatCount == 0) {
            cout << endl << "Eloszor adj hozza vonatot!" << endl;
        } else {
            Menu::VonatAdatModositas();
        }
        break;
    case 3: {
        bool talalt = false;
        if (vonatCount == 0) {
            cout << endl << "Eloszor adj hozza vonatot!" << endl;
        } else {
            Menu::VonatTorles(talalt);
            if (!talalt) {
                cout << "Nincs ilyen vonat a rendszerben!" << endl;
            }
        }
        break;
    }
    default:
        cout << endl << "Ervenytelen opcio!" << endl;
        break;
    }
}

///jegyet ad ki a felhasznalonak, letrehoz egy megfelelo jegy tipust
void Menu::kettesOpcio() {
    if (vonatCount == 0) {
        cout << endl << "Eloszor adj hozza vonatot!" << endl;
    } else {
        srand(time(NULL));
        int utas, ar;

        cout << endl << "Milyen tipusu szemelyrol beszelunk? (1- Felnott, 2- Diak, 3- Nyugdijas, 4- Gyerek - 14 ev alatti)? ";
        utas = Menu::opcioValasztas();

        if (utas < 1 || utas > 4) {
            cout << endl << "Hibas bemenet!" << endl;
            waitForKeyPress();
            clearScreen();
            return;
        }

        cout << "Mennyi az ut ara: ";
        while (!(cin >> ar)) {
            cout << endl << "Hibas bemenet! Kerlek, adj meg egy szamot: ";
            cin.clear();
            cin.ignore(numeric_limits<int>::max(), '\n');
        }

        cout << "Jegy kiadasa!" << endl;

        if (jegy != NULL) {
            delete jegy;
        }

        switch (utas) {
            case 1: jegy = new Felnott(); jegy->setPerson("Felnott"); break;
            case 2: jegy = new Diak(); jegy->setPerson("Diak"); break;
            case 3: jegy = new Nyugdijas(); jegy->setPerson("Nyugdijas"); break;
            case 4: jegy = new Gyerek(); jegy->setPerson("Gyerek (14 ev alatti)"); break;
            default: break;
        }

        jegy->setHelySzam(rand() % 100 + 1);
        jegy->setKocsiSzam(rand() % 5 + 1);
        jegy->setPrice(ar);

        int vonatszam;
        cout << "Add meg a vonatszamot: ";
        while (!(cin >> vonatszam)) {
            cout << endl << "Hibas bemenet! Kerlek, adj meg egy szamot: ";
            cin.clear();
            cin.ignore(numeric_limits<int>::max(), '\n');
        }

        bool talalt = false;
        for (int i = 0; i < vonatCount; ++i) {
            if (vonatok[i]->getvonatszam() == vonatszam) {
                cout << "|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" << endl;
                cout << "| Vonatszam: " << vonatok[i]->getvonatszam() << endl;
                cout << "| Indulasi ido: " << vonatok[i]->getindulasiIdo() << endl;
                cout << "| Erkezesi ido: " << vonatok[i]->geterkezesiIdo() << endl;
                cout << "| Indulasi hely: " << vonatok[i]->getindulasiHely() << endl;
                cout << "| Erkezesi hely: " << vonatok[i]->geterkezesiHely() << endl;
                cout << "| Kocsi szam: " << jegy->getKocsiSzam() << endl;
                cout << "| Helyszam: " << jegy->getHelySzam() << endl;
                cout << "| Szemely kategoria: " << jegy->getPerson() << endl;
                cout << "| Ut ara: " << ar << endl;
                cout << "| Fizetendo osszeg: " << jegy->getPrice() << endl;
                cout << "|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" << endl;
                jegy->setVonat(vonatok[i]);
                if (jegyCount >= jegyKapacitas) {
                    int ujKapacitas = jegyKapacitas == 0 ? 10 : jegyKapacitas * 2;
                    Jegy** ujJegyek = new Jegy*[ujKapacitas];
                    for (int i = 0; i < jegyCount; ++i)
                        ujJegyek[i] = jegyek[i];
                    delete[] jegyek;
                    jegyek = ujJegyek;
                    jegyKapacitas = ujKapacitas;
                }
                jegyek[jegyCount++] = jegy;
                jegy = NULL;

                talalt = true;
                break;
            }
        }
        if (!talalt) {
            cout << endl << "Nincs ilyen vonat a rendszerben!" << endl;
        }
    }
}

///megvaltoztatja egy vonat erkezesi idejet
void Menu::harmasOpcio() {
    if (vonatCount == 0) {
        cout << endl << "Eloszor adj hozza vonatot!" << endl;
    } else {
        String ujErkezesiIdo;
        cout << "Add meg az uj erkezesi idot: ";
        cin >> ujErkezesiIdo;
        cout << endl;

        int vonatszam;
        cout << "Add meg a vonatszamot: ";
        cin >> vonatszam;

        KesesKezelo kesesKezelo;
        bool talalt = false;

        for (int i = 0; i < vonatCount; ++i) {
            if (vonatok[i]->getvonatszam() == vonatszam) {
                cout << "Keses a kovetkezo: " << vonatok[i]->geterkezesiIdo();
                kesesKezelo.keses(vonatok[i], ujErkezesiIdo);
                cout << " helyett: " << vonatok[i]->geterkezesiIdo() << " lesz!" << endl << endl;
                talalt = true;
                break;
            }
        }

        if (!talalt) {
            cout << "Nincs ilyen vonat a rendszerben!" << endl;
        }
    }
}

///listazza a kiadott jegyeket
void Menu::jegyekListazasa() {
    if (jegyCount == 0) {
        cout << endl << "Nincs meg kiadott jegy." << endl;
        return;
    }

    for (int i = 0; i < jegyCount; ++i) {
        cout << endl << i + 1 << ". " << jegyek[i]->getPerson()
             << " | Vonatszam: ";

        if (jegyek[i]->getVonat() != NULL) {
            cout << jegyek[i]->getVonat()->getvonatszam();
        } else {
            cout << "TOROLVE";
        }

        cout << " | Kocsi: " << jegyek[i]->getKocsiSzam()
             << " | Hely: " << jegyek[i]->getHelySzam()
             << " | Ar: " << jegyek[i]->getPrice()
             << endl;
    }
}

///kilepes elotti lepesek: vonatok mentese es memoria felszabaditasa
void Menu::kilepesOpcio(const String& filename) {
    try {
        mentesVonatok(filename);
    } catch (FileNotFoundException e) {
        cout << e.what();
    }
}
