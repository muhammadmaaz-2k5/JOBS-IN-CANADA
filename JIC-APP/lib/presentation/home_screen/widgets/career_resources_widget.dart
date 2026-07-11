import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import '../../../core/app_export.dart';

class CareerResourcesWidget extends StatelessWidget {
  final List<Map<String, dynamic>> resources;

  const CareerResourcesWidget({required this.resources, super.key});

  Color _parseHexColor(String? hexString, Color fallback) {
    if (hexString == null || hexString.isEmpty) return fallback;
    try {
      final hex = hexString.replaceAll('#', '');
      if (hex.length == 6) {
        return Color(int.parse('FF$hex', radix: 16));
      } else if (hex.length == 8) {
        return Color(int.parse(hex, radix: 16));
      }
    } catch (_) {}
    return fallback;
  }

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
          children: resources.asMap().entries.map((entry) {
            final i = entry.key;
            final resource = entry.value;
            final color = _parseHexColor(resource['color'] as String?, Colors.grey.shade100);
            final iconColor = _parseHexColor(resource['iconColor'] as String?, theme.colorScheme.primary);

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
                          color: color,
                          borderRadius: BorderRadius.circular(10),
                        ),
                        child: Center(
                          child: resource['icon'] is IconData
                              ? Icon(
                                  resource['icon'] as IconData,
                                  color: iconColor,
                                  size: 20,
                                )
                              : CustomIconWidget(
                                  iconName: (resource['icon'] as String? ?? 'help_outline'),
                                  color: iconColor,
                                  size: 20,
                                ),
                        ),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              resource['title'] as String? ?? '',
                              style: GoogleFonts.plusJakartaSans(
                                fontSize: 14,
                                fontWeight: FontWeight.w600,
                                color: theme.colorScheme.onSurface,
                              ),
                            ),
                            Text(
                              resource['subtitle'] as String? ?? '',
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
                if (i < resources.length - 1)
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

