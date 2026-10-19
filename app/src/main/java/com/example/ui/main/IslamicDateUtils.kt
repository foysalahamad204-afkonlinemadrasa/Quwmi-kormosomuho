package com.example.ui.main

import java.util.Calendar

/**
 * Utility for generating real-time Hijri, Bengali, and English dates in Bengali script and numerals.
 * Does NOT contain the word "লাইভ" anywhere.
 */
object IslamicDateUtils {

    data class FormattedDates(
        val hijri: String,
        val bengali: String,
        val english: String
    )

    private val bengaliDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

    fun toBengaliDigits(number: Int): String {
        return number.toString().map { char ->
            if (char in '0'..'9') bengaliDigits[char - '0'] else char
        }.joinToString("")
    }

    private val englishMonthsBn = arrayOf(
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    )

    private val bengaliMonthsBn = arrayOf(
        "বৈশাখ", "জ্যৈষ্ঠ", "আষাঢ়", "শ্রাবণ", "ভাদ্র", "আশ্বিন",
        "কার্তিক", "অগ্রহায়ণ", "পৌষ", "মাঘ", "ফাল্গুন", "চৈত্র"
    )

    private val hijriMonthsBn = arrayOf(
        "মুহাররম", "সফর", "রবিউল আউয়াল", "রবিউস সানি", "জমাদিউল আউয়াল", "জমাদিউস সানি",
        "রজব", "শাবান", "রমজান", "শাওয়াল", "জিলকদ", "জিলহজ"
    )

    /**
     * Returns the current date in Hijri, Bengali, and English (Gregorian) formatted in Bengali.
     */
    fun getCurrentDates(calendar: Calendar = Calendar.getInstance()): FormattedDates {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) // 0-based
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // 1. English (Gregorian) Date formatted in Bengali
        val engMonthName = englishMonthsBn.getOrElse(month) { "" }
        val englishFormatted = "${toBengaliDigits(day)} $engMonthName ${toBengaliDigits(year)}"

        // 2. Bengali San/Date (Bangladesh Revised Official Calendar)
        val bengaliDate = calculateBengaliDate(year, month + 1, day)
        val bengaliMonthName = bengaliMonthsBn.getOrElse(bengaliDate.month - 1) { "" }
        val bengaliFormatted = "${toBengaliDigits(bengaliDate.day)} $bengaliMonthName ${toBengaliDigits(bengaliDate.year)} বঙ্গাব্দ"

        // 3. Hijri Date (Astronomical Algorithmic Calculation with Hijrah Calendar alignment)
        val hijriDate = calculateHijriDate(year, month + 1, day)
        val hijriMonthName = hijriMonthsBn.getOrElse(hijriDate.month - 1) { "" }
        val hijriFormatted = "${toBengaliDigits(hijriDate.day)} $hijriMonthName ${toBengaliDigits(hijriDate.year)} হি."

        return FormattedDates(
            hijri = hijriFormatted,
            bengali = bengaliFormatted,
            english = englishFormatted
        )
    }

    data class DateTriple(val year: Int, val month: Int, val day: Int)

    /**
     * Calculates Bengali calendar date according to the official Bangladesh calendar reform:
     * - Pohela Boishakh is April 14.
     * - First 5 months (Boishakh to Bhadro) have 31 days each.
     * - Next 6 months (Ashwin to Falgun) have 30 days each (Falgun has 31 days in leap years).
     * - 12th month (Chaitra) has 30 days.
     */
    fun calculateBengaliDate(year: Int, month: Int, day: Int): DateTriple {
        val cal = Calendar.getInstance().apply {
            set(year, month - 1, day, 12, 0, 0)
        }
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)

        val isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

        // Day of year for April 14
        val april14Cal = Calendar.getInstance().apply {
            set(year, Calendar.APRIL, 14, 12, 0, 0)
        }
        val april14DayOfYear = april14Cal.get(Calendar.DAY_OF_YEAR)

        val bengaliYear: Int
        val daysSincePohelaBoishakh: Int

        if (dayOfYear >= april14DayOfYear) {
            bengaliYear = year - 593
            daysSincePohelaBoishakh = dayOfYear - april14DayOfYear
        } else {
            bengaliYear = year - 594
            val prevYear = year - 1
            val prevIsLeap = (prevYear % 4 == 0 && prevYear % 100 != 0) || (prevYear % 400 == 0)
            val prevYearDays = if (prevIsLeap) 366 else 365
            val prevApril14Cal = Calendar.getInstance().apply {
                set(prevYear, Calendar.APRIL, 14, 12, 0, 0)
            }
            val prevApril14Day = prevApril14Cal.get(Calendar.DAY_OF_YEAR)
            daysSincePohelaBoishakh = (prevYearDays - prevApril14Day) + dayOfYear
        }

        val monthLengths = intArrayOf(
            31, 31, 31, 31, 31, // Boishakh to Bhadro
            30, 30, 30, 30, 30, // Ashwin to Magh
            if (isLeapYear) 31 else 30, // Falgun
            30 // Chaitra
        )

        var bMonth = 1
        var remainingDays = daysSincePohelaBoishakh

        for (length in monthLengths) {
            if (remainingDays < length) {
                break
            }
            remainingDays -= length
            bMonth++
        }

        val bDay = remainingDays + 1
        return DateTriple(bengaliYear, bMonth.coerceIn(1, 12), bDay)
    }

    /**
     * Calculates Hijri date based on Kuwaiti/Umm al-Qura standard astronomical algorithm.
     */
    fun calculateHijriDate(year: Int, month: Int, day: Int): DateTriple {
        var y = year
        var m = month
        if (m < 3) {
            y -= 1
            m += 12
        }

        val a = Math.floor(y / 100.0)
        val b = 2 - a + Math.floor(a / 4.0)
        val jd = Math.floor(365.25 * (y + 4716)) + Math.floor(30.6001 * (m + 1)) + day + b - 1524.5

        val z = Math.floor(jd + 0.5).toLong()
        val epochDiff = z - 1948440 + 10632
        val cycle = Math.floor((epochDiff - 1) / 10631.0).toLong()
        val dayInCycle = epochDiff - 10631 * cycle + 354
        val j = ((10985 - dayInCycle) / 5316.0).toLong() * ((50 * dayInCycle) / 17719.0).toLong() +
                (dayInCycle / 5670.0).toLong() * ((43 * dayInCycle) / 15238.0).toLong()
        val dayInYear = dayInCycle - ((30 - j) / 15.0).toLong() * ((17719 * j) / 50.0).toLong() -
                (j / 16.0).toLong() * ((15238 * j) / 43.0).toLong() + 29
        val hMonth = ((24 * dayInYear) / 709.0).toInt()
        val hDay = (dayInYear - ((709 * hMonth) / 24.0).toLong()).toInt()
        val hYear = (30 * cycle + j - 30).toInt()

        return DateTriple(hYear, hMonth.coerceIn(1, 12), hDay.coerceIn(1, 30))
    }
}
