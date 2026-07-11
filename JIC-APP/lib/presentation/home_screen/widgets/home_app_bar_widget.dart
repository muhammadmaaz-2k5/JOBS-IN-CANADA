import 'package:flutter/material.dart';

import '../../../core/app_export.dart';

class HomeAppBarWidget extends StatelessWidget {
  final String userName;
  final String userAvatarUrl;
  final String userAvatarSemanticLabel;
  final int notificationCount;

  const HomeAppBarWidget({
    required this.userName,
    required this.userAvatarUrl,
    required this.userAvatarSemanticLabel,
    required this.notificationCount,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final firstName = userName.split(' ').first;

    return Container(
      padding: const EdgeInsets.fromLTRB(16, 12, 16, 8),
      color: theme.colorScheme.surface,
      child: Row(
        children: [
          ClipRRect(
            borderRadius: BorderRadius.circular(100),
            child: CustomImageWidget(
              imageUrl: userAvatarUrl,
              width: 42,
              height: 42,
              fit: BoxFit.cover,
              semanticLabel: userAvatarSemanticLabel,
            ),
          ),
          const SizedBox(width: 10),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'Hello, $firstName 👋',
                  style: GoogleFonts.plusJakartaSans(
                    fontSize: 13,
                    fontWeight: FontWeight.w400,
                    color: theme.colorScheme.onSurfaceVariant,
                  ),
                ),
                Text(
                  'Find your next role',
                  style: GoogleFonts.plusJakartaSans(
                    fontSize: 14,
                    fontWeight: FontWeight.w600,
                    color: theme.colorScheme.onSurface,
                  ),
                ),
              ],
            ),
          ),
          Stack(
            children: [
              Container(
                width: 40,
                height: 40,
                decoration: BoxDecoration(
                  color: theme.colorScheme.surfaceContainerHighest,
                  borderRadius: BorderRadius.circular(12),
                ),
                child: CustomIconWidget(
                  iconName: 'notifications_outlined',
                  color: theme.colorScheme.onSurface,
                  size: 22,
                ),
              ),
              if (notificationCount > 0)
                Positioned(
                  top: 6,
                  right: 6,
                  child: Container(
                    width: 8,
                    height: 8,
                    decoration: const BoxDecoration(
                      color: Color(0xFFDC2626),
                      shape: BoxShape.circle,
                    ),
                  ),
                ),
            ],
          ),
        ],
      ),
    );
  }
}
