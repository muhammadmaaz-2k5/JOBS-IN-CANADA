<?php

namespace App\Services;

use Illuminate\Support\Facades\Http;

class FCMService
{
    /**
     * Send an FCM push notification.
     */
    public static function send($title, $body, $imageUrl = null, $screen = null, $jobId = null, $resourceId = null)
    {
        $path = self::getFirebaseCredentialsPath();
        if (!file_exists($path)) {
            throw new \Exception("Firebase service account file not found at $path. Please verify FIREBASE_CREDENTIALS_PATH in .env.");
        }
        
        $credentials = json_decode(file_get_contents($path), true);
        $projectId = $credentials['project_id'];

        // Retrieve OAuth2 Bearer Access Token
        $accessToken = self::getAccessToken($credentials);

        // Setup the FCM payload
        $payload = [
            'message' => [
                'topic' => 'all', // Broadcast to all subscribed devices
                'notification' => [
                    'title' => (string) $title,
                    'body' => (string) $body,
                    'image' => (string) ($imageUrl ?? ''),
                ],
                'data' => [
                    'title' => (string) $title,
                    'body' => (string) $body,
                    'image_url' => (string) ($imageUrl ?? ''),
                    'screen' => (string) ($screen ?? ''),
                    'route' => (string) ($screen ?? ''),
                    'job_id' => (string) ($jobId ?? ''),
                    'resource_id' => (string) ($resourceId ?? ''),
                ]
            ]
        ];

        // Send request to Firebase V1 Endpoint
        $response = Http::withHeaders([
            'Authorization' => 'Bearer ' . $accessToken,
            'Content-Type' => 'application/json'
        ])->post("https://fcm.googleapis.com/v1/projects/{$projectId}/messages:send", $payload);

        if (!$response->successful()) {
            throw new \Exception("FCM API error: " . $response->body());
        }

        return $response->json();
    }

    /**
     * Fetch a random image URL from public/notificationsimages.
     */
    public static function getRandomNotificationImageUrl(): ?string
    {
        $dir = public_path('notificationsimages');
        if (!is_dir($dir)) {
            return null;
        }
        
        $files = array_diff(scandir($dir), ['.', '..']);
        if (empty($files)) {
            return null;
        }
        
        $allowedExtensions = ['jpg', 'jpeg', 'png', 'webp'];
        $imageFiles = array_filter($files, function($file) use ($allowedExtensions) {
            $ext = strtolower(pathinfo($file, PATHINFO_EXTENSION));
            return in_array($ext, $allowedExtensions);
        });
        
        if (empty($imageFiles)) {
            return null;
        }
        
        $randomFile = $imageFiles[array_rand($imageFiles)];
        return asset('notificationsimages/' . $randomFile);
    }

    private static function getAccessToken($credentials)
    {
        $header = json_encode(['alg' => 'RS256', 'typ' => 'JWT']);
        $now = time();
        $payload = json_encode([
            'iss' => $credentials['client_email'],
            'scope' => 'https://www.googleapis.com/auth/firebase.messaging',
            'aud' => 'https://oauth2.googleapis.com/token',
            'exp' => $now + 3600,
            'iat' => $now,
        ]);

        $base64UrlHeader = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($header));
        $base64UrlPayload = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($payload));
        $signatureInput = $base64UrlHeader . "." . $base64UrlPayload;

        $signature = '';
        openssl_sign($signatureInput, $signature, $credentials['private_key'], 'sha256WithRSAEncryption');
        $base64UrlSignature = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($signature));
        
        $jwt = $signatureInput . "." . $base64UrlSignature;

        $response = Http::asForm()->post('https://oauth2.googleapis.com/token', [
            'grant_type' => 'urn:ietf:params:oauth:grant-type:jwt-bearer',
            'assertion' => $jwt,
        ]);

        if ($response->successful()) {
            return $response->json()['access_token'];
        }

        throw new \Exception("Failed to obtain access token: " . $response->body());
    }

    private static function getFirebaseCredentialsPath()
    {
        $configuredPath = env('FIREBASE_CREDENTIALS_PATH');
        if ($configuredPath) {
            if (str_starts_with($configuredPath, '/') || str_contains($configuredPath, ':')) {
                $path = $configuredPath;
            } else {
                $path = base_path($configuredPath);
            }
            if (file_exists($path)) {
                return $path;
            }
        }
        return base_path('firebase-service-account.json');
    }
}
