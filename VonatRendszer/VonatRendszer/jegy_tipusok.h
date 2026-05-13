#ifndef JEGY_TIPUSOK_H
#define JEGY_TIPUSOK_H

#include <iostream>
#include <string>
#include <ctime>
#include <cstdlib>
#include <limits>
#include <fstream>
#include <cstdio>

#include "jegy.h"
#include "String.h"
#include "memtrace.h"

using namespace std;

/**
* az osztalyban ket virtualis fuggveny lesz feluldefinialva
* a getPrice()-ban a jegy aranak 50%-a ter vissza, mivel a diak jegy -50%
* a getPerson()-be a nevet adja vissza, hogy milyen szemely tipusrol van szo
* mindkettore szugseg van a jegy kiiratasanal
*/
class Diak: public Jegy{
public:
    int getPrice()const {return this->price * 50 / 100;}
    String getPerson() {this->person="diak"; return this->person;}
};

/**
* az osztalyban ket virtualis fuggveny lesz feluldefinialva
* a getPrice()-ban a jegy aranak 30%-a ter vissza, mivel a nyugdijas jegy -70%
* a getPerson()-be a nevet adja vissza, hogy milyen szemely tipusrol van szo
* mindkettore szugseg van a jegy kiiratasanal
*/
class Nyugdijas: public Jegy{
public:
    int getPrice()const {return this->price * 30 / 100;}
    String getPerson() {this->person="nyugdijas"; return this->person;}
};

/**
* az osztalyban ket virtualis fuggveny lesz feluldefinialva
* a getPrice()-ban a jegy aranak 100%-a ter vissza, mivel a felnotteknek nincs kedvezmeny
* a getPerson()-be a nevet adja visszAkka, hogy milyen szemely tipusrol van szo
* mindkettore szugseg van a jegy kiiratasanal
*/
class Felnott: public Jegy{
public:
    int getPrice()const {return this->price;}
    String getPerson() {this->person="felnott"; return this->person;}
};

/**
* az osztalyban ket virtualis fuggveny lesz feluldefinialva
* a getPrice()-ban a jegy aranak 25%-a ter vissza, mivel a gyerek jegy -75%
* a getPerson()-be a nevet adja vissza, hogy milyen szemely tipusrol van szo
* mindkettore szugseg van a jegy kiiratasanal
*/
class Gyerek: public Jegy{
public:
    int getPrice()const {return this->price * 25 / 100;}
    String getPerson() {this->person="gyerek"; return this->person;}
};

#endif // JEGY_TIPUSOK_H
