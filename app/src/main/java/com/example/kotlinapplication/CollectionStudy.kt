package com.example.kotlinapplication

/**
 * Created by lenovo on 2023/5/31    21:22
 *
 * 学习 Kotlin 中的集合。Kotlin 中的集合基于 java 的集合，同时通过扩展函数增强了它
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

    fun studyList() {
        //List 有序、可重复
        //set 不可重复，HashSet 无序、TreeSet 有序
        //Map key-value，key不能重复

        //Kotlin 中的集合分为 只读(List) 和 可变类型(MutableList)
        //只读List 在和 java 互操作时，可能会被改变！因为java 不区分只读 和 可变
        val list = listOf(1, 2, 3, 4, 5)
        list.filter { it > 2 }.map { it * 2 }

        //序列 Sequence 懒加载元素(性能好)、序列支持无限个元素
        list.asSequence().filter {
            println("filter $it")
            it > 2
        }.map {
            println("map $it")
            it * 2
        }.toList()

        //构建一个无限长度的序列
        val natNumList = generateSequence(0) { it + 1 }
        var result = natNumList.takeWhile { it <= 9 }.toList()
        println(result)
    }

}