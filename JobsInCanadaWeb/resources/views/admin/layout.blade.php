<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>@yield('title', 'Admin') &middot; Jobs in Canada</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="{{ asset('css/admin.css') }}">
</head>
<body>
<div class="admin-shell">
    <aside class="sidebar">
        <div class="brand">Jobs<span>inCanada</span> Admin</div>
        <nav>
            <div class="nav-label">Overview</div>
            <a class="nav-item {{ request()->routeIs('admin.dashboard') ? 'active' : '' }}" href="{{ route('admin.dashboard') }}">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 13h8V3H3zM13 21h8V11h-8zM13 3v6h8V3zM3 21h8v-6H3z"/></svg>
                Dashboard
            </a>

            <div class="nav-label">Content</div>
            <a class="nav-item {{ request()->routeIs('admin.jobs.*') ? 'active' : '' }}" href="{{ route('admin.jobs.index') }}">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 7h16M4 12h16M4 17h10"/></svg>
                Jobs
            </a>
            <a class="nav-item {{ request()->routeIs('admin.companies.*') ? 'active' : '' }}" href="{{ route('admin.companies.index') }}">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 21V8l9-5 9 5v13M9 21v-6h6v6"/></svg>
                Companies
            </a>
            <a class="nav-item {{ request()->routeIs('admin.categories.*') ? 'active' : '' }}" href="{{ route('admin.categories.index') }}">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 5h7v7H4zM13 5h7v7h-7zM4 14h7v5H4zM13 14h7v5h-7z"/></svg>
                Categories
            </a>
            <a class="nav-item {{ request()->routeIs('admin.provinces.*') ? 'active' : '' }}" href="{{ route('admin.provinces.index') }}">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2a7 7 0 0 0-7 7c0 5 7 13 7 13s7-8 7-13a7 7 0 0 0-7-7zM12 9a2 2 0 1 0 0-4 2 2 0 0 0 0 4z"/></svg>
                Provinces
            </a>
            <a class="nav-item {{ request()->routeIs('admin.logos.*') ? 'active' : '' }}" href="{{ route('admin.logos.index') }}">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 4h16v12H4zM4 20h16M9 16v4M15 16v4"/></svg>
                Logos
            </a>
            <a class="nav-item {{ request()->routeIs('admin.career-resources.*') ? 'active' : '' }}" href="{{ route('admin.career-resources.index') }}">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 19V5a2 2 0 0 1 2-2h9l5 5v11a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2z"/></svg>
                Career Resources
            </a>

            <div class="nav-label">System</div>
            <a class="nav-item {{ request()->routeIs('admin.settings.*') ? 'active' : '' }}" href="{{ route('admin.settings.index') }}">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 8a4 4 0 1 0 0 8 4 4 0 0 0 0-8zM4 8h2l1.5-3 2.5 5 2-4 2 4 2.5-5L19 8h1v8h-2l-1.5 3-2.5-5-2 4-2-4-2.5 5L5 16H4z"/></svg>
                Settings
            </a>
        </nav>
        <div class="user-box">
            <strong>{{ auth()->user()->name }}</strong>
            {{ auth()->user()->email }}
        </div>
    </aside>

    <div class="sidebar-overlay" id="sidebarOverlay"></div>

    <div class="main">
        <header class="topbar">
            <div style="display:flex; align-items:center;">
                <button class="menu-toggle" id="menuToggle" type="button" aria-label="Toggle menu">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 6h16M4 12h16M4 18h16"/></svg>
                </button>
                <div>
                    <h1>@yield('page-title', 'Dashboard')</h1>
                    @hasSection('page-sub')
                        <div class="sub">@yield('page-sub')</div>
                    @endif
                </div>
            </div>
            <form method="POST" action="{{ route('admin.logout') }}">
                @csrf
                <button class="btn btn-ghost btn-sm" type="submit">Log out</button>
            </form>
        </header>

        <main class="content">
            @if (session('success'))
                <div class="alert alert-success">{{ session('success') }}</div>
            @endif
            @if (session('error'))
                <div class="alert alert-error">{{ session('error') }}</div>
            @endif

            @yield('content')
        </main>
    </div>
</div>

<script>
    (function () {
        var sidebar = document.querySelector('.sidebar');
        var overlay = document.getElementById('sidebarOverlay');
        var toggle = document.getElementById('menuToggle');

        function openSidebar() {
            sidebar.classList.add('open');
            overlay.classList.add('show');
        }
        function closeSidebar() {
            sidebar.classList.remove('open');
            overlay.classList.remove('show');
        }

        toggle.addEventListener('click', function () {
            if (sidebar.classList.contains('open')) {
                closeSidebar();
            } else {
                openSidebar();
            }
        });

        overlay.addEventListener('click', closeSidebar);

        sidebar.querySelectorAll('a.nav-item').forEach(function (link) {
            link.addEventListener('click', closeSidebar);
        });
    })();
</script>
</body>
</html>
