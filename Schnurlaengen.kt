/**
 *  Programmieraufgabe:
 *
 *  Schnurlängen
 *  https://www.programmieraufgaben.ch/aufgabe/schnurlaengen/acgo5ap7
 *
 *  Eine Schnur mit einer Gesamtlänge von 450 m soll in Teilstücke der Länge
 *  17 m, 19m und 21 m geteilt werden. Ist das ohne Rest möglich? Die Frage
 *  reduziert sich darauf, ob eine der drei Differenzen
 *      450 - 17 = 433,
 *      450 - 19 = 431 und
 *      450 - 21 = 429
 *  ohne Rest aufgeschnitten werden kann. Denn wenn dies für eine der genannten
 *  drei Differenzen möglich ist, so ist es sicher auch für 450 m möglich.
 *  Schreiben Sie eine Methode zerlegbar(gesamt, laenge1, laenge2, laenge3),
 *  die im Wesentlichen prüft, ob eine der drei Bedingungen
 *      zerlegbar(gesamt-laenge1, laenge1, laenge2, laenge3),
 *      zerlegbar(gesamt-laenge2, laenge1, laenge2, laenge3) oder
 *      zerlegbar(gesamt-laenge3, laenge1, laenge2, laenge3)
 *  zutrifft.
 *
 *  Autor:
 *      Ulrich Berntien 2018-08-26
 *
 *  Sprache:
 *      Kotlin 1.2.51
 */


/**
 *  Kontrolliert ob Gesamtlänge in einzelne Längen zerlebar ist.
 *  @param gesamtLaenge Die zu zerlegende Gesamtlänge.
 *  @param teilLaengen In diese Längen kann zerlegt werden.
 *  @return true, genau dann wenn die Gesamtlänge ohne Rest in die Teillängen zerlegbar ist.
 */
fun zerlegbar(gesamtLaenge: Int, teilLaengen: IntArray): Boolean {
    assert(gesamtLaenge > 0)
    assert(teilLaengen.all { it > 0 })
    for (laenge in teilLaengen) {
        if (gesamtLaenge == laenge) {
            // Diese Teillänge passt genau
            return true
        } else if (gesamtLaenge > laenge) {
            // Bei dieser Teillänge würde ein Rest entstehen.
            if (zerlegbar(gesamtLaenge - laenge, teilLaengen)) {
                // Der Rest ist zerlegbar, also eine Möglichkeit gefunden.
                return true
            }
        }
    }
    // Mit keiner Teillänge wurde eine Zerlegung gefunden,
    // also gibt es keine Möglichkeit.
    return false
}

/**
 * Testfälle automatisch ausführen.
 * @param argv wird ignoriert.
 */
fun main(argv: Array<String>) {
    val tests = arrayOf(
            Pair(450, intArrayOf(17, 19, 21)),
            Pair(100, intArrayOf(17, 19, 21)),
            Pair(101, intArrayOf(17, 19, 21)))
    for ((gesamt, teile) in tests) {
        val result = zerlegbar(gesamt, teile)
        println("Gesamtlänge $gesamt zerlegbar in ${teile.contentToString()}: $result")
    }
}
