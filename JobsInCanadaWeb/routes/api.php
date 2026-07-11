<?php

use App\Http\Controllers\Api\JobBoardController;
use Illuminate\Support\Facades\Route;

Route::get('/categories', [JobBoardController::class, 'categories']);
Route::get('/companies', [JobBoardController::class, 'companies']);
Route::get('/provinces', [JobBoardController::class, 'provinces']);
Route::get('/jobs', [JobBoardController::class, 'jobs']);
Route::get('/jobs/{job}', [JobBoardController::class, 'job']);
Route::get('/career-resources', [JobBoardController::class, 'careerResources']);
Route::get('/stats', [JobBoardController::class, 'stats']);
Route::get('/settings', [JobBoardController::class, 'settings']);
