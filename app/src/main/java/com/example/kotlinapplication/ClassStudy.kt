package com.example.kotlinapplication

/**
 * Created by lenovo on 2023/5/27    16:38
 */
class ClassStudy(var name: String, val age: Int = 1) {

    val sex: String by lazy {
        //第一次访问 sex 时，才会执行大括号内的语句
        //内部使用了Sync 所以默认是线程安全的
        println("懒加载 val 修饰的属性")
        if (name.length < 2) "man" else "woman"
    }

    //懒加载 var 对应 lateInit；val 对应 by lazy
    lateinit var number: Number

    //通过构造方法 参数默认值，可以解决java中构造方法重载的问题

    //通过 constructor 关键字定义构造方法
    constructor() : this("default name", 12) {
        println(sex)
    }

    init {
        //java写在构造函数中的代码，现在放到init语句块中
        println("first init block")
        this.name = "first init block"//构造方法中的参数可以通过 this. 在此处调用
    }

    init {
        //多个init块 按照代码顺序执行
        println("second init block")
        this.name = "second init block"
    }

    //方法默认是public的，且被final修饰
    fun sayHello() {
        println("My name is $name, Hello")
    }

    //inner定义内部类，含义和 java 一致
    inner class InnerClass() {
        override fun toString(): String {
            return "I am inner class"
        }
    }

    //静态内部类 等于 java 中 public static final 修饰的内部类
    class StaticClass() {
        override fun toString(): String {
            return "我是一个静态内部类"
        }
    }

}