@extends('admin.layout')

@section('page-title', 'Push Notifications')
@section('page-sub', 'Broadcast immediate or automatic push notifications to all devices')

@section('content')
<style>
    /* Premium UI Customizations */
    .premium-container {
        max-width: 950px;
        margin: 0 auto;
    }

    .quick-populate-card {
        background: linear-gradient(135deg, rgba(37, 99, 235, 0.04) 0%, rgba(147, 51, 234, 0.04) 100%);
        border: 1px solid rgba(37, 99, 235, 0.12);
        border-radius: 16px;
        padding: 24px;
        margin-bottom: 28px;
        display: flex;
        flex-direction: column;
        gap: 16px;
        box-shadow: 0 4px 20px -2px rgba(37, 99, 235, 0.05);
    }

    .form-control-custom {
        width: 100%;
        padding: 12px 16px;
        border: 1.5px solid var(--border);
        border-radius: 10px;
        background: var(--surface);
        color: var(--text);
        font-family: inherit;
        font-size: 14px;
        transition: all 0.2s ease-in-out;
    }

    .form-control-custom:focus {
        outline: none;
        border-color: var(--primary);
        box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.12);
    }

    .premium-label {
        font-size: 12.5px;
        font-weight: 700;
        color: var(--text-muted);
        text-transform: uppercase;
        letter-spacing: 0.05em;
        margin-bottom: 8px;
        display: block;
    }
</style>

<div class="premium-container">

    <!-- Controls & Send Random -->
    <div style="display: grid; grid-template-columns: 1fr; gap: 20px; margin-bottom: 28px;">
        <!-- Job Random Dispatcher Card -->
        <div class="card card-pad" style="display: flex; flex-direction: column; justify-content: space-between; border-top: 4px solid var(--primary);">
            <div>
                <h3 style="margin: 0 0 6px 0; font-size: 15px; font-weight: 800; color: var(--text);">💼 Job Alert Dispatcher</h3>
                <p style="margin: 0; font-size: 13px; color: var(--text-muted); line-height: 1.4;">Send a random active job (prioritizing today's jobs) to all users via push notification.</p>
            </div>
            <button onclick="sendRandomNotification('job', this)" class="btn btn-secondary" style="margin-top: 16px; width: 100%; max-width: 250px; font-weight: 700; font-size: 13px; display: inline-flex; align-items: center; justify-content: center; gap: 6px;">
                🎲 Dispatch Random Job
            </button>
        </div>
    </div>
    
    <!-- Quick Populate Section -->
    <div class="quick-populate-card">
        <div>
            <h3 style="margin: 0 0 6px 0; font-size: 16px; font-weight: 800; color: var(--text);">⚡ Quick Populate Listing Content</h3>
            <p style="margin: 0; font-size: 13.5px; color: var(--text-muted);">Choose an active job listing to automatically generate the notification title, description, and dynamic image banner.</p>
        </div>
        
        <div style="display: flex; gap: 12px; align-items: center; flex-wrap: wrap;">
            <select id="populate_job_id" class="form-control-custom" style="flex: 1; min-width: 250px;">
                <option value="">-- Choose a Job Listing --</option>
                @foreach ($jobs as $job)
                    <option value="{{ $job->id }}">{{ $job->title }} ({{ $job->company?->name ?? 'Unknown Company' }})</option>
                @endforeach
            </select>
            <button type="button" class="btn btn-secondary" id="btn-autofill" onclick="autoFillFromJob()" style="white-space: nowrap; height: 45px; display: inline-flex; align-items: center; justify-content: center; gap: 8px;">
                <span>✨ Fetch & Auto-Fill</span>
            </button>
        </div>
    </div>

    <!-- Direct Broadcast Card -->
    <div class="card card-pad" style="margin-bottom: 40px;">
        <div style="margin-bottom: 24px;">
            <h2 style="margin: 0 0 6px 0; font-size: 17px; font-weight: 800;">Send Instant Notification</h2>
            <p style="margin: 0; font-size: 13.5px; color: var(--text-muted);">Broadcast a system-wide FCM notification targeting all registered user devices immediately.</p>
        </div>

        <form method="POST" action="{{ route('admin.notifications.send') }}" style="display: flex; flex-direction: column; gap: 20px;">
            @csrf

            <div>
                <label for="instant_title" class="premium-label">Notification Title</label>
                <input type="text" id="instant_title" name="title" class="form-control-custom" value="{{ old('title') }}" placeholder="e.g. New Engineering Jobs Available!" required>
                @error('title')<span class="hint" style="color:var(--danger); display:block; margin-top:4px;">{{ $message }}</span>@enderror
            </div>

            <div>
                <label for="instant_body" class="premium-label">Notification Body</label>
                <textarea id="instant_body" name="body" rows="3" class="form-control-custom" placeholder="e.g. Apply today for exciting engineering roles in Vancouver, BC." required>{{ old('body') }}</textarea>
                @error('body')<span class="hint" style="color:var(--danger); display:block; margin-top:4px;">{{ $message }}</span>@enderror
            </div>

            <div>
                <label for="instant_image_url" class="premium-label">Image URL (Optional)</label>
                <input type="url" id="instant_image_url" name="image_url" class="form-control-custom" value="{{ old('image_url') }}" placeholder="https://example.com/notification-banner.jpg">
                @error('image_url')<span class="hint" style="color:var(--danger); display:block; margin-top:4px;">{{ $message }}</span>@enderror
            </div>

            <div style="display: grid; grid-template-columns: 1fr; gap: 20px; align-items: start;">
                <div>
                    <label for="instant_screen" class="premium-label">Target Screen Routing</label>
                    <select id="instant_screen" name="screen" class="form-control-custom" onchange="toggleTargetFields()">
                        <option value="home" {{ old('screen') == 'home' ? 'selected' : '' }}>🏠 Home Screen</option>
                        <option value="detail" {{ old('screen') == 'detail' ? 'selected' : '' }}>💼 Job Details Screen</option>
                    </select>
                    @error('screen')<span class="hint" style="color:var(--danger); display:block; margin-top:4px;">{{ $message }}</span>@enderror
                </div>

                <div id="instant_job_field" style="display: none;">
                    <label for="instant_job_id" class="premium-label">Select Target Job Listing</label>
                    <select id="instant_job_id" name="job_id" class="form-control-custom">
                        <option value="">-- Select a Job --</option>
                        @foreach ($jobs as $job)
                            <option value="{{ $job->id }}" {{ old('job_id') == $job->id ? 'selected' : '' }}>
                                {{ $job->title }} ({{ $job->company?->name ?? 'Unknown Company' }})
                            </option>
                        @endforeach
                    </select>
                    @error('job_id')<span class="hint" style="color:var(--danger); display:block; margin-top:4px;">{{ $message }}</span>@enderror
                </div>
            </div>

            <div style="border-top: 1px solid var(--border); padding-top: 20px; display: flex; justify-content: flex-end; margin-top: 12px;">
                <button class="btn btn-primary" type="submit" style="padding: 12px 32px; font-weight: 700;">🚀 Broadcast Immediately</button>
            </div>
        </form>
    </div>
</div>

<!-- Toast Alert Notification -->
<div id="toast" style="position: fixed; bottom: 24px; right: 24px; z-index: 9999; display: none; transform: translateY(20px); transition: all 0.3s ease;">
    <div id="toast-inner" style="padding: 14px 24px; border-radius: 12px; font-weight: 700; color: #fff; box-shadow: 0 10px 25px -5px rgba(0,0,0,0.1), 0 8px 10px -6px rgba(0,0,0,0.1); font-size: 13.5px; display: flex; align-items: center; gap: 8px;">
    </div>
</div>

<script>
    function showToast(message, type = 'success') {
        var toast = document.getElementById('toast');
        var inner = document.getElementById('toast-inner');
        
        inner.innerText = message;
        if (type === 'error') {
            inner.style.backgroundColor = '#ef4444';
        } else {
            inner.style.backgroundColor = '#10b981';
        }
        
        toast.style.display = 'block';
        setTimeout(function() {
            toast.style.transform = 'translateY(0)';
            toast.style.opacity = '1';
        }, 50);
        
        setTimeout(function() {
            toast.style.transform = 'translateY(20px)';
            toast.style.opacity = '0';
            setTimeout(function() {
                toast.style.display = 'none';
            }, 300);
        }, 4000);
    }

    function sendRandomNotification(type, button) {
        var originalText = button.innerHTML;
        button.disabled = true;
        button.innerHTML = '<span>⏳ Dispatching...</span>';

        var csrfToken = document.querySelector('input[name="_token"]')?.value || '';

        fetch('{{ route("admin.notifications.send-random") }}', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken,
                'Accept': 'application/json'
            },
            body: JSON.stringify({ type: type })
        })
        .then(function(response) {
            return response.json().then(function(data) {
                if (response.ok && data.success) {
                    showToast(data.message, 'success');
                } else {
                    showToast(data.message || 'Failed to send random notification.', 'error');
                }
            });
        })
        .catch(function(error) {
            showToast('Network error or server error.', 'error');
            console.error('Error:', error);
        })
        .finally(function() {
            button.disabled = false;
            button.innerHTML = originalText;
        });
    }

    function toggleTargetFields() {
        var screen = document.getElementById('instant_screen').value;
        var jobField = document.getElementById('instant_job_field');

        if (screen === 'detail') {
            jobField.style.display = 'block';
        } else {
            jobField.style.display = 'none';
        }
    }

    function autoFillFromJob() {
        var jobId = document.getElementById('populate_job_id').value;
        if (!jobId) {
            alert('Please select a job from the list first.');
            return;
        }
        
        var btn = document.getElementById('btn-autofill');
        var originalText = btn.innerHTML;
        btn.disabled = true;
        btn.innerHTML = '<span>⏳ Fetching...</span>';
        
        fetch('{{ url("/admin/notifications/job-details") }}/' + jobId)
            .then(function(response) {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(function(data) {
                btn.disabled = false;
                btn.innerHTML = originalText;
                
                // Populate Direct Broadcast Form
                document.getElementById('instant_title').value = data.title;
                document.getElementById('instant_body').value = data.body;
                document.getElementById('instant_image_url').value = data.image_url;
                document.getElementById('instant_screen').value = 'detail';
                toggleTargetFields();
                document.getElementById('instant_job_id').value = data.job_id;
                
                showToast('Content successfully populated!', 'success');
            })
            .catch(function(error) {
                btn.disabled = false;
                btn.innerHTML = originalText;
                alert('Error fetching job details. Please verify your connection.');
                console.error('Error fetching job details:', error);
            });
    }

    // Initialize toggle screens on DOM ready
    document.addEventListener('DOMContentLoaded', function() {
        toggleTargetFields();
    });
</script>
@endsection
