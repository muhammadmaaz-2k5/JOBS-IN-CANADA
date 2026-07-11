import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class CareerResourcesWidget extends StatelessWidget {
  const CareerResourcesWidget({super.key});

  static const List<Map<String, dynamic>> _resources = [
    {
      'icon': Icons.description_outlined,
      'title': 'Resume Review',
      'subtitle': 'Get expert feedback on your Canadian resume',
      'color': Color(0xFFDBEAFE),
      'iconColor': Color(0xFF2563EB),
    },
    {
      'icon': Icons.chat_bubble_outline_rounded,
      'title': 'Interview Tips',
      'subtitle': 'Ace your next Canadian job interview',
      'color': Color(0xFFDCFCE7),
      'iconColor': Color(0xFF16A34A),
    },
    {
      'icon': Icons.insights_rounded,
      'title': 'Salary Insights',
      'subtitle': 'Know your worth in the Canadian market',
      'color': Color(0xFFFEF3C7),
      'iconColor': Color(0xFFD97706),
    },
    {
      'icon': Icons.school_outlined,
      'title': 'Work Permit Guide',
      'subtitle': 'Understand LMIA & work authorization in Canada',
      'color': Color(0xFFF3E8FF),
      'iconColor': Color(0xFFA855F7),
    },
  ];

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Container(
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
          children: _resources.asMap().entries.map((entry) {
            final i = entry.key;
            final resource = entry.value;
            return Column(
              children: [
                Padding(
                  padding: const EdgeInsets.symmetric(
                    horizontal: 16,
                    vertical: 14,
                  ),
                  child: Row(
                    children: [
                      Container(
                        width: 40,
                        height: 40,
                        decoration: BoxDecoration(
                          color: resource['color'] as Color,
                          borderRadius: BorderRadius.circular(10),
                        ),
                        child: Icon(
                          resource['icon'] as IconData,
                          color: resource['iconColor'] as Color,
                          size: 20,
                        ),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              resource['title'] as String,
                              style: GoogleFonts.plusJakartaSans(
                                fontSize: 14,
                                fontWeight: FontWeight.w600,
                                color: theme.colorScheme.onSurface,
                              ),
                            ),
                            Text(
                              resource['subtitle'] as String,
                              style: GoogleFonts.plusJakartaSans(
                                fontSize: 12,
                                color: theme.colorScheme.onSurfaceVariant,
                              ),
                            ),
                          ],
                        ),
                      ),
                      Icon(
                        Icons.arrow_forward_ios_rounded,
                        size: 14,
                        color: theme.colorScheme.onSurfaceVariant,
                      ),
                    ],
                  ),
                ),
                if (i < _resources.length - 1)
                  Divider(
                    height: 1,
                    color: theme.colorScheme.outlineVariant,
                    indent: 16,
                    endIndent: 16,
                  ),
              ],
            );
          }).toList(),
        ),
      ),
    );
  }
}
