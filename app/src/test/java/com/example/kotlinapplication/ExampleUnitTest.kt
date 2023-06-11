package com.example.kotlinapplication

import com.example.kotlinapplication.javasource.User
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        val user1 = User("Jane", "Doe")
        val user2 = User("Jane", "Doe")
        val structurallyEqual = user1 == user2 // true == 等于 java 的equals
        val referentiallyEqual = user1 === user2 // false  === 等于java 的 ==
        assertEquals(structurallyEqual, true)
        assertEquals(referentiallyEqual, false)

        val c1 = DataClassStudy("Andy", 12, Date())
        //copy 方法代替了 java Object 类中的 clone()。 copy() 也是浅拷贝
        val c2 = c1.copy()

        //解构 大大提高编码效率,可以只解构自己需要的字段
        val (name, _, date, custom) = c1
        println("name = $name ,date = $date , custom =$custom")

        val os = ObjectStudy(1)
        println("模式 = ${ObjectStudy.isRedpack(os)}")

        InlineStudy().studyInline()


    }

}