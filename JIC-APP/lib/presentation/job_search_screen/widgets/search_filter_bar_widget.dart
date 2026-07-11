import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class SearchFilterBarWidget extends StatelessWidget {
  final TextEditingController controller;
  final ValueChanged<String> onChanged;
  final VoidCallback onFilterTap;
  final int activeFilterCount;

  const SearchFilterBarWidget({
    required this.controller,
    required this.onChanged,
    required this.onFilterTap,
    required this.activeFilterCount,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Row(
      children: [
        Expanded(
          child: TextField(
            controller: controller,
            onChanged: onChanged,
            style: GoogleFonts.plusJakartaSans(
              fontSize: 14,
              color: theme.colorScheme.onSurface,
            ),
            decoration: InputDecoration(
              hintText: 'Job title, company, or skill...',
              prefixIcon: Icon(
                Icons.search_rounded,
                color: theme.colorScheme.onSurfaceVariant,
                size: 20,
              ),
              suffixIcon: controller.text.isNotEmpty
                  ? IconButton(
                      icon: Icon(
                        Icons.close_rounded,
                        size: 18,
                        color: theme.colorScheme.onSurfaceVariant,
                      ),
                      onPressed: () {
                        controller.clear();
                        onChanged('');
                      },
                    )
                  : null,
              filled: true,
              fillColor: theme.colorScheme.surfaceContainerHighest,
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
                borderSide: BorderSide.none,
              ),
              enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
                borderSide: BorderSide.none,
              ),
              focusedBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
                borderSide: BorderSide(
                  color: theme.colorScheme.primary,
                  width: 1.5,
                ),
              ),
              contentPadding: const EdgeInsets.symmetric(
                horizontal: 16,
                vertical: 12,
              ),
            ),
          ),
        ),
        const SizedBox(width: 10),
        GestureDetector(
          onTap: onFilterTap,
          child: Stack(
            children: [
              Container(
                width: 46,
                height: 46,
                decoration: BoxDecoration(
                  color: activeFilterCount > 0
                      ? theme.colorScheme.primaryContainer
                      : theme.colorScheme.surfaceContainerHighest,
                  borderRadius: BorderRadius.circular(12),
                  border: activeFilterCount > 0
                      ? Border.all(color: theme.colorScheme.primary, width: 1.5)
                      : null,
                ),
                child: Icon(
                  Icons.tune_rounded,
                  color: activeFilterCount > 0
                      ? theme.colorScheme.primary
                      : theme.colorScheme.onSurfaceVariant,
                  size: 22,
                ),
              ),
              if (activeFilterCount > 0)
                Positioned(
                  top: 4,
                  right: 4,
                  child: Container(
                    width: 16,
                    height: 16,
                    decoration: BoxDecoration(
                      color: theme.colorScheme.primary,
                      shape: BoxShape.circle,
                    ),
                    child: Center(
                      child: Text(
                        '$activeFilterCount',
                        style: GoogleFonts.plusJakartaSans(
                          fontSize: 9,
                          fontWeight: FontWeight.w700,
                          color: Colors.white,
                        ),
                      ),
                    ),
                  ),
                ),
            ],
          ),
        ),
      ],
    );
  }
}
