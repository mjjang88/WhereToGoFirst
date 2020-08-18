package com.mjjang.wheretogofirst.manager

import android.content.SharedPreferences
import android.preference.PreferenceManager

object AppPreference {

    fun getInstance(): SharedPreferences {
        return preference
    }

    private val preference: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.applicationContext())

    private operator fun set(key: String, value: Any?) {
        when (value) {
            is String? -> preference.edit().putString(key, value).apply()
            is Int -> preference.edit().putInt(key, value).apply()
            is Boolean -> preference.edit().putBoolean(key, value).apply()
            is Float -> preference.edit().putFloat(key, value).apply()
            is Long -> preference.edit().putLong(key, value).apply()
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    private inline operator fun <reified T : Any> get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> preference.getString(key, defaultValue as? String) as T?
            Int::class -> preference.getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> preference.getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> preference.getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> preference.getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    const val PREF_KEY_SID = "PREF_KEY_SID"

    fun getSid(): Int {
        val result = preference.getInt(PREF_KEY_SID, 0)
        preference.edit().putInt(PREF_KEY_SID, result + 1).apply()
        return result
    }
}