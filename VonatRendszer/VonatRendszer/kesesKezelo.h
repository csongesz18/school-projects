#ifndef KESESKEZELO_H
#define KESESKEZELO_H

#include <iostream>
#include "vonat.h"
#include "String.h"
#include "memtrace.h"

/**
* ket parametere van a fuggvenynek, az elso az adott vonat, amelynek kell valtoztassuk az erkezesi idejet, pont ezert lesz a masodik parameter pont ez
*/
class KesesKezelo {
public:
    void keses(Vonat* v, String erkezesiIdo);
};

#endif // KESESKEZELO_H
