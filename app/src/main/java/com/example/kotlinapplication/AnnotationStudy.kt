package com.example.kotlinapplication

import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties

/**
 * Created by admin on 2023/6/16    19:43
 */
class AnnotationStudy {
    // 元编程：把程序作为输入或者输出！程序既是数据 数据既是程序。
    // 常见的元编程技术：反射、动态执行代码(把文本作为代码运行)、代码编译器

    fun toMap(a: User): Map<String, Any> {
        return hashMapOf("name" to a.name, "age" to a.age)
    }

    // Kotlin 中的反射
    // KClass 通过 (对象/类名 + ::class) 语法获取
    // kClass 有个字段为 java 表示KClass对应的java类
    private fun <A : Any> toMap2(a: A) {
        val result = a::class.members.map {
            if (it is KProperty) {
                val p = it
                p.name to p.call(a)
            } else {
                "function" to it.name
            }
        }.toMap()
        println(result)
    }

    /**
     * 通过反射去修改一个可变属性
     */
    private fun <A : Any> changeMoney(a: A) {
        a::class.memberProperties.apply {
            for (it in this) {
                when (it) {
                    is KMutableProperty<*> -> {
                        it.setter.call(a, 100)
                    }

                    else -> {}
                }
            }
        }
        println(a)
    }

    private fun <A : Any> studyParameters(a: A) {
        a::class.members.apply {
            for (it in this) {
                println("name = ${it.name} parameters = ${it.parameters}")//参数列表
                println("name = ${it.name} kType = ${it.returnType}")//返回值类型
                println("name = ${it.name} typeParameters = ${it.typeParameters}")//泛型类型，无泛型，返回一个空List
            }
        }
    }

    fun studyAnnotation() {
        studyParameters(User("andy", 12, -1))
    }

    fun parseAnnotation() {
        //通过反射获取注解
        val hero = Hero("andy", 10000)
        hero::class.memberProperties.apply {
            for (item in this) {
                for (an in item.annotations) {
                    println(" ${item.name} has annotations = $an")
                }
            }
        }
        val value = hero::class.annotations.find { it is MyAnnotation } as? MyAnnotation
        value?.apply {
            println("找到类上的注解,携带的数据为 = ${this.bar}")
        }
    }

}

data class User(val name: String, val age: Int, var money: Int) {
    fun <B> get(a: B): B {
        return a
    }
}

/**
 * 定义一个注解
 *
 * 参数只能val修饰
 *
 * 参数类型：基本类型、字符串、Class对象、其他注解、以及上述类型数组
 *
 * 元注解：Kotlin 不支持 Inherited，其他和 java 基本一致
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable()//表示注解在单个元素上可重复
@MustBeDocumented
annotation class MyAnnotation(val bar: String)

@MyAnnotation("class")
data class Hero(
    @property:MyAnnotation("property") val name: String,
    @field:MyAnnotation("field") val initHp: Int
)

/**
 * 精确指定注解的使用位置。(如不指定，Kotlin会帮我选择一个位置)
 * file (文件)
 * property（具有此目标的注解对 Java 不可见）
 * field (标注 Java 字段)
 * get（属性 getter）
 * set（属性 setter）
 * receiver（扩展函数或属性的接收者参数）
 * param（构造函数参数）
 * setparam（属性 setter 参数）
 * delegate（为委托属性存储其委托实例的字段）
 */