#include "String.h"
#include "memtrace.h"
#include <cstring>

///default konstruktor: ures stringet hoz letre
String::String() {
    data = new char[1];
    data[0] = '\0';
}

///konstruktor c-string alapjan
String::String(const char* str) {
    data = new char[strlen(str) + 1];
    strcpy(data, str);
}

///masolo konstruktor
String::String(const String& other) {
    data = new char[strlen(other.data) + 1];
    strcpy(data, other.data);
}

///ertekado operator masik string alapjan
String& String::operator=(const String& other) {
    if (this != &other) {
        delete[] data;
        data = new char[strlen(other.data) + 1];
        strcpy(data, other.data);
    }
    return *this;
}

///ertekado operator c-string alapjan
String& String::operator=(const char* str) {
    delete[] data;
    data = new char[strlen(str) + 1];
    strcpy(data, str);
    return *this;
}

///destruktor: felszabaditja a memoriat
String::~String() {
    delete[] data;
}

///visszaadja a karakterlancot c-stringkent
const char* String::c_str() const {
    return data;
}

///beallit egy uj karakterlancot
void String::set(const char* str) {
    delete[] data;
    data = new char[strlen(str) + 1];
    strcpy(data, str);
}

///visszaadja a string hosszat
size_t String::length() const {
    return strlen(data);
}

///ket string egyenloseget vizsgalja
bool String::operator==(const String& other) const {
    return strcmp(data, other.data) == 0;
}

///beolvasas inputbol
std::istream& operator>>(std::istream& is, String& s) {
    char buffer[1024];
    is >> buffer;
    s.set(buffer);
    return is;
}

///kiiras outputra
std::ostream& operator<<(std::ostream& os, const String& s) {
    os << s.data;
    return os;
}

///nem egyenlo operator
bool String::operator!=(const String& other) const {
    return !(*this == other);
}
