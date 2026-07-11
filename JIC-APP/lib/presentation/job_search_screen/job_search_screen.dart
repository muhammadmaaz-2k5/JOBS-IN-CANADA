import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:google_fonts/google_fonts.dart';

import '../../routes/app_routes.dart';
import '../../theme/app_theme.dart';
import '../../widgets/empty_state_widget.dart';
import '../../widgets/loading_skeleton_widget.dart';
import './widgets/filter_bottom_sheet_widget.dart';
import './widgets/search_filter_bar_widget.dart';
import './widgets/search_job_card_widget.dart';

// TODO: Replace with Riverpod/Bloc for production state management

class JobSearchScreen extends StatefulWidget {
  const JobSearchScreen({super.key});

  @override
  State<JobSearchScreen> createState() => _JobSearchScreenState();
}

class _JobSearchScreenState extends State<JobSearchScreen> {
  final TextEditingController _searchController = TextEditingController();
  final ScrollController _scrollController = ScrollController();
  final bool _isLoading = false;
  String _searchQuery = '';
  String _sortBy = 'Relevance';
  Map<String, dynamic> _activeFilters = {};

  final List<String> _sortOptions = [
    'Relevance',
    'Most Recent',
    'Highest Salary',
    'Most Applicants',
  ];

  // TODO: Replace with Riverpod/Bloc — fetch from backend
  final List<Map<String, dynamic>> _allJobMaps = [
    {
      'id': 'search_001',
      'title': 'Senior iOS Developer',
      'company': 'TD Bank',
      'companyLogo':
          'https://images.pexels.com/photos/259249/pexels-photo-259249.jpeg?w=80&h=80&fit=crop',
      'companyLogoSemanticLabel':
          'TD Bank financial institution logo with green background',
      'salary': '\$145K',
      'salaryMin': 145000,
      'salaryPeriod': 'year',
      'location': 'Toronto, ON',
      'jobType': 'Full-Time',
      'isRemote': false,
      'isNew': true,
      'applicants': 62,
      'category': 'Engineering',
      'province': 'Ontario',
      'postedDaysAgo': 0,
      'isFeatured': false,
      'isSaved': false,
      'applicantAvatars': [],
      'applyUrl': 'https://jobs.td.com',
      'skills': ['Swift', 'Xcode', 'UIKit', 'Core Data'],
      'description':
          'Join TD\'s mobile engineering team to build next-generation banking experiences for millions of Canadians.',
    },
    {
      'id': 'search_002',
      'title': 'Data Scientist',
      'company': 'Bombardier',
      'companyLogo':
          'https://img.rocket.new/generatedImages/rocket_gen_img_1070c7b03-1783736381221.png',
      'companyLogoSemanticLabel':
          'Bombardier aerospace and transportation company logo',
      'salary': '\$130K',
      'salaryMin': 130000,
      'salaryPeriod': 'year',
      'location': 'Montréal, QC',
      'jobType': 'Full-Time',
      'isRemote': false,
      'isNew': false,
      'applicants': 38,
      'category': 'Data',
      'province': 'Quebec',
      'postedDaysAgo': 1,
      'isFeatured': false,
      'isSaved': true,
      'applicantAvatars': [],
      'applyUrl': 'https://jobs.bombardier.com',
      'skills': ['Python', 'ML', 'SQL', 'Tableau'],
      'description':
          'Use machine learning to optimize aircraft manufacturing and supply chain operations.',
    },
    {
      'id': 'search_003',
      'title': 'Marketing Manager',
      'company': 'Lululemon',
      'companyLogo':
          'https://images.pexels.com/photos/1536619/pexels-photo-1536619.jpeg?w=80&h=80&fit=crop',
      'companyLogoSemanticLabel':
          'Lululemon athletic apparel brand lifestyle photo',
      'salary': '\$95K',
      'salaryMin': 95000,
      'salaryPeriod': 'year',
      'location': 'Vancouver, BC',
      'jobType': 'Full-Time',
      'isRemote': false,
      'isNew': false,
      'applicants': 91,
      'category': 'Marketing',
      'province': 'British Columbia',
      'postedDaysAgo': 2,
      'isFeatured': false,
      'isSaved': false,
      'applicantAvatars': [],
      'applyUrl': 'https://info.lululemon.com/careers',
      'skills': ['Brand Strategy', 'Digital Marketing', 'Analytics'],
      'description':
          'Drive brand awareness and demand generation for Lululemon\'s Canadian market.',
    },
    {
      'id': 'search_004',
      'title': 'DevOps Engineer',
      'company': 'Telus',
      'companyLogo':
          'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=80&h=80&fit=crop',
      'companyLogoSemanticLabel':
          'Telus telecommunications company logo with purple branding',
      'salary': '\$125K',
      'salaryMin': 125000,
      'salaryPeriod': 'year',
      'location': 'Calgary, AB',
      'jobType': 'Full-Time',
      'isRemote': true,
      'isNew': true,
      'applicants': 27,
      'category': 'Engineering',
      'province': 'Alberta',
      'postedDaysAgo': 0,
      'isFeatured': false,
      'isSaved': false,
      'applicantAvatars': [],
      'applyUrl': 'https://www.telus.com/careers',
      'skills': ['AWS', 'Kubernetes', 'Terraform', 'CI/CD'],
      'description':
          'Build and maintain cloud infrastructure powering Canada\'s largest telecom network.',
    },
    {
      'id': 'search_005',
      'title': 'Product Manager',
      'company': 'Hootsuite',
      'companyLogo':
          'https://images.pexels.com/photos/1181671/pexels-photo-1181671.jpeg?w=80&h=80&fit=crop',
      'companyLogoSemanticLabel':
          'Hootsuite social media management platform logo',
      'salary': '\$120K',
      'salaryMin': 120000,
      'salaryPeriod': 'year',
      'location': 'Vancouver, BC',
      'jobType': 'Full-Time',
      'isRemote': true,
      'isNew': false,
      'applicants': 54,
      'category': 'Product',
      'province': 'British Columbia',
      'postedDaysAgo': 3,
      'isFeatured': false,
      'isSaved': false,
      'applicantAvatars': [],
      'applyUrl': 'https://www.hootsuite.com/careers',
      'skills': ['Roadmapping', 'Agile', 'Analytics', 'Stakeholder Mgmt'],
      'description':
          'Own the product roadmap for Hootsuite\'s core scheduling features used by 18M users.',
    },
    {
      'id': 'search_006',
      'title': 'Financial Analyst',
      'company': 'Manulife',
      'companyLogo':
          'https://images.pixabay.com/photo/2016/11/27/21/42/stock-1863880_960_720.jpg',
      'companyLogoSemanticLabel':
          'Manulife insurance and financial services company logo',
      'salary': '\$85K',
      'salaryMin': 85000,
      'salaryPeriod': 'year',
      'location': 'Toronto, ON',
      'jobType': 'Full-Time',
      'isRemote': false,
      'isNew': false,
      'applicants': 73,
      'category': 'Finance',
      'province': 'Ontario',
      'postedDaysAgo': 4,
      'isFeatured': false,
      'isSaved': false,
      'applicantAvatars': [],
      'applyUrl': 'https://careers.manulife.com',
      'skills': ['Excel', 'Financial Modeling', 'CFA', 'Bloomberg'],
      'description':
          'Support investment decisions with financial analysis and market research for Manulife\'s Canadian portfolio.',
    },
    {
      'id': 'search_007',
      'title': 'Registered Nurse',
      'company': 'CAMH',
      'companyLogo':
          'https://images.pexels.com/photos/4386466/pexels-photo-4386466.jpeg?w=80&h=80&fit=crop',
      'companyLogoSemanticLabel':
          'Centre for Addiction and Mental Health hospital facility photo',
      'salary': '\$88K',
      'salaryMin': 88000,
      'salaryPeriod': 'year',
      'location': 'Toronto, ON',
      'jobType': 'Full-Time',
      'isRemote': false,
      'isNew': false,
      'applicants': 18,
      'category': 'Healthcare',
      'province': 'Ontario',
      'postedDaysAgo': 1,
      'isFeatured': false,
      'isSaved': false,
      'applicantAvatars': [],
      'applyUrl': 'https://www.camh.ca/careers',
      'skills': ['Patient Care', 'Mental Health', 'RN License', 'EMR'],
      'description':
          'Provide compassionate psychiatric nursing care at Canada\'s leading mental health teaching hospital.',
    },
    {
      'id': 'search_008',
      'title': 'UX Writer',
      'company': 'Intuit',
      'companyLogo':
          'https://img.rocket.new/generatedImages/rocket_gen_img_19938e190-1783736380383.png',
      'companyLogoSemanticLabel':
          'Intuit financial software company logo with blue branding',
      'salary': '\$105K',
      'salaryMin': 105000,
      'salaryPeriod': 'year',
      'location': 'Mississauga, ON',
      'jobType': 'Remote',
      'isRemote': true,
      'isNew': true,
      'applicants': 33,
      'category': 'Design',
      'province': 'Ontario',
      'postedDaysAgo': 0,
      'isFeatured': false,
      'isSaved': false,
      'applicantAvatars': [],
      'applyUrl': 'https://jobs.intuit.com',
      'skills': ['Content Strategy', 'Figma', 'A/B Testing', 'Plain Language'],
      'description':
          'Shape the words that help millions of Canadians file taxes and manage their finances with TurboTax.',
    },
  ];

  List<Map<String, dynamic>> get _filteredJobs {
    List<Map<String, dynamic>> results = List.from(_allJobMaps);

    if (_searchQuery.isNotEmpty) {
      final q = _searchQuery.toLowerCase();
      results = results.where((job) {
        return (job['title'] as String).toLowerCase().contains(q) ||
            (job['company'] as String).toLowerCase().contains(q) ||
            (job['category'] as String).toLowerCase().contains(q) ||
            (job['location'] as String).toLowerCase().contains(q);
      }).toList();
    }

    if (_activeFilters['province'] != null &&
        (_activeFilters['province'] as List).isNotEmpty) {
      final provinces = _activeFilters['province'] as List<String>;
      results = results
          .where((job) => provinces.contains(job['province']))
          .toList();
    }

    if (_activeFilters['jobType'] != null &&
        (_activeFilters['jobType'] as List).isNotEmpty) {
      final types = _activeFilters['jobType'] as List<String>;
      results = results
          .where(
            (job) => types.any(
              (t) => (job['jobType'] as String).toLowerCase().contains(
                t.toLowerCase(),
              ),
            ),
          )
          .toList();
    }

    if (_activeFilters['remoteOnly'] == true) {
      results = results.where((job) => job['isRemote'] == true).toList();
    }

    switch (_sortBy) {
      case 'Most Recent':
        results.sort(
          (a, b) =>
              (a['postedDaysAgo'] as int).compareTo(b['postedDaysAgo'] as int),
        );
        break;
      case 'Highest Salary':
        results.sort(
          (a, b) => (b['salaryMin'] as int).compareTo(a['salaryMin'] as int),
        );
        break;
      case 'Most Applicants':
        results.sort(
          (a, b) => (b['applicants'] as int).compareTo(a['applicants'] as int),
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
        },
      ),
    );
  }

  void _onJobTap(Map<String, dynamic> job) {
    context.push(AppRoutes.jobDetailScreen, extra: job);
  }

  @override
  void dispose() {
    _searchController.dispose();
    _scrollController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final isTablet = MediaQuery.of(context).size.width >= 600;
    final results = _filteredJobs;
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
                    onChanged: (v) => setState(() => _searchQuery = v),
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
                          onTap: () => setState(() {
                            _activeFilters['remoteOnly'] =
                                !(_activeFilters['remoteOnly'] == true);
                          }),
                        ),
                        const SizedBox(width: 8),
                        _QuickChip(
                          label: 'Today',
                          isSelected: _activeFilters['todayOnly'] == true,
                          onTap: () => setState(() {
                            _activeFilters['todayOnly'] =
                                !(_activeFilters['todayOnly'] == true);
                          }),
                        ),
                        const SizedBox(width: 8),
                        _QuickChip(
                          label: '\$100K+',
                          isSelected: _activeFilters['highSalary'] == true,
                          onTap: () => setState(() {
                            _activeFilters['highSalary'] =
                                !(_activeFilters['highSalary'] == true);
                          }),
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
              child: _isLoading
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
                      onCta: () => setState(() {
                        _searchController.clear();
                        _searchQuery = '';
                        _activeFilters = {};
                      }),
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
                            childAspectRatio: 1.35,
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
