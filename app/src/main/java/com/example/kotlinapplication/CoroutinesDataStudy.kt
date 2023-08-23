package com.example.kotlinapplication

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

/**
 * Created by admin on 2023/8/22    14:16
 *
 * 学习如何处理：协程间数据共享
 */
class CoroutinesDataStudy {
    //Dispatchers.Default 和 Dispatchers.IO 协程调度器，其内部都是使用共享线程池实现。
    //所以会遇到 多线程间的 数据安全问题

    private var counter = 0

    fun study() {
        runBlocking {
            withContext(Dispatchers.Default) {
                massiveRun {
                    counter++
                }
            }
            //并不会打印100000
            println("Counter = $counter")
        }
    }

    //一中解决办法：是使用线程安全的数据结构
    private val counter2 = AtomicInteger()

    fun study2() {
        runBlocking {
            withContext(Dispatchers.Default) {
                massiveRun {
                    counter2.incrementAndGet()
                }
            }
            //打印100000
            println("Counter2 = $counter2")
        }
    }

    //一中解决办法：使用Mutex，类似 java 中的 synchronized
    private val mutex = Mutex()
    private var counter3 = 0

    fun study3() {
        runBlocking {
            withContext(Dispatchers.Default) {
                massiveRun {
                    // 用锁保护每次自增
                    // 因为同一时间只能有一个线程执行大括号的代码、所以加锁会极大提高执行时间
                    mutex.withLock { counter3++ }
                }
            }
            //打印100000
            println("counter3 = $counter3")
        }
    }

    // 一中解决办法：使用 actor
    // actor 在高负载下比锁更有效，因为在这种情况下它总是有工作要做，而且根本不需要切换到不同的上下文。
    // 计数器 Actor 的各种类型
    sealed class CounterMsg
    object IncCounter : CounterMsg() // 递增计数器的单向消息
    class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // 携带回复的请求

    fun studyActors() {
        runBlocking {
            val counter = counterActor() // 创建该 actor
            withContext(Dispatchers.Default) {
                massiveRun {
                    counter.send(IncCounter)
                }
            }
            // 发送一条消息以用来从一个 actor 中获取计数值
            val response = CompletableDeferred<Int>()
            counter.send(GetCounter(response))
            println("Counter = ${response.await()}")
            counter.close() // 关闭该actor
        }
    }

    // 这个函数启动一个新的计数器 actor
    private fun CoroutineScope.counterActor() = actor<CounterMsg> {
        var counter = 0 // actor 状态
        for (msg in channel) { // 即将到来消息的迭代器
            when (msg) {
                is IncCounter -> counter++
                is GetCounter -> msg.response.complete(counter)
            }
        }
    }

    private suspend fun massiveRun(action: suspend () -> Unit) {
        val n = 100  // 启动的协程数量
        val k = 1000 // 每个协程重复执行同一动作的次数
        val time = measureTimeMillis {
            coroutineScope { // 协程的作用域
                repeat(n) {
                    launch {
                        repeat(k) { action() }
                    }
                }
            }
        }
        println("Completed ${n * k} actions in $time ms")
    }

}