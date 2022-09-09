package me.kofesst.spring.souvenirstore

import me.kofesst.spring.souvenirstore.util.yearsUntil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.text.SimpleDateFormat
import java.util.stream.Stream

class DateUtilsTest {
    companion object {
        private val sdf = SimpleDateFormat("dd.MM.yyyy")

        @JvmStatic
        fun yearsUntilParameters(): Stream<Arguments> = Stream.of(
            Arguments.of("10.10.2000", "11.11.2020", 20),
            Arguments.of("10.10.2000", "10.10.2020", 20),
            Arguments.of("10.10.2000", "10.09.2020", 19),
            Arguments.of("10.10.2000", "09.10.2020", 19),
            Arguments.of("10.10.2000", "09.09.2020", 19),
        )
    }

    @ParameterizedTest
    @MethodSource("yearsUntilParameters")
    fun `untilYears works as expected`(from: String, to: String, yearsExpected: Int) {
        val yearsActual = getDate(from) yearsUntil getDate(to)
        assertEquals(yearsExpected, yearsActual)
    }

    private fun getDate(format: String) = sdf.parse(format)!!
}