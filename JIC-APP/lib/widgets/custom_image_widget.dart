import 'package:flutter/material.dart';

class CustomImageWidget extends StatelessWidget {
  final String imageUrl;
  final double? width;
  final double? height;
  final BoxFit? fit;
  final String? semanticLabel;

  const CustomImageWidget({
    super.key,
    required this.imageUrl,
    this.width,
    this.height,
    this.fit,
    this.semanticLabel,
  });

  @override
  Widget build(BuildContext context) {
    if (imageUrl.isEmpty) {
      return _placeholder();
    }

    return Image.network(
      imageUrl,
      width: width,
      height: height,
      fit: fit ?? BoxFit.cover,
      semanticLabel: semanticLabel,
      loadingBuilder: (context, child, progress) {
        if (progress == null) return child;
        return Container(
          width: width,
          height: height,
          alignment: Alignment.center,
          child: const CircularProgressIndicator(strokeWidth: 2),
        );
      },
      errorBuilder: (context, error, stackTrace) => _placeholder(),
    );
  }

  Widget _placeholder() {
    final iconSize = (width != null && width! < 40) ? 16.0 : 24.0;

    return Container(
      width: width,
      height: height,
      alignment: Alignment.center,
      decoration: BoxDecoration(
        color: Colors.grey.shade200,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Icon(
        Icons.image_outlined,
        size: iconSize,
        color: Colors.grey.shade400,
      ),
    );
  }
}
