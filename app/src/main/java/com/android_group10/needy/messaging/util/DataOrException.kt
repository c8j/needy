package com.android_group10.needy.messaging.util

data class DataOrException<T, E : Exception?>(val data: T, val exception: E)
