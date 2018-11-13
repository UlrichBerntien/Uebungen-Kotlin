/**
 *  Programmieraufgabe:
 *
 *  Kamele beladen
 *  https://www.programmieraufgaben.ch/aufgabe/kamele-beladen/6gddr4zm
 *
 *  Ein Kamel soll optimal beladen werden. Das Kamel kann maximal 270 kg tragen.
 *  Aktuell sind Waren mit den folgenden Gewichten zu transportieren: 5, 18, 32,
 *  34, 45, 57, 63, 69, 94, 98 und 121 kg. Nicht alle Gewichte müssen verwendet
 *  werden; die 270 kg sollen aber möglichst gut, wenn nicht sogar ganz ohne
 *  Rest beladen werden. Die Funktion
 *  beladeOptimal(kapazitaet: integer, vorrat: integer[]): integer[]
 *  erhält die maximal tragbare Last (kapazitaet) und eine Menge von
 *  aufzuteilenden Gewichten (vorrat). Das Resultat (hier integer[]) ist die
 *  Auswahl aus dem Vorrat, die der Belastbarkeit möglichst nahe kommt. Gehen
 *  Sie wie folgt rekursiv vor: Für jedes vorhandene Gewicht g aus dem Vorrat
 *  soll das Problem vereinfacht werden. Dazu wird dieses Gewicht probehalber
 *  aufgeladen:
 *      tmpLadung: integer[]
 *      tmpLadung := beladeOptimal(kapazitaet - g, "vorrat ohne g")
 *  Danach wird das beste Resultat tmpLadung + g gesucht und als Array
 *  zurückgegeben. Behandeln Sie in der Methode beladeOptimal() zunächst die
 *  Abbruchbedingungen:
 *      Vorrat leer
 *      alle vorhandenen Gewichte sind zu schwer
 *      nur noch ein Gewicht im Vorrat
 *
 *  Autor:
 *      Ulrich Berntien 2018-06-24
 *
 *  Sprache:
 *      Kotlin 1.2.51
 */


/**
 * Bestimmt die optimale Beladung.
 * @param capacity Die maximale Beladung.
 * @param pool Die zur Beladung möglichen Elemente.
 * @return Die optimale Beladung, ein Teilmenge von pool.
 */
fun optimalLoad(capacity: Int, pool: IntArray): IntArray {
    var tmpOptimalLoad = 0
    var tmpOptimalBag = IntArray(0)
    for (index in pool.indices)
        if (pool[index] <= capacity) {
            val bag = optimalLoad(capacity - pool[index], pool.sliceArray(pool.indices - index))
            val total = bag.sum() + pool[index]
            if (total > tmpOptimalLoad) {
                tmpOptimalLoad = total
                tmpOptimalBag = bag + pool[index]
            }
        }
    return tmpOptimalBag
}


/**
 * Hauptprogramm.
 * @param argv Aufrufparamter werden ignotiert.
 */
fun main(argv: Array<String>) {
    val capacity = 270
    val pool = intArrayOf(18, 32, 34, 45, 57, 63, 69, 94, 98, 121)
    // Mit mehr Elementen dauert es wesentlioh länger:
    //val capacity =  1000
    //val pool = arrayOf(181, 130, 128, 125, 124, 121, 104, 101, 98, 94, 69, 61, 13)
    val bag = optimalLoad(capacity, pool)
    val load = bag.sum()
    println("""
    |Beladung: ${bag.contentToString()}
    |Summe Beladung: $load
    |Freie Kapazität: ${capacity - load}
    """.trimMargin())
}
