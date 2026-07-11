@extends('admin.layout')

@php
    $editing = !is_null($province);
@endphp

@section('page-title', $editing ? 'Edit Province' : 'New Province')
@section('page-sub', $editing ? $province->name : 'Add a Canadian province or territory')

@section('content')
<div class="card card-pad" style="max-width:620px;">
    <form method="POST" action="{{ $editing ? route('admin.provinces.update', $province) : route('admin.provinces.store') }}">
        @csrf
        @if ($editing) @method('PUT') @endif

        <div class="form-grid">
            <div class="field">
                <label for="name">Name</label>
                <input type="text" id="name" name="name" value="{{ old('name', $province->name ?? '') }}" required>
                @error('name')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>

            <div class="field">
                <label for="code">Code</label>
                <input type="text" id="code" name="code" value="{{ old('code', $province->code ?? '') }}" required placeholder="ON">
                @error('code')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>

            <div class="field">
                <label for="sort_order">Sort Order</label>
                <input type="number" id="sort_order" name="sort_order" value="{{ old('sort_order', $province->sort_order ?? 0) }}">
            </div>
        </div>

        <div class="actions" style="margin-top:22px;">
            <button class="btn btn-primary" type="submit">{{ $editing ? 'Update' : 'Create' }}</button>
            <a class="btn btn-ghost" href="{{ route('admin.provinces.index') }}">Cancel</a>
        </div>
    </form>
</div>
@endsection
