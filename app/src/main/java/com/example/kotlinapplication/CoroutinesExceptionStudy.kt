package com.example.kotlinapplication

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.IOException

/**
 * Created by admin on 2023/6/27    19:57
 */
class CoroutinesExceptionStudy {

    fun studyException() {
        runBlocking {
            val job =
                GlobalScope.launch { // launch 根协程 launch方式将异常视为未捕获异常，可使用CoroutineExceptionHandler处理
                    //可使用CoroutineExceptionHandler
                    println("Throwing exception from launch")
                    throw IndexOutOfBoundsException() // 我们将在控制台打印 IndexOutOfBoundsException
                }
            job.join()
            println("Joined failed job")
            val deferred = GlobalScope.async {
                //async 根协程 async依赖用户来最终消费异常
                //async 构建器捕获所有异常，并将其表示在结果 Deferred 对象中
                //无法使用CoroutineExceptionHandler
                println("Throwing exception from async")
                throw ArithmeticException() // 没有打印任何东西，依赖用户去调用等待
            }
            try {
                deferred.await()
                println("Unreached")
            } catch (e: ArithmeticException) {
                println("Caught ArithmeticException")
            }
        }
    }

    fun testExceptionHandler() {
//        runBlocking {
//            val handler = CoroutineExceptionHandler { _, exception ->
//                //该处理者用于记录异常，显示某种错误消息，终止和（或）重新启动应用程序
//                println("CoroutineExceptionHandler got $exception")
//            }
//            val job = GlobalScope.launch(context = handler) { // 根协程，运行在 GlobalScope 中
//                throw AssertionError()
//            }
//            val deferred = GlobalScope.async(handler) { // 同样是根协程，但使用 async 代替了 launch
//                throw ArithmeticException() // 没有打印任何东西，依赖用户去调用 deferred.await()并自己tryCatch
//            }
//            joinAll(job, deferred)
//        }

        val handlerTop = CoroutineExceptionHandler { _, exception ->
            //这个handler设置了和没设置一样
            println("CoroutineExceptionHandler got $exception (handlerTop)")
        }

        runBlocking(handlerTop) {
            val handler = CoroutineExceptionHandler { _, exception ->
                //所有的子协程都结束后，才会执行这里的逻辑，CoroutineExceptionHandler 的实例并不用于子协程
                //回调这里时，协程已结束。这里用来记录和查看异常
                println("CoroutineExceptionHandler got $exception")//4
            }
            val job = GlobalScope.launch(handler) {
                launch { // 第一个子协程
                    try {
                        delay(Long.MAX_VALUE)
                    } finally {
                        withContext(NonCancellable) {
                            println("Children are cancelled, but exception is not handled until all children terminate")//2
                            delay(100)
                            println("The first child finished its non cancellable block")//3
                        }
                    }
                }
                launch { // 第二个子协程
                    delay(10)
                    println("Second child throws an exception")//1
                    throw ArithmeticException()
                }
            }
            job.join()
            println("I am runBlocking last")//无join()最先输出1，有join()最后输出5
        }

    }

    fun testCancellationException() {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")//4
        }
        runBlocking {
            val job = launch {
                val child = launch {
                    try {
                        delay(Long.MAX_VALUE)
                    } finally {
                        println("Child is cancelled")//2
                    }
                }
                yield()
                println("Cancelling child")//1
                child.cancel()//协程内部使用 CancellationException 来进行取消，这个异常会被所有的处理者忽略
                child.join()
                yield()
                println("Parent is not cancelled")//3
            }
            job.join()
        }
    }

    fun testMultiException() {
        runBlocking {
            val handler = CoroutineExceptionHandler { _, exception ->
                //演示捕获多个异常，suppressed 字段是 Throwable[] 类型的
                //CancellationException 我们不需要处理
                println("CoroutineExceptionHandler got $exception with suppressed ${exception.suppressed.contentToString()}")
            }
            val job = GlobalScope.launch(handler) {
                launch {
                    try {
                        delay(Long.MAX_VALUE) // 当另一个同级的协程因 IOException  失败时，它将被取消
                    } finally {
                        throw ArithmeticException() // 第二个异常
                    }
                }
                launch {
                    delay(100)
                    throw IOException() // 首个异常
                }
                delay(Long.MAX_VALUE)
            }
            job.join()
        }
    }

}