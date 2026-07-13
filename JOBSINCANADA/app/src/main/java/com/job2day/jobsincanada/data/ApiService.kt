package com.job2day.jobsincanada.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ApiService {
    private const val TAG = "ApiService"
    private var baseUrl: String = "https://moviebox.nazaarabox.com/api"
    var adsEnabled: Boolean = false

    fun initialize(context: android.content.Context) {
        val prefs = context.getSharedPreferences("api_settings", android.content.Context.MODE_PRIVATE)
        baseUrl = prefs.getString("base_url", "https://moviebox.nazaarabox.com/api") ?: "https://moviebox.nazaarabox.com/api"
        Log.d(TAG, "Initialized API Service with Base URL: $baseUrl")
    }

    fun updateBaseUrl(context: android.content.Context, url: String) {
        var cleanUrl = url.trim()
        if (!cleanUrl.startsWith("http://") && !cleanUrl.startsWith("https://")) {
            cleanUrl = "http://$cleanUrl"
        }
        if (cleanUrl.endsWith("/")) {
            cleanUrl = cleanUrl.substring(0, cleanUrl.length - 1)
        }
        baseUrl = cleanUrl
        context.getSharedPreferences("api_settings", android.content.Context.MODE_PRIVATE)
            .edit()
            .putString("base_url", cleanUrl)
            .apply()
        Log.d(TAG, "Updated API Service Base URL to: $baseUrl")
    }

    fun getBaseUrl(): String = baseUrl

    private fun JSONObject.optStringOrNull(key: String, fallbackKey: String? = null): String? {
        if (this.isNull(key)) {
            if (fallbackKey != null && !this.isNull(fallbackKey)) {
                val value = this.optString(fallbackKey)
                return if (value == "null" || value.isEmpty()) null else value
            }
            return null
        }
        val value = this.optString(key)
        if (value == "null" || value.isEmpty()) {
            if (fallbackKey != null && !this.isNull(fallbackKey)) {
                val fbValue = this.optString(fallbackKey)
                return if (fbValue == "null" || fbValue.isEmpty()) null else fbValue
            }
            return null
        }
        return value
    }

    private suspend fun makeGetRequest(endpoint: String): String? = withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null
        try {
            val url = URL("$baseUrl$endpoint")
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            connection.setRequestProperty("Accept", "application/json")

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                return@withContext response.toString()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching from network for $endpoint: ${e.message}")
        } finally {
            connection?.disconnect()
        }
        return@withContext null
    }

    suspend fun getCategories(): List<Category> {
        val jsonStr = makeGetRequest("/categories") ?: throw java.io.IOException("Failed to connect to the server at $baseUrl/categories")
        try {
            val list = mutableListOf<Category>()
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    Category(
                        id = obj.optInt("id"),
                        label = obj.optString("name", obj.optString("label")),
                        icon = obj.optString("icon"),
                        color = obj.optString("color"),
                        count = obj.optInt("count", 0)
                    )
                )
            }
            return list
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing categories: ${e.message}")
            throw e
        }
    }

    suspend fun getCompanies(): List<Company> {
        val jsonStr = makeGetRequest("/companies") ?: throw java.io.IOException("Failed to connect to the server at $baseUrl/companies")
        try {
            val list = mutableListOf<Company>()
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    Company(
                        id = obj.optInt("id"),
                        name = obj.optString("name"),
                        logoUrl = obj.optStringOrNull("logo", "logoUrl") ?: "",
                        website = obj.optString("website")
                    )
                )
            }
            return list
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing companies: ${e.message}")
            throw e
        }
    }

    suspend fun getCareerResources(): List<CareerResource> {
        val jsonStr = makeGetRequest("/career-resources") ?: throw java.io.IOException("Failed to connect to the server at $baseUrl/career-resources")
        try {
            val list = mutableListOf<CareerResource>()
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    CareerResource(
                        id = obj.optInt("id"),
                        title = obj.optString("title"),
                        subtitle = obj.optString("subtitle"),
                        icon = obj.optString("icon"),
                        color = obj.optString("color"),
                        iconColor = obj.optString("icon_color", obj.optString("iconColor")),
                        content = obj.optString("content"),
                        url = obj.optString("url")
                    )
                )
            }
            return list
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing career resources: ${e.message}")
            throw e
        }
    }

    suspend fun getSettings(): Map<String, Int> {
        val jsonStr = makeGetRequest("/settings") ?: throw java.io.IOException("Failed to connect to the server at $baseUrl/settings")
        try {
            val obj = JSONObject(jsonStr)
            
            val settingsMap = mutableMapOf<String, String>()
            val keys = obj.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                settingsMap[key] = obj.optString(key, "")
            }
            com.job2day.jobsincanada.utils.AdManager.applySettings(settingsMap)
            
            adsEnabled = com.job2day.jobsincanada.utils.AdManager.isAdsEnabled
            
            return mapOf(
                "jobsToday" to obj.optInt("jobsToday", 4),
                "jobsThisWeek" to obj.optInt("jobsThisWeek", 15)
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing settings: ${e.message}")
            throw e
        }
    }

    suspend fun getJobs(
        query: String? = null,
        category: String? = null,
        featured: Boolean? = null,
        remote: Boolean? = null,
        type: String? = null,
        province: String? = null,
        today: Boolean? = null,
        minSalary: Int? = null,
        page: Int = 1,
        perPage: Int = 20
    ): Map<String, Any> {
        val params = mutableListOf<String>()
        params.add("page=$page")
        params.add("per_page=$perPage")
        if (!query.isNullOrEmpty()) params.add("q=$query")
        if (!category.isNullOrEmpty()) params.add("category=$category")
        if (featured != null) params.add("featured=${if (featured) 1 else 0}")
        if (remote != null) params.add("remote=${if (remote) 1 else 0}")
        if (!type.isNullOrEmpty()) params.add("type=$type")
        if (!province.isNullOrEmpty()) params.add("province=$province")
        if (today != null) params.add("today=${if (today) 1 else 0}")
        if (minSalary != null) params.add("min_salary=$minSalary")

        val queryStr = "?" + params.joinToString("&")
        val jsonStr = makeGetRequest("/jobs$queryStr") ?: throw java.io.IOException("Failed to connect to the server at $baseUrl/jobs")

        try {
            val root = JSONObject(jsonStr)
            val dataArray = root.getJSONArray("data")
            val jobsList = mutableListOf<JobListing>()
            for (i in 0 until dataArray.length()) {
                val obj = dataArray.getJSONObject(i)
                
                val skillsArr = obj.optJSONArray("skills")
                val skillsList = mutableListOf<String>()
                if (skillsArr != null) {
                    for (j in 0 until skillsArr.length()) {
                        skillsList.add(skillsArr.getString(j))
                    }
                }

                val avatarsArr = obj.optJSONArray("applicantAvatars")
                val avatarsList = mutableListOf<String>()
                if (avatarsArr != null) {
                    for (j in 0 until avatarsArr.length()) {
                        avatarsList.add(avatarsArr.getString(j))
                    }
                } else {
                    avatarsList.addAll(listOf(
                        "https://randomuser.me/api/portraits/men/32.jpg",
                        "https://randomuser.me/api/portraits/women/44.jpg",
                        "https://randomuser.me/api/portraits/men/12.jpg"
                    ))
                }

                // Resolve nested company object if present
                var compName = obj.optString("company")
                var compLogo = obj.optStringOrNull("companyLogo", "logo") ?: ""
                if (obj.has("company_relation") && !obj.isNull("company_relation")) {
                    val compObj = obj.getJSONObject("company_relation")
                    compName = compObj.optString("name", compName)
                    compLogo = compObj.optStringOrNull("logo", "companyLogo") ?: compLogo
                }

                jobsList.add(
                    JobListing(
                        id = obj.optInt("id"),
                        title = obj.optString("title"),
                        company = compName,
                        companyLogo = compLogo,
                        category = obj.optString("category_name", obj.optString("category", "Engineering")),
                        salary = obj.optString("salary", "$100K"),
                        salaryPeriod = obj.optString("salary_period", "year"),
                        salaryMin = obj.optInt("salary_min", 100000),
                        location = obj.optString("location", "Toronto, ON"),
                        province = obj.optString("province", "Ontario"),
                        jobType = obj.optString("job_type", "Full-Time"),
                        isRemote = obj.optInt("is_remote", 0) == 1 || obj.optBoolean("is_remote", false),
                        isNew = obj.optInt("is_new", 0) == 1 || obj.optBoolean("is_new", false),
                        isFeatured = obj.optInt("is_featured", 0) == 1 || obj.optBoolean("is_featured", false),
                        applicants = obj.optInt("applicants", 12),
                        applyUrl = obj.optString("apply_url", "https://google.com"),
                        description = obj.optString("description", ""),
                        skills = skillsList,
                        postedDaysAgo = obj.optInt("postedDaysAgo", 1),
                        applicantAvatars = avatarsList
                    )
                )
            }
            return mapOf(
                "data" to jobsList,
                "current_page" to root.optInt("current_page", 1),
                "last_page" to root.optInt("last_page", 1)
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing jobs: ${e.message}")
            throw e
        }
    }

    suspend fun getJob(id: Int): JobListing = withContext(Dispatchers.IO) {
        val jsonStr = makeGetRequest("/jobs/$id") ?: throw java.io.IOException("Failed to connect to the server at $baseUrl/jobs/$id")
        try {
            val obj = JSONObject(jsonStr)
            
            val skillsArr = obj.optJSONArray("skills")
            val skillsList = mutableListOf<String>()
            if (skillsArr != null) {
                for (j in 0 until skillsArr.length()) {
                    skillsList.add(skillsArr.getString(j))
                }
            }

            val avatarsArr = obj.optJSONArray("applicantAvatars")
            val avatarsList = mutableListOf<String>()
            if (avatarsArr != null) {
                for (j in 0 until avatarsArr.length()) {
                    avatarsList.add(avatarsArr.getString(j))
                }
            } else {
                avatarsList.addAll(listOf(
                    "https://randomuser.me/api/portraits/men/32.jpg",
                    "https://randomuser.me/api/portraits/women/44.jpg",
                    "https://randomuser.me/api/portraits/men/12.jpg"
                ))
            }

            var compName = obj.optString("company")
            var compLogo = obj.optStringOrNull("companyLogo", "logo") ?: ""
            if (obj.has("company_relation") && !obj.isNull("company_relation")) {
                val compObj = obj.getJSONObject("company_relation")
                compName = compObj.optString("name", compName)
                compLogo = compObj.optStringOrNull("logo", "companyLogo") ?: compLogo
            }

            return@withContext JobListing(
                id = obj.optInt("id"),
                title = obj.optString("title"),
                company = compName,
                companyLogo = compLogo,
                category = obj.optString("category_name", obj.optString("category", "Engineering")),
                salary = obj.optString("salary", "$100K"),
                salaryPeriod = obj.optString("salary_period", "year"),
                salaryMin = obj.optInt("salary_min", 100000),
                location = obj.optString("location", "Toronto, ON"),
                province = obj.optString("province", "Ontario"),
                jobType = obj.optString("job_type", "Full-Time"),
                isRemote = obj.optInt("is_remote", 0) == 1 || obj.optBoolean("is_remote", false),
                isNew = obj.optInt("is_new", 0) == 1 || obj.optBoolean("is_new", false),
                isFeatured = obj.optInt("is_featured", 0) == 1 || obj.optBoolean("is_featured", false),
                applicants = obj.optInt("applicants", 12),
                applyUrl = obj.optString("apply_url", "https://google.com"),
                description = obj.optString("description", ""),
                skills = skillsList,
                postedDaysAgo = obj.optInt("postedDaysAgo", 1),
                applicantAvatars = avatarsList
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing job: ${e.message}")
            throw e
        }
    }

    suspend fun getCareerResourceById(id: Int): CareerResource? {
        val resources = getCareerResources()
        return resources.find { it.id == id }
    }
}

