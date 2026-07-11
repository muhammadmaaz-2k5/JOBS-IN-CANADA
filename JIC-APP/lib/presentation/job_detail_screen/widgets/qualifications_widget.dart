import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

// Anatomy locked: section card with icon header row + list rows with circle icons

class QualificationsWidget extends StatelessWidget {
  final Map<String, dynamic> job;

  const QualificationsWidget({required this.job, super.key});

  List<String> get _qualifications {
    final category = job['category'] as String? ?? 'Engineering';
    switch (category.toLowerCase()) {
      case 'engineering':
        return [
          'Bachelor\'s degree in Computer Science or related field',
          '${_expFromTitle(job['title'] as String? ?? '')}+ years of software engineering experience',
          'Strong proficiency in ${(job['skills'] as List<dynamic>?)?.take(2).join(' and ') ?? 'relevant technologies'}',
          'Experience with Agile/Scrum development methodology',
          'Excellent problem-solving and communication skills',
        ];
      case 'design':
        return [
          'Degree in Design, HCI, or equivalent practical experience',
          '${_expFromTitle(job['title'] as String? ?? '')}+ years of UX/product design experience',
          'Expert proficiency in Figma and design systems',
          'Strong portfolio demonstrating end-to-end design process',
          'Experience collaborating with cross-functional teams',
        ];
      case 'data':
        return [
          'Bachelor\'s or Master\'s in Data Science, Statistics, or CS',
          '${_expFromTitle(job['title'] as String? ?? '')}+ years of data science or analytics experience',
          'Strong Python and SQL skills required',
          'Experience with ML frameworks (scikit-learn, TensorFlow)',
          'Ability to communicate insights to non-technical stakeholders',
        ];
      case 'marketing':
        return [
          'Bachelor\'s degree in Marketing, Communications, or related',
          '${_expFromTitle(job['title'] as String? ?? '')}+ years of marketing experience',
          'Proven track record of driving brand awareness and growth',
          'Strong digital marketing and analytics skills',
          'Excellent written and verbal communication skills',
        ];
      default:
        return [
          'Relevant degree or equivalent work experience',
          '${_expFromTitle(job['title'] as String? ?? '')}+ years in a similar role',
          'Strong analytical and problem-solving abilities',
          'Excellent communication and interpersonal skills',
          'Ability to work in a fast-paced Canadian work environment',
        ];
    }
  }

  int _expFromTitle(String title) {
    if (title.toLowerCase().contains('senior') ||
        title.toLowerCase().contains('staff') ||
        title.toLowerCase().contains('lead')) {
      return 5;
    }
    if (title.toLowerCase().contains('junior') ||
        title.toLowerCase().contains('intern')) {
      return 1;
    }
    return 3;
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: theme.colorScheme.surface,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withAlpha(13),
            blurRadius: 12,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Section header with icon
          Row(
            children: [
              Container(
                width: 36,
                height: 36,
                decoration: BoxDecoration(
                  color: theme.colorScheme.primaryContainer,
                  borderRadius: BorderRadius.circular(10),
                ),
                child: Icon(
                  Icons.workspace_premium_outlined,
                  color: theme.colorScheme.primary,
                  size: 18,
                ),
              ),
              const SizedBox(width: 10),
              Text(
                'Qualifications',
                style: GoogleFonts.plusJakartaSans(
                  fontSize: 16,
                  fontWeight: FontWeight.w700,
                  color: theme.colorScheme.onSurface,
                ),
              ),
            ],
          ),
          const SizedBox(height: 14),
          // Qualification list items
          ..._qualifications.map(
            (q) => Padding(
              padding: const EdgeInsets.only(bottom: 10),
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Container(
                    width: 20,
                    height: 20,
                    margin: const EdgeInsets.only(top: 1),
                    decoration: BoxDecoration(
                      color: theme.colorScheme.primaryContainer,
                      shape: BoxShape.circle,
                    ),
                    child: Icon(
                      Icons.check_rounded,
                      size: 12,
                      color: theme.colorScheme.primary,
                    ),
                  ),
                  const SizedBox(width: 10),
                  Expanded(
                    child: Text(
                      q,
                      style: GoogleFonts.plusJakartaSans(
                        fontSize: 13,
                        color: theme.colorScheme.onSurface,
                        height: 1.5,
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
