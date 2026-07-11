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
    private const val BASE_URL = "http://10.0.2.2:8000/api"

    private suspend fun makeGetRequest(endpoint: String): String? = withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null
        try {
            val url = URL("$BASE_URL$endpoint")
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
        val jsonStr = makeGetRequest("/categories")
        if (jsonStr == null) {
            Log.d(TAG, "Falling back to categories mock data")
            return MockData.categories
        }
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
            return MockData.categories
        }
    }

    suspend fun getCompanies(): List<Company> {
        val jsonStr = makeGetRequest("/companies")
        if (jsonStr == null) {
            return MockData.companies
        }
        try {
            val list = mutableListOf<Company>()
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    Company(
                        id = obj.optInt("id"),
                        name = obj.optString("name"),
                        logoUrl = obj.optString("logo", obj.optString("logoUrl")),
                        website = obj.optString("website")
                    )
                )
            }
            return list
        } catch (e: Exception) {
            return MockData.companies
        }
    }

    suspend fun getCareerResources(): List<CareerResource> {
        val jsonStr = makeGetRequest("/career-resources")
        if (jsonStr == null) {
            return MockData.careerResources
        }
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
                        url = obj.optString("url")
                    )
                )
            }
            return list
        } catch (e: Exception) {
            return MockData.careerResources
        }
    }

    suspend fun getSettings(): Map<String, Int> {
        val jsonStr = makeGetRequest("/settings")
        if (jsonStr == null) {
            return mapOf("jobsToday" to 4, "jobsThisWeek" to 15)
        }
        try {
            val obj = JSONObject(jsonStr)
            return mapOf(
                "jobsToday" to obj.optInt("jobsToday", 4),
                "jobsThisWeek" to obj.optInt("jobsThisWeek", 15)
            )
        } catch (e: Exception) {
            return mapOf("jobsToday" to 4, "jobsThisWeek" to 15)
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
        val jsonStr = makeGetRequest("/jobs$queryStr")
        if (jsonStr == null) {
            // Apply client side mock filtering
            var filtered = MockData.jobs
            if (!query.isNullOrEmpty()) {
                filtered = filtered.filter { it.title.contains(query, ignoreCase = true) || it.company.contains(query, ignoreCase = true) }
            }
            if (!category.isNullOrEmpty()) {
                filtered = filtered.filter { it.category.equals(category, ignoreCase = true) }
            }
            if (featured != null) {
                filtered = filtered.filter { it.isFeatured == featured }
            }
            if (remote != null) {
                filtered = filtered.filter { it.isRemote == remote }
            }
            if (!type.isNullOrEmpty()) {
                filtered = filtered.filter { it.jobType.equals(type, ignoreCase = true) }
            }
            if (!province.isNullOrEmpty()) {
                filtered = filtered.filter { it.province.equals(province, ignoreCase = true) }
            }
            if (today != null && today) {
                filtered = filtered.filter { it.postedDaysAgo == 0 }
            }
            if (minSalary != null) {
                filtered = filtered.filter { it.salaryMin >= minSalary }
            }

            return mapOf(
                "data" to filtered,
                "current_page" to 1,
                "last_page" to 1
            )
        }

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
                var compLogo = obj.optString("companyLogo", obj.optString("logo"))
                if (obj.has("company_relation")) {
                    val compObj = obj.getJSONObject("company_relation")
                    compName = compObj.optString("name", compName)
                    compLogo = compObj.optString("logo", compLogo)
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
            return mapOf(
                "data" to MockData.jobs,
                "current_page" to 1,
                "last_page" to 1
            )
        }
    }
}
