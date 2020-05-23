package dev.kingominho.covid_19dashboard.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import dev.kingominho.covid_19dashboard.R
import java.text.NumberFormat

//@BindingAdapter("formattedDate")
//fun TextView.bindDate(date:String) {
//    val converted =  convertFormat(date)
//    val str = "Updated on: $converted"
//    text = str
//}

val numberFormat = requireNotNull(NumberFormat.getInstance())

@BindingAdapter("formattedTotal")
fun TextView.bindTotal(number:Int) {
    val formatted = numberFormat.format(number)
    text = resources.getString(R.string.total_format, formatted)
}

@BindingAdapter("formattedNew")
fun TextView.bindNew(number:Int) {
    val formatted = numberFormat.format(number)
    text = resources.getString(R.string.new_format, formatted)
}

fun Int.formatNumber():String {
    return numberFormat.format(this)
}