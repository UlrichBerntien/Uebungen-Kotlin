/**
 *  Programmieraufgabe:
 *
 *  Waschautomat
 *  https://www.programmieraufgaben.ch/aufgabe/waschautomat/vyyuvu3u
 *
 *  Für eine öffentliche Waschmaschine soll eine automatische Kasse
 *  programmiert werden. Dazu wird die folgende Methode benötigt:
 *      wechselgeldInCent(
 *          vorhandeneMuenzenInCent  : integer[],
 *          waschPreisInCent         : integer  ,
 *          eingeworfeneMuenzenInCent: integer[] ): integer[]
 *  Dabei sind die vorhandenenMuenzenInCent einfach diejenigen Münzen,
 *  die bereits vor der Wäsche im Apparat waren, um Wechselgeld herausgeben
 *  zu können. Beachten Sie auch, dass nach der Kundeneingabe
 *  (eingeworfeneMuenzenInCent) neue Münzen im Automaten sind, die gleich
 *  wieder als vorhandeneMuenzenInCent verwendet werden können.
 *
 *  Autor:
 *      Ulrich Berntien 2018-09-01
 *
 *  Sprache:
 *      Kotlin 1.2.51
 */


/**
 * Einfache Fehlermeldung bei Eingabefehler.
 * @param message Fehlermeldung.
 */
class UserException(override val message: String?) : RuntimeException(message)


/**
 * Bestimmt die Münzen für die Geldrückgabe.
 *  @param vorhandeneMuenzenInCent Münzen (in Cent), die bereits vor der
 *  Wäsche im Waschautomat waren.
 *  @param waschPreisInCent Preis (in Cent) für die Wäsche.
 *  @param eingeworfeneMuenzenInCent Münzen (in Cent), die eingeworfen wurden.
 *  @return Münzen (in Cent), die als Rückgeld ausgegeben werden sollen.
 */
fun wechselgeldInCent(vorhandeneMuenzenInCent: IntArray,
                      waschPreisInCent: Int,
                      eingeworfeneMuenzenInCent: IntArray): IntArray {
    val eingeworfen = eingeworfeneMuenzenInCent.sum()
    val rueckgeld = eingeworfen - waschPreisInCent
    if (rueckgeld < 0)
        throw UserException("Zu wenige Münzen eingeworfen")
    // Alle Münzen stehen für die Rückgabe zur Verfügung,
    // auch die gerade eingeworfenen Münzen.
    val alleMuenzen = vorhandeneMuenzenInCent + eingeworfeneMuenzenInCent
    var rueckgabe = intArrayOf()
    var rest = rueckgeld
    // Durch die Sortierung der Münzen werden zuerst die großen Münzen
    // verwendet, so die Anzahl der ausgegebenen Münzen reduziert.
    for (muenze in alleMuenzen.sortedDescending())
        if (muenze <= rest) {
            rueckgabe += muenze
            rest -= muenze
        }
    if (rest > 0)
        throw UserException("Zu wenige Münzen für Rückgabe")
    assert(rueckgabe.sum() == rueckgeld)
    return rueckgabe
}


/**
 * Testfall automatisch ausführen.
 * @param argv Parameter werden ignoriert.
 */
fun main(argv: Array<String>) {
    val vorhanden = intArrayOf(100, 200, 500, 500, 500)
    val preis = 400
    val eingeworfen = intArrayOf(100, 200, 500)
    println("""
        |Vorhandene Münzen:  ${vorhanden.contentToString()}
        |Waschpreis:         $preis
        |Eingeworfene Münzen:${eingeworfen.contentToString()}
        |Wechselgeld Münzen: ${wechselgeldInCent(vorhanden, preis, eingeworfen).contentToString()}
        """.trimIndent())
}
