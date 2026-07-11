import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../../../core/app_export.dart';

class RecommendedSectionWidget extends StatelessWidget {
  final List<Map<String, dynamic>> jobs;
  final Function(Map<String, dynamic>) onJobTap;

  const RecommendedSectionWidget({
    required this.jobs,
    required this.onJobTap,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    if (jobs.isEmpty) return const SizedBox.shrink();
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: jobs.asMap().entries.map((entry) {
          final job = entry.value;
          return Expanded(
            child: Padding(
              padding: EdgeInsets.only(
                right: entry.key < jobs.length - 1 ? 8 : 0,
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
                  imageUrl: job['companyLogo'] as String? ?? '',
                  width: 36,
                  height: 36,
                  fit: BoxFit.cover,
                  semanticLabel: job['companyLogoSemanticLabel'] as String? ?? 'Company logo',
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
