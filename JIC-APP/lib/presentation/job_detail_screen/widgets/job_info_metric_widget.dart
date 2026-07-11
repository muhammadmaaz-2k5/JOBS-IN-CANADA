import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

// Anatomy locked: 3 equal outlined containers, label top (small muted) + value bold bottom

class JobInfoMetricWidget extends StatelessWidget {
  final Map<String, dynamic> job;

  const JobInfoMetricWidget({required this.job, super.key});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    final metrics = [
      {
        'label': 'Salary',
        'value': job['salary'] as String? ?? 'N/A',
        'icon': Icons.attach_money_rounded,
      },
      {
        'label': 'Job Time',
        'value': job['jobType'] as String? ?? 'Full-Time',
        'icon': Icons.schedule_outlined,
      },
      {
        'label': 'Location',
        'value': _shortLocation(job['location'] as String? ?? ''),
        'icon': Icons.location_on_outlined,
      },
    ];

    return Row(
      children: metrics.asMap().entries.map((entry) {
        final i = entry.key;
        final metric = entry.value;
        return Expanded(
          child: Padding(
            padding: EdgeInsets.only(right: i < metrics.length - 1 ? 10 : 0),
            child: Container(
              padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 8),
              decoration: BoxDecoration(
                color: theme.colorScheme.surface,
                borderRadius: BorderRadius.circular(12),
                border: Border.all(color: theme.colorScheme.outline, width: 1),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withAlpha(10),
                    blurRadius: 8,
                    offset: const Offset(0, 2),
                  ),
                ],
              ),
              child: Column(
                children: [
                  Text(
                    metric['label'] as String,
                    style: GoogleFonts.plusJakartaSans(
                      fontSize: 11,
                      fontWeight: FontWeight.w400,
                      color: theme.colorScheme.onSurfaceVariant,
                    ),
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 4),
                  FittedBox(
                    fit: BoxFit.scaleDown,
                    child: Text(
                      metric['value'] as String,
                      style: GoogleFonts.plusJakartaSans(
                        fontSize: 13,
                        fontWeight: FontWeight.w700,
                        color: theme.colorScheme.onSurface,
                        fontFeatures: metric['label'] == 'Salary'
                            ? const [FontFeature.tabularFigures()]
                            : null,
                      ),
                      textAlign: TextAlign.center,
                    ),
                  ),
                ],
              ),
            ),
          ),
        );
      }).toList(),
    );
  }

  String _shortLocation(String location) {
    final parts = location.split(',');
    if (parts.isEmpty) return location;
    return parts.first.trim();
  }
}
