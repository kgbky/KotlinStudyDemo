package com.example.kotlinapplication

/**
 * Created by lenovo on 2023/5/27    22:51
 *
 * 学习 Object 关键字,用来替代 static。Kotlin 已移除 static 关键字
 *
 * object 可用来实现单例模式
 */
class ObjectStudy(val type: Int) {

    fun sayHello() {
        println("I am Andy,Hello")
    }

    //伴生对象 伴随着类，属于类而不是类的实例。类被加载时就会初始化
    //用来替代 java 中 static 修饰的方法和变量
    companion object {
        val TYPE_REDPACK = 0
        val TYPE_COUPON = 1

        fun isRedpack(os: ObjectStudy) = os.type == TYPE_REDPACK
    }

    //object 表达式，用来生成一个 匿名内部类的实例。可实现多个接口
    private val comparator1 = object : Comparator<String>, Iterable<String> {
        override fun compare(p0: String?, p1: String?): Int {
            return if (p0 == null) -1 else if (p1 == null) 1 else p0.compareTo(p1)
        }

        override fun iterator(): Iterator<String> {
            TODO("Not yet implemented")
        }
    }

    //使用 Lambda 表达式简化书写，只能实现一个接口
    val comparator2 = Comparator<String> { p0, p1 ->
        if (p0 == null) -1 else if (p1 == null) 1 else p0.compareTo(p1)
    }


}