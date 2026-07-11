@extends('admin.layout')

@php
    $editing = !is_null($logo);
@endphp

@section('page-title', $editing ? 'Edit Logo' : 'Add Logo')
@section('page-sub', $editing ? $logo->name : 'Add a logo to the reusable library')

@section('content')
<div class="card card-pad" style="max-width:620px;">
    <form method="POST" action="{{ $editing ? route('admin.logos.update', $logo) : route('admin.logos.store') }}">
        @csrf
        @if ($editing) @method('PUT') @endif

        <div class="form-grid">
            <div class="field">
                <label for="name">Name</label>
                <input type="text" id="name" name="name" value="{{ old('name', $logo->name ?? '') }}" required>
                @error('name')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>

            <div class="field">
                <label for="sort_order">Sort Order</label>
                <input type="number" id="sort_order" name="sort_order" value="{{ old('sort_order', $logo->sort_order ?? 0) }}">
            </div>

            <div class="field full">
                <label for="url">Logo URL</label>
                <input type="url" id="url" name="url" value="{{ old('url', $logo->url ?? '') }}" required placeholder="https://...">
                @error('url')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
                @if ($editing && $logo->url)
                    <img src="{{ $logo->url }}" alt="preview" style="width:56px;height:56px;object-fit:contain;margin-top:10px;border-radius:10px;background:#eef2f7;">
                @endif
            </div>
        </div>

        <div class="actions" style="margin-top:22px;">
            <button class="btn btn-primary" type="submit">{{ $editing ? 'Update' : 'Create' }}</button>
            <a class="btn btn-ghost" href="{{ route('admin.logos.index') }}">Cancel</a>
        </div>
    </form>
</div>
@endsection
