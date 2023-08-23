package com.example.kotlinapplication

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime

/**
 * Created by lenovo on 2023/6/22    19:10
 */
class CoroutinesStudy {

    //协程是线程调度器
    fun studyCoroutines() {
        //GlobalScope 是全局的，只受整个应用程序的生命周期限制
        val job = GlobalScope.launch(Dispatchers.IO) { // 在后台启动一个新的协程并继续
            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒） 挂起协程
            println("World!") // 在延迟后打印输出
            println("线程名字：${Thread.currentThread().name}")
            susFun()
        }
        println("Hello,") // 协程已在等待时主线程还在继续
        runBlocking { delay(2000L) }//runBlocking 会阻塞当前线程，直到其内部协程执行完毕
    }

    //运行结果一致，但写法不同
    fun studyCoroutines2() {
        runBlocking {
            val job = launch(Dispatchers.IO) {// 子协程
                delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒） 挂起协程
                println("World!") // 在延迟后打印输出
                println("线程名字：${Thread.currentThread().name}")
                susFun()
            }
            println("Hello,") // 主协程
//            delay(2000L)
            job.join()//使用 join 替代 delay。更优雅，主协程会等待子协程执行完毕,再继续向下执行
            println("主协程执行完毕")
            //主协程 在其内部所有子协程都执行完毕时，才会结束。他们使用同一个 CoroutineScope
        }
    }

    fun studyCoroutines3() {
        //runBlocking 会阻塞当前线程来等待
        runBlocking {
            launch {
                delay(200L)
                println("Task from runBlocking")// 2
            }

            coroutineScope { // 创建一个协程作用域,并且在所有已启动子协程执行完毕之前不会结束
                launch {
                    delay(500L)
                    println("Task from nested launch")// 3
                }
                delay(100L)
                println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出 1
            }

            println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出 4
        }

    }

    fun downloadImage() {
        GlobalScope.launch(Dispatchers.IO) {
            delay(3000)//延迟3秒模拟下载图片
            withContext(Dispatchers.Main) {
                //下载完成后切回到主线程，显示图片
            }
        }
    }

    //suspend 关键字，表示方法必须在协程或者另一个 suspend 方法中调用。
    //自定义 suspend，常用来修饰耗时方法
    private suspend fun susFun() {
        println("我是一个 suspend function")
    }

    fun testGlobal() {
        //在 GlobalScope 中启动的活动协程并不会使进程保活。它们就像守护线程。
        GlobalScope.launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        runBlocking {
            delay(1300L) // 在延迟后退出
        }
    }

    fun studyJob() {
        runBlocking {
            val job = launch {
                try {
                    // withContext(NonCancellable){} 包裹的挂起函数不会被 cancel() 取消
                    repeat(20) { i ->
                        println("job: I'm sleeping $i ...")
                        delay(500L)
                    }
                } catch (e: CancellationException) { //RuntimeException job 被取消时抛出
                    //可以不捕获，它会被协程机制忽略
                    println("捕获到异常：$e")
                } finally {
                    //finally 包裹的代码一定会被执行，可在这里释放资源
                    println("job: I'm running finally")
                }
            }
            delay(1300L) // 延迟一段时间
            println("main: I'm tired of waiting!")
            job.cancel() // 取消该作业,不取消的话，job会一直执行
            job.join() // 等待作业执行结束
//            job.cancelAndJoin()// 合并了 cancel 和 join 的调用
            println("main: Now I can quit.")
        }

    }

    fun studyCancelJob() {
        //协程的取消是协作(理解为需要合作)的:
        //kotlinx.coroutines 包中的挂起函数都是可被取消的，它们检查协程的取消，并在取消时抛出CancellationException
        //编码时，我们也需要在协程代码中检查是否已取消。这和 java 结束一个线程的方式类似
        runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.IO) {
                var nextPrintTime = startTime
                var i = 0
                while (isActive) {//检查协程是否被取消
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("job : i am sleeping ${i++}")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L)
            println("main: i am tired of waiting!")
            job.cancelAndJoin()
            println("main: Now I can quit")
        }

    }

    fun studyTimeout() {
        runBlocking {
            //超时时：withTimeout() 会抛出 TimeoutCancellationException 需要自己处理
            //超时时：withTimeoutOrNull() 会返回 null，替代异常的抛出
            val result = withTimeoutOrNull(1300L) {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
            }
            println("Result is $result")
        }
    }

    fun studyTimeOutResource() {
        runBlocking {
            repeat(100_000) { // Launch 100K coroutines
                launch {
                    val resource = withTimeout(600) { // Timeout of 600 ms
                        delay(1000) // Delay for 1000 ms
                        Resource() // Acquire a resource and return it from withTimeout block
                    }
                    resource.close() // Release the resource
                }
            }
        }
        // Outside of runBlocking all coroutines have completed
        // Print the number of resources still acquired
        println(acquired)
    }

    //操作多个挂起函数
    @OptIn(ExperimentalTime::class)
    fun studyMulti() {
        runBlocking {
            //顺序执行 用时大约 7500 ms
//            val time = measureTimeMillis {
//                val one = doSomethingUsefulOne()
//                val two = doSomethingUsefulTwo()
//                println("The answer is ${one + two}")
//            }
//            println("Completed in $time ms")

            //并发执行 省时间 用时大约 5000 ms
//            val time2 = measureTimeMillis {
//                val one = async { doSomethingUsefulOne() }
//                val two = async { doSomethingUsefulTwo() }
//                println("The answer is ${one.await() + two.await()}")
//            }
//            println("Completed in $time2 ms")

            //惰性启动的 async 用时大约 5000 ms
//            val time2 = measureTimeMillis {
//                // lazy 模式时，只有调用 await() or start() 才会启动协程
//                val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
//                val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
//                one.start()
//                two.start()
//                //只调用 await() 不调用start() 会导致顺序执行
//                println("The answer is ${one.await() + two.await()}")
//            }
//            println("Completed(lazy async) in $time2 ms")
        }

        //调用async函数  kotlin完全不推荐这种写法
//        val time = measureTimeMillis {
//            // 我们可以在协程外面启动异步执行
//            val one = somethingUsefulOneAsync()
//            val two = somethingUsefulTwoAsync()
//            // 但是等待结果必须调用其它的挂起或者阻塞
//            // 当我们等待结果的时候，这里我们使用 `runBlocking { …… }` 来阻塞主线程
//            runBlocking {
//                println("The answer is ${one.await() + two.await()}")
//            }
//        }
//        println("Completed(async function) in $time ms")

        //结构化并发 Kotlin 使用这种方式 代替上面的方式
        runBlocking {
            val time3 = measureTimeMillis {
                println("The answer is ${concurrentSum()}")
            }
            println("Completed in $time3 ms")
        }

    }

    // 假设我们在这里做了一些有用的事
    private suspend fun doSomethingUsefulOne(): Int {
        for (i in 1..5) {
            delay(500L)
            println("one function : $i")
        }
//        throw RuntimeException("主动抛出异常")
        return 13
    }

    // 假设我们在这里也做了一些有用的事
    private suspend fun doSomethingUsefulTwo(): Int {
        for (i in 1..5) {
            delay(1000L)
            println("two function : $i")
        }
        return 29
    }

    // somethingUsefulOneAsync 是普通函数，返回值类型是 Deferred<Int>
    private fun somethingUsefulOneAsync() = GlobalScope.async {
        doSomethingUsefulOne()
    }

    // somethingUsefulTwoAsync 是普通函数，返回值类型是 Deferred<Int>
    private fun somethingUsefulTwoAsync() = GlobalScope.async {
        doSomethingUsefulTwo()
    }

    private suspend fun concurrentSum(): Int = coroutineScope {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        one.await() + two.await()
    }

    fun studyCancel() {
        runBlocking<Unit> {
            try {
                failedConcurrentSum()
            } catch (e: ArithmeticException) {
                println("Computation failed with ArithmeticException")
            }
            println("父协程执行结束")
        }
    }

    private suspend fun failedConcurrentSum(): Int = coroutineScope {
        val one = async<Int> {
            try {
                delay(Long.MAX_VALUE) // 模拟一个长时间的运算
                42
            } finally {
                println("First child was cancelled")
            }
        }
        val two = async<Int> {
            println("Second child throws an exception")
            //取消始终通过协程的层次结构来进行传递
            //这里抛出异常时，one 也会取消
            throw ArithmeticException()
        }
        one.await() + two.await()
    }

}

var acquired = 0

class Resource {
    init {
        acquired++// Acquire the resource
    }

    fun close() {
        acquired--// Release the resource
    }
}