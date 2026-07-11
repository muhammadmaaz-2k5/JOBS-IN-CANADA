import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:google_fonts/google_fonts.dart';

import '../../routes/app_routes.dart';
import '../../theme/app_theme.dart';
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

  final List<String> _filterCategories = [
    'All',
    'Design',
    'Engineering',
    'Marketing',
    'Product',
    'Data',
    'Finance',
    'Healthcare',
  ];

  final List<Map<String, dynamic>> _featuredJobMaps = [
    {
      'id': 'job_001',
      'title': 'Senior Product Designer',
      'company': 'Shopify',
      'companyLogo':
          'https://img.rocket.new/generatedImages/rocket_gen_img_17f3d00d9-1783736379242.png',
      'companyLogoSemanticLabel': 'Shopify company logo mark',
      'salary': '\$115K',
      'salaryPeriod': 'year',
      'location': 'Ottawa, ON',
      'jobType': 'Full-Time',
      'isRemote': false,
      'isNew': true,
      'applicants': 47,
      'category': 'Design',
      'province': 'Ontario',
      'postedDaysAgo': 0,
      'isFeatured': true,
      'isSaved': false,
      'applicantAvatars': [
        'https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg?w=40&h=40&fit=crop',
        'https://images.pexels.com/photos/1222271/pexels-photo-1222271.jpeg?w=40&h=40&fit=crop',
        'https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg?w=40&h=40&fit=crop',
      ],
      'applyUrl': 'https://www.shopify.com/careers',
    },
    {
      'id': 'job_002',
      'title': 'Staff Software Engineer',
      'company': 'Wealthsimple',
      'companyLogo':
          'https://img.rocket.new/generatedImages/rocket_gen_img_1ed3b6573-1783736379632.png',
      'companyLogoSemanticLabel': 'Wealthsimple company logo mark',
      'salary': '\$175K',
      'salaryPeriod': 'year',
      'location': 'Toronto, ON',
      'jobType': 'Full-Time',
      'isRemote': true,
      'isNew': true,
      'applicants': 83,
      'category': 'Engineering',
      'province': 'Ontario',
      'postedDaysAgo': 0,
      'isFeatured': true,
      'isSaved': false,
      'applicantAvatars': [
        'https://images.pexels.com/photos/1239291/pexels-photo-1239291.jpeg?w=40&h=40&fit=crop',
        'https://images.pexels.com/photos/1681010/pexels-photo-1681010.jpeg?w=40&h=40&fit=crop',
        'https://images.pexels.com/photos/733872/pexels-photo-733872.jpeg?w=40&h=40&fit=crop',
      ],
      'applyUrl': 'https://jobs.lever.co/wealthsimple',
    },
  ];

  @override
  void initState() {
    super.initState();
    _simulateLoad();
  }

  Future<void> _simulateLoad() async {
    setState(() => _isLoading = true);
    await Future.delayed(const Duration(milliseconds: 800));
    if (mounted) setState(() => _isLoading = false);
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
          onRefresh: _simulateLoad,
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
                            onSelected: (_) =>
                                setState(() => _selectedCategory = cat),
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
                child: TodayJobsBannerWidget(todayCount: 124, weekCount: 847),
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
                        padding: EdgeInsets.symmetric(horizontal: 16),
                        child: Column(
                          children: [
                            // TODO: Replace with Riverpod/Bloc loading state
                          ],
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
                                itemCount: _featuredJobMaps.length,
                                itemBuilder: (_, i) => FeaturedJobCardWidget(
                                  job: _featuredJobMaps[i],
                                  onTap: () => _onJobTap(_featuredJobMaps[i]),
                                ),
                              )
                            : Column(
                                children: _featuredJobMaps
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
                child: RecommendedSectionWidget(onJobTap: _onJobTap),
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
              const SliverToBoxAdapter(child: TopCompaniesWidget()),
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
                child: CategoryGridWidget(
                  onCategoryTap: (cat) {
                    setState(() => _selectedCategory = cat);
                    context.go(AppRoutes.jobSearchScreen);
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
              const SliverToBoxAdapter(child: CareerResourcesWidget()),
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
