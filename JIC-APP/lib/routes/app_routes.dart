import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

import '../presentation/home_screen/home_screen.dart';
import '../presentation/job_detail_screen/job_detail_screen.dart';
import '../presentation/job_search_screen/job_search_screen.dart';
import '../widgets/app_scaffold.dart';

class AppRoutes {
  static const String initial = '/';
  static const String homeScreen = '/home-screen';
  static const String jobSearchScreen = '/job-search-screen';
  static const String jobDetailScreen = '/job-detail-screen';
}

final GoRouter appRouter = GoRouter(
  initialLocation: AppRoutes.initial,
  routes: [
    GoRoute(
      path: AppRoutes.initial,
      pageBuilder: (context, state) => CustomTransitionPage(
        key: state.pageKey,
        child: const HomeScreen(),
        transitionDuration: const Duration(milliseconds: 280),
        transitionsBuilder: (context, animation, secondaryAnimation, child) {
          return FadeTransition(
            opacity: CurvedAnimation(
              parent: animation,
              curve: Curves.easeOutCubic,
            ),
            child: child,
          );
        },
      ),
    ),
    StatefulShellRoute.indexedStack(
      builder: (context, state, navigationShell) =>
          AppScaffold(navigationShell: navigationShell),
      branches: [
        StatefulShellBranch(
          routes: [
            GoRoute(
              path: AppRoutes.homeScreen,
              pageBuilder: (context, state) => CustomTransitionPage(
                key: state.pageKey,
                child: const HomeScreen(),
                transitionDuration: const Duration(milliseconds: 280),
                transitionsBuilder:
                    (context, animation, secondaryAnimation, child) {
                      return SlideTransition(
                        position:
                            Tween<Offset>(
                              begin: const Offset(0.04, 0),
                              end: Offset.zero,
                            ).animate(
                              CurvedAnimation(
                                parent: animation,
                                curve: Curves.easeOutCubic,
                              ),
                            ),
                        child: FadeTransition(opacity: animation, child: child),
                      );
                    },
              ),
            ),
          ],
        ),
        StatefulShellBranch(
          routes: [
            GoRoute(
              path: AppRoutes.jobSearchScreen,
              pageBuilder: (context, state) => CustomTransitionPage(
                key: state.pageKey,
                child: const JobSearchScreen(),
                transitionDuration: const Duration(milliseconds: 280),
                transitionsBuilder:
                    (context, animation, secondaryAnimation, child) {
                      return SlideTransition(
                        position:
                            Tween<Offset>(
                              begin: const Offset(0.04, 0),
                              end: Offset.zero,
                            ).animate(
                              CurvedAnimation(
                                parent: animation,
                                curve: Curves.easeOutCubic,
                              ),
                            ),
                        child: FadeTransition(opacity: animation, child: child),
                      );
                    },
              ),
            ),
          ],
        ),
        StatefulShellBranch(
          routes: [
            GoRoute(
              path: AppRoutes.jobDetailScreen,
              pageBuilder: (context, state) => CustomTransitionPage(
                key: state.pageKey,
                child: JobDetailScreen(
                  job: state.extra as Map<String, dynamic>? ?? {},
                ),
                transitionDuration: const Duration(milliseconds: 280),
                transitionsBuilder:
                    (context, animation, secondaryAnimation, child) {
                      return SlideTransition(
                        position:
                            Tween<Offset>(
                              begin: const Offset(0.04, 0),
                              end: Offset.zero,
                            ).animate(
                              CurvedAnimation(
                                parent: animation,
                                curve: Curves.easeOutCubic,
                              ),
                            ),
                        child: FadeTransition(opacity: animation, child: child),
                      );
                    },
              ),
            ),
          ],
        ),
      ],
    ),
  ],
);
