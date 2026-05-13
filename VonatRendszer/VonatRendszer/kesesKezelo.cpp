#include "vonat.h"
#include "kesesKezelo.h"
#include "String.h"
#include "memtrace.h"

/**
* a parameterkent megadott vonatra meghivja az erkezesi ido setteret, ezaltal felulirva az eredeti erkezesi idot
*/
void KesesKezelo::keses(Vonat* v, String erkezesiIdo) {
    v->seterkezesiIdo(erkezesiIdo);
}
