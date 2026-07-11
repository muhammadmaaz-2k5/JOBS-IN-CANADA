import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../../../core/app_export.dart';
import '../../../widgets/status_badge_widget.dart';
import '../../../services/bookmark_service.dart';

class SearchJobCardWidget extends StatefulWidget {
  final Map<String, dynamic> job;
  final VoidCallback onTap;
  final int animationIndex;
  final VoidCallback? onBookmarkToggle;

  const SearchJobCardWidget({
    required this.job,
    required this.onTap,
    required this.animationIndex,
    this.onBookmarkToggle,
    super.key,
  });

  @override
  State<SearchJobCardWidget> createState() => _SearchJobCardWidgetState();
}

class _SearchJobCardWidgetState extends State<SearchJobCardWidget>
    with SingleTickerProviderStateMixin {
  late AnimationController _entranceController;
  late Animation<double> _fadeAnimation;
  late Animation<Offset> _slideAnimation;
  bool _isSaved = false;

  @override
  void initState() {
    super.initState();
    _isSaved = widget.job['isSaved'] as bool? ?? false;
    _checkSavedStatus();
    _entranceController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 350),
    );
    _fadeAnimation = Tween<double>(begin: 0.0, end: 1.0).animate(
      CurvedAnimation(parent: _entranceController, curve: Curves.easeOutCubic),
    );
    _slideAnimation =
        Tween<Offset>(begin: const Offset(0, 0.06), end: Offset.zero).animate(
          CurvedAnimation(
            parent: _entranceController,
            curve: Curves.easeOutCubic,
          ),
        );

    final delay = Duration(
      milliseconds: (widget.animationIndex * 60).clamp(0, 400),
    );
    Future.delayed(delay, () {
      if (mounted) _entranceController.forward();
    });
  }

  Future<void> _checkSavedStatus() async {
    final status = await BookmarkService.isSaved(widget.job['id'] as int);
    if (mounted) {
      setState(() {
        _isSaved = status;
      });
    }
  }

  @override
  void dispose() {
    _entranceController.dispose();
    super.dispose();
  }

  String _postedLabel(int daysAgo) {
    if (daysAgo == 0) return 'Today';
    if (daysAgo == 1) return 'Yesterday';
    return '${daysAgo}d ago';
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final job = widget.job;

    return FadeTransition(
      opacity: _fadeAnimation,
      child: SlideTransition(
        position: _slideAnimation,
        child: GestureDetector(
          onTap: () {
            HapticFeedback.selectionClick();
            widget.onTap();
          },
          child: Container(
            margin: const EdgeInsets.only(bottom: 12),
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: theme.colorScheme.surface,
              borderRadius: BorderRadius.circular(16),
              boxShadow: [
                BoxShadow(
                  color: Colors.black.withAlpha(15),
                  blurRadius: 14,
                  offset: const Offset(0, 3),
                ),
              ],
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Header row
                Row(
                  children: [
                    Container(
                      width: 46,
                      height: 46,
                      decoration: BoxDecoration(
                        color: AppTheme.backgroundLight,
                        borderRadius: BorderRadius.circular(12),
                      ),
                      padding: const EdgeInsets.all(6),
                      child: ClipRRect(
                        borderRadius: BorderRadius.circular(8),
                        child: CustomImageWidget(
                          imageUrl: job['companyLogo'] as String? ?? '',
                          width: 34,
                          height: 34,
                          fit: BoxFit.cover,
                          semanticLabel:
                              job['companyLogoSemanticLabel'] as String? ?? 'Company logo',
                        ),
                      ),
                    ),
                    const SizedBox(width: 10),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            job['company'] as String,
                            style: GoogleFonts.plusJakartaSans(
                              fontSize: 13,
                              fontWeight: FontWeight.w600,
                              color: theme.colorScheme.onSurface,
                            ),
                          ),
                          Row(
                            children: [
                              Icon(
                                Icons.location_on_outlined,
                                size: 11,
                                color: theme.colorScheme.onSurfaceVariant,
                              ),
                              const SizedBox(width: 2),
                              Flexible(
                                child: Text(
                                  job['location'] as String,
                                  style: GoogleFonts.plusJakartaSans(
                                    fontSize: 11,
                                    color: theme.colorScheme.onSurfaceVariant,
                                  ),
                                  overflow: TextOverflow.ellipsis,
                                ),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                     GestureDetector(
                        onTap: () async {
                          await BookmarkService.toggleSave(widget.job);
                          final savedStatus = await BookmarkService.isSaved(widget.job['id'] as int);
                          if (mounted) {
                            setState(() {
                              _isSaved = savedStatus;
                            });
                          }
                          widget.onBookmarkToggle?.call();
                        },
                       child: Icon(
                         _isSaved
                             ? Icons.bookmark_rounded
                             : Icons.bookmark_outline_rounded,
                         color: _isSaved
                             ? AppTheme.primary
                             : theme.colorScheme.onSurfaceVariant,
                         size: 22,
                       ),
                     ),
                  ],
                ),
                const SizedBox(height: 10),
                // Salary
                Text(
                  job['salary'] as String,
                  style: GoogleFonts.plusJakartaSans(
                    fontSize: 20,
                    fontWeight: FontWeight.w800,
                    color: theme.colorScheme.primary,
                    fontFeatures: const [FontFeature.tabularFigures()],
                  ),
                ),
                const SizedBox(height: 2),
                Text(
                  job['title'] as String,
                  style: GoogleFonts.plusJakartaSans(
                    fontSize: 15,
                    fontWeight: FontWeight.w600,
                    color: theme.colorScheme.onSurface,
                  ),
                ),
                const SizedBox(height: 8),
                // Skills preview
                Wrap(
                  spacing: 6,
                  runSpacing: 4,
                  children: (job['skills'] as List<dynamic>)
                      .take(3)
                      .map(
                        (s) => Container(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 8,
                            vertical: 3,
                          ),
                          decoration: BoxDecoration(
                            color: theme.colorScheme.surfaceContainerHighest,
                            borderRadius: BorderRadius.circular(6),
                          ),
                          child: Text(
                            s as String,
                            style: GoogleFonts.plusJakartaSans(
                              fontSize: 11,
                              fontWeight: FontWeight.w500,
                              color: theme.colorScheme.onSurfaceVariant,
                            ),
                          ),
                        ),
                      )
                      .toList(),
                ),
                const SizedBox(height: 10),
                // Footer row
                Row(
                  children: [
                    StatusBadgeWidget.jobType(job['jobType'] as String),
                    const SizedBox(width: 6),
                    if (job['isRemote'] == true)
                      StatusBadgeWidget.jobType('Remote'),
                    const Spacer(),
                    Icon(
                      Icons.schedule_outlined,
                      size: 12,
                      color: theme.colorScheme.onSurfaceVariant,
                    ),
                    const SizedBox(width: 3),
                    Text(
                      _postedLabel(job['postedDaysAgo'] as int),
                      style: GoogleFonts.plusJakartaSans(
                        fontSize: 11,
                        color: job['postedDaysAgo'] == 0
                            ? AppTheme.success
                            : theme.colorScheme.onSurfaceVariant,
                        fontWeight: job['postedDaysAgo'] == 0
                            ? FontWeight.w600
                            : FontWeight.w400,
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
