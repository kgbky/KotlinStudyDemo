package com.example.kotlinapplication

import android.view.View

/**
 * Created by lenovo on 2023/5/31    20:47
 */
class LambdaStudy {

    fun studyLambda() {
        val view: View? = null
        //Lambda 简化了书写
        view?.setOnClickListener { println("被点击了") }

        val result = 2.sum(5)//result = 7

        val string: String = "hello"
        //{} 内是Lambda表达式，是 with 方法的最后一个参数
        // with 源码中会把 第二个参数变为第一个参数的扩展函数
        val length: Int = with(string) {
            //this 指代第一个参数.可以省略
            this.substring(2)
            length
        }
        println("length  = $length")

        //和 with 一样 也是扩展函数实现的
        val s2 = string.apply { println("String use apply function") }
        println("s2 === string ${string === s2}")
    }

    //扩展函数,对 Int 类进行扩展
    fun Int.sum(t: Int): Int {
        return this + t
    }

    //函数实例化：Lambda实现，it 单个参数的隐式名称
    var funInstance: ((Int) -> Unit)? = { println("x = $it") }

    //函数实例化：匿名函数实现
    var funInstance2: ((Int) -> Unit)? = fun(x: Int) { println("x = $x") }

    //函数实例化：指向已定义的函数
    var funInstance3: ((Int) -> Unit) = ::printInt

    fun printInt(x: Int) {
        println("x = $x")
    }

    //调用函数
    fun callFunInstance() {
        printInt(11)
        funInstance3.invoke(22)//通过 invoke 操作符调用函数变量
        funInstance3(33)//直接调用函数变量
    }
}