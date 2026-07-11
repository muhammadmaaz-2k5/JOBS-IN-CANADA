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

        <div class="actions" style="margin-top:22px;">
            <button class="btn btn-primary" type="submit">Save Settings</button>
        </div>
    </form>
</div>
@endsection
