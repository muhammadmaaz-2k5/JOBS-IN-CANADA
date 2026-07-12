<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Logo extends Model
{
    use HasFactory;

    protected $fillable = ['name', 'url', 'sort_order'];

    protected $casts = [
        'sort_order' => 'integer',
    ];

    protected static function boot(): void
    {
        parent::boot();

        static::saved(function (self $logo) {
            Company::where('name', $logo->name)->update(['logo' => $logo->url]);
        });
    }
}
