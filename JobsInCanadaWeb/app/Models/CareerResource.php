<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class CareerResource extends Model
{
    use HasFactory;

    protected $fillable = ['title', 'slug', 'subtitle', 'icon', 'color', 'icon_color', 'sort_order', 'content'];

    protected $casts = [
        'sort_order' => 'integer',
    ];

    protected static function boot(): void
    {
        parent::boot();

        static::creating(function (self $resource) {
            $resource->slug = $resource->slug ?: \Str::slug($resource->title);
        });

        static::updating(function (self $resource) {
            if (! $resource->slug) {
                $resource->slug = \Str::slug($resource->title);
            }
        });
    }
}
