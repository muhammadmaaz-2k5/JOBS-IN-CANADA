import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:url_launcher/url_launcher.dart';

import '../../theme/app_theme.dart';
import './widgets/about_company_widget.dart';
import './widgets/job_detail_header_widget.dart';
import './widgets/job_info_metric_widget.dart';
import './widgets/job_overview_widget.dart';
import './widgets/qualifications_widget.dart';
import './widgets/skill_chips_widget.dart';
import './widgets/sticky_apply_bar_widget.dart';

// TODO: Replace with Riverpod/Bloc for production state management

class JobDetailScreen extends StatefulWidget {
  final Map<String, dynamic> job;

  const JobDetailScreen({required this.job, super.key});

  @override
  State<JobDetailScreen> createState() => _JobDetailScreenState();
}

class _JobDetailScreenState extends State<JobDetailScreen> {
  bool _isSaved = false;
  bool _isApplying = false;

  @override
  void initState() {
    super.initState();
    _isSaved = widget.job['isSaved'] as bool? ?? false;
  }

  Future<void> _onApply() async {
    final applyUrl = widget.job['applyUrl'] as String?;
    if (applyUrl == null || applyUrl.isEmpty) return;

    setState(() => _isApplying = true);
    HapticFeedback.mediumImpact();

    try {
      final uri = Uri.parse(applyUrl);
      if (await canLaunchUrl(uri)) {
        await launchUrl(uri, mode: LaunchMode.externalApplication);
      } else {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(
                'Could not open the application link. Try again later.',
                style: GoogleFonts.plusJakartaSans(fontSize: 13),
              ),
              backgroundColor: AppTheme.errorColor,
              behavior: SnackBarBehavior.floating,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(12),
              ),
            ),
          );
        }
      }
    } catch (_) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(
              'Unable to open employer website. Please try again.',
              style: GoogleFonts.plusJakartaSans(fontSize: 13),
            ),
            backgroundColor: AppTheme.errorColor,
            behavior: SnackBarBehavior.floating,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
            ),
          ),
        );
      }
    } finally {
      if (mounted) setState(() => _isApplying = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final job = widget.job;
    final isTablet = MediaQuery.of(context).size.width >= 600;

    return Scaffold(
      backgroundColor: AppTheme.backgroundLight,
      extendBodyBehindAppBar: false,
      appBar: AppBar(
        backgroundColor: theme.colorScheme.surface,
        elevation: 0,
        scrolledUnderElevation: 1,
        leading: GestureDetector(
          onTap: () => Navigator.of(context).pop(),
          child: Container(
            margin: const EdgeInsets.all(8),
            decoration: BoxDecoration(
              color: theme.colorScheme.surfaceContainerHighest,
              shape: BoxShape.circle,
            ),
            child: Icon(
              Icons.arrow_back_rounded,
              color: theme.colorScheme.onSurface,
              size: 20,
            ),
          ),
        ),
        title: Text(
          'Job Details',
          style: GoogleFonts.plusJakartaSans(
            fontSize: 16,
            fontWeight: FontWeight.w700,
            color: theme.colorScheme.onSurface,
          ),
        ),
        centerTitle: true,
        actions: [
          IconButton(
            icon: Icon(
              Icons.more_vert_rounded,
              color: theme.colorScheme.onSurface,
            ),
            onPressed: () => _showShareSheet(context, job),
          ),
        ],
      ),
      body: SafeArea(
        child: _buildUnifiedLayout(context, job, isTablet),
      ),
      bottomNavigationBar: StickyApplyBarWidget(
        isSaved: _isSaved,
        isApplying: _isApplying,
        onSave: () {
          HapticFeedback.lightImpact();
          setState(() => _isSaved = !_isSaved);
        },
        onApply: _onApply,
      ),
    );
  }

  Widget _buildUnifiedLayout(
    BuildContext context,
    Map<String, dynamic> job,
    bool isTablet,
  ) {
    final double paddingVal = isTablet ? 32 : 16;
    final double elementSpacing = isTablet ? 24 : 20;

    return SingleChildScrollView(
      child: Center(
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 800),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              JobDetailHeaderWidget(job: job),
              Padding(
                padding: EdgeInsets.fromLTRB(paddingVal, 20, paddingVal, 0),
                child: JobInfoMetricWidget(job: job),
              ),
              Padding(
                padding:
                    EdgeInsets.fromLTRB(paddingVal, elementSpacing, paddingVal, 0),
                child: JobOverviewWidget(job: job),
              ),
              Padding(
                padding:
                    EdgeInsets.fromLTRB(paddingVal, elementSpacing, paddingVal, 0),
                child: QualificationsWidget(job: job),
              ),
              Padding(
                padding:
                    EdgeInsets.fromLTRB(paddingVal, elementSpacing, paddingVal, 0),
                child: SkillChipsWidget(job: job),
              ),
              Padding(
                padding:
                    EdgeInsets.fromLTRB(paddingVal, elementSpacing, paddingVal, 0),
                child: AboutCompanyWidget(job: job),
              ),
              const SizedBox(height: 40),
            ],
          ),
        ),
      ),
    );
  }

  void _showShareSheet(BuildContext context, Map<String, dynamic> job) {
    showModalBottomSheet(
      context: context,
      backgroundColor: Colors.transparent,
      builder: (_) => Container(
        padding: const EdgeInsets.all(24),
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Container(
              width: 36,
              height: 4,
              decoration: BoxDecoration(
                color: Colors.grey[300],
                borderRadius: BorderRadius.circular(100),
              ),
            ),
            const SizedBox(height: 20),
            Text(
              'Share Job',
              style: GoogleFonts.plusJakartaSans(
                fontSize: 16,
                fontWeight: FontWeight.w700,
              ),
            ),
            const SizedBox(height: 16),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                _ShareOption(
                  icon: Icons.link_rounded,
                  label: 'Copy Link',
                  onTap: () {
                    Clipboard.setData(
                      ClipboardData(text: job['applyUrl'] as String? ?? ''),
                    );
                    Navigator.pop(context);
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(
                        content: Text(
                          'Link copied!',
                          style: GoogleFonts.plusJakartaSans(),
                        ),
                        behavior: SnackBarBehavior.floating,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(12),
                        ),
                      ),
                    );
                  },
                ),
                _ShareOption(
                  icon: Icons.mail_outline_rounded,
                  label: 'Email',
                  onTap: () => Navigator.pop(context),
                ),
                _ShareOption(
                  icon: Icons.message_outlined,
                  label: 'Message',
                  onTap: () => Navigator.pop(context),
                ),
              ],
            ),
            const SizedBox(height: 24),
          ],
        ),
      ),
    );
  }
}

class _ShareOption extends StatelessWidget {
  final IconData icon;
  final String label;
  final VoidCallback onTap;

  const _ShareOption({
    required this.icon,
    required this.label,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return GestureDetector(
      onTap: onTap,
      child: Column(
        children: [
          Container(
            width: 52,
            height: 52,
            decoration: BoxDecoration(
              color: theme.colorScheme.surfaceContainerHighest,
              shape: BoxShape.circle,
            ),
            child: Icon(icon, color: theme.colorScheme.onSurface, size: 24),
          ),
          const SizedBox(height: 6),
          Text(
            label,
            style: GoogleFonts.plusJakartaSans(
              fontSize: 12,
              color: theme.colorScheme.onSurfaceVariant,
            ),
          ),
        ],
      ),
    );
  }
}
