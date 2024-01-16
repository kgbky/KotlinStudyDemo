package com.example.kotlinapplication

/**
 * Created by admin on 2024/1/16    11:09
 *
 * 抽象的，不能被实例化
 *
 * 受限制的继承，子类必须定义在父类所在的package内
 *
 * 枚举只有单个实例；密封类的子类可以有多个实例
 */
sealed class SealedClassStudy

sealed interface SealedInterfaceStudy

object SealedClassImplFirst : SealedClassStudy()

object SealedClassImplSecond : SealedClassStudy()

/**
 * 配合 when 表达式，在编译时可以帮助我们检查错误。
 *
 * 如果已列出所有可能得情况，可省略else分支
 */
fun sealedTest(value: SealedClassStudy) {
    when (value) {
        is SealedClassImplFirst -> {
            println("SealedClassImplFirst")
        }

        is SealedClassImplSecond -> {
            println("SealedClassImplSecond")
        }
    }
}
