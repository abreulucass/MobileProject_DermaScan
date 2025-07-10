package br.com.abreulucas.mobileproject2.common.utils

fun getCurrentTimestamp(): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date())
}
