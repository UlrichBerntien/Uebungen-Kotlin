/*
 *  Programmieraufgabe:
 *
 *  Binärcode Reader (zu Zeichen umwandeln)
 *  https://www.programmieraufgaben.ch/aufgabe/binaercode-reader-zu-zeichen-umwandeln/h8nkecsd
 *
 *  Schreiben Sie Programm, dass einen beliebigen Binärcode in lesbare Zeichen
 *  umwandelt!
 *  Zu Zeichen zählen alle in Ihrem Code vorkommenden Buchstaben, Sonderzeichen
 *  und Ziffern (z. B. "a", ".", "7", "?" oder "!").
 *  Das folgende Beispiel ist im ASCII codiert (American Standard Code for
 *  Information Interchange):
 *  Was bedeutet dieser Binärcode: "010010000110000101101100011011000110111100100001"?
 *  Sie dürfen durchaus eine andere Codierung (wie UTF-8 oder EBCDIC) verwenden. 
 *
 *  Autor:
 *      Ulrich Berntien 2018-08-29
 *
 *  Sprache:
 *      Kotlin 1.2.51
 */


/**
 *  Umwandlung eines Binärcodes in einen String.
 *  Verwendet den US-ASCII Zeichensatz.
 *  @param code Der Binärcode als String.
 *  @return Die Zeichen im Code als String.
 */
fun reader(code: String): String {
    // Ein Byte besteht aus maximal 8 Bits. Sind die Bytes durch
    // Leerzeichen getrennt, dann sind weniger als 8 Bit möglich.
    val bytePattern = Regex("""[01]{1,8}\s*""")
    return bytePattern
            .findAll(code)
            .map { it.value.toByte(2) }
            .toList()
            .toByteArray()
            .toString(Charsets.US_ASCII)
}


/**
 *  Testfall aus der Aufgabe ausführen.
 *  @param args wird ignoriert.
 */
fun main(vararg args: String) {
    val code = "010010000110000101101100011011000110111100100001"
    val result = reader(code)
    assert(result == "Hallo!")
    println("$code = $result")
}
