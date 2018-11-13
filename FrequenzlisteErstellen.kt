/**
 *  Programmieraufgabe:
 *
 *  Frequenzliste erstellen
 *  https://www.programmieraufgaben.ch/aufgabe/frequenzliste-erstellen/xmzhj65g
 *
 *  Schreiben Sie ein Programm, welches eine Textdatei (txt) einliest und anhand
 *  deren Inhalt eine Frequenzliste erstellt.
 *  Unter einer Frequenzliste wird hier eine Liste der Häufigkeiten der einzelnen
 *  Elemente (im folgenden der Wörter) verstanden.
 *  Der User soll den Pfad der Datei, sowie das Zeichen an dem getrennt werden
 *  soll (Default Leerzeichen) angeben können.
 *  Das Programm soll dann die Frequenzliste, also das Ergebnis absteigend
 *  sortiert nach Häufigkeit ausgeben.
 *
 *  Autor:
 *      Ulrich Berntien 2018-09-18
 *
 *  Sprache:
 *      Kotlin 1.2.51
 */


fun generateFrequenzList(filename: String, wordSeperatorPattern: String) {
    java.io.File(filename).forEachLine { println(it) }
}


/**
 *  Frequenzliste erstellen und auf stdout ausgeben.
 *  @param args 1) Dateiname
 *  2) optional Pattern für die Trennung der Wörter
 */
fun main(args: Array<String>) {
    if (args.size !in 1..2) {
        println("Argumente: Dateiname [Worttrennerpattern]")
    } else {
        val filename = args[0]
        val wordSeperatorPattern = if (args.size == 2) args[1] else """\s+"""
        try {
            generateFrequenzList(filename, wordSeperatorPattern)
        } catch (ex: java.io.IOException) {
            println("Dateifehler: ${ex.message}")
        }
    }
}
