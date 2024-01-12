package com.example.kotlinapplication

/**
 * Created by lenovo on 2023/5/27    16:43
 *
 * 接口不保存状态，因为是方法的集合
 */
interface InterfaceStudy {
    //抽象属性 java中没有
    val age: Int
    var name: String
    fun kind()
    fun fly() {
        //方法的默认实现
        println("I can fly")
    }

}

class InterfaceImpl(override val age: Int, override var name: String, private val count: Int) :
    InterfaceStudy {
    override fun kind() {
        TODO("Not yet implemented")
    }

    override fun fly() {
        super.fly()
    }

    fun printlnCount() {
        println("count = $count")
    }

}