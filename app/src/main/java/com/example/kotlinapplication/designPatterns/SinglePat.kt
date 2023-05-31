package com.example.kotlinapplication.designPatterns

/**
 * Created by lenovo on 2023/5/27    23:27
 *
 * 一个 object 关键字即可实现单例模式
 *
 * 原理是使用 java 懒汉单例，所以是线程安全的
 */
object SinglePat {
    val host = "127.0.0.1"
    var port: Int = 8080
}