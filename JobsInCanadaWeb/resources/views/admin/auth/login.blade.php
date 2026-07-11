<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login &middot; Jobs in Canada</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="{{ asset('css/admin.css') }}">
</head>
<body>
<div class="login-wrap">
    <div class="login-card">
        <div class="brand">Jobs<span style="color:var(--primary)">in</span>Canada</div>
        <h2>Admin sign in</h2>
        <p class="muted" style="margin:0 0 22px;">Manage the mobile app content.</p>

        @if (session('error'))
            <div class="alert alert-error">{{ session('error') }}</div>
        @endif

        <form method="POST" action="{{ route('admin.login.submit') }}">
            @csrf

            <div class="field">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" value="{{ old('email') }}" required autofocus>
                @error('email')
                    <span class="hint" style="color:var(--danger)">{{ $message }}</span>
                @enderror
            </div>

            <div class="field">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
                @error('password')
                    <span class="hint" style="color:var(--danger)">{{ $message }}</span>
                @enderror
            </div>

            <label class="check" style="margin: 4px 0 20px;">
                <input type="checkbox" name="remember"> Remember me
            </label>

            <button class="btn btn-primary" style="width:100%; justify-content:center;" type="submit">Sign in</button>
        </form>
    </div>
</div>
</body>
</html>
