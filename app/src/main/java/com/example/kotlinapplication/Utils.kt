package com.example.kotlinapplication

/**
 * Created by lenovo on 2023/5/29    16:36
 */
inline fun <reified T> cast(original: Any): T? = original as? T