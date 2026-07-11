<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class JobListing extends Model
{
    use HasFactory;

    protected $table = 'job_listings';

    protected $fillable = [
        'title', 'slug', 'company_id', 'category_id', 'salary', 'salary_period',
        'location', 'province', 'job_type', 'is_remote', 'is_new', 'applicants',
        'is_featured', 'apply_url', 'description', 'skills', 'company_logo',
        'company_logo_label', 'applicant_avatars', 'posted_at', 'is_active', 'salary_min',
    ];

    protected $casts = [
        'is_remote' => 'boolean',
        'is_new' => 'boolean',
        'is_featured' => 'boolean',
        'is_active' => 'boolean',
        'applicants' => 'integer',
        'salary_min' => 'integer',
        'skills' => 'array',
        'applicant_avatars' => 'array',
        'posted_at' => 'datetime',
    ];

    public function company(): BelongsTo
    {
        return $this->belongsTo(Company::class);
    }

    public function category(): BelongsTo
    {
        return $this->belongsTo(Category::class);
    }

    public function logoUrl(): ?string
    {
        return $this->company_logo ?: ($this->company?->logo);
    }

    public function logoLabel(): string
    {
        return $this->company_logo_label ?: ($this->company?->name.' logo') ?: 'Company logo';
    }

    public function postedDaysAgo(): int
    {
        if (! $this->posted_at) {
            return 0;
        }

        return max(0, (int) now()->diffInDays($this->posted_at));
    }

    protected static function boot(): void
    {
        parent::boot();

        static::creating(function (self $job) {
            $job->slug = $job->slug ?: \Str::slug($job->title).'-'.substr(md5(uniqid()), 0, 6);
        });
    }
}
