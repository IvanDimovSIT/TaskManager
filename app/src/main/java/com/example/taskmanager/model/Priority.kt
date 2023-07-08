package com.example.taskmanager.model

enum class Priority {
    LOW{
        override fun toString():String{
            return "Нисък";
        }
        override fun getValue(): Int {
            return 0
        }
    },
    MEDIUM{
        override fun toString():String{
            return "Среден";
        }
        override fun getValue(): Int {
            return 1
        }

    },
    HIGH{
        override fun toString():String{
            return "Висок";
        }

        override fun getValue(): Int {
            return 2
        }
    };

    abstract fun getValue(): Int
}
