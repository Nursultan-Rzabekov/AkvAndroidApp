package com.akv.akvandroidapprelease.util

class PasswordChecker {
    companion object{
        fun checkPassword(password: String): Boolean {
            val pattern = Regex("^(?=.*[a-z])(?=.*[A-Z]).*.{8,}\$")

            return pattern.matches(password)
        }
    }
}