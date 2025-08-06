@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.anantmittal.ecellkmp.utility.domain

import platform.Foundation.NSLog

actual object AppLogger {
    actual fun d(tag: String, message: String) {
        NSLog("DEBUG [$tag]: $message")
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        val fullMessage = "ERROR [$tag]: $message" + (throwable?.let { "\n${it.stackTraceToString()}" } ?: "")
        NSLog(fullMessage)
    }

    actual fun i(tag: String, message: String) {
        NSLog("INFO [$tag]: $message")
    }

    actual fun w(tag: String, message: String, throwable: Throwable?) {
        val fullMessage = "WARN [$tag]: $message" + (throwable?.let { "\n${it.stackTraceToString()}" } ?: "")
        NSLog(fullMessage)
    }

    actual fun v(tag: String, message: String) {
        NSLog("VERBOSE [$tag]: $message")
    }
}