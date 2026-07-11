@extends('admin.layout')

@section('page-title', 'Jobs')
@section('page-sub', 'Job listings shown in the mobile app')

@section('content')
<div class="page-head">
    <h2>All Jobs</h2>
    <div style="display:flex; gap:10px;">
        <button class="btn btn-ghost" onclick="document.getElementById('import-dialog').showModal()">JSON Import</button>
        <a class="btn btn-primary" href="{{ route('admin.jobs.create') }}">+ New Job</a>
    </div>
</div>

<dialog id="import-dialog" style="border:none; border-radius:12px; padding:24px; box-shadow: 0 4px 24px rgba(0,0,0,0.15); max-width: 600px; width:100%; font-family:inherit;">
    <form method="POST" action="{{ route('admin.jobs.import-json') }}">
        @csrf
        <h3 style="margin-top:0; margin-bottom:8px;">Import Jobs from JSON</h3>
        <p style="color:#666; font-size:13px; margin-bottom:16px;">Paste a single job object or a JSON array of job objects. The importer will match/create companies and categories automatically by name.</p>
        <textarea name="json_data" rows="12" style="width:100%; border:1px solid #ccc; border-radius:6px; padding:12px; font-family:monospace; font-size:12px; resize:vertical; box-sizing:border-box;" placeholder='[
  {
    "title": "Senior Flutter Developer",
    "company": "Google",
    "category": "Engineering",
    "location": "Toronto, ON",
    "salary": "$135K",
    "job_type": "Full-Time",
    "is_remote": true,
    "skills": ["Flutter", "Dart", "GoRouter"]
  }
]' required></textarea>
        <div style="margin-top:20px; display:flex; justify-content:flex-end; gap:10px;">
            <button type="button" class="btn btn-ghost" onclick="document.getElementById('import-dialog').close()">Cancel</button>
            <button type="submit" class="btn btn-primary">Import Jobs</button>
        </div>
    </form>
</dialog>

<form class="search-bar" method="GET">
    <input type="text" name="q" value="{{ request('q') }}" placeholder="Search title or company...">
    <select name="category">
        <option value="">All categories</option>
        @foreach ($categories as $c)
            <option value="{{ $c->id }}" @selected(request('category') == $c->id)>{{ $c->name }}</option>
        @endforeach
    </select>
    <button class="btn btn-ghost" type="submit">Filter</button>
    @if (request('q') || request('category'))
        <a class="btn btn-ghost" href="{{ route('admin.jobs.index') }}">Clear</a>
    @endif
</form>

<div class="card card-pad">
    @if ($jobs->isEmpty())
        <div class="empty">No jobs found.</div>
    @else
        <table class="data">
            <thead>
                <tr><th>Title</th><th>Company</th><th>Category</th><th>Location</th><th>Type</th><th>Status</th><th style="text-align:right">Actions</th></tr>
            </thead>
            <tbody>
                @foreach ($jobs as $job)
                    <tr>
                        <td><strong>{{ $job->title }}</strong></td>
                        <td>{{ $job->company?->name ?? '—' }}</td>
                        <td>{{ $job->category?->name ?? '—' }}</td>
                        <td>{{ $job->location ?? '—' }}</td>
                        <td><span class="badge gray">{{ $job->job_type }}</span></td>
                        <td>
                            @if ($job->is_featured)<span class="badge amber">Featured</span> @endif
                            @if ($job->is_remote)<span class="badge green">Remote</span> @endif
                            @if ($job->is_active)<span class="badge green">Active</span> @else <span class="badge gray">Hidden</span> @endif
                        </td>
                        <td>
                            <div class="actions" style="justify-content:flex-end">
                                <a class="btn btn-ghost btn-sm" href="{{ route('admin.jobs.edit', $job) }}">Edit</a>
                                <form method="POST" action="{{ route('admin.jobs.destroy', $job) }}" onsubmit="return confirm('Delete this job?');">
                                    @csrf @method('DELETE')
                                    <button class="btn btn-danger btn-sm" type="submit">Delete</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                @endforeach
            </tbody>
        </table>

        <div style="margin-top:18px;">
            {{ $jobs->links() }}
        </div>
    @endif
</div>
@endsection
