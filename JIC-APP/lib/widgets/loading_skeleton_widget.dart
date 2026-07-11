import 'package:flutter/material.dart';

class LoadingSkeletonWidget extends StatefulWidget {
  final double width;
  final double height;
  final double borderRadius;

  const LoadingSkeletonWidget({
    this.width = double.infinity,
    this.height = 16,
    this.borderRadius = 8,
    super.key,
  });

  @override
  State<LoadingSkeletonWidget> createState() => _LoadingSkeletonWidgetState();
}

class _LoadingSkeletonWidgetState extends State<LoadingSkeletonWidget>
    with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  late Animation<double> _shimmerPosition;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 1200),
    )..repeat();
    _shimmerPosition = Tween<double>(
      begin: -0.5,
      end: 1.5,
    ).animate(CurvedAnimation(parent: _controller, curve: Curves.easeInOut));
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final base = theme.colorScheme.surfaceContainerHighest;
    return AnimatedBuilder(
      animation: _shimmerPosition,
      builder: (_, __) => Container(
        width: widget.width,
        height: widget.height,
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(widget.borderRadius),
          gradient: LinearGradient(
            colors: [base, base.withAlpha(128), base],
            stops: [
              (_shimmerPosition.value - 0.3).clamp(0.0, 1.0),
              _shimmerPosition.value.clamp(0.0, 1.0),
              (_shimmerPosition.value + 0.3).clamp(0.0, 1.0),
            ],
            begin: Alignment.centerLeft,
            end: Alignment.centerRight,
          ),
        ),
      ),
    );
  }
}

class JobCardSkeletonWidget extends StatelessWidget {
  const JobCardSkeletonWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Theme.of(context).colorScheme.surface,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withAlpha(15),
            blurRadius: 12,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              const LoadingSkeletonWidget(
                width: 44,
                height: 44,
                borderRadius: 12,
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: const [
                    LoadingSkeletonWidget(height: 14),
                    SizedBox(height: 6),
                    LoadingSkeletonWidget(width: 120, height: 12),
                  ],
                ),
              ),
            ],
          ),
          const SizedBox(height: 12),
          const LoadingSkeletonWidget(width: 100, height: 20),
          const SizedBox(height: 6),
          const LoadingSkeletonWidget(width: 160, height: 14),
          const SizedBox(height: 12),
          Row(
            children: const [
              LoadingSkeletonWidget(width: 70, height: 24, borderRadius: 100),
              SizedBox(width: 8),
              LoadingSkeletonWidget(width: 80, height: 24, borderRadius: 100),
            ],
          ),
        ],
      ),
    );
  }
}
