package com.example.kotlinapplication

/**
 * Created by lenovo on 2023/6/11    21:31
 *
 * 目前已知作用是为了优化 Lambda 开销
 */
class InlineStudy {
    //定义：使用 inline 关键字修饰的函数
    //实现：编译代码时，不产生匿名内部类对象，而是直接把代码复制过去。提高性能

    fun studyInline() {
        //这么写 会产生一个 java 的 匿名内部类对象
        foo { println("andy into Kotlin...") }
        //这么写 不会产生对象，而是把 foo2 的代码 copy 到 studyInline 方法
        foo2 { println("andy into2 Kotlin...") }
        //执行了这个方法会直接返回,后面的代码不再执行
//        localReturn { return }

        testInline({ println("andy step 1...") }, { println("andy step 2...") })

        val list: List<Int> = listOf(1, 2, 3, 4, 5)
        //foreach 中的 return 会结束 studyInline 方法,因为是内联的
//        list.forEach { if (it == 3) return else println(it) }

        println("lasted code")

        getType<Int>()
    }

    fun foo(block: () -> Unit) {
        println("before block")
        block()
        println("end block")
    }

    inline fun foo2(block: () -> Unit) {
        println("before block")
        block()
        println("end block")
    }

    //通过 noinline 关键字，指定内联函数中的 参数 不内联
    private inline fun testInline(block: () -> Unit, noinline block2: () -> Unit) {
        println("before block1")
        block()
        println("end block1")
        println("before block2")
        block2()
        println("end block2")
    }

    //非局部返回。内联函数的实现原理，会导致内联函数中的 return，会结束调用内联函数 的函数
    //为了防止意外的非局部返回，可以使用 crossinline 修饰lambda参数。这样lambda语句中就不能使用 return 关键字
    //在内联的 lambda 中，目前还不支持 break 和 continue
    private inline fun localReturn(returning: () -> Unit) {
        returning()
    }

    //内联函数 支持 具体化参数类型，使用 reified 关键字实现
    //普通函数不支持 reified
    private inline fun <reified T> getType() {
        println("class = ${T::class}")
        println("class.java = ${T::class.java}")
    }

}