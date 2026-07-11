package com.job2day.jobsincanada.service

import android.content.Context
import android.content.SharedPreferences

object BookmarkService {
    private const val PREFS_NAME = "saved_jobs_prefs"
    private const val KEY_SAVED_JOBS = "saved_jobs_ids"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getSavedJobIds(context: Context): Set<Int> {
        val prefs = getPrefs(context)
        val stringSet = prefs.getStringSet(KEY_SAVED_JOBS, emptySet()) ?: emptySet()
        return stringSet.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun isSaved(context: Context, jobId: Int): Boolean {
        return getSavedJobIds(context).contains(jobId)
    }

    fun toggleSave(context: Context, jobId: Int) {
        val prefs = getPrefs(context)
        val currentIds = getSavedJobIds(context).toMutableSet()
        if (currentIds.contains(jobId)) {
            currentIds.remove(jobId)
        } else {
            currentIds.add(jobId)
        }
        prefs.edit().putStringSet(KEY_SAVED_JOBS, currentIds.map { it.toString() }.toSet()).apply()
    }
}
