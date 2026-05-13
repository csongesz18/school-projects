#ifndef JEGY_H
#define JEGY_H

#include <iostream>

#include "vonat.h"
#include "String.h"
#include "memtrace.h"

/**
* A privat adattagok a jegyek specialis adatai
* A public reszben ezeknek az adatoknak vannak a getterei, setterei, konstruktora, default konstruktora, illetve destruktora
*/
class Jegy {
protected:
    const Vonat* vonat;
    int kocsiSzam;
    int helySzam;
    String person;
    int price;
public:
    ///Paraméteres konstruktor
    Jegy(const Vonat* v, int kocsiSzam, int hely, String person, int price);
    Jegy() {}

    ///Destruktor
    virtual ~Jegy() {}

    ///Getter
    virtual int getPrice() const;
    const Vonat* getVonat() const;
    int getKocsiSzam() const;
    int getHelySzam() const;
    virtual const String& getPerson() const;

    ///Setterek
    void setVonat(const Vonat* vonat);
    void setKocsiSzam(int kocsiSzam);
    void setHelySzam(int helySzam);
    void setPerson(const String& person);
    void setPrice(int price);

    void torolVonat();
};

#endif // JEGY_H
