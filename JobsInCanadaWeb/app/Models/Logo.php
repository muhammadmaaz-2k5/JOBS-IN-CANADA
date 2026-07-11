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
}
