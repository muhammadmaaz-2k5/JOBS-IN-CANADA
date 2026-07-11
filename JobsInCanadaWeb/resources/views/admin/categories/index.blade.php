@extends('admin.layout')

@section('page-title', 'Categories')
@section('page-sub', 'Job categories shown in the mobile app')

@section('content')
<div class="page-head">
    <h2>All Categories</h2>
    <a class="btn btn-primary" href="{{ route('admin.categories.create') }}">+ New Category</a>
</div>

<div class="card card-pad">
    @if ($categories->isEmpty())
        <div class="empty">No categories yet.</div>
    @else
        <table class="data">
            <thead>
                <tr><th>Name</th><th>Slug</th><th>Icon</th><th>Color</th><th>Order</th><th style="text-align:right">Actions</th></tr>
            </thead>
            <tbody>
                @foreach ($categories as $category)
                    <tr>
                        <td><strong>{{ $category->name }}</strong></td>
                        <td class="muted">{{ $category->slug }}</td>
                        <td>{{ $category->icon ?? '—' }}</td>
                        <td>
                            <span style="display:inline-block;width:16px;height:16px;border-radius:4px;background:{{ $category->color }};vertical-align:middle;margin-right:6px;"></span>
                            {{ $category->color }}
                        </td>
                        <td>{{ $category->sort_order }}</td>
                        <td>
                            <div class="actions" style="justify-content:flex-end">
                                <a class="btn btn-ghost btn-sm" href="{{ route('admin.categories.edit', $category) }}">Edit</a>
                                <form method="POST" action="{{ route('admin.categories.destroy', $category) }}" onsubmit="return confirm('Delete this category?');">
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
