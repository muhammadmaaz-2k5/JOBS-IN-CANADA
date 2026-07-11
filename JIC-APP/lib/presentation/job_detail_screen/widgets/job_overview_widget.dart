import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class JobOverviewWidget extends StatefulWidget {
  final Map<String, dynamic> job;

  const JobOverviewWidget({required this.job, super.key});

  @override
  State<JobOverviewWidget> createState() => _JobOverviewWidgetState();
}

class _JobOverviewWidgetState extends State<JobOverviewWidget> {
  bool _expanded = false;

  String get _fullDescription {
    final desc = widget.job['description'] as String?;
    if (desc != null && desc.isNotEmpty) return desc;
    final title = widget.job['title'] as String? ?? 'this role';
    final company = widget.job['company'] as String? ?? 'our team';
    return 'We are looking for an exceptional $title to join $company. '
        'You will work in a collaborative, fast-paced Canadian environment where your contributions '
        'directly impact millions of users. This role offers competitive compensation, comprehensive '
        'benefits including extended health, dental and vision coverage, RRSP matching, flexible work '
        'arrangements, and generous vacation time. You\'ll have the opportunity to mentor junior team '
        'members, shape product direction, and grow your career in one of Canada\'s top tech companies. '
        'We are committed to building an inclusive workplace that reflects the diversity of Canada.';
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final preview = _fullDescription.length > 180
        ? '${_fullDescription.substring(0, 180)}...'
        : _fullDescription;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'Job Overview',
          style: GoogleFonts.plusJakartaSans(
            fontSize: 16,
            fontWeight: FontWeight.w700,
            color: theme.colorScheme.onSurface,
          ),
        ),
        const SizedBox(height: 10),
        AnimatedCrossFade(
          firstChild: Text(
            preview,
            style: GoogleFonts.plusJakartaSans(
              fontSize: 13,
              color: theme.colorScheme.onSurfaceVariant,
              height: 1.65,
            ),
          ),
          secondChild: Text(
            _fullDescription,
            style: GoogleFonts.plusJakartaSans(
              fontSize: 13,
              color: theme.colorScheme.onSurfaceVariant,
              height: 1.65,
            ),
          ),
          crossFadeState: _expanded
              ? CrossFadeState.showSecond
              : CrossFadeState.showFirst,
          duration: const Duration(milliseconds: 250),
        ),
        const SizedBox(height: 6),
        GestureDetector(
          onTap: () => setState(() => _expanded = !_expanded),
          child: Text(
            _expanded ? 'See Less' : 'See More',
            style: GoogleFonts.plusJakartaSans(
              fontSize: 13,
              fontWeight: FontWeight.w600,
              color: theme.colorScheme.primary,
            ),
          ),
        ),
      ],
    );
  }
}
