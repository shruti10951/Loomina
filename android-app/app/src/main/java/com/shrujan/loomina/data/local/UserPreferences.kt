package com.shrujan.loomina.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


// Create an extension property on Context to initialize a DataStore instance
// "user_prefs" is the name of the DataStore file where preferences will be stored.
val Context.dataStore by preferencesDataStore("user_prefs")

/**
 * UserPreferences class handles storing, retrieving, and clearing user-related
 * preferences (e.g., access tokens) using Jetpack DataStore.
 */
class UserPreferences(private val context: Context) {

    companion object {
        // Preference key for storing the access token
        private val TOKEN_KEY = stringPreferencesKey("access_token")
    }

    /**
     * Saves the given token string into DataStore.
     *
     * @param token the access token to save
     */
    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token  // Store the token value
        }
    }

    /**
     * A Flow that emits the saved token whenever it changes.
     * Returns null if no token is stored.
     */
    val token: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[TOKEN_KEY]  // Retrieve the token value
    }

    /**
     * Clears the saved token from DataStore.
     */
    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)  // Remove the token key-value pair
        }
    }
}
