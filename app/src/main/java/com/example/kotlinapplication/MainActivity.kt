package com.example.kotlinapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.kotlinapplication.ui.theme.KotlinApplicationTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        //开启 kotlin 协程日志调试功能
        System.setProperty("kotlinx.coroutines.debug", "on")

        super.onCreate(savedInstanceState)
        setContent {
            KotlinApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        doSomething()
    }

    override fun onPause() {
        super.onPause()
        mainScope.cancel()//取消该作用域内的所有协程
        //管理携程的生命周期，防止内存泄露
    }

    private fun doSomething() {
        mainScope = MainScope()//scope 被取消之后，每次都需要重新创建
        // 在示例中启动了 10 个协程，且每个都工作了不同的时长
        repeat(10) { i ->
            mainScope.launch {
                delay((i + 1) * 1000L) // 延迟 200 毫秒、400 毫秒、600 毫秒等等不同的时间
                println("[${Thread.currentThread().name}] Coroutine $i is done")
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinApplicationTheme {
        Greeting("Android")
    }
}