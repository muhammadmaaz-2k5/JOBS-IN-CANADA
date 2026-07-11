import 'dart:io';
import 'package:dio/dio.dart';
import 'package:flutter/foundation.dart';

class ApiService {
  static final ApiService _instance = ApiService._internal();
  factory ApiService() => _instance;

  late final Dio _dio;

  ApiService._internal() {
    String baseUrl;
    if (kIsWeb) {
      baseUrl = 'http://localhost:8000/api';
    } else if (Platform.isAndroid) {
      // 10.0.2.2 points to host localhost in Android Emulator
      baseUrl = 'http://10.0.2.2:8000/api';
    } else {
      baseUrl = 'http://localhost:8000/api';
    }

    _dio = Dio(BaseOptions(
      baseUrl: baseUrl,
      connectTimeout: const Duration(seconds: 10),
      receiveTimeout: const Duration(seconds: 10),
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
    ));

    // Optional: add logging interceptors for debugging
    _dio.interceptors.add(LogInterceptor(
      requestBody: true,
      responseBody: true,
    ));
  }

  Dio get dio => _dio;

  Future<List<Map<String, dynamic>>> getCategories() async {
    try {
      final response = await _dio.get('/categories');
      if (response.data is List) {
        return List<Map<String, dynamic>>.from(
          (response.data as List).map((x) => Map<String, dynamic>.from(x)),
        );
      }
      return [];
    } catch (e) {
      debugPrint('Error fetching categories: $e');
      return [];
    }
  }

  Future<List<Map<String, dynamic>>> getProvinces() async {
    try {
      final response = await _dio.get('/provinces');
      if (response.data is List) {
        return List<Map<String, dynamic>>.from(
          (response.data as List).map((x) => Map<String, dynamic>.from(x)),
        );
      }
      return [];
    } catch (e) {
      debugPrint('Error fetching provinces: $e');
      return [];
    }
  }

  Future<List<Map<String, dynamic>>> getCompanies() async {
    try {
      final response = await _dio.get('/companies');
      if (response.data is List) {
        return List<Map<String, dynamic>>.from(
          (response.data as List).map((x) => Map<String, dynamic>.from(x)),
        );
      }
      return [];
    } catch (e) {
      debugPrint('Error fetching companies: $e');
      return [];
    }
  }

  Future<Map<String, dynamic>> getJobs({
    String? query,
    String? category,
    bool? featured,
    bool? remote,
    String? type,
    String? province,
    bool? isNew,
    bool? today,
    int? minSalary,
    int page = 1,
    int perPage = 20,
  }) async {
    try {
      final queryParams = <String, dynamic>{
        'page': page,
        'per_page': perPage,
      };

      if (query != null && query.isNotEmpty) queryParams['q'] = query;
      if (category != null && category.isNotEmpty) queryParams['category'] = category;
      if (featured != null) queryParams['featured'] = featured ? 1 : 0;
      if (remote != null) queryParams['remote'] = remote ? 1 : 0;
      if (type != null && type.isNotEmpty) queryParams['type'] = type;
      if (province != null && province.isNotEmpty) queryParams['province'] = province;
      if (isNew != null) queryParams['new'] = isNew ? 1 : 0;
      if (today != null) queryParams['today'] = today ? 1 : 0;
      if (minSalary != null) queryParams['min_salary'] = minSalary;

      final response = await _dio.get('/jobs', queryParameters: queryParams);
      if (response.data is Map<String, dynamic>) {
        return response.data;
      }
      return {'data': [], 'current_page': 1, 'last_page': 1};
    } catch (e) {
      debugPrint('Error fetching jobs: $e');
      return {'data': [], 'current_page': 1, 'last_page': 1};
    }
  }

  Future<Map<String, dynamic>?> getJobDetail(dynamic jobId) async {
    try {
      final response = await _dio.get('/jobs/$jobId');
      if (response.data is Map<String, dynamic>) {
        return response.data;
      }
      return null;
    } catch (e) {
      debugPrint('Error fetching job details: $e');
      return null;
    }
  }

  Future<List<Map<String, dynamic>>> getCareerResources() async {
    try {
      final response = await _dio.get('/career-resources');
      if (response.data is List) {
        return List<Map<String, dynamic>>.from(
          (response.data as List).map((x) => Map<String, dynamic>.from(x)),
        );
      }
      return [];
    } catch (e) {
      debugPrint('Error fetching career resources: $e');
      return [];
    }
  }

  Future<Map<String, dynamic>> getStats() async {
    try {
      final response = await _dio.get('/stats');
      if (response.data is Map<String, dynamic>) {
        return response.data;
      }
      return {};
    } catch (e) {
      debugPrint('Error fetching stats: $e');
      return {};
    }
  }

  Future<Map<String, dynamic>> getSettings() async {
    try {
      final response = await _dio.get('/settings');
      if (response.data is Map<String, dynamic>) {
        return response.data;
      }
      return {};
    } catch (e) {
      debugPrint('Error fetching settings: $e');
      return {};
    }
  }
}
