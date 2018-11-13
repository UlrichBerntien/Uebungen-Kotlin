/*
 *  Programmieraufgabe:
 *
 *  Poker verteilen
 *  https://www.programmieraufgaben.ch/aufgabe/poker-verteilen/i4si4ts5
 *
 *  Schreiben Sie ein Programm, das vier Spielerinnen je fünf Pokerkarten verteilt.
 *  Dabei ist ein Array zunächst mit den Zahlen 1 bis 55 zu füllen. Am einfachsten
 *  verwenden Sie dazu die Indizes 1 bis 55. 1-13 entspricht den Herz-Karten,
 *  14-26 sind die Pik-Karten, danach folgen 13 Karo- und zuletzt die Kreuz-Karten.
 *  Die Nummern 53, 54 und 55 sind die drei Joker-Karten. Die erste bis und mit
 *  die zehnte Karte pro Farbe sind jeweils die Zahl-Karten, die elfte entspricht
 *  dem Jungen (J), die zwölfte der Queen (Q = Dame) und die dreizehnte. ist der
 *  König (K).
 *  Mischen Sie den Array nach folgendem Algorithmus (D. Knuth: The Art of
 *  Computer Programming Vol. 2; ISBN 0-201-89684-2; Addison-Wesley; S. 145
 *  Shuffling) und verteilen Sie die ersten 20 Karten reihum an vier Spielende.
 *  Misch-Algorithmus:
 *      mischenAbPos := 1
 *      while(mischenAbPos < 55)
 *      {
 *         zufallsPos   := zufällige Position aus den Zahlen [mischenAbPos bis 55]
 *         vertausche die Elemente "array[mischenAbPos]" mit "array[zufallsPos]"
 *         mischenAbPos := mischenAbPos + 1
 *      }
 *
 *  Autor:
 *      Ulrich Berntien 2018-08-31
 *
 *  Sprache:
 *      Kotlin 1.2.51
 */


/**
 *  Eine Spielkarte.
 *  Alle Spielkarten sind mit einem Code 1 .. 55 versehen, der beim Erzeugen
 *  der Karte vergeben wird. Die Codes sind in der Aufgabe definiert.
 */
data class Card(private val code: Int) {

    private companion object {
        /**
         *  Namen der Kartengruppen.
         *  Es gibt 4 Gruppen.
         */
        val gruppenName = arrayOf("Herz", "Pik", "Karo", "Kreuz")

        /**
         *  Namen der Werte.
         *  In jeder der 4 Gruppen gibt es 13 Werte.
         */
        val wertName = Array(10) { (it + 1).toString() } + arrayOf("Junge", "Dame", "König")
    }

    init {
        assert(code in 1..55)
        assert(gruppenName.size == 4)
        assert(wertName.size == 13)
    }

    /**
     *  Name der Karte.
     *  Es gibt 13 Karten in jeder Gruppe (4*13=52) dazu 3 Joker (4*13+3=55)
     *  @return Name der Karte.
     */
    override fun toString() =
            if (code <= 52) "${gruppenName[(code - 1) / 13]} ${wertName[(code - 1) % 13]}" else "Joker"
}


/**
 *  Mischen der Werte im Array.
 *  @param array Die Werte in diesem Array werden gemischt.
 */
fun <T> riffle(array: Array<T>) {
    for (indexA in array.indices) {
        val indexB = (Math.random() * array.size).toInt()
        val temp = array[indexA]
        array[indexA] = array[indexB]
        array[indexB] = temp
    }
}


/**
 * Spielkarten an 4 Personen verteilen.
 */
fun main(args: Array<String>) {
    val cardSet = Array(55) { Card(it + 1) }
    println("alle Karten: ${cardSet.contentToString()}")
    riffle(cardSet)
    // Kontrolle: Alle Karten sind noch im Spiel
    assert((1..55).all { Card(it) in cardSet })
    // Kontrolle: Die Anzahl der Karten stimmt noch, also kann keine Karte doppelt sein.
    assert(cardSet.size == 55)
    for (person in 1..4) {
        val personCardSet = cardSet.sliceArray(person * 5 - 5 until person * 5)
        // Kontrolle: Es werden 5 Karten gegeben.
        assert(personCardSet.size == 5)
        println("Karten für Person $person: ${personCardSet.contentToString()}")
    }
}
