package net.ccbluex.liquidbounce.utils.timer

class MSTimer2 {
    var time = -1L
    fun hasTimePassed(MS: Long): Boolean {
        return System.currentTimeMillis() >= time + MS
    }

    fun hasTimeLeft(MS: Long): Long {
        return MS + time - System.currentTimeMillis()
    }

    fun reset() {
        time = System.currentTimeMillis()
    }
}