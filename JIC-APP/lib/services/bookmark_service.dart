import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';

class BookmarkService {
  static const String _key = 'saved_jobs';

  static Future<List<Map<String, dynamic>>> getSavedJobs() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final List<String> list = prefs.getStringList(_key) ?? [];
      return list.map((item) => jsonDecode(item) as Map<String, dynamic>).toList();
    } catch (e) {
      debugPrint('Error getting saved jobs: $e');
      return [];
    }
  }

  static Future<bool> isSaved(int jobId) async {
    final jobs = await getSavedJobs();
    return jobs.any((j) => j['id'] == jobId);
  }

  static Future<void> toggleSave(Map<String, dynamic> job) async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final jobs = await getSavedJobs();
      final int jobId = job['id'] as int;

      final index = jobs.indexWhere((j) => j['id'] == jobId);
      if (index >= 0) {
        jobs.removeAt(index);
      } else {
        // Create a copy of the job map to ensure we don't save references that might fail serialization
        final jobCopy = Map<String, dynamic>.from(job);
        // Make sure isSaved state key is updated to true in the stored item
        jobCopy['isSaved'] = true;
        jobs.add(jobCopy);
      }

      final List<String> stringList = jobs.map((j) => jsonEncode(j)).toList();
      await prefs.setStringList(_key, stringList);
    } catch (e) {
      debugPrint('Error toggling saved job: $e');
    }
  }
}
