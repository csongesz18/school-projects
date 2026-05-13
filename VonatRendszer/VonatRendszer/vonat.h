#ifndef VONAT_H
#define VONAT_H

#include "String.h"
#include "memtrace.h"

class Vonat {
private:
    int vonatszam;
    String erkezesiIdo;
    String indulasiIdo;
    String indulasiHely;
    String erkezesiHely;
public:
    ///Getterek
    int getvonatszam() const;
    const String& geterkezesiIdo() const;
    const String& getindulasiIdo() const;
    const String& getindulasiHely() const;
    const String& geterkezesiHely() const;

    ///Setterek
    void setvonatszam(int vonatszam);
    void seterkezesiIdo(const String& erkezesiIdo);
    void setindulasiIdo(const String& indulasiIdo);
    void setindulasiHely(const String& indulasiHely);
    void seterkezesiHely(const String& erkezesiHely);

    ///Paraméteres konstruktor
    Vonat(int v, const String& e, const String& iI, const String& iH, const String& eH);

    Vonat() {}

    ///Destruktor
    ~Vonat() {}
};

#endif // VONAT_H
