<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Company extends Model
{
    use HasFactory;

    protected $fillable = ['name', 'slug', 'logo', 'description', 'website', 'sort_order'];

    protected $casts = [
        'sort_order' => 'integer',
    ];

    public function jobListings(): HasMany
    {
        return $this->hasMany(JobListing::class);
    }

    protected static function boot(): void
    {
        parent::boot();

        static::creating(function (self $company) {
            $company->slug = $company->slug ?: \Str::slug($company->name);
        });

        static::updating(function (self $company) {
            if (! $company->slug) {
                $company->slug = \Str::slug($company->name);
            }
        });
    }
}
