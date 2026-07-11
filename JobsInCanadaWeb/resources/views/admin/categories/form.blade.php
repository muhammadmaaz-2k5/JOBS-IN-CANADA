@extends('admin.layout')

@php
    $editing = !is_null($category);
@endphp

@section('page-title', $editing ? 'Edit Category' : 'New Category')
@section('page-sub', $editing ? $category->name : 'Create a job category')

@section('content')
<div class="card card-pad" style="max-width:760px;">
    <form method="POST" action="{{ $editing ? route('admin.categories.update', $category) : route('admin.categories.store') }}">
        @csrf
        @if ($editing) @method('PUT') @endif

        <div class="form-grid">
            <div class="field">
                <label for="name">Name</label>
                <input type="text" id="name" name="name" value="{{ old('name', $category->name ?? '') }}" required>
                @error('name')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>

            <div class="field">
                <label for="slug">Slug <span class="hint">(auto if empty)</span></label>
                <input type="text" id="slug" name="slug" value="{{ old('slug', $category->slug ?? '') }}">
            </div>

            <div class="field">
                <label for="icon">Icon <span class="hint">(Material icon name)</span></label>
                <input type="text" id="icon" name="icon" value="{{ old('icon', $category->icon ?? '') }}" placeholder="e.g. code_rounded">
            </div>

            <div class="field">
                <label for="color">Color</label>
                <input type="text" id="color" name="color" value="{{ old('color', $category->color ?? '#2563EB') }}">
            </div>

            <div class="field">
                <label for="sort_order">Sort Order</label>
                <input type="number" id="sort_order" name="sort_order" value="{{ old('sort_order', $category->sort_order ?? 0) }}">
            </div>
        </div>

        <div class="actions" style="margin-top:22px;">
            <button class="btn btn-primary" type="submit">{{ $editing ? 'Update' : 'Create' }}</button>
            <a class="btn btn-ghost" href="{{ route('admin.categories.index') }}">Cancel</a>
        </div>
    </form>
</div>
@endsection
