import 'package:flutter/material.dart';

import '../../../core/app_export.dart';

class TopCompaniesWidget extends StatelessWidget {
  final List<Map<String, dynamic>> companies;
  final Function(String) onCompanyTap;

  const TopCompaniesWidget({
    required this.companies,
    required this.onCompanyTap,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return SizedBox(
      height: 90,
      child: ListView.builder(
        scrollDirection: Axis.horizontal,
        padding: const EdgeInsets.symmetric(horizontal: 16),
        itemCount: companies.length,
        itemBuilder: (_, i) {
          final company = companies[i];
          final logoUrl = company['logoUrl'] as String? ?? '';
          final name = company['name'] as String? ?? '';
          final semanticLabel = company['semanticLabel'] as String? ?? '$name company logo';

          return GestureDetector(
            onTap: () => onCompanyTap(name),
            child: Container(
              width: 80,
              margin: const EdgeInsets.only(right: 12),
            child: Column(
              children: [
                Container(
                  width: 56,
                  height: 56,
                  decoration: BoxDecoration(
                    color: theme.colorScheme.surface,
                    shape: BoxShape.circle,
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withAlpha(18),
                        blurRadius: 10,
                        offset: const Offset(0, 2),
                      ),
                    ],
                  ),
                  child: ClipOval(
                    child: logoUrl.isNotEmpty
                        ? CustomImageWidget(
                            imageUrl: logoUrl,
                            width: 56,
                            height: 56,
                            fit: BoxFit.cover,
                            semanticLabel: semanticLabel,
                          )
                        : Container(
                            color: theme.colorScheme.primaryContainer,
                            alignment: Alignment.center,
                            child: Text(
                              name.isNotEmpty ? name.substring(0, 1) : '?',
                              style: TextStyle(
                                color: theme.colorScheme.primary,
                                fontWeight: FontWeight.bold,
                                fontSize: 20,
                              ),
                            ),
                          ),
                  ),
                ),
                const SizedBox(height: 6),
                Text(
                  name,
                  style: GoogleFonts.plusJakartaSans(
                    fontSize: 11,
                    fontWeight: FontWeight.w500,
                    color: theme.colorScheme.onSurface,
                  ),
                  textAlign: TextAlign.center,
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
              ],
            ),
          ),
        );
      },
      ),
    );
  }
}

