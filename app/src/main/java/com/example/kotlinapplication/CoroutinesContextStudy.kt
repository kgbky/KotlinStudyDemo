package com.example.kotlinapplication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

/**
 * Created by lenovo on 2023/6/24    20:20
 */
class CoroutinesContextStudy {
    //Coroutine context 包含一个  CoroutineDispatcher 它确定了协程执行在那些线程上
    //CoroutineDispatcher 可以指定协程：在一个线程执行、分派到一个线程池执行、不受限制的执行
    //所有的协程构建器都支持 CoroutineContext 参数(可选)、该参数指定调度器

    fun studyCoroutinesContext() {
        runBlocking {
            launch { // 运行在父协程的上下文中，即 runBlocking 主协程
                println("main runBlocking : I'm working in thread ${Thread.currentThread().name}")
            }
            launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
                println("Unconfined : I'm working in thread ${Thread.currentThread().name}")
            }
            launch(Dispatchers.Default) { // 将会获取默认调度器
                println("Default : I'm working in thread ${Thread.currentThread().name}")
            }
            launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
                println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
            }
        }

    }

}