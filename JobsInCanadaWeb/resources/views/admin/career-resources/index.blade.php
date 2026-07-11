@extends('admin.layout')

@section('page-title', 'Career Resources')
@section('page-sub', 'Helpful resources shown on the home screen')

@section('content')
<div class="page-head">
    <h2>All Resources</h2>
    <a class="btn btn-primary" href="{{ route('admin.career-resources.create') }}">+ New Resource</a>
</div>

<div class="card card-pad">
    @if ($resources->isEmpty())
        <div class="empty">No resources yet.</div>
    @else
        <table class="data">
            <thead>
                <tr><th>Title</th><th>Subtitle</th><th>Icon</th><th>Order</th><th style="text-align:right">Actions</th></tr>
            </thead>
            <tbody>
                @foreach ($resources as $resource)
                    <tr>
                        <td><strong>{{ $resource->title }}</strong></td>
                        <td class="muted">{{ $resource->subtitle ?? '—' }}</td>
                        <td>{{ $resource->icon ?? '—' }}</td>
                        <td>{{ $resource->sort_order }}</td>
                        <td>
                            <div class="actions" style="justify-content:flex-end">
                                <a class="btn btn-ghost btn-sm" href="{{ route('admin.career-resources.edit', $resource) }}">Edit</a>
                                <form method="POST" action="{{ route('admin.career-resources.destroy', $resource) }}" onsubmit="return confirm('Delete this resource?');">
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
