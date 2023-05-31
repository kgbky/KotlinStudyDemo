package com.example.kotlinapplication

/**
 * Created by lenovo on 2023/5/26    17:31
 *
 * 学习各种表达式
 */
object ExpressionsStudy {

    fun studyIf(x: Int) {
        val value = if (x < 10) x * 2 else x
    }

    fun studyTryCatch(x: Int) {
        val value: Int = try {
            if (x < 10) x * 2 else x
        } catch (e: Exception) {
            -1
        } finally {
            //finally 快中的语句不会对 value 的类型产生影响
            println("我是 finally")
        }
        println(value)
    }

    //?: Elvis运算符
    //当且仅当左侧为空时，才会对右侧表达式求值。
    fun studyElvis(may: Int?) {
        // may为null 时返回-1
        val value = may ?: -1
        println(value)
    }

    fun studyWhen(e: EnumStudy) {
        //kotlin 中没有 switch 语句，但提供了功能更加强大的 when 表达式
        when (e) {
            EnumStudy.FRI -> println("today FRI")
            EnumStudy.MON -> println("today MON")
            EnumStudy.THU -> println("today THU")
            EnumStudy.SAT -> println("today SAT")
            else -> {
                println("today else")
            }
        }

    }

    fun studyFor() {
        // .. range表达式。包括1和10。step 定义步长
        for (i in 1..10 step 2) {
            println(i)
        }

        val list = listOf("b", "c")
        //通过 withIndex 方法获取 index 和 value
        for ((index, value) in list.withIndex()) {
            println(" the element at $index is $value")
        }

        // downTo 倒序。包括20和14。step 定义步长
        for (i in 20 downTo 14 step 2) {
            println(i)
        }

        // 实现了Comparable接口的类都可以使用 range表达式
        "abc".."XYZ"

        // in 检查一个元素是否在一个集合、数组、区间内
        // ! 取反操作
        val b = "a" in listOf("b", "c")
        val b1 = "kot" in "abc".."xyz"
        println("b = $b, + b1 = $b1")
    }

    fun studyInfix() {
        //中缀函数 例如：in step to 等
        val b = "a" to 1
        val c = "a".to(2)//推挤使用上面的写法
        println(b)
        println(c)
    }

    //通过 infix 关键字定义一个中缀方法
    infix fun called(name: String) {
        println("My name is $name")
    }

    //可变参数，java中可变参数必须是最后一个参数，kotlin中位置灵活
    fun studyVarargs(vararg letters: String, count: Int) {
        print("$count letters are")
        for (lett in letters) print(lett)
    }

    fun studyLet(value: String) {
        //这里的it指代value
        val value = value.let { "$it end" }
        println("let study = $value")
    }

    fun studyIs(value: Any) {
        //使用 is 进行类型检查
        if (value is String) println("字符串 参数，长度位 ${value.length}")
        else if (value !is Cloneable) println("参数不为Cloneable")
    }

    fun studyAs(value: Any?) {
        //as 进行类型转换。转换失败，会抛出ClassCastException
        //as? 是空安全的,转换失败，返回null
        val str: String? = value as? String
        if (str != null) println("字符串 参数，长度位 ${str.length}")
        else println("类型转换失败")

        val ans = cast<String>("haha")
        println("ans =  $ans")
    }


}