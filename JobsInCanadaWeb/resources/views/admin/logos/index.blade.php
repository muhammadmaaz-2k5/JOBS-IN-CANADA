@extends('admin.layout')

@section('page-title', 'Logos')
@section('page-sub', 'Reusable logo library for companies & jobs')

@section('content')
<div class="page-head">
    <h2>Logo Library</h2>
    <a class="btn btn-primary" href="{{ route('admin.logos.create') }}">+ Add Logo</a>
</div>

<div class="card card-pad">
    @if ($logos->isEmpty())
        <div class="empty">No logos yet.</div>
    @else
        <div class="logo-grid">
            @foreach ($logos as $logo)
                <div class="logo-cell">
                    <img src="{{ $logo->url }}" alt="{{ $logo->name }}" class="logo-img">
                    <div class="logo-name">{{ $logo->name }}</div>
                    <div class="actions" style="justify-content:center; margin-top:8px;">
                        <a class="btn btn-ghost btn-sm" href="{{ route('admin.logos.edit', $logo) }}">Edit</a>
                        <form method="POST" action="{{ route('admin.logos.destroy', $logo) }}" onsubmit="return confirm('Remove this logo?');">
                            @csrf @method('DELETE')
                            <button class="btn btn-danger btn-sm" type="submit">Delete</button>
                        </form>
                    </div>
                </div>
            @endforeach
        </div>
    @endif
</div>
@endsection
