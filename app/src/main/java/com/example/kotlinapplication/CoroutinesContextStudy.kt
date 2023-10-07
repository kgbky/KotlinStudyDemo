package com.example.kotlinapplication

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

/**
 * Created by lenovo on 2023/6/24    20:20
 */
class CoroutinesContextStudy {
    //Coroutine context 包含一个  CoroutineDispatcher 它确定了协程执行在那些线程上
    //CoroutineDispatcher 可以指定协程：在一个线程执行、分派到一个线程池执行、不受限制的执行
    //所有的协程构建器（launch or async）都支持 CoroutineContext 参数(可选)、该参数指定调度器
    //子协程：继承父协程的 coroutineContext，父协程被取消时，子协程会被递归取消
    //子线程：通过 cancel() 取消时，不会取消它的父协程
    //子线程：遇到 CancellationException 以外的异常时，它将使用该异常取消它的父协程
    //父协程：父协程总是等待所有子协程执行结束，父协程内的代码会先执行完毕。默认行为

    fun studyCoroutinesContext() {
        runBlocking {// 主协程
            launch { // 运行在父协程的上下文中，即 runBlocking 主协程
                println("main runBlocking : I'm working in thread ${Thread.currentThread().name}")
            }
            launch(Dispatchers.Unconfined) { // 不受限的——这个例子中将工作在主线程中
                //适合用于执行不消耗CPU时间的任务，不适合更新UI
                //不应该在通常的代码中使用
                //其内部可能会有线程切换
                println("Unconfined : I'm working in thread ${Thread.currentThread().name}")
                delay(500)
                println("Unconfined : after delay I'm working in thread ${Thread.currentThread().name}")
            }
            launch(Dispatchers.Default) { // 将会获取默认调度器，使用共享的后台线程池，适合占用大量CPU资源的操作
                println("Default : I'm working in thread ${Thread.currentThread().name}")
            }
            launch(Dispatchers.IO) { // 执行在IO线程，使用共享的后台线程池
                println("IO : I'm working in thread ${Thread.currentThread().name}")
            }
            launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
                //一个专用的线程是一种非常昂贵的资源
                println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
            }
        }
    }

    fun studyCoroutinesUnconfined() {
        runBlocking {
            launch(Dispatchers.Unconfined) { // 非受限的——将和主线程一起工作
                println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")//在 main 中运行
                delay(500)
                println("Unconfined      : After delay in thread ${Thread.currentThread().name}")//在其他线程恢复执行
            }
            launch { // 父协程的上下文，主 runBlocking 协程
                println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")//在 main 中运行
                delay(1000)
                println("main runBlocking: After delay in thread ${Thread.currentThread().name}")//在 main 中运行
                withContext(Dispatchers.IO) {//同一个协程，代码在不同线程中执行
                    println("withContext runBlocking: I am work in thread ${Thread.currentThread().name}")//在 io 中运行
                }
                println("launch job is ${coroutineContext[Job]}")
            }
            //coroutineContext [Job] 表达式在上下文中检索 Job
            println("My job is ${coroutineContext[Job]}")
        }

    }

    fun studyChildCoroutines() {
        runBlocking {
            val request = launch {
                // 孵化了两个协程, 其中一个通过 GlobalScope 启动。该协程没有父协程
                GlobalScope.launch {
                    println("job1: 我在GlobalScope中运行并独立执行！")
                    delay(1000)
                    println("job1: 我不受取消请求的影响")
                }
                // 另一个则承袭了父协程的上下文
                launch {
                    delay(100)
                    println("job2: 我是请求协程的子项")
                    delay(1000)
                    println("job2: 如果我的父请求被取消，我将不会执行此行")
                }
            }
            delay(500)
            request.cancel() // 取消请求（request）的执行
            delay(1000) // 延迟一秒钟来看看发生了什么
            println("main: 谁在请求取消中幸存下来？")
        }
    }

    fun studyFatherCoroutines() {
        runBlocking {
            // 启动一个协程来处理某种传入请求（request）
            val request = launch {
                repeat(3) { i -> // 启动少量的子作业
                    launch {
                        println("Coroutine child : before delay $i")
                        delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒的时间
                        println("Coroutine $i is done")
                    }
                }
                println("request: I'm done and I don't explicitly join my children that are still active")
            }
            request.join() // 等待请求的完成，包括其所有子协程
            println("Now processing of the request is complete")
        }
    }

    fun main() {
        runBlocking {
            //设置 CoroutineContext 的多个属性。 + 是操作符重载
            val a = async(Dispatchers.IO + CoroutineName("testSettingName")) {
                log("I'm computing a piece of the answer")
                6
            }
            val b = async {
                log("I'm computing another piece of the answer")
                7
            }
            log("The answer is ${a.await() * b.await()}")
            println("执行完毕")
        }
    }

    private val threadLocal: ThreadLocal<String> = object : ThreadLocal<String>() {
        override fun initialValue(): String {
            return System.currentTimeMillis().toString()
        }
    }

    //在协程间共享 ThreadLocal 对象
    fun studyThreadLocal() {
        runBlocking {
            threadLocal.set("main")
            println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
            val job =
                launch(Dispatchers.Default + threadLocal.asContextElement(value = "launchCustom")) {
                    println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
                    yield()
                    println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
                }
            job.join()
            println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        }
    }

}

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")