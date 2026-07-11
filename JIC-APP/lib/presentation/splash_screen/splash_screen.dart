import 'package:flutter/material.dart';
import 'package:video_player/video_player.dart';
import 'package:go_router/go_router.dart';

import '../../routes/app_routes.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  late VideoPlayerController _controller;
  bool _isInitialized = false;

  @override
  void initState() {
    super.initState();
    _initializeVideo();
  }

  Future<void> _initializeVideo() async {
    try {
      _controller = VideoPlayerController.asset('assets/splashscreen.mp4');
      await _controller.initialize();
      _controller.addListener(_videoListener);
      if (mounted) {
        setState(() {
          _isInitialized = true;
        });
        _controller.play();
      }
    } catch (e) {
      debugPrint('Error initializing video splash: $e');
      _navigateToHome(); // Fallback if video fails to load
    }
  }

  void _videoListener() {
    if (_controller.value.position >= _controller.value.duration) {
      _navigateToHome();
    }
  }

  void _navigateToHome() {
    if (mounted) {
      _controller.removeListener(_videoListener);
      context.go(AppRoutes.homeScreen);
    }
  }

  @override
  void dispose() {
    _controller.removeListener(_videoListener);
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      body: Stack(
        children: [
          Center(
            child: _isInitialized
                ? AspectRatio(
                    aspectRatio: _controller.value.aspectRatio,
                    child: VideoPlayer(_controller),
                  )
                : const CircularProgressIndicator(
                    color: Colors.white,
                  ),
          ),
          Positioned(
            top: 40,
            right: 20,
            child: TextButton(
              onPressed: _navigateToHome,
              style: TextButton.styleFrom(
                foregroundColor: Colors.white,
                backgroundColor: Colors.black.withAlpha(80),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(20),
                ),
              ),
              child: const Text('Skip'),
            ),
          ),
        ],
      ),
    );
  }
}
