@extends('admin.layout')

@section('page-title', 'Dashboard')
@section('page-sub', 'Overview of your mobile app content')

@section('content')
<div class="stat-grid">
    <div class="stat"><div class="label">Active Jobs</div><div class="value">{{ $stats['active_jobs'] }}</div><div class="bar"></div></div>
    <div class="stat"><div class="label">Featured Jobs</div><div class="value">{{ $stats['featured_jobs'] }}</div><div class="bar" style="background:var(--warning)"></div></div>
    <div class="stat"><div class="label">Companies</div><div class="value">{{ $stats['companies'] }}</div><div class="bar" style="background:var(--success)"></div></div>
    <div class="stat"><div class="label">Categories</div><div class="value">{{ $stats['categories'] }}</div><div class="bar" style="background:#a855f7"></div></div>
    <div class="stat"><div class="label">Resources</div><div class="value">{{ $stats['resources'] }}</div><div class="bar" style="background:#0ea5e9"></div></div>
    <div class="stat"><div class="label">Admins</div><div class="value">{{ $stats['admins'] }}</div><div class="bar" style="background:#64748b"></div></div>
</div>

<div class="card card-pad">
    <div class="page-head">
        <h2>Recently added jobs</h2>
        <a class="btn btn-ghost btn-sm" href="{{ route('admin.jobs.index') }}">View all</a>
    </div>

    @if ($recentJobs->isEmpty())
        <div class="empty">No jobs yet. <a href="{{ route('admin.jobs.create') }}" style="color:var(--primary);font-weight:700;">Add your first job</a>.</div>
    @else
        <table class="data">
            <thead>
                <tr><th>Title</th><th>Company</th><th>Category</th><th>Location</th><th>Status</th></tr>
            </thead>
            <tbody>
                @foreach ($recentJobs as $job)
                    <tr>
                        <td><strong>{{ $job->title }}</strong></td>
                        <td>{{ $job->company?->name ?? '—' }}</td>
                        <td>{{ $job->category?->name ?? '—' }}</td>
                        <td>{{ $job->location ?? '—' }}</td>
                        <td>
                            @if ($job->is_featured)<span class="badge amber">Featured</span> @endif
                            @if ($job->is_active)<span class="badge green">Active</span> @else <span class="badge gray">Hidden</span> @endif
                        </td>
                    </tr>
                @endforeach
            </tbody>
        </table>
    @endif
</div>
@endsection
