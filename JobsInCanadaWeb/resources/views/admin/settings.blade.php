@extends('admin.layout')

@section('page-title', 'Settings')
@section('page-sub', 'Home screen content shown in the mobile app')

@section('content')
<div class="card card-pad" style="max-width:620px;">
    <p class="muted" style="margin-top:0;">These values power the "Today in Canada" banner on the app home screen.</p>

    <form method="POST" action="{{ route('admin.settings.update') }}">
        @csrf
        @method('PUT')

        <div class="form-grid">
            <div class="field">
                <label for="jobs_today">Jobs posted today</label>
                <input type="number" id="jobs_today" name="jobs_today" value="{{ old('jobs_today', $jobsToday) }}" min="0" required>
                @error('jobs_today')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>

            <div class="field">
                <label for="jobs_this_week">Jobs posted this week</label>
                <input type="number" id="jobs_this_week" name="jobs_this_week" value="{{ old('jobs_this_week', $jobsThisWeek) }}" min="0" required>
                @error('jobs_this_week')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>
        </div>

        <hr style="border:0; border-top:1px solid var(--outline); margin:24px 0;">
        <h4 style="margin:0 0 12px 0; color:var(--text)">Ad & Webview Management</h4>
        <p class="muted" style="margin-top:0; margin-bottom:18px;">Manage dynamic Webview options and active Ads settings for the mobile app.</p>

        <div class="form-grid">
            <div class="field">
                <label for="ads_enabled">Global Ads Toggle</label>
                <select id="ads_enabled" name="ads_enabled">
                    <option value="true" {{ old('ads_enabled', $adsEnabled) == 'true' ? 'selected' : '' }}>Enabled</option>
                    <option value="false" {{ old('ads_enabled', $adsEnabled) == 'false' ? 'selected' : '' }}>Disabled</option>
                </select>
                @error('ads_enabled')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>

            <div class="field">
                <label for="enable_webview_ads">Webview Ads</label>
                <select id="enable_webview_ads" name="enable_webview_ads">
                    <option value="true" {{ old('enable_webview_ads', $enableWebviewAds) == 'true' ? 'selected' : '' }}>Enabled</option>
                    <option value="false" {{ old('enable_webview_ads', $enableWebviewAds) == 'false' ? 'selected' : '' }}>Disabled</option>
                </select>
                @error('enable_webview_ads')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>

            <div class="field" style="grid-column: span 2;">
                <label for="webview_ad_url">Webview Ad Destination URL</label>
                <input type="url" id="webview_ad_url" name="webview_ad_url" value="{{ old('webview_ad_url', $webviewAdUrl) }}" placeholder="https://nazaarabox.com" required>
                @error('webview_ad_url')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>
        </div>

        <h3 style="margin-top:28px; margin-bottom:6px; font-size:18px;">Job Details Page Ads</h3>
        <p class="muted" style="margin-top:0; margin-bottom:18px;">Configure the status and unique URLs for the 10 dynamic webview ad placements shown on the job details screen.</p>
        
        <div class="form-grid">
            @for ($i = 1; $i <= 10; $i++)
                <div class="field">
                    <label for="enable_ad_detail_{{ $i }}">Ad #{{ $i }} Status</label>
                    <select id="enable_ad_detail_{{ $i }}" name="enable_ad_detail_{{ $i }}">
                        <option value="true" {{ old("enable_ad_detail_$i", $enableAdDetail[$i]) == 'true' ? 'selected' : '' }}>Enabled</option>
                        <option value="false" {{ old("enable_ad_detail_$i", $enableAdDetail[$i]) == 'false' ? 'selected' : '' }}>Disabled</option>
                    </select>
                    @error("enable_ad_detail_$i")<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
                </div>
                
                <div class="field">
                    <label for="ad_url_detail_{{ $i }}">Ad #{{ $i }} Custom URL</label>
                    <input type="url" id="ad_url_detail_{{ $i }}" name="ad_url_detail_{{ $i }}" value="{{ old("ad_url_detail_$i", $adUrlDetail[$i]) }}" placeholder="Leave blank to use global URL">
                    @error("ad_url_detail_$i")<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
                </div>
            @endfor
        </div>

        <div class="actions" style="margin-top:22px;">
            <button class="btn btn-primary" type="submit">Save Settings</button>
        </div>
    </form>
</div>
@endsection
