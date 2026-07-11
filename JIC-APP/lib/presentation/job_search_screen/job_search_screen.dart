import 'dart:async';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:google_fonts/google_fonts.dart';

import '../../routes/app_routes.dart';
import '../../theme/app_theme.dart';
import '../../services/api_service.dart';
import '../../widgets/empty_state_widget.dart';
import '../../widgets/loading_skeleton_widget.dart';
import './widgets/filter_bottom_sheet_widget.dart';
import './widgets/search_filter_bar_widget.dart';
import './widgets/search_job_card_widget.dart';

// TODO: Replace with Riverpod/Bloc for production state management

class JobSearchScreen extends StatefulWidget {
  final Map<String, dynamic>? initialFilters;

  const JobSearchScreen({this.initialFilters, super.key});

  @override
  State<JobSearchScreen> createState() => _JobSearchScreenState();
}

class _JobSearchScreenState extends State<JobSearchScreen> {
  final TextEditingController _searchController = TextEditingController();
  final ScrollController _scrollController = ScrollController();
  
  bool _isLoading = false;
  String _searchQuery = '';
  String _sortBy = 'Relevance';
  Map<String, dynamic> _activeFilters = {};
  
  List<Map<String, dynamic>> _jobs = [];
  int _currentPage = 1;
  int _lastPage = 1;
  Timer? _debounce;

  final List<String> _sortOptions = [
    'Relevance',
    'Most Recent',
    'Highest Salary',
    'Most Applicants',
  ];

  @override
  void initState() {
    super.initState();
    if (widget.initialFilters != null) {
      _activeFilters = Map<String, dynamic>.from(widget.initialFilters!);
    }
    _fetchJobs(isRefresh: true);
    _scrollController.addListener(_onScroll);
  }

  void _onScroll() {
    if (_scrollController.position.pixels >= _scrollController.position.maxScrollExtent - 200) {
      if (!_isLoading && _currentPage < _lastPage) {
        _currentPage++;
        _fetchJobs();
      }
    }
  }

  Future<void> _fetchJobs({bool isRefresh = false}) async {
    if (!mounted) return;
    if (_isLoading && !isRefresh) return;

    setState(() {
      _isLoading = true;
      if (isRefresh) {
        _currentPage = 1;
      }
    });

    try {
      final api = ApiService();

      String? category = _activeFilters['category'] as String?;
      bool? remote = _activeFilters['remoteOnly'] as bool?;

      String? type;
      if (_activeFilters['jobType'] != null && (_activeFilters['jobType'] as List).isNotEmpty) {
        type = (_activeFilters['jobType'] as List).first as String;
      }

      String? province;
      if (_activeFilters['province'] != null && (_activeFilters['province'] as List).isNotEmpty) {
        province = (_activeFilters['province'] as List).first as String;
      }

      bool? today = _activeFilters['todayOnly'] as bool?;
      int? minSalary = _activeFilters['highSalary'] == true ? 100000 : null;

      final results = await api.getJobs(
        query: _searchQuery,
        category: category,
        remote: remote,
        type: type,
        province: province,
        today: today,
        minSalary: minSalary,
        page: _currentPage,
        perPage: 20,
      );

      final List<dynamic> jobsList = results['data'] as List? ?? [];
      final int lastPageVal = results['last_page'] as int? ?? 1;

      if (mounted) {
        setState(() {
          final newJobs = List<Map<String, dynamic>>.from(jobsList);
          if (isRefresh) {
            _jobs = newJobs;
          } else {
            _jobs.addAll(newJobs);
          }
          _lastPage = lastPageVal;
          _isLoading = false;
        });
      }
    } catch (e) {
      debugPrint('Error fetching jobs: $e');
      if (mounted) {
        setState(() => _isLoading = false);
      }
    }
  }

  void _onSearchChanged(String query) {
    if (_debounce?.isActive ?? false) _debounce!.cancel();
    _debounce = Timer(const Duration(milliseconds: 500), () {
      setState(() => _searchQuery = query);
      _fetchJobs(isRefresh: true);
    });
  }

  List<Map<String, dynamic>> get _sortedJobs {
    List<Map<String, dynamic>> results = List.from(_jobs);

    switch (_sortBy) {
      case 'Most Recent':
        results.sort(
          (a, b) =>
              (a['postedDaysAgo'] as int? ?? 0).compareTo(b['postedDaysAgo'] as int? ?? 0),
        );
        break;
      case 'Highest Salary':
        results.sort(
          (a, b) => (b['salaryMin'] as int? ?? 0).compareTo(a['salaryMin'] as int? ?? 0),
        );
        break;
      case 'Most Applicants':
        results.sort(
          (a, b) => (b['applicants'] as int? ?? 0).compareTo(a['applicants'] as int? ?? 0),
        );
        break;
    }

    return results;
  }

  void _openFilterSheet() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      builder: (_) => FilterBottomSheetWidget(
        currentFilters: _activeFilters,
        onApply: (filters) {
          setState(() => _activeFilters = filters);
          _fetchJobs(isRefresh: true);
        },
      ),
    );
  }

  void _onJobTap(Map<String, dynamic> job) {
    context.push(AppRoutes.jobDetailScreen, extra: job).then((_) {
      if (mounted) {
        setState(() {});
      }
    });
  }

  @override
  void dispose() {
    _searchController.dispose();
    _scrollController.dispose();
    _debounce?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final isTablet = MediaQuery.of(context).size.width >= 600;
    final results = _sortedJobs;
    final activeFilterCount = _countActiveFilters();

    return Scaffold(
      backgroundColor: AppTheme.backgroundLight,
      body: SafeArea(
        child: Column(
          children: [
            // Sticky header
            Container(
              color: theme.colorScheme.surface,
              padding: const EdgeInsets.fromLTRB(16, 12, 16, 0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    children: [
                      Expanded(
                        child: Text(
                          'Browse Jobs',
                          style: GoogleFonts.plusJakartaSans(
                            fontSize: 22,
                            fontWeight: FontWeight.w800,
                            color: theme.colorScheme.onSurface,
                          ),
                        ),
                      ),
                      Text(
                        '${results.length} results',
                        style: GoogleFonts.plusJakartaSans(
                          fontSize: 13,
                          color: theme.colorScheme.onSurfaceVariant,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 10),
                  SearchFilterBarWidget(
                    controller: _searchController,
                    onChanged: _onSearchChanged,
                    onFilterTap: _openFilterSheet,
                    activeFilterCount: activeFilterCount,
                  ),
                  const SizedBox(height: 10),
                  // Sort + quick filters row
                  SingleChildScrollView(
                    scrollDirection: Axis.horizontal,
                    child: Row(
                      children: [
                        // Sort dropdown
                        Container(
                          height: 34,
                          padding: const EdgeInsets.symmetric(horizontal: 12),
                          decoration: BoxDecoration(
                            color: theme.colorScheme.surfaceContainerHighest,
                            borderRadius: BorderRadius.circular(100),
                          ),
                          child: DropdownButtonHideUnderline(
                            child: DropdownButton<String>(
                              value: _sortBy,
                              isDense: true,
                              icon: const Icon(
                                Icons.keyboard_arrow_down_rounded,
                                size: 16,
                              ),
                              style: GoogleFonts.plusJakartaSans(
                                fontSize: 12,
                                fontWeight: FontWeight.w600,
                                color: theme.colorScheme.onSurface,
                              ),
                              onChanged: (v) => setState(() => _sortBy = v!),
                              items: _sortOptions
                                  .map(
                                    (s) => DropdownMenuItem(
                                      value: s,
                                      child: Text(s),
                                    ),
                                  )
                                  .toList(),
                            ),
                          ),
                        ),
                        const SizedBox(width: 8),
                        _QuickChip(
                          label: 'Remote Only',
                          isSelected: _activeFilters['remoteOnly'] == true,
                          onTap: () {
                            setState(() {
                              _activeFilters['remoteOnly'] =
                                  !(_activeFilters['remoteOnly'] == true);
                            });
                            _fetchJobs(isRefresh: true);
                          },
                        ),
                        const SizedBox(width: 8),
                        _QuickChip(
                          label: 'Today',
                          isSelected: _activeFilters['todayOnly'] == true,
                          onTap: () {
                            setState(() {
                              _activeFilters['todayOnly'] =
                                  !(_activeFilters['todayOnly'] == true);
                            });
                            _fetchJobs(isRefresh: true);
                          },
                        ),
                        const SizedBox(width: 8),
                        _QuickChip(
                          label: '\$100K+',
                          isSelected: _activeFilters['highSalary'] == true,
                          onTap: () {
                            setState(() {
                              _activeFilters['highSalary'] =
                                  !(_activeFilters['highSalary'] == true);
                            });
                            _fetchJobs(isRefresh: true);
                          },
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 12),
                ],
              ),
            ),
            // Results
            Expanded(
              child: _isLoading && _jobs.isEmpty
                  ? ListView.builder(
                      padding: const EdgeInsets.all(16),
                      itemCount: 5,
                      itemBuilder: (_, __) => const JobCardSkeletonWidget(),
                    )
                  : results.isEmpty
                      ? EmptyStateWidget(
                          icon: Icons.search_off_rounded,
                          title: 'No jobs found',
                          subtitle:
                              'Try adjusting your search or filters to find Canadian jobs matching your skills.',
                          ctaLabel: 'Clear Filters',
                          onCta: () {
                            setState(() {
                              _searchController.clear();
                              _searchQuery = '';
                              _activeFilters = {};
                            });
                            _fetchJobs(isRefresh: true);
                          },
                        )
                      : isTablet
                          ? GridView.builder(
                              controller: _scrollController,
                              padding: const EdgeInsets.all(16),
                              gridDelegate:
                                  const SliverGridDelegateWithFixedCrossAxisCount(
                                    crossAxisCount: 2,
                                    crossAxisSpacing: 12,
                                    mainAxisSpacing: 12,
                                    childAspectRatio: 1.15,
                                  ),
                              itemCount: results.length,
                              itemBuilder: (_, i) => SearchJobCardWidget(
                                job: results[i],
                                onTap: () => _onJobTap(results[i]),
                                animationIndex: i,
                              ),
                            )
                          : ListView.builder(
                              controller: _scrollController,
                              padding: const EdgeInsets.all(16),
                              itemCount: results.length,
                              itemBuilder: (_, i) => SearchJobCardWidget(
                                job: results[i],
                                onTap: () => _onJobTap(results[i]),
                                animationIndex: i,
                              ),
                            ),
            ),
          ],
        ),
      ),
    );
  }

  int _countActiveFilters() {
    int count = 0;
    if ((_activeFilters['province'] as List?)?.isNotEmpty == true) count++;
    if ((_activeFilters['jobType'] as List?)?.isNotEmpty == true) count++;
    if (_activeFilters['remoteOnly'] == true) count++;
    if (_activeFilters['todayOnly'] == true) count++;
    if (_activeFilters['highSalary'] == true) count++;
    if (_activeFilters['category'] != null) count++;
    return count;
  }
}

class _QuickChip extends StatelessWidget {
  final String label;
  final bool isSelected;
  final VoidCallback onTap;

  const _QuickChip({
    required this.label,
    required this.isSelected,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return GestureDetector(
      onTap: onTap,
      child: Container(
        height: 34,
        padding: const EdgeInsets.symmetric(horizontal: 14),
        decoration: BoxDecoration(
          color: isSelected
              ? theme.colorScheme.primaryContainer
              : theme.colorScheme.surfaceContainerHighest,
          borderRadius: BorderRadius.circular(100),
          border: isSelected
              ? Border.all(color: theme.colorScheme.primary, width: 1)
              : null,
        ),
        alignment: Alignment.center,
        child: Text(
          label,
          style: GoogleFonts.plusJakartaSans(
            fontSize: 12,
            fontWeight: FontWeight.w500,
            color: isSelected
                ? theme.colorScheme.primary
                : theme.colorScheme.onSurface,
          ),
        ),
      ),
    );
  }
}
