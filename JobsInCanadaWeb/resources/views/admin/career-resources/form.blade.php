@extends('admin.layout')

@php
    $editing = !is_null($resource);
@endphp

@section('page-title', $editing ? 'Edit Resource' : 'New Resource')
@section('page-sub', $editing ? $resource->title : 'Add a career resource')

@section('content')
<div class="card card-pad" style="max-width:760px;">
    <form method="POST" action="{{ $editing ? route('admin.career-resources.update', $resource) : route('admin.career-resources.store') }}">
        @csrf
        @if ($editing) @method('PUT') @endif

        <div class="form-grid">
            <div class="field">
                <label for="title">Title</label>
                <input type="text" id="title" name="title" value="{{ old('title', $resource->title ?? '') }}" required>
            </div>

            <div class="field">
                <label for="slug">Slug <span class="hint">(auto if empty)</span></label>
                <input type="text" id="slug" name="slug" value="{{ old('slug', $resource->slug ?? '') }}">
            </div>

            <div class="field full">
                <label for="subtitle">Subtitle</label>
                <input type="text" id="subtitle" name="subtitle" value="{{ old('subtitle', $resource->subtitle ?? '') }}" placeholder="Short description">
            </div>

            <div class="field">
                <label for="icon">Icon <span class="hint">(Material icon name)</span></label>
                <input type="text" id="icon" name="icon" value="{{ old('icon', $resource->icon ?? '') }}" placeholder="e.g. description_outlined">
            </div>

            <div class="field">
                <label for="sort_order">Sort Order</label>
                <input type="number" id="sort_order" name="sort_order" value="{{ old('sort_order', $resource->sort_order ?? 0) }}">
            </div>

            <div class="field">
                <label for="color">Background Color</label>
                <input type="text" id="color" name="color" value="{{ old('color', $resource->color ?? '#DBEAFE') }}">
            </div>

            <div class="field">
                <label for="icon_color">Icon Color</label>
                <input type="text" id="icon_color" name="icon_color" value="{{ old('icon_color', $resource->icon_color ?? '#2563EB') }}">
            </div>

            <div class="field full">
                <label for="content">Content <span class="hint">(Markdown supported)</span></label>
                <textarea id="content" name="content" style="height: 250px; font-family: monospace;">{{ old('content', $resource->content ?? '') }}</textarea>
            </div>
        </div>

        <div class="actions" style="margin-top:22px;">
            <button class="btn btn-primary" type="submit">{{ $editing ? 'Update' : 'Create' }}</button>
            <a class="btn btn-ghost" href="{{ route('admin.career-resources.index') }}">Cancel</a>
        </div>
    </form>
</div>
@endsection
