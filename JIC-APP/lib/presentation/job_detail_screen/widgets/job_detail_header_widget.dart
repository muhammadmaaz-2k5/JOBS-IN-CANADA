import 'package:flutter/material.dart';

import '../../../core/app_export.dart';
import '../../../widgets/status_badge_widget.dart';

class JobDetailHeaderWidget extends StatelessWidget {
  final Map<String, dynamic> job;

  const JobDetailHeaderWidget({required this.job, super.key});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    // Determine header accent color based on category
    final category = job['category'] as String? ?? 'Design';
    final Color headerColor = _headerColorForCategory(category);

    return Stack(
      clipBehavior: Clip.none,
      children: [
        // Colored header band — ~170px
        Container(
          height: 170,
          width: double.infinity,
          decoration: BoxDecoration(
            color: headerColor,
            borderRadius: const BorderRadius.only(
              bottomLeft: Radius.circular(0),
              bottomRight: Radius.circular(0),
            ),
          ),
          child: Align(
            alignment: Alignment.topRight,
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: job['isNew'] == true
                  ? StatusBadgeWidget.newBadge()
                  : const SizedBox.shrink(),
            ),
          ),
        ),
        // Company logo circle — overlaps header bottom edge
        Positioned(
          bottom: -40,
          left: 0,
          right: 0,
          child: Center(
            child: Container(
              width: 80,
              height: 80,
              decoration: BoxDecoration(
                color: Colors.white,
                shape: BoxShape.circle,
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withAlpha(31),
                    blurRadius: 16,
                    offset: const Offset(0, 4),
                  ),
                ],
              ),
              padding: const EdgeInsets.all(12),
              child: ClipOval(
                child: CustomImageWidget(
                  imageUrl: job['companyLogo'] as String? ?? '',
                  width: 56,
                  height: 56,
                  fit: BoxFit.cover,
                  semanticLabel:
                      job['companyLogoSemanticLabel'] as String? ??
                      'Company logo',
                ),
              ),
            ),
          ),
        ),
        // Spacer to push content below the overlap
        const SizedBox(height: 170),
      ],
    );
  }

  Color _headerColorForCategory(String category) {
    switch (category.toLowerCase()) {
      case 'engineering':
        return const Color(0xFFDBEAFE);
      case 'design':
        return const Color(0xFFF3E8FF);
      case 'marketing':
        return const Color(0xFFFEF3C7);
      case 'product':
        return const Color(0xFFDCFCE7);
      case 'data':
        return const Color(0xFFFFEDD5);
      case 'finance':
        return const Color(0xFFF0FDF4);
      case 'healthcare':
        return const Color(0xFFFFF1F2);
      default:
        return const Color(0xFFE0F2FE);
    }
  }
}

class JobDetailTitleWidget extends StatelessWidget {
  final Map<String, dynamic> job;

  const JobDetailTitleWidget({required this.job, super.key});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Padding(
      padding: const EdgeInsets.fromLTRB(16, 52, 16, 0),
      child: Column(
        children: [
          Text(
            job['title'] as String? ?? '',
            style: GoogleFonts.plusJakartaSans(
              fontSize: 20,
              fontWeight: FontWeight.w800,
              color: theme.colorScheme.onSurface,
            ),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 4),
          Text(
            job['company'] as String? ?? '',
            style: GoogleFonts.plusJakartaSans(
              fontSize: 14,
              fontWeight: FontWeight.w500,
              color: theme.colorScheme.onSurfaceVariant,
            ),
            textAlign: TextAlign.center,
          ),
        ],
      ),
    );
  }
}
