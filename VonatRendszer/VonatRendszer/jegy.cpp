#include "jegy.h"
#include "vonat.h"
#include "String.h"
#include "memtrace.h"

/**
* Jegy konstruktor
*/
Jegy::Jegy(const Vonat* v, int kocsiSzam, int hely, String person, int price) {
    this->vonat = v;
    this->kocsiSzam = kocsiSzam;
    this->helySzam = hely;
    this->person = person;
    this->price = price;
}

/**
* A Jegy osztaly privat adattagjainak setterei es getterei
* price, vonat, kocsiszam, helyszam, person
*/
int Jegy::getPrice() const {
    return this->price;
}

void Jegy::setPrice(int price) {
    this->price = price;
}

const Vonat* Jegy::getVonat() const {
    return this->vonat;
}

void Jegy::setVonat(const Vonat* v) {
    this->vonat = v;
}

int Jegy::getKocsiSzam() const {
    return this->kocsiSzam;
}

void Jegy::setKocsiSzam(int kocsiSzam) {
    this->kocsiSzam = kocsiSzam;
}

int Jegy::getHelySzam() const {
    return this->helySzam;
}

void Jegy::setHelySzam(int helySzam) {
    this->helySzam = helySzam;
}

const String& Jegy::getPerson() const {
    return this->person;
}

void Jegy::setPerson(const String& person) {
    this->person = person;
}

void Jegy::torolVonat() { vonat = NULL; }
