package com.example.kotlinapplication

/**
 * Created by lenovo on 2023/5/29    17:47
 *
 * 创造泛型的初衷是为了保证类型安全，所以泛型是不变的
 * List<String> 和 List<Object> 并没有子父类的关系
 */
class TStudy {

    //Kotlin 中的泛型函数
    fun <T> sum(a: T, b: T): T {
        return a
    }

    //Kotlin 中的泛型类
    class SmartList<T> : ArrayList<T>() {

        fun find(t: T): Int {
            return indexOf(t)
        }

    }

    //指定泛型的范围 Number及其子类
    fun <T : Number> sum2(a: T, b: T): T {
        return a
    }

    //指定泛型的范围 Number及其子类
    fun <T> sum3(a: T): T where T : Number {
        return a
    }

    //通过内联函数获取泛型类型。reified 修饰的函数不能在java 中使用
    inline fun <reified T> ArrayList<T>.getType(): Class<T> {
        return T::class.java
    }

    fun studyT() {
        var sList = SmartList<String>()
//        val sList = ArrayList<String>()
        sList.add("zero")
        sList.add("one")
        val index = sList.find("one")
        println(if (index < 0) "没找到" else "元素下标为$index")

        var ss = SmartList<Int>()
        val type = ss.getType()
        println("ArrayList 内的泛型类型为：$type")

        val strList: List<String> = ArrayList<String>()
        val strList2: MutableList<String> = ArrayList<String>()
        //类型通配符：Kotlin(*)   java(?)
        val list:MutableList<*> = mutableListOf(1,"kotlin")
    }

    //扩展函数
    fun <T> ArrayList<T>.find(t: T): Int {
        return indexOf(t)
    }

    //in 对应 java 中  ? super String，表示 Sting 及其父类。能存不能取
    //out 对应 java 中  ? extends String，表示 String 及其之类。能取不能存

}