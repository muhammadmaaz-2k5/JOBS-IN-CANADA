import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

// Anatomy locked: Wrap of outlined rectangular chips

class SkillChipsWidget extends StatelessWidget {
  final Map<String, dynamic> job;

  const SkillChipsWidget({required this.job, super.key});

  List<String> get _skills {
    final fromJob = job['skills'] as List<dynamic>?;
    if (fromJob != null && fromJob.isNotEmpty) {
      return fromJob.map((s) => s as String).toList();
    }
    return [
      'Communication',
      'Problem Solving',
      'Team Collaboration',
      'Time Management',
      'Adaptability',
    ];
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'Skills Required',
          style: GoogleFonts.plusJakartaSans(
            fontSize: 16,
            fontWeight: FontWeight.w700,
            color: theme.colorScheme.onSurface,
          ),
        ),
        const SizedBox(height: 12),
        Wrap(
          spacing: 8,
          runSpacing: 8,
          children: _skills
              .map(
                (skill) => Container(
                  padding: const EdgeInsets.symmetric(
                    horizontal: 14,
                    vertical: 8,
                  ),
                  decoration: BoxDecoration(
                    color: Colors.transparent,
                    borderRadius: BorderRadius.circular(8),
                    border: Border.all(
                      color: theme.colorScheme.outline,
                      width: 1.2,
                    ),
                  ),
                  child: Text(
                    skill,
                    style: GoogleFonts.plusJakartaSans(
                      fontSize: 13,
                      fontWeight: FontWeight.w500,
                      color: theme.colorScheme.onSurface,
                    ),
                  ),
                ),
              )
              .toList(),
        ),
      ],
    );
  }
}
