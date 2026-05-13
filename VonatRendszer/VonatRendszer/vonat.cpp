#include "vonat.h"
#include "kesesKezelo.h"
#include "memtrace.h"

///Konstruktor, ertekadasok
Vonat::Vonat(int v, const String& e, const String& iI, const String& iH, const String& eH) {
    this->vonatszam = v;
    this->erkezesiIdo = e;
    this->indulasiIdo = iI;
    this->indulasiHely = iH;
    this->erkezesiHely = eH;
}

///a vonatszamot adja vissza
int Vonat::getvonatszam() const {
    return vonatszam;
}

///beallitja a vonatszamot
void Vonat::setvonatszam(int vonatszam) {
    this->vonatszam = vonatszam;
}

///az erkezesi idot adja vissza
const String& Vonat::geterkezesiIdo() const {
    return erkezesiIdo;
}

///beallitja az erkezesi idot
void Vonat::seterkezesiIdo(const String& erkezesiIdo) {
    this->erkezesiIdo = erkezesiIdo;
}

///az indulasi idot adja vissza
const String& Vonat::getindulasiIdo() const {
    return indulasiIdo;
}

///beallitja az indulasi idot
void Vonat::setindulasiIdo(const String& indulasiIdo) {
    this->indulasiIdo = indulasiIdo;
}

///az indulasi helyet adja vissza
const String& Vonat::getindulasiHely() const {
    return indulasiHely;
}

///beallitja az indulasi helyes
void Vonat::setindulasiHely(const String& indulasiHely) {
    this->indulasiHely = indulasiHely;
}

///az erkezesi helyet adja vissza
const String& Vonat::geterkezesiHely() const {
    return erkezesiHely;
}

///beallitja az erkezesi helyet
void Vonat::seterkezesiHely(const String& erkezesiHely) {
    this->erkezesiHely = erkezesiHely;
}
