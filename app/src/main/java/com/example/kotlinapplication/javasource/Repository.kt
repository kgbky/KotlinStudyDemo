package com.example.kotlinapplication.javasource

/**
 * Created by lenovo on 2023/5/25    22:00
 */
class Repository private constructor() {
    private val users: MutableList<User?>

    // init 块中的代码 和 java 构造函数中的代码 效果一致
    init {
        val user1 = User("Jane", null)
        val user2 = User("John", null)
        val user3 = User("Anne", "Doe")
        users = ArrayList()
        users.add(user1)
        users.add(user2)
        users.add(user3)
    }

    fun getUsers(): List<User?> {
        return users
    }

    val formattedUserNames: List<String?>
        get() {
            val userNames: MutableList<String?> = ArrayList(users.size)
            for (user in users) {
                var name: String? = if (user!!.lastName != null) {
                    if (user.firstName != null) {
                        user.firstName + " " + user.lastName
                    } else {
                        user.lastName
                    }
                } else if (user.firstName != null) {
                    user.firstName
                } else {
                    "Unknown"
                }
                userNames.add(name)
            }
            return userNames
        }

    companion object {
        //伴生对线 等于 java static 修饰的变量
        @Volatile
        private var INSTANCE: Repository? = null
        val instance: Repository?
            get() {
                if (INSTANCE == null) {
                    synchronized(Repository::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = Repository()
                        }
                    }
                }
                return INSTANCE
            }
    }
}