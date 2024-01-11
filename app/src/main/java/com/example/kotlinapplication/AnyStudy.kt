package com.example.kotlinapplication

/**
 * Created by lenovo on 2023/5/29    16:46
 */
class AnyStudy {
    //Any 类是 kotlin 中所有类型 的 超类。等于 java中 的 object
    val an: Any? = null

    //感觉上有点类似 java 的 Void
    val nothing: Nothing? = null

    //基本数据类型 和 java 一样
    val b: Byte? = null
    val s: Short? = null
    val i: Int? = null//32位
    val L: Long? = null
    val f: Float? = null//32位
    var d: Double = 0.8
        set(value) {
            field = value - 1
        }
        get() = field * 2

    //数组
    val funArray = arrayOf(1, 3, 5, 55)
    val arrayInt = intArrayOf(1, 3, 5, 55)

}