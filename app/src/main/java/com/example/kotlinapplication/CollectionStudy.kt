package com.example.kotlinapplication

/**
 * Created by lenovo on 2023/5/31    21:22
 *
 * 学习 Kotlin 中的集合
 */
class CollectionStudy {

    fun studyMap() {
        val list = listOf(1, 2, 4)
        //map 是一个高阶函数，大括号内是一个匿名函数
        var newList = list.map { it * 2 }

        val foo: (Int) -> Int = { it * 2 }
        newList = newList.map(foo)
        //Collections 文件中提供了很多类似 map 的函数:sum fold groupBy 等。方便我们处理集合

        var strList = listOf("a", "454", "ad", "andy", "abc")
        strList = strList.filter { it.length > 2 }
        println(strList)

        //求和
        val count: Int = strList.count { it.length > 3 }

        //这个api很有用
        val map: Map<Int, List<String>> = strList.groupBy { it.length }

        //嵌套集合api:flatten(展平) flatMap(展平和加工一起)
        val resultList: List<Char> = strList.flatMap {
            val charList = ArrayList<Char>()
            val arr = it.toCharArray()
            for (char in arr) charList.add(char)
            charList
        }
        println("resultList = $resultList")

    }

}