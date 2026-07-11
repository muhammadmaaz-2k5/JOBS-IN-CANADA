import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

enum JobTypeBadge { fullTime, partTime, contract, remote, internship }

class StatusBadgeWidget extends StatelessWidget {
  final String label;
  final Color backgroundColor;
  final Color textColor;
  final double fontSize;

  const StatusBadgeWidget({
    required this.label,
    required this.backgroundColor,
    required this.textColor,
    this.fontSize = 11,
    super.key,
  });

  factory StatusBadgeWidget.jobType(String type) {
    Color bg;
    Color fg;
    switch (type.toLowerCase()) {
      case 'full-time':
        bg = const Color(0xFFDCFCE7);
        fg = const Color(0xFF16A34A);
        break;
      case 'part-time':
        bg = const Color(0xFFDBEAFE);
        fg = const Color(0xFF2563EB);
        break;
      case 'contract':
        bg = const Color(0xFFFEF3C7);
        fg = const Color(0xFFD97706);
        break;
      case 'remote':
        bg = const Color(0xFFF3E8FF);
        fg = const Color(0xFFA855F7);
        break;
      case 'internship':
        bg = const Color(0xFFFFEDD5);
        fg = const Color(0xFFEA580C);
        break;
      default:
        bg = const Color(0xFFF3F4F6);
        fg = const Color(0xFF6B7280);
    }
    return StatusBadgeWidget(label: type, backgroundColor: bg, textColor: fg);
  }

  factory StatusBadgeWidget.newBadge() {
    return const StatusBadgeWidget(
      label: 'NEW',
      backgroundColor: Color(0xFFDCFCE7),
      textColor: Color(0xFF16A34A),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
      decoration: BoxDecoration(
        color: backgroundColor,
        borderRadius: BorderRadius.circular(100),
      ),
      child: Text(
        label,
        style: GoogleFonts.plusJakartaSans(
          fontSize: fontSize,
          fontWeight: FontWeight.w600,
          color: textColor,
          letterSpacing: 0.2,
        ),
      ),
    );
  }
}
