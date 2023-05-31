package com.example.kotlinapplication

/**
 * Created by lenovo on 2023/5/26    21:19
 */
class StringStudy {

    fun studyString() {
        val string = "Hello world!"
        var value = string.substring(0)
        value = value.filter { it in 'a'..'h' }
        //kotlin 的字符串拼接
        println("字符串为$value")
        //字符串拼接表达式
        println("字符串长度为${if (value.isNotEmpty()) value.length else 0}")

        val rawString = """我是原生字符串，\n 我内部的格式不会被修改"""
        println("字符串为$rawString")
    }

}