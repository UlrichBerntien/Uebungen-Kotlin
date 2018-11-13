/**
 *  Sudoku-Rätsel lösen.
 *
 *  Autor:
 *      Ulrich Berntien 2018-08-23
 *
 *  Sprache:
 *      Kotlin 1.2.51
 */

val numberRange = 1..9

/**
 * Errors of the Sudoku classes.
 * @param message Error message.
 */
class SudokuException(message: String) : RuntimeException(message)

/**
 * The options for a cell in the Sudoku field.
 * Stores the possible numbers a cell in the Suduko field cound contain.
 * The options are limited by the other numbers in the group.
 */
class Options {

    private companion object {

        /**
         * All bits set for all numbers 1..9.
         */
        const val allNumbers = 0b111_111_111

        /**
         * Array used to find the bit value for a number very fast.
         * Index of the array is the number (1..9). For number 0 the dummy
         * value 0 is included. The values stored in the array are the
         * according value of the bits.
         */
        val number2option = IntArray(10, ::number2bit)

        /**
         * Array used to find fast the number of options.
         * Index of the array are the options. Values are the number of options.
         * The number of options is equal to the number of 1 bits in the option.
         */
        val options2count = IntArray(allNumbers + 1, ::optionCounter)

        /**
         * Calculates the bit value for a number.
         * @param n Calculate the bit value for this number (including 0)
         * @return The value of the bit representing the number.
         */
        fun number2bit(n: Int): Int {
            assert(n in 0..9)
            return when (n) {
                0 -> 0
                1 -> 1
                else -> 2 * number2bit(n - 1)
            }
        }

        /**
         * Calculates the number of options.
         * @param options Calculates number of options stored in this value.
         * @return The number of options.
         */
        fun optionCounter(options: Int): Int {
            assert(options in 0..allNumbers)
            // Count the number of 1 bits in the value.
            var n = 0
            var bit = 1
            while (bit <= options) {
                if (options and bit > 0) n += 1
                bit *= 2
            }
            assert(n in 0..9)
            return n
        }

    }

    /**
     * The options.
     * Each bit represents one possible number.
     * First bit -> 1, second bit -> 2, third bit -> 3, ...
     */
    private var options: Int = allNumbers

    /**
     * Adds a possible number.
     * @param number adds this number.
     */
    fun add(number: Int) {
        assert(number in numberRange)
        options = options or number2option[number]
        assert(contains(number))
    }

    /**
     * Removes a possible number.
     * @param number Removes this number from the option.
     */
    fun remove(number: Int) {
        assert(number in numberRange)
        val newOptions = options and number2option[number].inv()
        if (newOptions < 1)
            throw SudokuException("removeOption downto empty option")
        options = newOptions
        assert(!contains(number))
    }

    /**
     * Copies the options.
     * @param source Copies from this object.
     */
    fun copy(source: Options) {
        options = source.options
    }

    /**
     * Counts the number of options.
     */
    fun count() = options2count[options]

    /**
     * Gets the first option.
     * There is at least one option because the removeOption method keeps at least
     * one option.
     */
    fun first() = numberRange.first { options and number2option[it] > 0 }

    /**
     * Checks if the number is an option.
     * @param number Checks this number.
     * @return True if and only if the number is an option.
     */
    fun contains(number: Int): Boolean {
        assert(number in numberRange)
        return options and number2option[number] > 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return (other is Options) && (options == other.options)
    }

    override fun hashCode(): Int {
        return options
    }

    override fun toString(): String {
        return numberRange.filter(this::contains).joinToString { it.toString() }
    }
}


/**
 * One cell in the Sudoku field.
 * The cell could contain a number or is empty.
 * The cell has number options. The options will be updated by the Sudoku class.
 */
class Cell {

    /**
     * The number in the cell, could be null.
     */
    var number: Int? = null
        set(value) {
            assert(value == null || value in numberRange)
            if (value != null && !options.contains(value))
                throw SudokuException("value is not an option")
            field = value
        }

    /**
     * The options for this cell.
     */
    private val options = Options()

    /**
     * Checks if the cell ist empty e.g. contains no number.
     */
    fun isEmpty() = number == null

    /**
     * Counts the number options for this cell.
     */
    fun optionsCount() = options.count()

    /**
     * The first option for this cell.
     */
    fun optionsFirst() = options.first()

    /**
     * Checks if the number is an option for this cell.
     * @param num Checks this number.
     * @return True if and only if the number is an option.
     */
    fun containsOption(num: Int): Boolean {
        assert(num in numberRange)
        return options.contains(num)
    }

    /**
     * Removes an option from this cell.
     */
    fun removeOption(num: Int) {
        assert(num in numberRange)
        if (num == number)
            throw SudokuException("can not remove active option")
        if (optionsCount() == 1 && options.contains(num))
            throw SudokuException("can not remove last option")
        options.remove(num)
    }

    /**
     * Sets a number in the cell by a character code.
     * @param c The number coded as charachter. "." means no number givem, empty cell.
     */
    fun set(c: Char) {
        assert(c in ".123456789")
        number = when (c) {
            '.' -> null
            in '1'..'9' -> c.toInt() - '0'.toInt()
            else -> throw SudokuException("invalid number character")
        }
    }

    fun copy(source: Cell) {
        // first copy the options because the number setter checks the options
        options.copy(source.options)
        number = source.number
    }

    override fun toString(): String {
        val tmp = number
        assert(tmp == null || tmp in numberRange)
        return tmp?.toString() ?: "."
    }

}

/**
 * A Sudoku field.
 */
class Sudoku() {

    companion object {

        /**
         * Range of X coordinate.
         */
        val xRange = 0 until 9

        /**
         * Range of Y coordinate.
         */
        val yRange = 0 until 9

        /**
         * Range of index in the flat array.
         */
        val indexRange = 0 until 9 * 9

        /**
         * Calculates the flat array index from the coordinates.
         */
        fun xy2index(x: Int, y: Int): Int {
            assert(x in xRange)
            assert(y in yRange)
            return x + y * 9
        }

        /**
         * Calculates the X and Y coordinate from the index in the flat arry.
         */
        fun index2xy(index: Int): Pair<Int, Int> {
            assert(index in indexRange)
            return Pair(index % 9, index / 9)
        }

        /**
         * Generates an array with all indices to neighbour cells.
         * @param index The index of the cell for which the neighbours are calculated
         * @return An array containing the indices of all neighbour cells.
         */
        private fun allConnections(index: Int): IntArray {
            assert(index in indexRange)
            val (x, y) = index2xy(index)
            val result = IntArray(8 + 6 + 6)
            // (x0, y0) the coordinate of the first cell in the square containing (x,y)
            val x0 = (x / 3) * 3
            val y0 = (y / 3) * 3
            var destination = 0
            // Cells in the square without the row and the column
            for (xi in (x0..x0 + 2) - x)
                for (yi in (y0..y0 + 2) - y)
                    if (xi != x || yi != y)
                        result[destination++] = xy2index(xi, yi)
            // Cells in the row
            for (xi in xRange - x)
                result[destination++] = xy2index(xi, y)
            // Cells in the column
            for (yi in yRange - y)
                result[destination++] = xy2index(x, yi)
            assert(destination == result.indices.last + 1)
            return result
        }

        /**
         * Array contains arrays of all neighbours.
         * The index in the array is also the index in the array of cells.
         * The elements of the array are arrays. This arrays contains the indices
         * of all neighbour cells.
         */
        private val connections = Array(indexRange.last + 1, ::allConnections)
    }

    /**
     * Array of all cells in the Sudoku.
     * The flat index number coresponds with the (x,y) index by the methods
     * index2xy and xy2index.
     */
    private val cells = Array(9 * 9) { Cell() }

    /**
     * Construts a Sudoku problem by a text representation.
     * The text contains the content of each cell by a single character.
     * The character is the number 1..9 in the cell or "." if the cell is empty.
     * The text can be structured in rows separated by newline characters.
     * @param content The Sudoku problem as text.
     */
    constructor (content: String) : this() {
        val str = content.filter { it in ".123456789" }
        if (str.length != cells.size)
            throw SudokuException("invalid Sudoku description string")
        for (index in cells.indices)
            set(index, str[index])
    }

    fun get(x: Int, y: Int) = cells[xy2index(x, y)].number

    fun set(index: Int, value: Char) {
        assert(index in indexRange)
        cells[index].set(value)
        val number = cells[index].number
        if (number != null)
            for (other in connections[index])
                cells[other].removeOption(number)
    }

    fun set(index: Int, value: Int) {
        assert(index in indexRange)
        assert(value in numberRange)
        cells[index].number = value
        for (other in connections[index])
            cells[other].removeOption(value)
    }

    fun set(x: Int, y: Int, value: Int) {
        assert(x in xRange)
        assert(y in yRange)
        assert(value in numberRange)
        set(xy2index(x, y), value)
    }

    fun optionsCount(index: Int) = cells[index].optionsCount()

    fun firstOption(index: Int) = cells[index].optionsFirst()

    fun containsOption(index: Int, number: Int): Boolean {
        assert(index in indexRange)
        assert(number in numberRange)
        return cells[index].containsOption(number)
    }

    fun isEmpty(index: Int) = cells[index].isEmpty()

    fun isOpen() = cells.any { it.isEmpty() }

    override fun toString() =
            yRange.joinToString(separator = "\n")
            {
                (xy2index(0, it)..xy2index(8, it)).joinToString(separator = "") { cells[it].toString() }
            }

    fun copy(source: Sudoku) {
        for (index in indexRange)
            cells[index].copy(source.cells[index])
    }

    fun copyOf(): Sudoku {
        val result = Sudoku()
        result.copy(this)
        return result
    }
}


object SudokuSolver {

    private fun setOpenSingles(puzzle: Sudoku): Boolean {
        var foundAtLeastOne = false
        var foundInLastLoop = true
        while (foundInLastLoop) {
            foundInLastLoop = false
            for (index in Sudoku.indexRange)
                if (puzzle.isEmpty(index) && puzzle.optionsCount(index) == 1) {
                    foundInLastLoop = true
                    foundAtLeastOne = true
                    puzzle.set(index, puzzle.firstOption(index))
                }
        }
        return foundAtLeastOne
    }

    private fun tryAndError(puzzle: Sudoku) {
        assert(puzzle.isOpen())
        val workIndex = Sudoku.indexRange
                .filter(puzzle::isEmpty)
                .minBy(puzzle::optionsCount)
                ?: throw SudokuException("open sudoku without any option")
        assert(puzzle.isEmpty(workIndex))
        assert(puzzle.optionsCount(workIndex) > 0)
        val puzzleBackup = puzzle.copyOf()
        for (number in numberRange.filter { puzzle.containsOption(workIndex, it) })
            try {
                // test
                puzzle.set(workIndex, number)
                solve(puzzle)
                // found a possible solution
                break
            } catch (ex: SudokuException) {
                puzzle.copy(puzzleBackup)
            }
    }

    fun solve(puzzle: Sudoku) {
        while (setOpenSingles(puzzle)) {
        }
        if (puzzle.isOpen()) tryAndError(puzzle)
    }

}

fun main(argv: Array<String>) {
    val tests = arrayOf(
            """
        .....4.2.
        42.37.86.
        .675283..
        5.8...67.
        .71.5.24.
        .42...5.1
        ..916543.
        .34.89.56
        .5.7.....
        """, """
        ....1.8..
        52...3.7.
        618.72.4.
        471......
        ...3.7.1.
        8........
        ....3.426
        2.7...39.
        36.12..8.
        """, """
        ...2.1...
        ..9..8.6.
        ....7.84.
        2..18..35
        1.......6
        58..64..9
        .15.9....
        .7.5..1..
        ...8.3...
        """, """
        3.....5.2
        ..1......
        9.25.718.
        ..36742..
        ...9.5...
        ..98213..
        .467.98.1
        ......9..
        7.5.....4
        """, """
        ....5.1..
        .267..3.5
        ......762
        419..3.7.
        ......9.3
        3..6.78..
        .3.86.4..
        6.1...5..
        .473.5...
        """, """
        ......3..
        5.2.4..17
        ...71.26.
        ....536..
        .1.....2.
        ..682....
        .43.75...
        15..3.4.9
        ..7......
        """, """
        39.6....5
        ....9...7
        ..1..86..
        ..2.5...6
        .3.8.7.9.
        1...2.3..
        ..61..9..
        2...6....
        8....2.63
        """)
    for (test in tests) {
        val sudoku = Sudoku(test)
        println("------------------------")
        println(sudoku)
        SudokuSolver.solve(sudoku)
        println("    solved: ")
        println(sudoku)
        //TODO
    }
}
