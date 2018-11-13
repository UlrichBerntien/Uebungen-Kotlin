/**
 *  Programmieraufgabe:
 *
 *  Flaschenverteilung (Algorithmen)
 *  https://www.programmieraufgaben.ch/aufgabe/flaschenverteilung-1/i9kdigw5
 *
 *  Familie Kurse möchte Urlaub auf der trinkwasserlosen Insel Drøgø, deren
 *  Küste ringsherum sehr steil ist. Zum Glück gibt es einen Flaschenzug, mit
 *  dem die Getränkeflaschen nach oben gezogen werden können.Es stehen auch
 *  viele Behälter mit genügend Platz für alle Flaschen zur Verfügung, damit
 *  mehrere Flaschen auf einmal transportiert werden können.
 *  Bei 7 Flaschen und 2 Behältern, von denen in den einen 3 und in den anderen
 *  5 Flaschen passen, gibt es genau zwei Möglichkeiten: Der kleinere Behälter
 *  ist entweder ganz voll oder enthält genau 2 Flaschen. Auf 3 Behälter mit
 *  Platz für genau 2, 3 und 4 Flaschen lassen sich die sieben Flaschen auf
 *  genau sechs Arten verteilen.
 *  Schreiben Sie ein Programm, das eine Anzahl N von Flaschen, eine Anzahl k
 *  von Behältern und die k Fassungsvermögen der Behälter einliest und
 *  berechnet, auf wie viele Arten die Flaschen verteilt werden können. Die
 *  Flaschen sind nicht unterscheidbar, aber die Behälter sind es, auch wenn
 *  sie gleich groß sind.
 *
 *  Autor:
 *      Ulrich Berntien 2018-08-31
 *
 *  Sprache:
 *      Kotlin 1.2.51
 */


/**
 *  Bestimmt die Anzahl der Möglichkeiten die Flaschen zu verteilen.
 *  @param flaschen Anzahl der Flaschen, die verteilt werden müssen.
 *  @param kisten Liste mit den Größen der Kisten.
 *  @return Anzahl der Möglichkeiten
 */
fun moeglichkeiten(flaschen: Int, kisten: IntArray): Int {
    assert(flaschen >= 0)
    assert(kisten.all { it > 0 })
    if (flaschen == 0) {
        // Keine Flasche läst eine Möglichkeit.
        return 1
    }
    if (kisten.isEmpty()) {
        throw IllegalArgumentException("no containers")
    }
    if (kisten.size == 1) {
        if (kisten[0] < flaschen)
            throw IllegalArgumentException("container too small")
        // Es gibt nur eine Kiste und damit eine Möglichkeit:
        // alle Flaschen in diese Kiste
        assert(kisten.size == 1 && kisten[0] >= flaschen)
        return 1
    }
    assert(kisten.size > 1)
    // In dieser Rekursionsstufe werden alle Möglichkeiten für die
    // Füllung der ersten Kiste betrachtet.
    val ersteKiste = kisten[0]
    val restKisten = kisten.sliceArray(1..kisten.size - 1)
    // Mindestens so viele Flaschen in die Kiste,
    // dass der Rest der Flaschen in den Rest der Kisten passt.
    val minFlaschen = maxOf(0, flaschen - restKisten.sum())
    // Höchstens so viele Flaschen in die Kiste,
    // dass alle Flaschen in der Kiste sind oder die Kiste voll ist.
    val maxFlaschen = minOf(flaschen, ersteKiste)
    // Alle Möglichkeiten aufsummieren,
    // wenn n Flaschen in diese Kiste gestellt werden.
    return (minFlaschen..maxFlaschen)
            .map { moeglichkeiten(flaschen - it, restKisten) }
            .sum()
}

/**
 * Automatisch die Testfälle ausführen.
 * @param argv wird ignoriert.
 */
fun main(argv: Array<String>) {
    // Testfälle aus der Aufgabe
    // Array mit Pair aus Anzahl Flaschen, Array der Kistengrößen
    val testFaelle = arrayOf(
            Pair(7, intArrayOf(3, 5)),
            Pair(7, intArrayOf(2, 3, 4))
    )
    for ((flaschen, kisten) in testFaelle)
        println("""
        |Anzahl Flaschen:          $flaschen
        |Kistengrößen:             ${kisten.contentToString()}
        |Verteilungsmöglichkeiten: ${moeglichkeiten(flaschen, kisten)}
        |-------------------------
        """.trimMargin())
}
