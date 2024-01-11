package com.example.kotlinapplication

import android.content.Context

/**
 * Created by admin on 2023/6/13    11:47
 * 实现多态的三种方式：
 *
 * 继承
 *
 * 参数多态。方法的重载、泛型
 *
 * 特设多态。运算符重载、扩展
 */
class ExtensionsStudy {

    //学习运算符重载
    fun studyOperatorOverloading() {
        var result = "andy" - "Lucy"
        println(result)

        val array = "andy".toCharArray()
        val c = array[1] //对应get
        array[2] = '*'//对应set
        println("char = $c array = ${array.contentToString()}")
    }

    //operator 标记该函数重载一个运算符
    //Kotlin 规定 +(plus) -(minus) *(times) /(div) 取余(rem)
    //[] 索引运算符
    operator fun String.minus(that: String): String {
        val sb = StringBuilder()
        sb.append(this.substring(0, 2))
        sb.append(that.substring(0, 2))
        return sb.toString()
    }

    //扩展符合开闭原则
    fun studyExtensions() {
//        val list = mutableListOf(1, 2, 3)
//        list.exchange(0, 2)
//        println(list)
//        println("是偶数吗 = ${list.sumIsEven}")
//        Son().foo()

        Parent.test()
    }

    //扩展函数：普通函数前面带上类名。
    //扩展函数对比普通函数不会有额外的性能消耗
    //扩展函数定义在类中时，只有 该类及其子类 可以调用
    //类名.Companion.扩展函数。实现 java静态扩展函数 的效果
    private fun MutableList<Int>.exchange(fromIndex: Int, toIndex: Int) {
        val tmp = this[fromIndex]
        this[fromIndex] = this[toIndex]
        this[toIndex] = tmp
    }

    class Son {
        //函数同名时，成员函数优先级 > 扩展函数
        fun foo() = println("son called foo")
    }

    fun Son.foo() {
        println("Extensions called foo")
    }

    object Parent {
        private fun foo() = println("parent called foo")

        fun test() {
            Son().foo2()
        }

        fun Son.foo2() {
            this.foo()//this 指 Son 类的实例
            this@Parent.foo()//this@Parent 指 Parent 类的实例
        }
    }

    //扩展属性 不会有额外的性能开销
    val MutableList<Int>.sumIsEven: Boolean
        get() = this.sum() % 2 == 0

    //属性的 getter 和 setter
    var name: String = "default"
        get() = "andy"
        set(value) {
            field = if (value.length < 3) "error" else value
        }

    fun testRun() {
        //介绍常见的扩展api
        val nickName = "andy"
        kotlin.run {
            val nickName = "Lucy"
            println(nickName)
        }
        println(nickName)

        nickName.let { it.length }//判空是一个应用方向

        val result: Int? = nickName.takeIf { it.startsWith("A", true) }?.let {
            println("take length = ${it.length}")
            1
        }
        println("result = $result")

        val context: Context? = null
        context?.isMobileConnected()
    }

    //扩展函数的应用
    fun Context.isMobileConnected(): Boolean {
        //判断网络是否可用，这里是伪代码
        this.getSystemService(Context.CONNECTIVITY_SERVICE)
        return false
    }

}