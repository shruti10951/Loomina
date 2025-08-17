package com.shrujan.loomina.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


// Extension property on Context
val Context.dataStore by preferencesDataStore("user_prefs")


class UserPreferences (private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("access_token")
    }

    // save token
    suspend fun saveToken(token: String){
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    // read token
    val token: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[TOKEN_KEY]
    }

    // clear token
    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }

}