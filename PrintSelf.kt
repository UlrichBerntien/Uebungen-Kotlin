
/**
 *  Programmieraufgabe:
 *
 *  Erstelle ein Programm, dass seinen eigenen Source-Code ausgibt.
 *  Das Programm darf keine Daten einlesen. Der Source-Code muss exakt
 *  ausgegeben werden, so dass ein Filecompare keinen Unterschied findet.
 *
 *  Autor:
 *      Ulrich Berntien 2018-08-29
 *
 *  Sprache:
 *      Kotlin 1.2.51
 */

/**
 *  Template string for this source file.
 */
const val str = """
/**
 *  Programmieraufgabe:
 *
 *  Erstelle ein Programm, dass seinen eigenen Source-Code ausgibt.
 *  Das Programm darf keine Daten einlesen. Der Source-Code muss exakt
 *  ausgegeben werden, so dass ein Filecompare keinen Unterschied findet.
 *
 *  Autor:
 *      Ulrich Berntien 2018-08-29
 *
 *  Sprache:
 *      Kotlin 1.2.51
 */

/**
 *  Template string for this source file.
 */
const val str = ""%c%s""%c

/**
 *  Prints this source file.
 */
fun main(argv: Array<String>) {
    println(str.format('"', str, '"'))
}
"""

/**
 *  Prints this source file.
 */
fun main(argv: Array<String>) {
    println(str.format('"', str, '"'))
}

