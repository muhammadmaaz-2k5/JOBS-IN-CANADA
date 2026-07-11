import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

// THEME LOCK: light — source: domain signal (consumer job board, outdoor/commute use)
// Scaffold.backgroundColor = AppTheme.backgroundLight — ALL screens

class AppTheme {
  // Primary brand colors
  static const Color primary = Color(0xFF1A6B3C);
  static const Color primaryLight = Color(0xFF2D8A52);
  static const Color primaryContainer = Color(0xFFD1FAE5);
  static const Color onPrimaryContainer = Color(0xFF064E24);

  static const Color secondary = Color(0xFFA855F7);
  static const Color secondaryContainer = Color(0xFFF3E8FF);
  static const Color onSecondaryContainer = Color(0xFF5B21B6);

  static const Color tertiary = Color(0xFF2563EB);
  static const Color tertiaryContainer = Color(0xFFDBEAFE);

  // Semantic colors
  static const Color success = Color(0xFF16A34A);
  static const Color successContainer = Color(0xFFDCFCE7);
  static const Color warning = Color(0xFFD97706);
  static const Color warningContainer = Color(0xFFFEF3C7);
  static const Color errorColor = Color(0xFFDC2626);
  static const Color errorContainer = Color(0xFFFEE2E2);

  // Surface colors — light
  static const Color surfaceLight = Color(0xFFFFFFFF);
  static const Color surfaceVariantLight = Color(0xFFF8F9FA);
  static const Color backgroundLight = Color(0xFFF4F6F8);
  static const Color outlineLight = Color(0xFFE5E7EB);
  static const Color outlineVariantLight = Color(0xFFF3F4F6);

  // Text colors
  static const Color textPrimary = Color(0xFF111827);
  static const Color textSecondary = Color(0xFF6B7280);
  static const Color textMuted = Color(0xFF9CA3AF);

  // Surface colors — dark
  static const Color surfaceDark = Color(0xFF1E2028);
  static const Color backgroundDark = Color(0xFF13151A);
  static const Color surfaceVariantDark = Color(0xFF252830);

  static ThemeData get lightTheme => ThemeData(
    useMaterial3: true,
    colorScheme: const ColorScheme.light(
      primary: primary,
      onPrimary: Colors.white,
      primaryContainer: primaryContainer,
      onPrimaryContainer: onPrimaryContainer,
      secondary: secondary,
      onSecondary: Colors.white,
      secondaryContainer: secondaryContainer,
      onSecondaryContainer: onSecondaryContainer,
      tertiary: tertiary,
      tertiaryContainer: tertiaryContainer,
      surface: surfaceLight,
      onSurface: textPrimary,
      surfaceContainerHighest: surfaceVariantLight,
      onSurfaceVariant: textSecondary,
      outline: outlineLight,
      outlineVariant: outlineVariantLight,
      error: errorColor,
      onError: Colors.white,
      shadow: Color(0x1A000000),
    ),
    scaffoldBackgroundColor: backgroundLight,
    textTheme: GoogleFonts.plusJakartaSansTextTheme().copyWith(
      displayLarge: GoogleFonts.plusJakartaSans(
        fontSize: 32,
        fontWeight: FontWeight.w700,
        color: textPrimary,
      ),
      displayMedium: GoogleFonts.plusJakartaSans(
        fontSize: 28,
        fontWeight: FontWeight.w700,
        color: textPrimary,
      ),
      headlineLarge: GoogleFonts.plusJakartaSans(
        fontSize: 24,
        fontWeight: FontWeight.w700,
        color: textPrimary,
      ),
      headlineMedium: GoogleFonts.plusJakartaSans(
        fontSize: 20,
        fontWeight: FontWeight.w600,
        color: textPrimary,
      ),
      headlineSmall: GoogleFonts.plusJakartaSans(
        fontSize: 18,
        fontWeight: FontWeight.w600,
        color: textPrimary,
      ),
      titleLarge: GoogleFonts.plusJakartaSans(
        fontSize: 16,
        fontWeight: FontWeight.w600,
        color: textPrimary,
      ),
      titleMedium: GoogleFonts.plusJakartaSans(
        fontSize: 15,
        fontWeight: FontWeight.w600,
        color: textPrimary,
      ),
      titleSmall: GoogleFonts.plusJakartaSans(
        fontSize: 14,
        fontWeight: FontWeight.w500,
        color: textPrimary,
      ),
      bodyLarge: GoogleFonts.plusJakartaSans(
        fontSize: 16,
        fontWeight: FontWeight.w400,
        color: textPrimary,
      ),
      bodyMedium: GoogleFonts.plusJakartaSans(
        fontSize: 14,
        fontWeight: FontWeight.w400,
        color: textPrimary,
      ),
      bodySmall: GoogleFonts.plusJakartaSans(
        fontSize: 13,
        fontWeight: FontWeight.w400,
        color: textSecondary,
      ),
      labelLarge: GoogleFonts.plusJakartaSans(
        fontSize: 13,
        fontWeight: FontWeight.w600,
        color: textPrimary,
      ),
      labelMedium: GoogleFonts.plusJakartaSans(
        fontSize: 12,
        fontWeight: FontWeight.w500,
        color: textSecondary,
      ),
      labelSmall: GoogleFonts.plusJakartaSans(
        fontSize: 11,
        fontWeight: FontWeight.w400,
        color: textMuted,
        letterSpacing: 0.2,
      ),
    ),
    appBarTheme: AppBarThemeData(
      backgroundColor: surfaceLight,
      elevation: 0,
      scrolledUnderElevation: 1,
      shadowColor: const Color(0x1A000000),
      centerTitle: false,
      titleTextStyle: GoogleFonts.plusJakartaSans(
        fontSize: 18,
        fontWeight: FontWeight.w700,
        color: textPrimary,
      ),
      iconTheme: const IconThemeData(color: textPrimary),
    ),
    cardTheme: CardThemeData(
      color: surfaceLight,
      elevation: 0,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      margin: EdgeInsets.zero,
    ),
    inputDecorationTheme: InputDecorationThemeData(
      filled: true,
      fillColor: surfaceVariantLight,
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
        borderSide: const BorderSide(color: primary, width: 1.5),
      ),
      contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
      hintStyle: GoogleFonts.plusJakartaSans(fontSize: 14, color: textMuted),
    ),
    chipTheme: ChipThemeData(
      backgroundColor: surfaceVariantLight,
      selectedColor: primaryContainer,
      labelStyle: GoogleFonts.plusJakartaSans(
        fontSize: 13,
        fontWeight: FontWeight.w500,
      ),
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(100)),
      side: BorderSide.none,
    ),
    navigationBarTheme: NavigationBarThemeData(
      backgroundColor: surfaceLight,
      indicatorColor: primaryContainer,
      labelTextStyle: WidgetStateProperty.resolveWith((states) {
        if (states.contains(WidgetState.selected)) {
          return GoogleFonts.plusJakartaSans(
            fontSize: 12,
            fontWeight: FontWeight.w600,
            color: primary,
          );
        }
        return GoogleFonts.plusJakartaSans(
          fontSize: 12,
          fontWeight: FontWeight.w400,
          color: textMuted,
        );
      }),
      iconTheme: WidgetStateProperty.resolveWith((states) {
        if (states.contains(WidgetState.selected)) {
          return const IconThemeData(color: primary, size: 22);
        }
        return const IconThemeData(color: textMuted, size: 22);
      }),
      elevation: 8,
      shadowColor: const Color(0x1A000000),
    ),
    elevatedButtonTheme: ElevatedButtonThemeData(
      style: ElevatedButton.styleFrom(
        backgroundColor: primary,
        foregroundColor: Colors.white,
        elevation: 0,
        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 14),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(100)),
        textStyle: GoogleFonts.plusJakartaSans(
          fontSize: 15,
          fontWeight: FontWeight.w600,
        ),
      ),
    ),
    filledButtonTheme: FilledButtonThemeData(
      style: FilledButton.styleFrom(
        backgroundColor: primary,
        foregroundColor: Colors.white,
        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 14),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(100)),
        textStyle: GoogleFonts.plusJakartaSans(
          fontSize: 15,
          fontWeight: FontWeight.w600,
        ),
      ),
    ),
    dividerTheme: const DividerThemeData(
      color: outlineVariantLight,
      thickness: 1,
      space: 0,
    ),
    bottomSheetTheme: const BottomSheetThemeData(
      backgroundColor: surfaceLight,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      elevation: 8,
    ),
  );

  static ThemeData get darkTheme => ThemeData(
    useMaterial3: true,
    colorScheme: const ColorScheme.dark(
      primary: primaryLight,
      onPrimary: Colors.white,
      primaryContainer: Color(0xFF0D4A28),
      onPrimaryContainer: Color(0xFFD1FAE5),
      secondary: secondary,
      onSecondary: Colors.white,
      secondaryContainer: Color(0xFF3B0764),
      onSecondaryContainer: Color(0xFFF3E8FF),
      surface: surfaceDark,
      onSurface: Color(0xFFE6E6E6),
      surfaceContainerHighest: surfaceVariantDark,
      onSurfaceVariant: Color(0xFF9CA3AF),
      outline: Color(0xFF374151),
      outlineVariant: Color(0xFF1F2937),
      error: Color(0xFFCF6679),
      onError: Colors.white,
      shadow: Color(0x33000000),
    ),
    scaffoldBackgroundColor: backgroundDark,
    textTheme: GoogleFonts.plusJakartaSansTextTheme().copyWith(
      displayLarge: GoogleFonts.plusJakartaSans(
        fontSize: 32,
        fontWeight: FontWeight.w700,
        color: const Color(0xFFE6E6E6),
      ),
      headlineLarge: GoogleFonts.plusJakartaSans(
        fontSize: 24,
        fontWeight: FontWeight.w700,
        color: const Color(0xFFE6E6E6),
      ),
      titleLarge: GoogleFonts.plusJakartaSans(
        fontSize: 16,
        fontWeight: FontWeight.w600,
        color: const Color(0xFFE6E6E6),
      ),
      titleMedium: GoogleFonts.plusJakartaSans(
        fontSize: 15,
        fontWeight: FontWeight.w600,
        color: const Color(0xFFE6E6E6),
      ),
      bodyLarge: GoogleFonts.plusJakartaSans(
        fontSize: 16,
        fontWeight: FontWeight.w400,
        color: const Color(0xFFE6E6E6),
      ),
      bodyMedium: GoogleFonts.plusJakartaSans(
        fontSize: 14,
        fontWeight: FontWeight.w400,
        color: const Color(0xFFE6E6E6),
      ),
      bodySmall: GoogleFonts.plusJakartaSans(
        fontSize: 13,
        fontWeight: FontWeight.w400,
        color: const Color(0xFF9CA3AF),
      ),
      labelMedium: GoogleFonts.plusJakartaSans(
        fontSize: 12,
        fontWeight: FontWeight.w500,
        color: const Color(0xFF9CA3AF),
      ),
    ),
    appBarTheme: AppBarThemeData(
      backgroundColor: surfaceDark,
      elevation: 0,
      scrolledUnderElevation: 1,
      titleTextStyle: GoogleFonts.plusJakartaSans(
        fontSize: 18,
        fontWeight: FontWeight.w700,
        color: const Color(0xFFE6E6E6),
      ),
      iconTheme: const IconThemeData(color: Color(0xFFE6E6E6)),
    ),
    cardTheme: CardThemeData(
      color: surfaceDark,
      elevation: 0,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      margin: EdgeInsets.zero,
    ),
    inputDecorationTheme: InputDecorationThemeData(
      filled: true,
      fillColor: surfaceVariantDark,
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
        borderSide: const BorderSide(color: primaryLight, width: 1.5),
      ),
      hintStyle: GoogleFonts.plusJakartaSans(
        fontSize: 14,
        color: const Color(0xFF6B7280),
      ),
    ),
    navigationBarTheme: NavigationBarThemeData(
      backgroundColor: surfaceDark,
      indicatorColor: const Color(0xFF0D4A28),
      labelTextStyle: WidgetStateProperty.resolveWith((states) {
        if (states.contains(WidgetState.selected)) {
          return GoogleFonts.plusJakartaSans(
            fontSize: 12,
            fontWeight: FontWeight.w600,
            color: primaryLight,
          );
        }
        return GoogleFonts.plusJakartaSans(
          fontSize: 12,
          fontWeight: FontWeight.w400,
          color: const Color(0xFF6B7280),
        );
      }),
      iconTheme: WidgetStateProperty.resolveWith((states) {
        if (states.contains(WidgetState.selected)) {
          return const IconThemeData(color: primaryLight, size: 22);
        }
        return const IconThemeData(color: Color(0xFF6B7280), size: 22);
      }),
    ),
    dividerTheme: const DividerThemeData(
      color: Color(0xFF1F2937),
      thickness: 1,
      space: 0,
    ),
    bottomSheetTheme: const BottomSheetThemeData(
      backgroundColor: surfaceDark,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
    ),
  );
}
