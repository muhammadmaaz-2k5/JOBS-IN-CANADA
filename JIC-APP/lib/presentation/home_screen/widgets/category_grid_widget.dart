import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class CategoryGridWidget extends StatelessWidget {
  final Function(String) onCategoryTap;

  const CategoryGridWidget({required this.onCategoryTap, super.key});

  static const List<Map<String, dynamic>> _categories = [
    {
      'label': 'Design',
      'icon': Icons.design_services_outlined,
      'count': '1,200+',
      'color': Color(0xFFF3E8FF),
      'iconColor': Color(0xFFA855F7),
    },
    {
      'label': 'Marketing',
      'icon': Icons.campaign_outlined,
      'count': '980+',
      'color': Color(0xFFFEF3C7),
      'iconColor': Color(0xFFD97706),
    },
    {
      'label': 'Engineering',
      'icon': Icons.code_rounded,
      'count': '1,500+',
      'color': Color(0xFFDBEAFE),
      'iconColor': Color(0xFF2563EB),
    },
    {
      'label': 'Product',
      'icon': Icons.inventory_2_outlined,
      'count': '1,100+',
      'color': Color(0xFFDCFCE7),
      'iconColor': Color(0xFF16A34A),
    },
    {
      'label': 'Data',
      'icon': Icons.bar_chart_rounded,
      'count': '800+',
      'color': Color(0xFFFFEDD5),
      'iconColor': Color(0xFFEA580C),
    },
    {
      'label': 'Finance',
      'icon': Icons.account_balance_outlined,
      'count': '650+',
      'color': Color(0xFFF0FDF4),
      'iconColor': Color(0xFF15803D),
    },
    {
      'label': 'Healthcare',
      'icon': Icons.health_and_safety_outlined,
      'count': '920+',
      'color': Color(0xFFFFF1F2),
      'iconColor': Color(0xFFE11D48),
    },
    {
      'label': 'Legal',
      'icon': Icons.gavel_rounded,
      'count': '340+',
      'color': Color(0xFFF8FAFC),
      'iconColor': Color(0xFF475569),
    },
    {
      'label': 'Sales',
      'icon': Icons.trending_up_rounded,
      'count': '710+',
      'color': Color(0xFFEFF6FF),
      'iconColor': Color(0xFF3B82F6),
    },
    {
      'label': 'Education',
      'icon': Icons.school_outlined,
      'count': '430+',
      'color': Color(0xFFFFF7ED),
      'iconColor': Color(0xFFF97316),
    },
  ];

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
        itemCount: _categories.length,
        itemBuilder: (_, i) {
          final cat = _categories[i];
          return GestureDetector(
            onTap: () => onCategoryTap(cat['label'] as String),
            child: Column(
              children: [
                Container(
                  width: 52,
                  height: 52,
                  decoration: BoxDecoration(
                    color: cat['color'] as Color,
                    borderRadius: BorderRadius.circular(14),
                  ),
                  child: Icon(
                    cat['icon'] as IconData,
                    color: cat['iconColor'] as Color,
                    size: 24,
                  ),
                ),
                const SizedBox(height: 5),
                Text(
                  cat['label'] as String,
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
                  cat['count'] as String,
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
