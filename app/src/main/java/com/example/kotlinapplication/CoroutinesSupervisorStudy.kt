package com.example.kotlinapplication

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.yield

/**
 * Created by admin on 2023/6/28    16:29
 */
class CoroutinesSupervisorStudy {

    fun testSupervisorJob() {
        runBlocking {
            //协程的(失败)默认是双向转播的，SupervisorJob 的(失败)只会向下传播
            //这表示：子协程之间不会相互影响
            val supervisor = SupervisorJob()
            with(CoroutineScope(coroutineContext + supervisor)) {
                // 启动第一个子作业——这个示例将会忽略它的异常（不要在实践中这么做！）
                val firstChild =
                    launch(CoroutineExceptionHandler { _, exception -> println("CoroutineExceptionHandler got $exception") }) {
                        println("The first child is failing")//1
                        throw AssertionError("The first child is cancelled")
                    }
                // 启动第二个子作业
                val secondChild = launch {
                    firstChild.join()
                    // 取消了第一个子作业且没有传播给第二个子作业
                    println("The first child is cancelled: ${firstChild.isCancelled}, but the second one is still active")//2
                    try {
                        delay(10000)
                    } finally {
                        // 但是取消了监督的传播
                        println("The second child is cancelled because the supervisor was cancelled")//4
                    }
                }
                // 等待直到第一个子作业失败且执行完成
                firstChild.join()
                println("Cancelling the supervisor")//3
                supervisor.cancel()
                secondChild.join()
            }
        }
    }

    fun testSupervisorScope() {
        runBlocking {
            try {
                supervisorScope {
                    val child = launch {
                        try {
                            log("The child is sleeping") //1
                            delay(Long.MAX_VALUE)
                        } finally {
                            log("The child is cancelled")//3
                        }
                    }
                    // 使用 yield 来给我们的子作业一个机会来执行打印
                    yield()
                    log("Throwing an exception from the scope")//2
                    throw AssertionError()//这个异常会向下传播到 child
                }
            } catch (e: AssertionError) {
                log("Caught an assertion error")//4
            }
        }
    }

    fun testSupervisorException() {
        runBlocking {
            val handler = CoroutineExceptionHandler { _, exception ->
                println("CoroutineExceptionHandler got $exception")
            }
            supervisorScope {
                // supervisorScope 中每一个子作业都应该通过异常处理机制 处理自身的异常
                // coroutineScope 中的子协程、设置了ExceptionHandler也不会生效
                val child = launch(handler) {
                    println("The child throws an exception")
                    throw AssertionError()
                }
                println("The scope is completing")
            }
            println("The scope is completed")
        }
    }

}