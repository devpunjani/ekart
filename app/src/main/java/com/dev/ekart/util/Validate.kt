package com.dev.ekart.util

import android.util.Patterns
import android.widget.EditText
import android.widget.TextView

class Validate(private val tv: TextView) {

    fun isEmpty(msg: String): Validate {
        if (tv.text.toString().isEmpty()) {
            throw ValidationException(msg)
        }
        return this
    }

    fun isEmptyWithTrim(msg: String): Validate {
        if (tv.text.toString().trim().isEmpty()) {
            throw ValidationException(msg)
        }
        tv.text = tv.text.toString().trim()
        return this
    }


    fun minLength(size: Int, msg: String): Validate {
        if (tv.text.toString().length < size) {
            throw ValidationException(msg)
        }
        return this
    }

    fun isEmail(msg: String): Validate {
        if (!tv.text.toString().matches(Patterns.EMAIL_ADDRESS.toRegex())) {
            throw ValidationException(msg)
        }
        return this
    }

    fun isEqualTo(et: EditText, msg: String): Validate {
        if (tv.text.toString() != et.text.toString()) {
            throw ValidationException(msg)
        }
        return this
    }

    fun isCharacter(msg: String): Validate {
        if (!tv.text.toString().matches(Regex("[A-Za-z]+"))) {
            throw ValidationException(msg)
        }
        return this
    }

    fun isNumber(msg: String): Validate {
        val text = tv.text.toString()
        if (!text.matches(Regex("-?\\d+(\\.\\d+)?"))) {
            throw ValidationException(msg)
        }
        return this
    }
}

