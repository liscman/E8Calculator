package com.liscman.e8calculator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform