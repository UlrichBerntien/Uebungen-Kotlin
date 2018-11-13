/**
 *  Programmieraufgabe:
 *
 *  Sekunden-Umwandlung
 *  https://www.programmieraufgaben.ch/aufgabe/sekunden-umwandlung/sxmwsks3
 *
 *  Schreiben Sie ein Programm, das eine Anzahl Sekunden in die Form
 *  "h:mm:ss" (h = Stunden, mm = Minuten und ss = Sekunden) bringt.
 *  Zum Beispiel wird die Zahl 3674 in die Zeichenkette "1:01:14" umgewandelt.
 *
 *  Autor:
 *      Ulrich Berntien 2018-08-26
 *
 *  Sprache:
 *      Kotlin 1.2.51
 */


/**
 *  Konvertiert eine Angabe "h:mm:ss" in the Anzahl der Sekunden.
 *  @param hms Zeit im Format "stunde:minuten:sekunden"
 *  @return Zeit in Sekunden
 */
fun hms2seconds(hms: String): Int {
    val pattern = Regex("""(\d+):(\d+):(\d+)""")
    val match = pattern.matchEntire(hms)
            ?: throw IllegalArgumentException("invlid hms format")
    val (hour, minute, second) = match.groups
            .drop(1)
            .map { it -> it!!.value.toInt() }
    if (minute > 59 || second > 59)
        throw IllegalArgumentException("invlid hms format")
    return ((hour * 60) + minute) * 60 + second
}


/**
 * Konvertiert Anzahl Sekunden in h:mm:ss Format.
 * @param secondsArg Anzahl der Sekunden
 * @return Die Sekunden umgerechnet ins Format "h:mm_ss".
 */
fun seconds2hms(secondsArg: Int): String {
    val sign = if (secondsArg < 0) "-" else ""
    val seconds = if (secondsArg < 0) -secondsArg else secondsArg
    val secondsPart = seconds % 60
    val minutes = seconds / 60
    val minutesPart = minutes % 60
    val hoursPart = minutes / 60
    return "$sign$hoursPart:${"%02d".format(minutesPart)}:${"%02d".format(secondsPart)}"
}

/**
 * Test der beiden Funktionen.
 * @param argv wird ignoriert
 */
fun main(argv: Array<String>) {
    for (seconds in intArrayOf(3674, 367400, -3674))
        println("seconds = $seconds -> hms = ${seconds2hms(seconds)}")
    for (hms in arrayOf("1:01:14", "0:00:10", "0:01:00", "1:00:00", "100:00:00"))
        println("hms = $hms -> seconds = ${hms2seconds(hms)}")
}
