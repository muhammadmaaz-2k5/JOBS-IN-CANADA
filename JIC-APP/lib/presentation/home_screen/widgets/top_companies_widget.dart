import 'package:flutter/material.dart';

import '../../../core/app_export.dart';

class TopCompaniesWidget extends StatelessWidget {
  const TopCompaniesWidget({super.key});

  static final List<Map<String, dynamic>> _companies = [
    {
      'name': 'Spotify',
      'logoUrl':
          'https://img.rocket.new/generatedImages/rocket_gen_img_1da815be6-1769423589184.png',
      'semanticLabel': 'Spotify music streaming company logo',
      'jobCount': 23,
    },
    {
      'name': 'Apple',
      'logoUrl':
          'https://img.rocket.new/generatedImages/rocket_gen_img_19ec829ac-1783736379750.png',
      'semanticLabel': 'Apple technology company logo',
      'jobCount': 41,
    },
    {
      'name': 'Slack',
      'logoUrl':
          'https://img.rocket.new/generatedImages/rocket_gen_img_18b173690-1768637023216.png',
      'semanticLabel': 'Slack workplace communication platform logo',
      'jobCount': 17,
    },
    {
      'name': 'Shopify',
      'logoUrl':
          'https://img.rocket.new/generatedImages/rocket_gen_img_1fd7282dc-1772974660577.png',
      'semanticLabel': 'Shopify e-commerce platform company logo',
      'jobCount': 58,
    },
    {
      'name': 'Notion',
      'logoUrl':
          'https://img.rocket.new/generatedImages/rocket_gen_img_1caac3b51-1772623965291.png',
      'semanticLabel': 'Notion productivity software company logo',
      'jobCount': 9,
    },
    {
      'name': 'RBC',
      'logoUrl':
          'https://img.rocket.new/generatedImages/rocket_gen_img_1cfdd4e14-1783736379911.png',
      'semanticLabel': 'Royal Bank of Canada financial institution logo',
      'jobCount': 134,
    },
  ];

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return SizedBox(
      height: 90,
      child: ListView.builder(
        scrollDirection: Axis.horizontal,
        padding: const EdgeInsets.symmetric(horizontal: 16),
        itemCount: _companies.length,
        itemBuilder: (_, i) {
          final company = _companies[i];
          return Container(
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
                    child: CustomImageWidget(
                      imageUrl: company['logoUrl'] as String,
                      width: 56,
                      height: 56,
                      fit: BoxFit.cover,
                      semanticLabel: company['semanticLabel'] as String,
                    ),
                  ),
                ),
                const SizedBox(height: 6),
                Text(
                  company['name'] as String,
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
          );
        },
      ),
    );
  }
}
