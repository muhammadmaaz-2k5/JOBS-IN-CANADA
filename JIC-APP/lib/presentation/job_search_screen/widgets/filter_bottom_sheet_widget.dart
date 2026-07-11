import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class FilterBottomSheetWidget extends StatefulWidget {
  final Map<String, dynamic> currentFilters;
  final Function(Map<String, dynamic>) onApply;

  const FilterBottomSheetWidget({
    required this.currentFilters,
    required this.onApply,
    super.key,
  });

  @override
  State<FilterBottomSheetWidget> createState() =>
      _FilterBottomSheetWidgetState();
}

class _FilterBottomSheetWidgetState extends State<FilterBottomSheetWidget> {
  late List<String> _selectedProvinces;
  late List<String> _selectedJobTypes;
  late bool _remoteOnly;

  static const List<String> _provinces = [
    'Ontario',
    'British Columbia',
    'Quebec',
    'Alberta',
    'Manitoba',
    'Saskatchewan',
    'Nova Scotia',
    'New Brunswick',
    'Newfoundland',
    'Prince Edward Island',
  ];

  static const List<String> _jobTypes = [
    'Full-Time',
    'Part-Time',
    'Contract',
    'Internship',
    'Remote',
  ];

  @override
  void initState() {
    super.initState();
    _selectedProvinces = List<String>.from(
      widget.currentFilters['province'] as List? ?? [],
    );
    _selectedJobTypes = List<String>.from(
      widget.currentFilters['jobType'] as List? ?? [],
    );
    _remoteOnly = widget.currentFilters['remoteOnly'] as bool? ?? false;
  }

  void _toggleProvince(String p) {
    setState(() {
      _selectedProvinces.contains(p)
          ? _selectedProvinces.remove(p)
          : _selectedProvinces.add(p);
    });
  }

  void _toggleJobType(String t) {
    setState(() {
      _selectedJobTypes.contains(t)
          ? _selectedJobTypes.remove(t)
          : _selectedJobTypes.add(t);
    });
  }

  void _applyFilters() {
    widget.onApply({
      'province': _selectedProvinces,
      'jobType': _selectedJobTypes,
      'remoteOnly': _remoteOnly,
    });
    Navigator.pop(context);
  }

  void _clearAll() {
    setState(() {
      _selectedProvinces.clear();
      _selectedJobTypes.clear();
      _remoteOnly = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return DraggableScrollableSheet(
      initialChildSize: 0.75,
      maxChildSize: 0.92,
      minChildSize: 0.4,
      expand: false,
      builder: (_, scrollController) => Container(
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
        ),
        child: Column(
          children: [
            // Handle
            Container(
              margin: const EdgeInsets.only(top: 12),
              width: 36,
              height: 4,
              decoration: BoxDecoration(
                color: theme.colorScheme.outline,
                borderRadius: BorderRadius.circular(100),
              ),
            ),
            // Header
            Padding(
              padding: const EdgeInsets.fromLTRB(20, 16, 20, 0),
              child: Row(
                children: [
                  Text(
                    'Filter Jobs',
                    style: GoogleFonts.plusJakartaSans(
                      fontSize: 18,
                      fontWeight: FontWeight.w700,
                      color: theme.colorScheme.onSurface,
                    ),
                  ),
                  const Spacer(),
                  GestureDetector(
                    onTap: _clearAll,
                    child: Text(
                      'Clear All',
                      style: GoogleFonts.plusJakartaSans(
                        fontSize: 13,
                        fontWeight: FontWeight.w600,
                        color: theme.colorScheme.primary,
                      ),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 4),
            Divider(color: theme.colorScheme.outlineVariant, height: 1),
            Expanded(
              child: ListView(
                controller: scrollController,
                padding: const EdgeInsets.all(20),
                children: [
                  // Remote toggle
                  _FilterSection(
                    title: 'Work Mode',
                    child: Row(
                      children: [
                        Text(
                          'Remote Only',
                          style: GoogleFonts.plusJakartaSans(
                            fontSize: 14,
                            color: theme.colorScheme.onSurface,
                          ),
                        ),
                        const Spacer(),
                        Switch(
                          value: _remoteOnly,
                          onChanged: (v) => setState(() => _remoteOnly = v),
                          activeThumbColor: theme.colorScheme.primary,
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 20),
                  // Job Type
                  _FilterSection(
                    title: 'Job Type',
                    child: Wrap(
                      spacing: 8,
                      runSpacing: 8,
                      children: _jobTypes.map((t) {
                        final selected = _selectedJobTypes.contains(t);
                        return GestureDetector(
                          onTap: () => _toggleJobType(t),
                          child: Container(
                            padding: const EdgeInsets.symmetric(
                              horizontal: 14,
                              vertical: 8,
                            ),
                            decoration: BoxDecoration(
                              color: selected
                                  ? theme.colorScheme.primaryContainer
                                  : theme.colorScheme.surfaceContainerHighest,
                              borderRadius: BorderRadius.circular(100),
                              border: selected
                                  ? Border.all(
                                      color: theme.colorScheme.primary,
                                      width: 1,
                                    )
                                  : null,
                            ),
                            child: Text(
                              t,
                              style: GoogleFonts.plusJakartaSans(
                                fontSize: 13,
                                fontWeight: FontWeight.w500,
                                color: selected
                                    ? theme.colorScheme.primary
                                    : theme.colorScheme.onSurface,
                              ),
                            ),
                          ),
                        );
                      }).toList(),
                    ),
                  ),
                  const SizedBox(height: 20),
                  // Province
                  _FilterSection(
                    title: 'Province',
                    child: Wrap(
                      spacing: 8,
                      runSpacing: 8,
                      children: _provinces.map((p) {
                        final selected = _selectedProvinces.contains(p);
                        return GestureDetector(
                          onTap: () => _toggleProvince(p),
                          child: Container(
                            padding: const EdgeInsets.symmetric(
                              horizontal: 12,
                              vertical: 7,
                            ),
                            decoration: BoxDecoration(
                              color: selected
                                  ? theme.colorScheme.primaryContainer
                                  : theme.colorScheme.surfaceContainerHighest,
                              borderRadius: BorderRadius.circular(8),
                              border: selected
                                  ? Border.all(
                                      color: theme.colorScheme.primary,
                                      width: 1,
                                    )
                                  : null,
                            ),
                            child: Text(
                              p,
                              style: GoogleFonts.plusJakartaSans(
                                fontSize: 12,
                                fontWeight: FontWeight.w500,
                                color: selected
                                    ? theme.colorScheme.primary
                                    : theme.colorScheme.onSurface,
                              ),
                            ),
                          ),
                        );
                      }).toList(),
                    ),
                  ),
                  const SizedBox(height: 32),
                ],
              ),
            ),
            // Apply button
            Padding(
              padding: const EdgeInsets.fromLTRB(20, 0, 20, 24),
              child: SizedBox(
                width: double.infinity,
                height: 52,
                child: FilledButton(
                  onPressed: _applyFilters,
                  style: FilledButton.styleFrom(
                    backgroundColor: theme.colorScheme.primary,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(100),
                    ),
                  ),
                  child: Text(
                    'Apply Filters (${_selectedProvinces.length + _selectedJobTypes.length + (_remoteOnly ? 1 : 0)})',
                    style: GoogleFonts.plusJakartaSans(
                      fontSize: 15,
                      fontWeight: FontWeight.w600,
                      color: Colors.white,
                    ),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _FilterSection extends StatelessWidget {
  final String title;
  final Widget child;

  const _FilterSection({required this.title, required this.child});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          title,
          style: GoogleFonts.plusJakartaSans(
            fontSize: 15,
            fontWeight: FontWeight.w700,
            color: Theme.of(context).colorScheme.onSurface,
          ),
        ),
        const SizedBox(height: 12),
        child,
      ],
    );
  }
}
