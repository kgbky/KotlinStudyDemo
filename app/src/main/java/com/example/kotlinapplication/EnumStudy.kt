package com.example.kotlinapplication

/**
 * Created by lenovo on 2023/5/26    18:07
 *
 * 结合了 java 中 枚举与类
 */
enum class EnumStudy {
    MON, TUE, WEN, THU, FRI, SAT, SUN

    ;//可在;下定义额外的方法和属性

    fun test() {
        println("我是在枚举中定义的方法")
    }
}