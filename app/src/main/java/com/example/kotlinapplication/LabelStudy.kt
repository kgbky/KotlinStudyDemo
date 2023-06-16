package com.example.kotlinapplication

/**
 * Created by admin on 2023/6/13    13:35
 */
class LabelStudy {

    fun studyLabel() {
        //Kotlin 中任何语句 都用label标记
        loop@ for (i in 1..3) {
            for (y in 1..10) if (y == 5) break@loop else println(y)
        }

        //break continue 默认只影响最近的循环。通过使用 label 可以指定要去的地方
        listOf(1, 2, 3, 4, 5).forEach lit@{//@在后定义Label
            if (it == 3) return@lit //@在前使用Label
            print(it)
        }
        print(" done with explicit label")

        //隐式标签(implicit labels) 函数默认有一个标签 和函数名同名
        listOf(1, 2, 3, 4, 5).forEach {
            if (it == 3) return@forEach // local return to the caller of the lambda - the forEach loop
            print(it)
        }
        print(" done with implicit label2")
    }

    fun foo() {
        //使用标签实现非局部返回
        run loop@{
            listOf(1, 2, 3, 4, 5).forEach {
                if (it == 5) return@loop // return 到外部循环
                print(it)
            }
        }
        print(" done with nested loop")

        val value: Int = foo2()
        println("foo2 function result = $value")
    }

    private fun foo2(): Int {
        run loop@{
            listOf(1, 2, 3, 4, 5).forEach {
                if (it == 5) return@foo2 0 // return 0 到 foo2
                print(it)
            }
        }
        return -1
    }

}