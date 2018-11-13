/**
 *  Programmieraufgabe:
 *
 *  Alter aus zweistelligem Jahrgang und Jahr
 *  https://www.programmieraufgaben.ch/aufgabe/alter-aus-zweistelligem-jahrgang-und-jahr/694ee26d
 *
 *  Bei diesem Programm gibt der Anwender seinen Jahrgang zweistellig ein
 *  (z. B. 88). Das aktuelle Jahr soll auch zweistellig eingegeben werden
 *  (z. B. 14). Das Programm gibt dann das Alter des Anwenders korrekt aus unter
 *  der Bedingung, dass der Anwender nicht jünger als 3, aber auch nicht älter
 *  als 102 Jahre alt ist.
 *
 *  Autor:
 *      Ulrich Berntien 2018-08-29
 *
 *  Sprache:
 *      Kotlin 1.2.51
 */

/**
 *  Aus Jahrgang zweistellig ein (z. B. 88) und dem aktuelle Jahr,
 *  auch zweistellig (z. B. 14) das Programm Alter berechnen unter
 *  der Bedingung, dass der Anweder nicht jünger als 3, aber auch
 *  nicht älter als 102 Jahre alt ist.
 *  @param jahrgang Geburtsjahrgang (00..99)
 *  @param jahr Aktuelles Jahr (00..99)
 *  @return Das Alter (3..102)
 */
fun alter(jahrgang: Int, jahr: Int): Int {
    require(jahrgang in 0..99 && jahr in 0..99)
    return (197 + jahr - jahrgang) % 100 + 3
}


/**
 * Funktion "alter" automatisch testen.
 * @param argv wird ignoriert.
 */
fun main(argv: Array<String>) {
    print("test ... ")
    for (jahrgang in 0..99)
        for (testAlter in 3..102)
            if (alter(jahrgang, (jahrgang + testAlter) % 100) != testAlter) {
                println("Fehler bei Jahrgang=$jahrgang, Alter=$testAlter")
                return
            }
    println("OK")
    val einzelfaelle = arrayOf(
            Pair(90, 95),
            Pair(90, 0),
            Pair(88, 14),
            Pair(88, 89),
            Pair(77, 80),
            Pair(99, 1))
    for ((jahrgang, jahr) in einzelfaelle)
        println("Jahrgang=$jahrgang, Jahr=$jahr -> Alter=${alter(jahrgang, jahr)}")
}
