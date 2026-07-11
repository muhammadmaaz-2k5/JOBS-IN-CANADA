<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Category extends Model
{
    use HasFactory;

    protected $fillable = ['name', 'slug', 'icon', 'color', 'sort_order'];

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

        static::creating(function (self $category) {
            $category->slug = $category->slug ?: \Str::slug($category->name);
        });

        static::updating(function (self $category) {
            if (! $category->slug) {
                $category->slug = \Str::slug($category->name);
            }
        });
    }
}
