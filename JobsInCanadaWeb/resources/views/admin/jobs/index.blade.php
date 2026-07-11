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
        <p style="color:#666; font-size:13px; margin-bottom:12px;">Paste a single job object or a JSON array of job objects. The importer will match/create companies and categories automatically by name.</p>
        
        <details style="margin-bottom:16px; font-size:12px; color:#555; cursor:pointer;">
            <summary style="font-weight:600; color:#2563EB;">Show all supported JSON fields</summary>
            <ul style="margin-top:6px; padding-left:20px; line-height:1.5; color:#444;">
                <li><code>title</code> (Required - e.g. "Senior Software Engineer")</li>
                <li><code>company</code> (String - matches or creates company by name)</li>
                <li><code>category</code> (String - matches or creates category by name)</li>
                <li><code>slug</code> (String - auto-generated if empty)</li>
                <li><code>job_type</code> (String - e.g. "Full-Time", "Contract", "Part-Time", "Internship")</li>
                <li><code>salary</code> (String - e.g. "$115K")</li>
                <li><code>salary_period</code> (String - e.g. "year", "month", "hour")</li>
                <li><code>salary_min</code> (Numeric - minimum base for sorting/filters)</li>
                <li><code>location</code> (String - e.g. "Toronto, ON")</li>
                <li><code>province</code> (String - e.g. "Ontario", "Alberta")</li>
                <li><code>applicants</code> (Numeric - applicant count)</li>
                <li><code>posted_at</code> (String - date/time YYYY-MM-DD HH:MM:SS)</li>
                <li><code>apply_url</code> (String - link to apply page)</li>
                <li><code>description</code> (String - job details/requirements)</li>
                <li><code>skills</code> (Array of strings - e.g. <code>["Flutter", "Dart"]</code>)</li>
                <li><code>applicant_avatars</code> (Array of URLs - avatars shown in list)</li>
                <li><code>company_logo</code> (String - optional logo URL override)</li>
                <li><code>company_logo_label</code> (String - optional logo accessibility tag)</li>
                <li><code>is_remote</code> (Boolean - true/false)</li>
                <li><code>is_new</code> (Boolean - true/false)</li>
                <li><code>is_featured</code> (Boolean - true/false)</li>
                <li><code>is_active</code> (Boolean - true/false, visible in app)</li>
            </ul>
        </details>

        <details style="margin-bottom:16px; font-size:12px; color:#555; cursor:pointer;">
            <summary style="font-weight:600; color:#d97706;">📋 Copy AI Prompt Template</summary>
            <div style="margin-top:8px; background:#f4f4f4; border-radius:6px; padding:12px; font-family:monospace; position:relative;">
                <button type="button" style="position:absolute; right:8px; top:8px; background:#fff; border:1px solid #ccc; border-radius:4px; padding:2px 6px; cursor:pointer; font-size:10px;" onclick="navigator.clipboard.writeText(document.getElementById('ai-prompt-text').innerText); alert('Copied to clipboard!')">Copy</button>
                <div id="ai-prompt-text" style="white-space:pre-wrap; word-break:break-all; font-size:11px; line-height:1.4; color:#333;">Please generate a job listing in the following JSON format for the position of [Job Title] at [Company]. Here is the template to fill in with realistic Canadian market data:

{
  "title": "[Title of the Job]",
  "company": "[Company Name]",
  "category": "[One of: Design, Marketing, Engineering, Product, Data, Finance, Healthcare, Legal, Sales, Education]",
  "slug": "",
  "job_type": "[Full-Time, Part-Time, Contract, or Internship]",
  "salary": "[e.g. $115K or $45/hr]",
  "salary_period": "[year, month, or hour]",
  "salary_min": [numeric minimum e.g. 115000 or 45],
  "location": "[City, Province e.g. Toronto, ON]",
  "province": "[Full name e.g. Ontario, British Columbia, Alberta]",
  "applicants": 0,
  "posted_at": "[Current DateTime YYYY-MM-DD HH:MM:SS]",
  "apply_url": "[Company careers page URL]",
  "description": "[Detailed Markdown descriptions, requirements, and responsibilities]",
  "skills": ["Skill1", "Skill2", "Skill3"],
  "applicant_avatars": [],
  "company_logo": "",
  "company_logo_label": "",
  "is_remote": [true or false],
  "is_new": true,
  "is_featured": [true or false],
  "is_active": true
}</div>
            </div>
        </details>

        <textarea name="json_data" rows="14" style="width:100%; border:1px solid #ccc; border-radius:6px; padding:12px; font-family:monospace; font-size:12px; resize:vertical; box-sizing:border-box;" placeholder='{
  "title": "Senior Flutter Developer",
  "company": "Google Canada",
  "category": "Engineering",
  "slug": "senior-flutter-developer-toronto-2026",
  "job_type": "Full-Time",
  "salary": "$135K",
  "salary_period": "year",
  "salary_min": 135000,
  "location": "Toronto, ON",
  "province": "Ontario",
  "applicants": 42,
  "posted_at": "2026-07-11 12:00:00",
  "apply_url": "https://careers.google.com",
  "description": "We are seeking a Senior Flutter Developer...",
  "skills": ["Flutter", "Dart", "GoRouter", "REST API"],
  "applicant_avatars": [
    "https://api.dicebear.com/7.x/avataaars/svg?seed=Felix",
    "https://api.dicebear.com/7.x/avataaars/svg?seed=Aneka"
  ],
  "company_logo": "https://img.rocket.new/generatedImages/logo_google.png",
  "company_logo_label": "Google logo override",
  "is_remote": true,
  "is_new": true,
  "is_featured": true,
  "is_active": true
}' required></textarea>
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

@if (!$featuredJobs->isEmpty())
    <div style="margin-bottom: 24px;">
        <h3 style="margin-top:0; margin-bottom:12px; display:flex; align-items:center; gap:8px;">
            <span style="display:inline-block; width:10px; height:10px; background-color:#d97706; border-radius:50%;"></span>
            Featured Job Listings
        </h3>
        <div class="card card-pad" style="border-top: 3px solid #d97706;">
            <table class="data">
                <thead>
                    <tr><th>Title</th><th>Company</th><th>Category</th><th>Location</th><th>Type</th><th>Status</th><th style="text-align:right">Actions</th></tr>
                </thead>
                <tbody>
                    @foreach ($featuredJobs as $job)
                        <tr>
                            <td><strong>{{ $job->title }}</strong></td>
                            <td>{{ $job->company?->name ?? '—' }}</td>
                            <td>{{ $job->category?->name ?? '—' }}</td>
                            <td>{{ $job->location ?? '—' }}</td>
                            <td><span class="badge gray">{{ $job->job_type }}</span></td>
                            <td>
                                <span class="badge amber">Featured</span>
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
        </div>
    </div>
@endif

<h3 style="margin-top:0; margin-bottom:12px; display:flex; align-items:center; gap:8px;">
    <span style="display:inline-block; width:10px; height:10px; background-color:#4b5563; border-radius:50%;"></span>
    All Other Listings
</h3>
<div class="card card-pad">
    @if ($jobs->isEmpty())
        <div class="empty">No other jobs found.</div>
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
