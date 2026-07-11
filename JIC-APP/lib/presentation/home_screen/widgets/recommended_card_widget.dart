import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../../../core/app_export.dart';

class RecommendedSectionWidget extends StatelessWidget {
  final Function(Map<String, dynamic>) onJobTap;

  const RecommendedSectionWidget({required this.onJobTap, super.key});

  static final List<Map<String, dynamic>> _recommendedMaps = [
    {
      'id': 'rec_001',
      'title': 'UI Designer',
      'company': 'Figma',
      'companyLogo':
          'https://img.rocket.new/generatedImages/rocket_gen_img_14898759e-1772037482288.png',
      'companyLogoSemanticLabel': 'Figma design tool company logo',
      'salary': '\$110K',
      'salaryPeriod': 'yr',
      'location': 'Remote',
      'jobType': 'Full-Time',
      'isRemote': true,
      'isNew': false,
      'applicants': 29,
      'category': 'Design',
      'province': 'Remote',
      'postedDaysAgo': 1,
      'isFeatured': false,
      'isSaved': false,
      'applicantAvatars': [],
      'applyUrl': 'https://www.figma.com/careers',
    },
    {
      'id': 'rec_002',
      'title': 'UX Researcher',
      'company': 'Microsoft',
      'companyLogo':
          'https://img.rocket.new/generatedImages/rocket_gen_img_174ad6426-1783736379527.png',
      'companyLogoSemanticLabel': 'Microsoft technology company logo',
      'salary': '\$115K',
      'salaryPeriod': 'yr',
      'location': 'Vancouver, BC',
      'jobType': 'Full-Time',
      'isRemote': false,
      'isNew': true,
      'applicants': 14,
      'category': 'Design',
      'province': 'British Columbia',
      'postedDaysAgo': 0,
      'isFeatured': false,
      'isSaved': false,
      'applicantAvatars': [],
      'applyUrl': 'https://careers.microsoft.com',
    },
    {
      'id': 'rec_003',
      'title': 'Product Designer',
      'company': 'Airbnb',
      'companyLogo':
          'https://img.rocket.new/generatedImages/rocket_gen_img_19ed1076f-1783736379938.png',
      'companyLogoSemanticLabel': 'Airbnb travel platform company logo',
      'salary': '\$105K',
      'salaryPeriod': 'yr',
      'location': 'Toronto, ON',
      'jobType': 'Contract',
      'isRemote': false,
      'isNew': false,
      'applicants': 36,
      'category': 'Design',
      'province': 'Ontario',
      'postedDaysAgo': 2,
      'isFeatured': false,
      'isSaved': false,
      'applicantAvatars': [],
      'applyUrl': 'https://careers.airbnb.com',
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: _recommendedMaps.asMap().entries.map((entry) {
          final job = entry.value;
          return Expanded(
            child: Padding(
              padding: EdgeInsets.only(
                right: entry.key < _recommendedMaps.length - 1 ? 8 : 0,
              ),
              child: _RecommendedCard(job: job, onTap: () => onJobTap(job)),
            ),
          );
        }).toList(),
      ),
    );
  }
}

class _RecommendedCard extends StatelessWidget {
  final Map<String, dynamic> job;
  final VoidCallback onTap;

  const _RecommendedCard({required this.job, required this.onTap});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return GestureDetector(
      onTap: () {
        HapticFeedback.selectionClick();
        onTap();
      },
      child: Container(
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(
          color: theme.colorScheme.surface,
          borderRadius: BorderRadius.circular(14),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withAlpha(15),
              blurRadius: 10,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              width: 36,
              height: 36,
              decoration: BoxDecoration(
                color: AppTheme.backgroundLight,
                borderRadius: BorderRadius.circular(10),
              ),
              child: ClipRRect(
                borderRadius: BorderRadius.circular(8),
                child: CustomImageWidget(
                  imageUrl: job['companyLogo'] as String,
                  width: 36,
                  height: 36,
                  fit: BoxFit.cover,
                  semanticLabel: job['companyLogoSemanticLabel'] as String,
                ),
              ),
            ),
            const SizedBox(height: 8),
            Text(
              job['company'] as String,
              style: GoogleFonts.plusJakartaSans(
                fontSize: 12,
                fontWeight: FontWeight.w600,
                color: theme.colorScheme.onSurface,
              ),
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
            ),
            const SizedBox(height: 2),
            Text(
              job['title'] as String,
              style: GoogleFonts.plusJakartaSans(
                fontSize: 11,
                color: theme.colorScheme.onSurfaceVariant,
              ),
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
            ),
            const SizedBox(height: 6),
            Text(
              '${job['salary']}/${job['salaryPeriod']}',
              style: GoogleFonts.plusJakartaSans(
                fontSize: 12,
                fontWeight: FontWeight.w700,
                color: theme.colorScheme.primary,
                fontFeatures: const [FontFeature.tabularFigures()],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
