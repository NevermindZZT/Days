package com.letter.utils

/**
 * 颜色工具
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
object ColorUtils {

    /**
     * 是否是深色颜色
     * @param color Int 颜色
     * @return Boolean {@code true} 深色 {@code false} 浅色
     */
    fun isDeep(color: Int?): Boolean {
        if (color == null) {
            return false
        }
        val bright = (color.and(0x00FF0000).ushr(16) * 0.3
                + color.and(0x0000FF00).ushr(8) * 0.6
                + color.and(0x000000FF) * 0.1)
        return !(bright > 0x80 || color.toLong().and(0xFF000000).ushr(24) < 0x20)
    }

}