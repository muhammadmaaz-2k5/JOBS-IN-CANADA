import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../../../core/app_export.dart';
import '../../../widgets/status_badge_widget.dart';
import '../../../services/bookmark_service.dart';

class FeaturedJobCardWidget extends StatefulWidget {
  final Map<String, dynamic> job;
  final VoidCallback onTap;

  const FeaturedJobCardWidget({
    required this.job,
    required this.onTap,
    super.key,
  });

  @override
  State<FeaturedJobCardWidget> createState() => _FeaturedJobCardWidgetState();
}

class _FeaturedJobCardWidgetState extends State<FeaturedJobCardWidget>
    with SingleTickerProviderStateMixin {
  late AnimationController _scaleController;
  late Animation<double> _scaleAnimation;
  bool _isSaved = false;

  @override
  void initState() {
    super.initState();
    _isSaved = widget.job['isSaved'] as bool? ?? false;
    _checkSavedStatus();
    _scaleController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 100),
    );
    _scaleAnimation = Tween<double>(begin: 1.0, end: 0.97).animate(
      CurvedAnimation(parent: _scaleController, curve: Curves.easeOutCubic),
    );
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
    _scaleController.dispose();
    super.dispose();
  }

  bool get _isHighlighted =>
      widget.job['isFeatured'] == true &&
      widget.job['category'] == 'Engineering';

  Color get _cardColor {
    if (_isHighlighted) return AppTheme.secondary;
    return Colors.white;
  }

  Color get _textColor {
    if (_isHighlighted) return Colors.white;
    return AppTheme.textPrimary;
  }

  Color get _mutedTextColor {
    if (_isHighlighted) return Colors.white.withAlpha(204);
    return AppTheme.textSecondary;
  }

  @override
  Widget build(BuildContext context) {
    final job = widget.job;
    final avatars = job['applicantAvatars'] as List<dynamic>? ?? [];
    final applicants = job['applicants'] as int? ?? 0;

    return ScaleTransition(
      scale: _scaleAnimation,
      child: GestureDetector(
        onTapDown: (_) {
          HapticFeedback.lightImpact();
          _scaleController.forward();
        },
        onTapUp: (_) {
          _scaleController.reverse();
          widget.onTap();
        },
        onTapCancel: () => _scaleController.reverse(),
        child: Container(
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: _cardColor,
            borderRadius: BorderRadius.circular(16),
            boxShadow: [
              BoxShadow(
                color: _isHighlighted
                    ? AppTheme.secondary.withAlpha(64)
                    : Colors.black.withAlpha(18),
                blurRadius: 16,
                offset: const Offset(0, 3),
              ),
            ],
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Container(
                    width: 44,
                    height: 44,
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(12),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withAlpha(20),
                          blurRadius: 8,
                          offset: const Offset(0, 2),
                        ),
                      ],
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
                            fontSize: 14,
                            fontWeight: FontWeight.w600,
                            color: _textColor,
                          ),
                        ),
                        Row(
                          children: [
                            Icon(
                              Icons.location_on_outlined,
                              size: 12,
                              color: _mutedTextColor,
                            ),
                            const SizedBox(width: 2),
                            Text(
                              job['location'] as String,
                              style: GoogleFonts.plusJakartaSans(
                                fontSize: 12,
                                color: _mutedTextColor,
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
                    },
                    child: Icon(
                      _isSaved
                          ? Icons.bookmark_rounded
                          : Icons.bookmark_outline_rounded,
                      color: _isHighlighted
                          ? Colors.white
                          : (_isSaved ? AppTheme.primary : AppTheme.textMuted),
                      size: 22,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 12),
              Row(
                crossAxisAlignment: CrossAxisAlignment.baseline,
                textBaseline: TextBaseline.alphabetic,
                children: [
                  Text(
                    job['salary'] as String,
                    style: GoogleFonts.plusJakartaSans(
                      fontSize: 22,
                      fontWeight: FontWeight.w800,
                      color: _textColor,
                      fontFeatures: const [FontFeature.tabularFigures()],
                    ),
                  ),
                  const SizedBox(width: 4),
                  Text(
                    '/ ${job['salaryPeriod']}',
                    style: GoogleFonts.plusJakartaSans(
                      fontSize: 13,
                      color: _mutedTextColor,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 4),
              Text(
                job['title'] as String,
                style: GoogleFonts.plusJakartaSans(
                  fontSize: 15,
                  fontWeight: FontWeight.w600,
                  color: _textColor,
                ),
              ),
              const SizedBox(height: 10),
              Row(
                children: [
                  StatusBadgeWidget.jobType(job['jobType'] as String),
                  const SizedBox(width: 6),
                  if (job['isRemote'] == true)
                    StatusBadgeWidget.jobType('Remote'),
                  if (job['isNew'] == true) ...[
                    const SizedBox(width: 6),
                    StatusBadgeWidget.newBadge(),
                  ],
                ],
              ),
              const SizedBox(height: 12),
              Row(
                children: [
                  SizedBox(
                    height: 26,
                    child: Stack(
                      children: [
                        for (int i = 0; i < avatars.length.clamp(0, 3); i++)
                          Positioned(
                            left: i * 18.0,
                            child: Container(
                              decoration: BoxDecoration(
                                shape: BoxShape.circle,
                                border: Border.all(color: _cardColor, width: 2),
                              ),
                              child: ClipOval(
                                child: CustomImageWidget(
                                  imageUrl: avatars[i] as String,
                                  width: 24,
                                  height: 24,
                                  fit: BoxFit.cover,
                                  semanticLabel:
                                      'Applicant profile photo ${i + 1}',
                                ),
                              ),
                            ),
                          ),
                      ],
                    ),
                  ),
                  SizedBox(width: avatars.length.clamp(0, 3) * 18.0 + 8),
                  Text(
                    '$applicants+ applied',
                    style: GoogleFonts.plusJakartaSans(
                      fontSize: 12,
                      color: _mutedTextColor,
                    ),
                  ),
                  const Spacer(),
                  Container(
                    width: 36,
                    height: 36,
                    decoration: BoxDecoration(
                      color: _isHighlighted
                          ? Colors.white
                          : AppTheme.textPrimary,
                      shape: BoxShape.circle,
                    ),
                    child: Icon(
                      Icons.arrow_outward_rounded,
                      color: _isHighlighted ? AppTheme.secondary : Colors.white,
                      size: 18,
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
