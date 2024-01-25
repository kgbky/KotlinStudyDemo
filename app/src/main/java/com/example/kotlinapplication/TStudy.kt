package com.example.kotlinapplication

import java.util.Date

/**
 * Created by lenovo on 2023/5/29    17:47
 *
 * 创造泛型的初衷是为了保证类型安全，所以泛型是不型变的
 * List<String> 和 List<Object> 并没有子父类的关系
 *
 * 需要型变的时候可以使用 out 和 in 关键字:
 *
 * in 对应 java 中  ? super String，表示 Sting 及其父类。能写不能读(数据)
 *
 * out 对应 java 中  ? extends String，表示 String 及其之类。能读不能写(数据)
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

    //指定泛型的范围 Number及其子类。NumberList 可以存Int 或 double
    //但是不会既存Int、又存double
    class NumberList<T : Number> : ArrayList<T>() {

        //输出方法
        fun find(t: T): Int {
            return indexOf(t)
        }

        //输入方法
        fun changeZero(value: T) {
            set(0, value)
        }

    }

    //指定泛型的范围 Number及其子类
    fun <T : Number> sum2(a: T, b: T): T {
        return a
    }

    //指定泛型的范围，Where子句可以指定多个上界
    //Number及其子类，CharSequence及其子类
    fun <T> sum3(a: T): T where T : Number, T : CharSequence {
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
        val list: MutableList<*> = mutableListOf(1, "kotlin", Date())
    }

    //扩展函数
    fun <T> ArrayList<T>.find(t: T): Int {
        return indexOf(t)
    }

    //in 对应 java 中  ? super String，表示 Sting 及其父类。消费者
    //out 对应 java 中  ? extends String，表示 String 及其子类。生产者

    //声明处型变
    interface Source<out T> {
        fun nextT(): T
    }

    fun demo(strs: Source<String>) {
        // 这个没问题，因为 T 是一个 out-参数
        val objects: Source<CharSequence> = strs
    }

    //声明处型变
    interface Comparable<in T> {
        operator fun compareTo(other: T): Int
    }

    fun demo(x: Comparable<Number>) {
        x.compareTo(1.0) // 1.0 拥有类型 Double，它是 Number 的子类型
        val y: Comparable<Double> = x // 可以将 x 赋给类型为 Comparable <Double> 的变量
    }

    //使用处型变 对应于 Java 的 Array<? extends Object>
    fun copy(from: Array<out Any>, to: Array<Any>) {
        assert(from.size == to.size)
        for (i in from.indices)
            to[i] = from[i]
    }

    //使用处型变  对应于 Java 的 Array<? super String>
    fun fill(dest: Array<in String>, value: String) {
        //可以传递一个 CharSequence 数组或一个 Object 数组给 fill() 函数
    }

    fun demo() {
        val ints: Array<Int> = arrayOf(1, 2, 3)
        val any = Array<Any>(3) { "" }
        copy(ints, any)
        //   ^ 其类型为 Array<Int> 但此处期望 Array<Any>
    }

}