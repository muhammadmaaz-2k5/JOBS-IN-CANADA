import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

// Anatomy locked: bookmark icon left + full-width pill Apply button

class StickyApplyBarWidget extends StatelessWidget {
  final bool isSaved;
  final bool isApplying;
  final VoidCallback onSave;
  final VoidCallback onApply;

  const StickyApplyBarWidget({
    required this.isSaved,
    required this.isApplying,
    required this.onSave,
    required this.onApply,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final bottomPadding = MediaQuery.of(context).padding.bottom;

    return Container(
      padding: EdgeInsets.fromLTRB(16, 12, 16, 12 + bottomPadding),
      decoration: BoxDecoration(
        color: theme.colorScheme.surface,
        boxShadow: [
          BoxShadow(
            color: Colors.black.withAlpha(20),
            blurRadius: 16,
            offset: const Offset(0, -4),
          ),
        ],
      ),
      child: Row(
        children: [
          // Bookmark button
          GestureDetector(
            onTap: onSave,
            child: AnimatedContainer(
              duration: const Duration(milliseconds: 200),
              width: 50,
              height: 50,
              decoration: BoxDecoration(
                color: isSaved
                    ? theme.colorScheme.primaryContainer
                    : theme.colorScheme.surfaceContainerHighest,
                borderRadius: BorderRadius.circular(14),
                border: isSaved
                    ? Border.all(color: theme.colorScheme.primary, width: 1.5)
                    : null,
              ),
              child: Icon(
                isSaved
                    ? Icons.bookmark_rounded
                    : Icons.bookmark_outline_rounded,
                color: isSaved
                    ? theme.colorScheme.primary
                    : theme.colorScheme.onSurfaceVariant,
                size: 22,
              ),
            ),
          ),
          const SizedBox(width: 12),
          // Apply Now pill button
          Expanded(
            child: SizedBox(
              height: 50,
              child: FilledButton(
                onPressed: isApplying ? null : onApply,
                style: FilledButton.styleFrom(
                  backgroundColor: theme.colorScheme.primary,
                  disabledBackgroundColor: theme.colorScheme.primary.withAlpha(
                    153,
                  ),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(100),
                  ),
                ),
                child: isApplying
                    ? const SizedBox(
                        width: 20,
                        height: 20,
                        child: CircularProgressIndicator(
                          strokeWidth: 2,
                          color: Colors.white,
                        ),
                      )
                    : Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          const Icon(
                            Icons.open_in_new_rounded,
                            size: 16,
                            color: Colors.white,
                          ),
                          const SizedBox(width: 8),
                          Text(
                            'Apply Now',
                            style: GoogleFonts.plusJakartaSans(
                              fontSize: 15,
                              fontWeight: FontWeight.w700,
                              color: Colors.white,
                            ),
                          ),
                        ],
                      ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
