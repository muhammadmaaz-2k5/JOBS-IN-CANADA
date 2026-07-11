@extends('admin.layout')

@section('page-title', 'Companies')
@section('page-sub', 'Employers hiring through the app')

@section('content')
<div class="page-head">
    <h2>All Companies</h2>
    <a class="btn btn-primary" href="{{ route('admin.companies.create') }}">+ New Company</a>
</div>

<div class="card card-pad">
    @if ($companies->isEmpty())
        <div class="empty">No companies yet.</div>
    @else
        <table class="data">
            <thead>
                <tr><th>Logo</th><th>Name</th><th>Website</th><th>Jobs</th><th style="text-align:right">Actions</th></tr>
            </thead>
            <tbody>
                @foreach ($companies as $company)
                    <tr>
                        <td>
                            @if ($company->logo)
                                <img class="thumb" src="{{ $company->logo }}" alt="{{ $company->name }}">
                            @else
                                <span class="thumb"></span>
                            @endif
                        </td>
                        <td><strong>{{ $company->name }}</strong></td>
                        <td class="muted">{{ $company->website ?? '—' }}</td>
                        <td><span class="badge gray">{{ $company->job_listings_count }}</span></td>
                        <td>
                            <div class="actions" style="justify-content:flex-end">
                                <a class="btn btn-ghost btn-sm" href="{{ route('admin.companies.edit', $company) }}">Edit</a>
                                <form method="POST" action="{{ route('admin.companies.destroy', $company) }}" onsubmit="return confirm('Delete this company?');">
                                    @csrf @method('DELETE')
                                    <button class="btn btn-danger btn-sm" type="submit">Delete</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                @endforeach
            </tbody>
        </table>
    @endif
</div>
@endsection
