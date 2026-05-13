#ifndef STRING_H
#define STRING_H

#include <iostream>

#include "memtrace.h"

class String {
private:
    char* data;
public:
    /// Default konstruktor
    String();

    /// C-string konstruktor
    String(const char* str);

    /// Másoló konstruktor
    String(const String& other);

    /// Értékadó operátor (másik String)
    String& operator=(const String& other);

    /// Értékadó operátor C-stringből
    String& operator=(const char* str);

    /// Destruktor
    ~String();

    /// C-style string lekérés
    const char* c_str() const;

    /// Új érték beállítása
    void set(const char* str);

    /// Hossz lekérdezése
    size_t length() const;

    /// Összehasonlítás
    bool operator==(const String& other) const;

    ///Nem egyenlő operátor
    bool operator!=(const String& other) const;

    ///Beolvasás
    friend std::istream& operator>>(std::istream& is, String& s);

    /// Kiíratás
    friend std::ostream& operator<<(std::ostream& os, const String& s);
};

#endif // STRING_H
