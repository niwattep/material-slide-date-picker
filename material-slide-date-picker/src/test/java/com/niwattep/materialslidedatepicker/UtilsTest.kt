package com.niwattep.materialslidedatepicker

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun isLeapYear_ShouldReturn_True() {
        for (i in UtilsTestUtils.getLeapYearsFrom1970To2500()) {
            assertEquals(true, isLeapYear(i))
        }
    }

    @Test
    fun isLeapYear_ShouldReturn_False() {
        for (i in UtilsTestUtils.getNonLeapYearFrom1970To2500()) {
            assertEquals(false, isLeapYear(i))
        }
    }

    @Test
    fun getMonthMaxDay_ShouldReturn31() {
        val monthsWith31Days = arrayListOf<Int>(1, 3, 5, 7, 8, 10, 12)
        for (month in monthsWith31Days) {
            for (year in UtilsTestUtils.getYearsFrom1970To2500()) {
                assertEquals(31, getMonthMaxDay(month, year))
            }
        }
    }

    @Test
    fun getMonthMaxDay_ShouldReturn30() {
        val monthsWith31Days = arrayListOf<Int>(4, 6, 9, 11)
        for (month in monthsWith31Days) {
            for (year in UtilsTestUtils.getYearsFrom1970To2500()) {
                assertEquals(30, getMonthMaxDay(month, year))
            }
        }
    }

    @Test
    fun getMonthMaxDay_ShouldReturn29() {

        val leapYears = UtilsTestUtils.getLeapYearsFrom1970To2500()

        for (i in leapYears) {
            assertEquals(29, getMonthMaxDay(2, i))
        }
    }

    @Test
    fun getMonthMaxDay_ShouldReturn28() {

        val nonLeapYear = UtilsTestUtils.getNonLeapYearFrom1970To2500()

        for (i in nonLeapYear) {
            assertEquals(28, getMonthMaxDay(2, i))
        }
    }

    @Test
    fun getAvailableMonths_upperBound() {
        val upperBound = UtilsTestUtils.getCalendar(15, 7, 2019)
        val lowerBound = UtilsTestUtils.getCalendar(1, 1, 1970)

        val currentYear = 2019

        val expectedMonths = arrayListOf(1, 2, 3, 4, 5, 6, 7)

        assertEquals(expectedMonths, getAvailableMonths(currentYear, lowerBound, upperBound))
    }

    @Test
    fun getAvailableMonths_lowerBound() {
        val upperBound = UtilsTestUtils.getCalendar(15, 7, 2019)
        val lowerBound = UtilsTestUtils.getCalendar(1, 4, 1970)

        val currentYear = 1970

        val expectedMonths = arrayListOf(4, 5, 6, 7, 8, 9, 10, 11, 12)

        assertEquals(expectedMonths, getAvailableMonths(currentYear, lowerBound, upperBound))
    }

    @Test
    fun getAvailableMonths_nonBounded() {
        val upperBound = UtilsTestUtils.getCalendar(15, 7, 2019)
        val lowerBound = UtilsTestUtils.getCalendar(1, 4, 1970)

        val currentYear = 1995

        val expectedMonths = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

        assertEquals(expectedMonths, getAvailableMonths(currentYear, lowerBound, upperBound))
    }

    @Test
    fun getAvailableMonths_specialCase_sameYear() {
        val upperBound = UtilsTestUtils.getCalendar(15, 7, 2019)
        val lowerBound = UtilsTestUtils.getCalendar(1, 4, 2019)

        val currentYear = 2019

        val expectedMonths = arrayListOf(4, 5, 6, 7)

        assertEquals(expectedMonths, getAvailableMonths(currentYear, lowerBound, upperBound))
    }

    @Test
    fun getAvailableDays_upperBound() {
        val upperBound = UtilsTestUtils.getCalendar(15, 7, 2019)
        val lowerBound = UtilsTestUtils.getCalendar(1, 4, 1970)

        val currentYear = 2019
        val currentMonth = 7

        val expectedDays = (1..15).toList()

        assertEquals(expectedDays, getAvailableDays(currentYear, currentMonth, lowerBound, upperBound))
    }

    @Test
    fun getAvailableDays_lowerBound() {
        val upperBound = UtilsTestUtils.getCalendar(15, 7, 2019)
        val lowerBound = UtilsTestUtils.getCalendar(15, 4, 1970)

        val currentYear = 1970
        val currentMonth = 4

        val expectedDays = (15..30).toList()

        assertEquals(expectedDays, getAvailableDays(currentYear, currentMonth, lowerBound, upperBound))
    }

    @Test
    fun getAvailableDays_nonBounded() {
        val upperBound = UtilsTestUtils.getCalendar(15, 7, 2019)
        val lowerBound = UtilsTestUtils.getCalendar(1, 4, 1970)

        val currentYear = 2000
        val currentMonth = 5

        val expectedDays = (1..31).toList()

        assertEquals(expectedDays, getAvailableDays(currentYear, currentMonth, lowerBound, upperBound))
    }

    @Test
    fun getAvailableDays_specialCase_sameYearSameMonth() {
        val upperBound = UtilsTestUtils.getCalendar(23, 1, 2019)
        val lowerBound = UtilsTestUtils.getCalendar(7, 1, 2019)

        val currentYear = 2019
        val currentMonth = 1

        val expectedDays = (7..23).toList()

        assertEquals(expectedDays, getAvailableDays(currentYear, currentMonth, lowerBound, upperBound))
    }

}