package com.chessporg.local_attendance_app

import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun checkDateClass() {
        val date = Date()
        print(date.toString())
        print(date.toString().subSequence(0,3))
    }
}