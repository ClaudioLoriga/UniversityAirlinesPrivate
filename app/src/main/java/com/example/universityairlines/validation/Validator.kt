package com.example.universityairlines.validation

import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doOnTextChanged

fun setupValidation(buttonToEnable: Button, vararg viewsToCheck: EditText) =
    viewsToCheck.forEach {
        it.doOnTextChanged { _, _, _, _ -> validate(buttonToEnable, viewsToCheck) }
    }

private fun validate(buttonToEnabled: Button, viewsToCheck: Array<out EditText>)  {
    buttonToEnabled.isEnabled = viewsToCheck.none { it.text.isBlank() }
}