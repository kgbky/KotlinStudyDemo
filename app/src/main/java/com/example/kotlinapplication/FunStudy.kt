package com.example.kotlinapplication

import com.example.kotlinapplication.javasource.User

/**
 * Created by lenovo on 2023/5/26    15:34
 */
object FunStudy {

    //代码块函数体
    fun sum(x: Int, y: Int): Int {
        return x + y
    }

    //表达式函数体
    fun sum2(x: Int, y: Int) = x + y

    //if 也是表达式，返回值类型 取决于 分支的返回值类型
    fun foo(n: Int) = if (n == 0) 1 else 2

    var file = 1 //var 表示变量
    val value = 2f //val 表示值，内部是用 final 实现的

    fun studyVal() {
        // val 修饰的变量，引用不可变，但对象内部属性可以变。和 final 一个逻辑
        val user = User("a", "b")
        user.firstName = "aa"
        println(user)
    }

    // val 和 var 优先使用 val。代码逻辑更简单 且 防止意外的修改变量的引用

    //函数的类型 函数也可以是一个对象
    var funTest: ((Int) -> Unit)? = null

    //?表示可为null
    var funTest2: ((Int) -> ((Int) -> Unit))? = null

    //高阶函数：把其他函数作为参数或者返回值的函数
    fun highFun(v: ((Int) -> Unit)?): Boolean {
        v?.invoke(1)
        return true
    }

    fun studyHighFun() {
        // ?. 表示安全的调用 不会抛出nullException,整个表达式返回null
        val value = funTest?.invoke(1)
        println("空安全调用，值为$value")
        // !! 非空断言运算符.将任何值转换为非空类型，若该值为空则抛出 NPE 异常.

        //对象::方法命 来引用一个函数
        this::studyVal
        val b = highFun(funTest)
        println("高阶函数，值为$b")

        // Lambada表达式本质是一种语法糖，简化匿名函数的书写
        highFun { v -> v + 1 }

    }

    fun studyLambda() {
        // Lambada表达式本质是一种语法糖，简化匿名函数的书写
        // Lambada表达式必须用大括号包裹
        val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
        val foo: (Int) -> Unit = { println(it) }

        // it 单个参数的隐式名称,下面两句代码一个意思
        listOf(1, 2, 3).forEach { foo(it) }
        listOf(1, 2, 3).forEach { item -> foo(item) }
    }


}