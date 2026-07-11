import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:go_router/go_router.dart';

import '../../routes/app_routes.dart';
import '../../theme/app_theme.dart';
import '../../services/bookmark_service.dart';
import '../../widgets/empty_state_widget.dart';
import '../job_search_screen/widgets/search_job_card_widget.dart';

class SavedScreen extends StatefulWidget {
  const SavedScreen({super.key});

  @override
  State<SavedScreen> createState() => _SavedScreenState();
}

class _SavedScreenState extends State<SavedScreen> {
  void _onJobTap(BuildContext context, Map<String, dynamic> job) {
    context.push(AppRoutes.jobDetailScreen, extra: job).then((_) {
      // Refresh list when returning from detail screen (in case they unsaved there)
      if (mounted) {
        setState(() {});
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Scaffold(
      backgroundColor: AppTheme.backgroundLight,
      appBar: AppBar(
        backgroundColor: theme.colorScheme.surface,
        elevation: 0,
        scrolledUnderElevation: 1,
        title: Text(
          'Saved Jobs',
          style: GoogleFonts.plusJakartaSans(
            fontSize: 16,
            fontWeight: FontWeight.w700,
            color: theme.colorScheme.onSurface,
          ),
        ),
        centerTitle: true,
      ),
      body: FutureBuilder<List<Map<String, dynamic>>>(
        future: BookmarkService.getSavedJobs(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(
              child: CircularProgressIndicator(),
            );
          }

          final savedJobs = snapshot.data ?? [];

          if (savedJobs.isEmpty) {
            return EmptyStateWidget(
              icon: Icons.bookmark_border_rounded,
              title: 'No saved jobs yet',
              subtitle: 'Keep track of jobs you\'re interested in by saving them.',
              ctaLabel: 'Find Jobs',
              onCta: () {
                // Route to search screen
                context.go(AppRoutes.jobSearchScreen);
              },
            );
          }

          return Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Padding(
                padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
                child: Text(
                  '${savedJobs.length} ${savedJobs.length == 1 ? 'job' : 'jobs'} saved',
                  style: GoogleFonts.plusJakartaSans(
                    fontSize: 13,
                    fontWeight: FontWeight.w600,
                    color: theme.colorScheme.onSurfaceVariant,
                  ),
                ),
              ),
              Expanded(
                child: ListView.builder(
                  padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                  itemCount: savedJobs.length,
                  itemBuilder: (context, index) {
                    final job = savedJobs[index];
                    return SearchJobCardWidget(
                      job: job,
                      onTap: () => _onJobTap(context, job),
                      animationIndex: index,
                      onBookmarkToggle: () {
                        if (mounted) {
                          setState(() {});
                        }
                      },
                    );
                  },
                ),
              ),
            ],
          );
        },
      ),
    );
  }
}
