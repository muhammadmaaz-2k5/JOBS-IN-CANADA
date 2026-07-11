@extends('admin.layout')

@section('page-title', 'Admins')
@section('page-sub', 'Accounts with access to this panel')

@section('content')
<div class="page-head">
    <h2>Admin Accounts</h2>
</div>

<div class="card card-pad">
    <form method="POST" action="{{ route('admin.admins.store') }}" style="margin-bottom:22px;">
        @csrf
        <div class="form-grid">
            <div class="field">
                <label for="name">Name</label>
                <input type="text" id="name" name="name" required>
                @error('name')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>
            <div class="field">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>
                @error('email')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>
            <div class="field">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
                @error('password')<span class="hint" style="color:var(--danger)">{{ $message }}</span>@enderror
            </div>
            <div class="field">
                <label for="password_confirmation">Confirm Password</label>
                <input type="password" id="password_confirmation" name="password_confirmation" required>
            </div>
        </div>
        <div style="margin-top:16px;">
            <button class="btn btn-primary" type="submit">Add Admin</button>
        </div>
    </form>

    @if ($admins->isEmpty())
        <div class="empty">No admin accounts.</div>
    @else
        <table class="data">
            <thead>
                <tr><th>Name</th><th>Email</th><th style="text-align:right">Actions</th></tr>
            </thead>
            <tbody>
                @foreach ($admins as $admin)
                    <tr>
                        <td>
                            <strong>{{ $admin->name }}</strong>
                            @if ($admin->id === auth()->id())<span class="badge green">You</span>@endif
                        </td>
                        <td class="muted">{{ $admin->email }}</td>
                        <td>
                            <div class="actions" style="justify-content:flex-end">
                                @if ($admin->id !== auth()->id())
                                    <form method="POST" action="{{ route('admin.admins.destroy', $admin) }}" onsubmit="return confirm('Delete this admin?');">
                                        @csrf @method('DELETE')
                                        <button class="btn btn-danger btn-sm" type="submit">Delete</button>
                                    </form>
                                @endif
                            </div>
                        </td>
                    </tr>
                @endforeach
            </tbody>
        </table>
    @endif
</div>
@endsection
