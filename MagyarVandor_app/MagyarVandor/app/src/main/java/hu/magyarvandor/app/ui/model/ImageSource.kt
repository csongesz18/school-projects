package hu.magyarvandor.app.ui.model

// A kép hozzáadás módja a formon:
// - PICKER: telefon/emulátor képgalériából választunk
// - URL: internetes linket írunk be
enum class ImageSource {
    PICKER,
    URL
}