import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import '../../../core/app_export.dart';

class CategoryGridWidget extends StatelessWidget {
  final List<Map<String, dynamic>> categories;
  final Function(String) onCategoryTap;

  const CategoryGridWidget({
    required this.categories,
    required this.onCategoryTap,
    super.key,
  });

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
  Color _contrastColorFor(String? hexString, Color fallback) {
    if (hexString == null || hexString.isEmpty) return fallback;
    final cleanHex = hexString.replaceAll('#', '').toLowerCase();
    switch (cleanHex) {
      case 'f3e8ff': return const Color(0xFF7C3AED); // Design -> dark purple
      case 'fef3c7': return const Color(0xFFD97706); // Marketing -> dark amber
      case 'dbeafe': return const Color(0xFF2563EB); // Engineering -> dark blue
      case 'dcfce7': return const Color(0xFF15803D); // Product -> dark green
      case 'ffedd5': return const Color(0xFFEA580C); // Data -> dark orange
      case 'f0fdf4': return const Color(0xFF16A34A); // Finance -> dark green
      case 'fff1f2': return const Color(0xFFE11D48); // Healthcare -> dark rose
      case 'f8fafc': return const Color(0xFF475569); // Legal -> dark slate
      case 'eff6ff': return const Color(0xFF1D4ED8); // Sales -> dark blue
      case 'fff7ed': return const Color(0xFFC2410C); // Education -> dark orange
      default: return fallback;
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: GridView.builder(
        shrinkWrap: true,
        physics: const NeverScrollableScrollPhysics(),
        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 5,
          crossAxisSpacing: 8,
          mainAxisSpacing: 12,
          childAspectRatio: 0.75,
        ),
        itemCount: categories.length,
        itemBuilder: (_, i) {
          final cat = categories[i];
          final color = _parseHexColor(cat['color'] as String?, Colors.grey.shade100);
          final iconColor = _contrastColorFor(cat['color'] as String?, theme.colorScheme.primary);
          final countVal = cat['count'] != null ? '${cat['count']}' : '0';

          return GestureDetector(
            onTap: () => onCategoryTap(cat['label'] as String),
            child: Column(
              children: [
                Container(
                  width: 52,
                  height: 52,
                  decoration: BoxDecoration(
                    color: color,
                    borderRadius: BorderRadius.circular(14),
                  ),
                  child: Center(
                    child: cat['icon'] is IconData
                        ? Icon(
                            cat['icon'] as IconData,
                            color: iconColor,
                            size: 24,
                          )
                        : CustomIconWidget(
                            iconName: (cat['icon'] as String? ?? 'help_outline'),
                            color: iconColor,
                            size: 24,
                          ),
                  ),
                ),
                const SizedBox(height: 5),
                Text(
                  cat['label'] as String? ?? '',
                  style: GoogleFonts.plusJakartaSans(
                    fontSize: 10,
                    fontWeight: FontWeight.w600,
                    color: theme.colorScheme.onSurface,
                  ),
                  textAlign: TextAlign.center,
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
                Text(
                  countVal,
                  style: GoogleFonts.plusJakartaSans(
                    fontSize: 9,
                    color: theme.colorScheme.onSurfaceVariant,
                  ),
                  textAlign: TextAlign.center,
                ),
              ],
            ),
          );
        },
      ),
    );
  }
}

