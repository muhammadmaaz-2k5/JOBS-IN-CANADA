import 'package:flutter/material.dart';

import '../../../core/app_export.dart';

// Anatomy locked: Row: logo + Column(name+desc) + trailing chevron

class AboutCompanyWidget extends StatelessWidget {
  final Map<String, dynamic> job;

  const AboutCompanyWidget({required this.job, super.key});

  String get _companyDescription {
    final company = job['company'] as String? ?? 'this company';
    return '$company is a leading Canadian employer known for innovation, '
        'inclusive culture, and competitive compensation. They are '
        'committed to building diverse teams across Canada.';
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'About Company',
          style: GoogleFonts.plusJakartaSans(
            fontSize: 16,
            fontWeight: FontWeight.w700,
            color: theme.colorScheme.onSurface,
          ),
        ),
        const SizedBox(height: 12),
        Container(
          padding: const EdgeInsets.all(14),
          decoration: BoxDecoration(
            color: theme.colorScheme.surface,
            borderRadius: BorderRadius.circular(14),
            border: Border.all(
              color: theme.colorScheme.outlineVariant,
              width: 1,
            ),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withAlpha(10),
                blurRadius: 8,
                offset: const Offset(0, 2),
              ),
            ],
          ),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                width: 44,
                height: 44,
                decoration: BoxDecoration(
                  color: AppTheme.backgroundLight,
                  borderRadius: BorderRadius.circular(12),
                ),
                padding: const EdgeInsets.all(6),
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(8),
                  child: CustomImageWidget(
                    imageUrl: job['companyLogo'] as String? ?? '',
                    width: 32,
                    height: 32,
                    fit: BoxFit.cover,
                    semanticLabel:
                        job['companyLogoSemanticLabel'] as String? ??
                        'Company logo',
                  ),
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      job['company'] as String? ?? '',
                      style: GoogleFonts.plusJakartaSans(
                        fontSize: 14,
                        fontWeight: FontWeight.w700,
                        color: theme.colorScheme.onSurface,
                      ),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      _companyDescription,
                      style: GoogleFonts.plusJakartaSans(
                        fontSize: 12,
                        color: theme.colorScheme.onSurfaceVariant,
                        height: 1.55,
                      ),
                      maxLines: 3,
                      overflow: TextOverflow.ellipsis,
                    ),
                  ],
                ),
              ),
              const SizedBox(width: 8),
              Icon(
                Icons.arrow_forward_ios_rounded,
                size: 14,
                color: theme.colorScheme.onSurfaceVariant,
              ),
            ],
          ),
        ),
      ],
    );
  }
}
