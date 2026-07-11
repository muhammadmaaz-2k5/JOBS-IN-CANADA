@extends('admin.layout')

@php
    $editing = !is_null($company);
@endphp

@section('page-title', $editing ? 'Edit Company' : 'New Company')
@section('page-sub', $editing ? $company->name : 'Add an employer')

@section('content')
<div class="card card-pad" style="max-width:760px;">
    <form method="POST" action="{{ $editing ? route('admin.companies.update', $company) : route('admin.companies.store') }}">
        @csrf
        @if ($editing) @method('PUT') @endif

        <div class="form-grid">
            <div class="field">
                <label for="name">Name</label>
                <input type="text" id="name" name="name" value="{{ old('name', $company->name ?? '') }}" required>
                @error('name')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>

            <div class="field">
                <label for="slug">Slug <span class="hint">(auto if empty)</span></label>
                <input type="text" id="slug" name="slug" value="{{ old('slug', $company->slug ?? '') }}">
            </div>

            <div class="field">
                <label for="logo">Logo URL</label>
                <div class="input-with-button">
                    <input type="url" id="logo" name="logo" value="{{ old('logo', $company->logo ?? '') }}" placeholder="https://...">
                    <button type="button" class="btn btn-ghost btn-sm" onclick="openLogoLibrary(function(url){ document.getElementById('logo').value = url; })">Library</button>
                </div>
                @error('logo')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>

            <div class="field">
                <label for="website">Website</label>
                <input type="url" id="website" name="website" value="{{ old('website', $company->website ?? '') }}" placeholder="https://...">
            </div>

            <div class="field">
                <label for="sort_order">Sort Order</label>
                <input type="number" id="sort_order" name="sort_order" value="{{ old('sort_order', $company->sort_order ?? 0) }}">
            </div>

            <div class="field full">
                <label for="description">Description</label>
                <textarea id="description" name="description" placeholder="About the company">{{ old('description', $company->description ?? '') }}</textarea>
            </div>
        </div>

        <div class="actions" style="margin-top:22px;">
            <button class="btn btn-primary" type="submit">{{ $editing ? 'Update' : 'Create' }}</button>
            <a class="btn btn-ghost" href="{{ route('admin.companies.index') }}">Cancel</a>
        </div>
    </form>
</div>

@include('admin.partials.logo-library')
@endsection
