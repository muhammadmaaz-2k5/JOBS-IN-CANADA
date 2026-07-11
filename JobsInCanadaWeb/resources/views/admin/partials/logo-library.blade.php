@php
    $logos = \App\Models\Logo::orderBy('sort_order')->orderBy('name')->get();
@endphp

<div class="modal-overlay" id="logoLibraryOverlay">
    <div class="modal">
        <div class="modal-head">
            <h3>Choose a logo</h3>
            <button type="button" class="modal-close" onclick="closeLogoLibrary()">&times;</button>
        </div>
        <div class="logo-grid">
            @forelse ($logos as $logo)
                <button type="button" class="logo-pick" data-url="{{ $logo->url }}">
                    <img src="{{ $logo->url }}" alt="{{ $logo->name }}" class="logo-img">
                    <span class="logo-name">{{ $logo->name }}</span>
                </button>
            @empty
                <div class="empty">No logos in the library yet.</div>
            @endforelse
        </div>
    </div>
</div>

<script>
    (function () {
        window.openLogoLibrary = function (callback) {
            var overlay = document.getElementById('logoLibraryOverlay');
            overlay.classList.add('show');
            overlay._callback = callback;
        };

        window.closeLogoLibrary = function () {
            document.getElementById('logoLibraryOverlay').classList.remove('show');
        };

        var overlay = document.getElementById('logoLibraryOverlay');
        overlay.addEventListener('click', function (e) {
            if (e.target === overlay) closeLogoLibrary();
        });
        overlay.querySelectorAll('.logo-pick').forEach(function (btn) {
            btn.addEventListener('click', function () {
                var cb = overlay._callback;
                if (typeof cb === 'function') cb(btn.getAttribute('data-url'));
                closeLogoLibrary();
            });
        });
    })();
</script>
