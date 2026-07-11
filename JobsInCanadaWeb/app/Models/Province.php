<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Province extends Model
{
    use HasFactory;

    protected $fillable = ['name', 'code', 'sort_order'];

    protected $casts = [
        'sort_order' => 'integer',
    ];

    public function jobListings()
    {
        return $this->hasMany(JobListing::class, 'province', 'name');
    }
}
