/**
 *  Programmieraufgabe:
 *
 *  Bowlingkugel (Simulationen)
 *  https://www.programmieraufgaben.ch/aufgabe/bowlingkugel/sor45wf7
 *
 *  Die Aufgabe besteht darin, in einem 100-stöckigen Haus das Stockwerk zu
 *  bestimmen, aus welchem eine Bowlingkugel den freien Fall gerade noch
 *  übersteht. Sie haben für die Versuche zwei identische Kugeln zur Verfügung,
 *  welche beide in Bruch gehen dürfen. Nachdem die zweite Kugel defekt ist,
 *  müssen Sie das Stockwerk angeben können. Ziel ist es, möglichst wenige
 *  Fallversuche durchführen zu müssen.
 *  Je nach Material der Kugeln gehen diese bereits in einem unteren Stockwerk
 *  oder im extremen Fall gar nicht kaputt.
 *
 *  Schreiben Sie ein Programm, das mit möglichst wenigen Versuchen das gesuchte
 *  Stockwerk ermittelt. Legen Sie zu Beginn des Programms das Stockwerk, bei
 *  welchem die beiden Kugeln zerstört werden, per Zufallszahl zwischen 1 und
 *  100 fest.
 *
 *  Vorüberlegunng:
 *
 *  Es sind nur maximal 100 Stockwerke. Der Algorithmus kann daher
 *  für jedes Stockwerk ausprobiert werden, wenn nur deterministische
 *  Algorithmen verwendet werden.
 *  Es soll ein Algorithmus gefunden werden, der möglichst wenige Würfe
 *  benötigt. Das kann auf die maximale Anzahl von Würfe oder die
 *  mittlere Anzahl von Würfe bezogen sein. Bei den Versuchen wurde
 *  beides zusammen reduziert.
 *  Die minimale Anzahl kann auch optimiert werden. Das Minimum ist 1
 *  und wird von dem einfachen schrittweisen Ausprobieren erreicht,
 *  hier haben alle anderen Algotithmen ein Minimum von 2.
 *
 *  Autor:
 *      Ulrich Berntien 2018-08-09
 *
 *  Sprache:
 *      Kotlin 1.2.51
 */

import java.lang.Integer.max
import java.lang.Math.sqrt
import kotlin.reflect.jvm.isAccessible

/**
 *  Nummern der möglichen Etagen.
 *  Die Etagen tragen die Nummern 0, 1, .. 99.
 */
val LEVELS: IntRange = 0..99

/**
 * Nummer der möglichen Etagen bei denen die Kugel zerstört wird.
 * Die Bowlingkugeln werden zerstört bei einem Fall aus Stockwerk. Es
 * gibt die Möglichkeit, dass die Kugel den Fall aus dem höchsten
 * Stockwerk (Nummer 99) übersteht, dafür die Nummer 100
 */
val DESTROY_LEVELS: IntRange = 1..100

/**
 *  Anzahl der Bowlingkugeln für einen Versuch.
 */
const val NUMBER_BALLS: Int = 2

/**
 *  Ein Objekt der Klasse Balls ist das Modell für die beiden Bowlingkugeln.
 *  Die Funktion throw wift eine Kugel und gibt das Erebniss zurück.
 *  Automatisch werden die Würfe gezählt und auf die nächste Kugel gewechselt.
 *  @param destroyLevel Ab dieser Etagen-Nummer werden die Kugel zerstört.
 */
class Balls(private val destroyLevel: Int) {

    init {
        assert(destroyLevel in DESTROY_LEVELS)
    }

    /**
     *  Anzahl der noch verfügbaren Kugeln.
     */
    var numberBalls: Int = NUMBER_BALLS
        private set

    /**
     *  Anzahl der durchgeführten Fallversuche.
     */
    var numberThrows: Int = 0
        private set

    /**
     *  Mögliche Ergebnisse eines Fallversuchs.
     */
    enum class ThrowResult {
        /** Kugel ist unverändert in Ordnung */
        Ok,
        /** Kugel wurde zerstört */
        Destroyed,
        /** Keine Kugel mehr vorhanden */
        NoBall
    }

    /**
     *  Ein Kugel werfen.
     *  Wurde vorher bei einem Wurf eine Kugel zerstört, wird die
     *  nächste Kugel verwendet.
     *  Sind alle Kugel zerstört, wird immer False ausgegeben.
     *  @param startLevel: Aus dieser Höhe (Etagennummer) werfen.
     *  @return True. falls die Kugel überstanden hat. False, wenn die Kugel zerstört ist.
     */
    fun throwBall(startLevel: Int): ThrowResult {
        assert(startLevel in LEVELS)
        numberThrows += 1
        return when {
            numberBalls < 1
            -> ThrowResult.NoBall
            startLevel < destroyLevel
            -> ThrowResult.Ok
            else
            -> {
                numberBalls -= 1; ThrowResult.Destroyed
            }
        }
    }

}


/**
 *  Berechnet Anzahl der Würfe für die gegebene Höhe.
 *  @param algorithm Diese Funktion ist der Algotithmus.
 *  @param destroyLevel: Für diese Höhe soll der Algo verwendet werden.
 *  @return Anzahl der Würfe, die der Algorithmus benötigt hat.
 */
fun calculateNumberThrows(algorithm: (Balls) -> Int, destroyLevel: Int): Int {
    assert(destroyLevel in DESTROY_LEVELS)
    val balls = Balls(destroyLevel)
    val level = algorithm(balls)
    if (level != destroyLevel)
        throw Exception("Algorithm fails at level $destroyLevel, returns $level")
    return balls.numberThrows
}


/**
 *  Berechnet Anzahl der Würfe für jeden möglichen Destroy-Level.
 *  @param algorithm Diese Funktion ist der zu testende Algotithmus.
 *  @return Anzahl der Würfe, die der Algorithmus benötigt hat für jeden Destroy-Level.
 */
fun calculateAllNumberThrows(algorithm: (Balls) -> Int): IntArray =
        DESTROY_LEVELS
                .map { calculateNumberThrows(algorithm, it) }
                .toIntArray()


/**
 *  Arithmetischer Mittelwert.
 */
fun IntArray.mean(): Double =
        this.fold(0.0) { accu, it -> accu + it } / this.size


/**
 *  Einfacher Algorithmus.
 *  Immer ein Stockwerk höher, bis die Kugel zerstört ist.
 *  Das Stockwerk 0 muss nicht ausprobiert werden, weil jede
 *  Kugel den Fall aus dem Erdgeschoss übersteht.
 *  @param balls Die Kugeln für die Fallversuche.
 *  @return Das Stockwerk, bei dem die Kugel zerstört wird.
 */
fun algorithmSingleStep(balls: Balls): Int {
    for (testLevel in LEVELS - 0)
        if (balls.throwBall(testLevel) == Balls.ThrowResult.Destroyed)
            return testLevel
    return LEVELS.last + 1
}


/**
 *  'Matrix' oder 'Hacker' Algorithmus.
 *  Der Algo kennt die Simulation bzw. der Algo umgeht die Regeln.
 *  Eine Methode, die in der Praxis durchaus verwendet wird (siehe
 *  Manipulationen von verschiedene Testverfahren durch verschiedenen
 *  Herstellern.)
 *  @param balls Die Kugeln für die Fallversuche.
 *  @return Das Stockwerk, bei dem die Kugel zerstört wird.
 */
fun algorithmusMatrix(balls: Balls): Int {
    // Access private value via JVM reflection methods.
    val destroyLevelGetter = balls::class.members.first { it.name == "destroyLevel" }
    val originalAccessibleState = destroyLevelGetter.isAccessible
    destroyLevelGetter.isAccessible = true
    val result = destroyLevelGetter.call(balls) as Int
    destroyLevelGetter.isAccessible = originalAccessibleState
    return result
}


/**
 *  Binäre-Suche mit der ersten Kugel, dann mit Einzelschritten suchen.
 *  @param balls Die Kugeln für die Fallversuche.
 *  @return Das Stockwerk, bei dem die Kugel zerstört wird.
 */
fun algorithmBinary(balls: Balls): Int {
    var lastOkLevel = 0
    // Mit der ersten Kugel Binäre Suche
    var testLevel = (LEVELS.first + LEVELS.last) / 2
    while (testLevel in LEVELS && balls.throwBall(testLevel) == Balls.ThrowResult.Ok) {
        lastOkLevel = testLevel
        testLevel = max(testLevel + 1, (testLevel + LEVELS.last) / 2)
    }
    if (balls.numberBalls == 2) {
        // Die while Schleife wurde beendet bevor die Kugel zerstört wurde.
        return LEVELS.last + 1
    }
    // Auf dem testLevel wurde die Kugel zerstört
    val failLevel = testLevel
    testLevel = lastOkLevel + 1
    // Mit der zweiten Kugel nur noch die Stockwerk einzeln.
    while (testLevel < failLevel && balls.throwBall(testLevel) == Balls.ThrowResult.Ok)
        testLevel += 1
    return testLevel
}


/**
 *  Binäre-Suche mit dem Teilverhältnis aus dem Goldnen Schnitt
 *  mit der ersten Kugel, dann mit Einzelschritten suchen.
 *  @param balls Die Kugeln für die Fallversuche.
 *  @return Das Stockwerk, bei dem die Kugel zerstört wird.
 */
fun algorithmGold(balls: Balls): Int {
    val goldenRation = 0.38196601125
    var lastOkLevel = 0
    var testLevel = ((LEVELS.first + LEVELS.last) * goldenRation).toInt()
    while (testLevel in LEVELS && balls.throwBall(testLevel) == Balls.ThrowResult.Ok) {
        lastOkLevel = testLevel
        testLevel += max(1, ((LEVELS.last - testLevel) * goldenRation).toInt())
    }
    if (balls.numberBalls == 2) {
        // Die while Schleife wurde beendet bevor die Kugel zerstört wurde.
        return LEVELS.last + 1
    }
    // Auf dem testLevel wurde die Kugel zerstört
    val failLevel = testLevel
    testLevel = lastOkLevel + 1
    // Mit der zweiten Kugel nur noch die Stockwerk einzeln.
    while (testLevel < failLevel && balls.throwBall(testLevel) == Balls.ThrowResult.Ok)
        testLevel += 1
    return testLevel
}


/**
 *  Binäre-Suche mit optimiertem Teilverhältnis mit der ersten Kugel,
 *  dann mit Einzelschritten suchen.
 *  Teilverhältnis -> Mittlere Anzahl: 0.10 -> 13.43 ; 0.19 -> 10.94 ;
 *  0.20 -> 10.94 ; 0.21 -> 10.96 ; 0.30 -> 12.30
 *  @param balls Die Kugeln für die Fallversuche.
 *  @return Das Stockwerk, bei dem die Kugel zerstört wird.
 */
fun algorithmRation(balls: Balls): Int {
    val ration = 0.20
    var lastOkLevel = 0
    var testLevel = ((LEVELS.first + LEVELS.last) * ration).toInt()
    while (testLevel in LEVELS && balls.throwBall(testLevel) == Balls.ThrowResult.Ok) {
        lastOkLevel = testLevel
        testLevel += max(1, ((LEVELS.last - testLevel) * ration).toInt())
    }
    if (balls.numberBalls == 2) {
        // Die while Schleife wurde beendet bevor die Kugel zerstört wurde.
        return LEVELS.last + 1
    }
    // Auf dem testLevel wurde die Kugel zerstört
    val failLevel = testLevel
    testLevel = lastOkLevel + 1
    // Mit der zweiten Kugel nur noch die Stockwerk einzeln.
    while (testLevel < failLevel && balls.throwBall(testLevel) == Balls.ThrowResult.Ok)
        testLevel += 1
    return testLevel
}


/**
 *  Optimaler Algorithmus aus erster Musterlösung kopiert.
 *  Die Summe aus Würfe mit der ersten Kugel und maximal folgende Würfe
 *  mit der zweiten Kugel wird konstant gehalten. Entsprechend wird die
 *  Schrittweite (step) bei jedem Wurh der ersten Kugel um 1 reduziert.
 *  Der Startwert ergibt sich aus der Gaußschen Summenformel, aufgelöst
 *  nach der Anzahl der Werte.
 *  @param balls Die Kugeln für die Fallversuche.
 *  @return Das Stockwerk, bei dem die Kugel zerstört wird.
 */
fun algorithmOpt(balls: Balls): Int {
    var step = sqrt(2.0 * (LEVELS.last - 2) + 1).toInt()
    var lastOkLevel = 0
    var testLevel = step

    while (testLevel in LEVELS && balls.throwBall(testLevel) == Balls.ThrowResult.Ok) {
        lastOkLevel = testLevel
        testLevel += step
        step = max(1, step - 1)
    }

    val failLevel =
            if (balls.numberBalls == 2) {
                // Die while Schleife wurde beendet bevor die Kugel zerstört wurde.
                LEVELS.last + 1
            } else {
                // Auf dem test_level wurde die Kugel zerstört
                testLevel
            }
    testLevel = lastOkLevel + 1
    // Mit der zweiten Kugel nur noch die Stockwerk einzeln.
    while (testLevel < failLevel && balls.throwBall(testLevel) == Balls.ThrowResult.Ok)
        testLevel += 1
    return testLevel
}


/**
 *  Alle Algorithmen ausführen und Kennwerte berechnen.
 *  @param args wird ignoriert.
 */
fun main(args: Array<String>) {
    for (algo in arrayOf(::algorithmSingleStep, ::algorithmusMatrix,
            ::algorithmBinary, ::algorithmGold, ::algorithmRation, ::algorithmOpt)) {
        println("Teste Funktion ${algo.name}")
        val numberThrows = calculateAllNumberThrows(algo)
        println("""
            .. ${numberThrows.contentToString()}
            .. maximale Anzahl ${numberThrows.max()}
            .. minimale Anzahl ${numberThrows.min()}
            .. mittlere Anzahl ${"%.2f".format(numberThrows.mean())}
        """.trimIndent())
    }
}
