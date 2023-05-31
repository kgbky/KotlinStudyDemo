package com.example.kotlinapplication

import java.util.Date

/**
 * Created by lenovo on 2023/5/27    22:06
 *
 * 数据类 完美代替 javaBean
 * 自动帮我们生成 getter 和 setter、hashCode、equals、toString
 *
 * 会生成 copy() 代替 java Object 类中的 clone()。copy()也是浅拷贝
 *
 * 会生成 componentN() 帮助我们解构，使用 operator 关键字可自定义component系列方法
 */
data class DataClassStudy(var name: String, val age: Int, var date: Date?) {

    //Kotlin 根据主构造函数 的 参数个数 生成对应数量的 component 方法

    operator fun component4() = "我是自定义解构方法"

}