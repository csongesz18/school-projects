#ifndef FILEEXCEPTION_H
#define FILEEXCEPTION_H

#include "memtrace.h"
#include <exception>
/**
* Ezeknek az osztályoknak a segítségével vizsgáljuk, hogy a fájlunkat meg tudtuk-e nyitni, illetve a tartalmának létét
**/
class FileNotFoundException : public std::exception {
public:
    const char* what() const throw() {
        return "Hiba a vonatok.txt fajl megnyitasa soran!";
    }
};

class EmptyFileException : public std::exception {
public:
    const char* what() const throw() {
        return "A fajl ures!";
    }
};

#endif // FILEEXCEPTION_H
