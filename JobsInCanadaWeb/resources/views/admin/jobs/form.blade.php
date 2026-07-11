@extends('admin.layout')

@php
    $editing = !is_null($job);
    $jobTypes = ['Full-Time', 'Part-Time', 'Contract', 'Internship', 'Freelance'];
@endphp

@section('page-title', $editing ? 'Edit Job' : 'New Job')
@section('page-sub', $editing ? $job->title : 'Create a job listing')

@section('content')
<div class="card card-pad" style="max-width:880px;">
    <form method="POST" action="{{ $editing ? route('admin.jobs.update', $job) : route('admin.jobs.store') }}">
        @csrf
        @if ($editing) @method('PUT') @endif

        <div class="form-grid">
            <div class="field full">
                <label for="title">Job Title</label>
                <input type="text" id="title" name="title" value="{{ old('title', $job->title ?? '') }}" required>
                @error('title')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>

            <div class="field">
                <label for="company_id">Company</label>
                <select id="company_id" name="company_id">
                    <option value="">— None —</option>
                    @foreach ($companies as $c)
                        <option value="{{ $c->id }}" @selected(old('company_id', $job->company_id ?? '') == $c->id)>{{ $c->name }}</option>
                    @endforeach
                </select>
            </div>

            <div class="field">
                <label for="category_id">Category</label>
                <select id="category_id" name="category_id">
                    <option value="">— None —</option>
                    @foreach ($categories as $c)
                        <option value="{{ $c->id }}" @selected(old('category_id', $job->category_id ?? '') == $c->id)>{{ $c->name }}</option>
                    @endforeach
                </select>
            </div>

            <div class="field">
                <label for="slug">Slug <span class="hint">(auto if empty)</span></label>
                <input type="text" id="slug" name="slug" value="{{ old('slug', $job->slug ?? '') }}">
            </div>

            <div class="field">
                <label for="job_type">Job Type</label>
                <select id="job_type" name="job_type">
                    @foreach ($jobTypes as $t)
                        <option value="{{ $t }}" @selected(old('job_type', $job->job_type ?? 'Full-Time') == $t)>{{ $t }}</option>
                    @endforeach
                </select>
            </div>

            <div class="field">
                <label for="salary">Salary <span class="hint">(e.g. $115K)</span></label>
                <input type="text" id="salary" name="salary" value="{{ old('salary', $job->salary ?? '') }}">
            </div>

            <div class="field">
                <label for="salary_period">Salary Period</label>
                <input type="text" id="salary_period" name="salary_period" value="{{ old('salary_period', $job->salary_period ?? 'year') }}" placeholder="year / month / hr">
            </div>

            <div class="field">
                <label for="location">Location</label>
                <input type="text" id="location" name="location" value="{{ old('location', $job->location ?? '') }}" placeholder="Toronto, ON">
            </div>

            <div class="field">
                <label for="province">Province</label>
                <input type="text" id="province" name="province" value="{{ old('province', $job->province ?? '') }}" placeholder="Ontario">
            </div>

            <div class="field">
                <label for="applicants">Applicants</label>
                <input type="number" id="applicants" name="applicants" value="{{ old('applicants', $job->applicants ?? 0) }}" min="0">
            </div>

            <div class="field">
                <label for="posted_at">Posted At</label>
                <input type="date" id="posted_at" name="posted_at" value="{{ old('posted_at', isset($job) && $job->posted_at ? $job->posted_at->format('Y-m-d') : '') }}">
            </div>

            <div class="field">
                <label for="apply_url">Apply URL</label>
                <input type="url" id="apply_url" name="apply_url" value="{{ old('apply_url', $job->apply_url ?? '') }}" placeholder="https://...">
                @error('apply_url')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>

            <div class="field full">
                <label for="description">Description</label>
                <textarea id="description" name="description" placeholder="Job overview / description">{{ old('description', $job->description ?? '') }}</textarea>
            </div>

            <div class="field full">
                <label for="skills">Skills <span class="hint">(comma separated)</span></label>
                <input type="text" id="skills" name="skills" value="{{ old('skills', isset($job) && $job->skills ? implode(', ', $job->skills) : '') }}" placeholder="Communication, Leadership, Figma">
            </div>

            <div class="field full">
                <label for="applicant_avatars">Applicant Avatars <span class="hint">(one URL per line)</span></label>
                <textarea id="applicant_avatars" name="applicant_avatars" style="min-height:70px;">{{ old('applicant_avatars', isset($job) && $job->applicant_avatars ? implode("\n", $job->applicant_avatars) : '') }}</textarea>
            </div>

            <div class="field full">
                <label for="company_logo">Company Logo Override URL <span class="hint">(optional)</span></label>
                <input type="url" id="company_logo" name="company_logo" value="{{ old('company_logo', $job->company_logo ?? '') }}" placeholder="https://...">
            </div>

            <div class="field full">
                <label for="company_logo_label">Company Logo Label <span class="hint">(optional)</span></label>
                <input type="text" id="company_logo_label" name="company_logo_label" value="{{ old('company_logo_label', $job->company_logo_label ?? '') }}" placeholder="Company logo mark">
            </div>

            <div class="field full">
                <div class="check-row">
                    <label class="check"><input type="checkbox" name="is_remote" value="1" @checked(old('is_remote', $job->is_remote ?? false))> Remote</label>
                    <label class="check"><input type="checkbox" name="is_new" value="1" @checked(old('is_new', $job->is_new ?? false))> Mark as New</label>
                    <label class="check"><input type="checkbox" name="is_featured" value="1" @checked(old('is_featured', $job->is_featured ?? false))> Featured</label>
                    <label class="check"><input type="checkbox" name="is_active" value="1" @checked(old('is_active', $job->is_active ?? true))> Active (visible in app)</label>
                </div>
            </div>
        </div>

        <div class="actions" style="margin-top:22px;">
            <button class="btn btn-primary" type="submit">{{ $editing ? 'Update' : 'Create' }}</button>
            <a class="btn btn-ghost" href="{{ route('admin.jobs.index') }}">Cancel</a>
        </div>
    </form>
</div>
@endsection
