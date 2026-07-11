@extends('admin.layout')

@section('page-title', 'Provinces')
@section('page-sub', 'Canadian provinces & territories used for job locations')

@section('content')
<div class="page-head">
    <h2>All Provinces / Territories</h2>
    <a class="btn btn-primary" href="{{ route('admin.provinces.create') }}">+ New Province</a>
</div>

<div class="card card-pad">
    @if ($provinces->isEmpty())
        <div class="empty">No provinces yet.</div>
    @else
        <table class="data">
            <thead>
                <tr><th>Name</th><th>Code</th><th>Order</th><th style="text-align:right">Actions</th></tr>
            </thead>
            <tbody>
                @foreach ($provinces as $province)
                    <tr>
                        <td><strong>{{ $province->name }}</strong></td>
                        <td><span class="badge gray">{{ $province->code }}</span></td>
                        <td>{{ $province->sort_order }}</td>
                        <td>
                            <div class="actions" style="justify-content:flex-end">
                                <a class="btn btn-ghost btn-sm" href="{{ route('admin.provinces.edit', $province) }}">Edit</a>
                                <form method="POST" action="{{ route('admin.provinces.destroy', $province) }}" onsubmit="return confirm('Delete this province?');">
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
