import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:google_fonts/google_fonts.dart';

import '../../routes/app_routes.dart';
import '../../theme/app_theme.dart';
import '../../services/api_service.dart';
import './widgets/career_resources_widget.dart';
import './widgets/category_grid_widget.dart';
import './widgets/featured_job_card_widget.dart';
import './widgets/home_app_bar_widget.dart';
import './widgets/home_search_bar_widget.dart';
import './widgets/recommended_card_widget.dart';
import './widgets/today_jobs_banner_widget.dart';
import './widgets/top_companies_widget.dart';

// TODO: Replace with Riverpod/Bloc for production state management

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final ScrollController _scrollController = ScrollController();
  bool _isLoading = false;
  String _selectedCategory = 'All';

  List<String> _filterCategories = ['All'];
  List<Map<String, dynamic>> _categories = [];
  List<Map<String, dynamic>> _companies = [];
  List<Map<String, dynamic>> _featuredJobs = [];
  List<Map<String, dynamic>> _recommendedJobs = [];
  List<Map<String, dynamic>> _careerResources = [];
  int _jobsTodayCount = 0;
  int _jobsThisWeekCount = 0;

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    if (!mounted) return;
    setState(() => _isLoading = true);

    try {
      final api = ApiService();

      // Load all data concurrently
      final results = await Future.wait([
        api.getCategories(),
        api.getCompanies(),
        api.getJobs(featured: true, perPage: 10),
        api.getJobs(perPage: 3), // Recommended (first 3 jobs)
        api.getCareerResources(),
        api.getSettings(),
      ]);

      if (mounted) {
        setState(() {
          _categories = List<Map<String, dynamic>>.from(results[0] as List);
          if (_categories.isNotEmpty) {
            _filterCategories = ['All', ..._categories.map((c) => c['label'] as String? ?? '')];
          }

          _companies = List<Map<String, dynamic>>.from(results[1] as List);

          final featuredData = results[2] as Map<String, dynamic>;
          _featuredJobs = List<Map<String, dynamic>>.from(featuredData['data'] as List? ?? []);

          final recommendedData = results[3] as Map<String, dynamic>;
          _recommendedJobs = List<Map<String, dynamic>>.from(recommendedData['data'] as List? ?? []);

          _careerResources = List<Map<String, dynamic>>.from(results[4] as List);

          final settingsData = results[5] as Map<String, dynamic>;
          _jobsTodayCount = settingsData['jobsToday'] as int? ?? 0;
          _jobsThisWeekCount = settingsData['jobsThisWeek'] as int? ?? 0;

          _isLoading = false;
        });
      }
    } catch (e) {
      debugPrint('Error loading home data: $e');
      if (mounted) {
        setState(() => _isLoading = false);
      }
    }
  }

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }

  void _onJobTap(Map<String, dynamic> job) {
    context.push(AppRoutes.jobDetailScreen, extra: job);
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final isTablet = MediaQuery.of(context).size.width >= 600;

    return Scaffold(
      backgroundColor: AppTheme.backgroundLight,
      body: SafeArea(
        child: RefreshIndicator(
          color: theme.colorScheme.primary,
          onRefresh: _loadData,
          child: CustomScrollView(
            controller: _scrollController,
            slivers: [
              SliverToBoxAdapter(
                child: HomeAppBarWidget(
                  userName: 'Priya Sharma',
                  userAvatarUrl:
                      'https://images.pexels.com/photos/1239291/pexels-photo-1239291.jpeg?w=80&h=80&fit=crop',
                  userAvatarSemanticLabel:
                      'Profile photo of Priya Sharma, South Asian woman with dark hair in professional attire',
                  notificationCount: 3,
                ),
              ),
              SliverToBoxAdapter(
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(16, 4, 16, 16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'Find your',
                        style: GoogleFonts.plusJakartaSans(
                          fontSize: isTablet ? 28 : 24,
                          fontWeight: FontWeight.w500,
                          color: theme.colorScheme.onSurface,
                        ),
                      ),
                      Text(
                        'Dream Job in Canada 🍁',
                        style: GoogleFonts.plusJakartaSans(
                          fontSize: isTablet ? 30 : 26,
                          fontWeight: FontWeight.w800,
                          color: theme.colorScheme.primary,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              SliverPersistentHeader(
                pinned: true,
                delegate: _SearchBarDelegate(
                  onTap: () => context.go(AppRoutes.jobSearchScreen),
                ),
              ),
              SliverToBoxAdapter(
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(16, 12, 0, 0),
                  child: SingleChildScrollView(
                    scrollDirection: Axis.horizontal,
                    child: Row(
                      children: _filterCategories.map((cat) {
                        final isSelected = cat == _selectedCategory;
                        return Padding(
                          padding: const EdgeInsets.only(right: 8),
                          child: FilterChip(
                            label: Text(cat),
                            selected: isSelected,
                            onSelected: (_) {
                              setState(() => _selectedCategory = cat);
                              // Optional: Route to search screen with selected category
                              context.go(
                                AppRoutes.jobSearchScreen,
                                extra: cat != 'All' ? {'category': cat} : null,
                              );
                            },
                            backgroundColor: theme.colorScheme.surface,
                            selectedColor: theme.colorScheme.primaryContainer,
                            labelStyle: GoogleFonts.plusJakartaSans(
                              fontSize: 13,
                              fontWeight: FontWeight.w500,
                              color: isSelected
                                  ? theme.colorScheme.primary
                                  : theme.colorScheme.onSurfaceVariant,
                            ),
                            side: BorderSide(
                              color: isSelected
                                  ? theme.colorScheme.primary
                                  : theme.colorScheme.outline,
                              width: 1,
                            ),
                            shape: const StadiumBorder(),
                            showCheckmark: false,
                          ),
                        );
                      }).toList(),
                    ),
                  ),
                ),
              ),
              SliverToBoxAdapter(
                child: TodayJobsBannerWidget(
                  todayCount: _jobsTodayCount,
                  weekCount: _jobsThisWeekCount,
                ),
              ),
              // Featured Jobs Section
              SliverToBoxAdapter(
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(16, 20, 16, 12),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text('Featured Jobs', style: theme.textTheme.titleMedium),
                      GestureDetector(
                        onTap: () => context.go(AppRoutes.jobSearchScreen),
                        child: Text(
                          'See All',
                          style: GoogleFonts.plusJakartaSans(
                            fontSize: 13,
                            fontWeight: FontWeight.w600,
                            color: theme.colorScheme.primary,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              SliverToBoxAdapter(
                child: _isLoading
                    ? const Padding(
                        padding: EdgeInsets.symmetric(horizontal: 16, vertical: 20),
                        child: Center(
                          child: CircularProgressIndicator(),
                        ),
                      )
                    : Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 16),
                        child: isTablet
                            ? GridView.builder(
                                shrinkWrap: true,
                                physics: const NeverScrollableScrollPhysics(),
                                gridDelegate:
                                    const SliverGridDelegateWithFixedCrossAxisCount(
                                      crossAxisCount: 2,
                                      crossAxisSpacing: 12,
                                      mainAxisSpacing: 12,
                                      childAspectRatio: 1.1,
                                    ),
                                itemCount: _featuredJobs.length,
                                itemBuilder: (_, i) => FeaturedJobCardWidget(
                                  job: _featuredJobs[i],
                                  onTap: () => _onJobTap(_featuredJobs[i]),
                                ),
                              )
                            : Column(
                                children: _featuredJobs
                                    .map(
                                      (job) => Padding(
                                        padding: const EdgeInsets.only(
                                          bottom: 12,
                                        ),
                                        child: FeaturedJobCardWidget(
                                          job: job,
                                          onTap: () => _onJobTap(job),
                                        ),
                                      ),
                                    )
                                    .toList(),
                              ),
                      ),
              ),
              // Recommended Section
              SliverToBoxAdapter(
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(16, 20, 16, 12),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        'Recommended For You',
                        style: theme.textTheme.titleMedium,
                      ),
                      GestureDetector(
                        onTap: () => context.go(AppRoutes.jobSearchScreen),
                        child: Text(
                          'See All',
                          style: GoogleFonts.plusJakartaSans(
                            fontSize: 13,
                            fontWeight: FontWeight.w600,
                            color: theme.colorScheme.primary,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              SliverToBoxAdapter(
                child: _isLoading
                    ? const SizedBox.shrink()
                    : RecommendedSectionWidget(
                        jobs: _recommendedJobs,
                        onJobTap: _onJobTap,
                      ),
              ),
              // Top Companies
              SliverToBoxAdapter(
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(16, 20, 16, 12),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        'Top Companies Hiring',
                        style: theme.textTheme.titleMedium,
                      ),
                      Text(
                        'See All',
                        style: GoogleFonts.plusJakartaSans(
                          fontSize: 13,
                          fontWeight: FontWeight.w600,
                          color: theme.colorScheme.primary,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              SliverToBoxAdapter(
                child: _isLoading
                    ? const SizedBox.shrink()
                    : TopCompaniesWidget(companies: _companies),
              ),
              // Job Categories
              SliverToBoxAdapter(
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(16, 20, 16, 12),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        'Job Categories',
                        style: theme.textTheme.titleMedium,
                      ),
                      Text(
                        'See All',
                        style: GoogleFonts.plusJakartaSans(
                          fontSize: 13,
                          fontWeight: FontWeight.w600,
                          color: theme.colorScheme.primary,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              SliverToBoxAdapter(
                child: _isLoading
                    ? const SizedBox.shrink()
                    : CategoryGridWidget(
                        categories: _categories,
                        onCategoryTap: (cat) {
                          setState(() => _selectedCategory = cat);
                          context.go(
                            AppRoutes.jobSearchScreen,
                            extra: {'category': cat},
                          );
                        },
                      ),
              ),
              // Career Resources
              SliverToBoxAdapter(
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(16, 20, 16, 12),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        'Career Resources',
                        style: theme.textTheme.titleMedium,
                      ),
                      Text(
                        'See All',
                        style: GoogleFonts.plusJakartaSans(
                          fontSize: 13,
                          fontWeight: FontWeight.w600,
                          color: theme.colorScheme.primary,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              SliverToBoxAdapter(
                child: _isLoading
                    ? const SizedBox.shrink()
                    : CareerResourcesWidget(resources: _careerResources),
              ),
              const SliverToBoxAdapter(child: SizedBox(height: 24)),
            ],
          ),
        ),
      ),
    );
  }
}


class _SearchBarDelegate extends SliverPersistentHeaderDelegate {
  final VoidCallback onTap;

  _SearchBarDelegate({required this.onTap});

  @override
  double get minExtent => 64;
  @override
  double get maxExtent => 64;

  @override
  Widget build(
    BuildContext context,
    double shrinkOffset,
    bool overlapsContent,
  ) {
    return HomeSearchBarWidget(onTap: onTap);
  }

  @override
  bool shouldRebuild(covariant SliverPersistentHeaderDelegate oldDelegate) =>
      false;
}
