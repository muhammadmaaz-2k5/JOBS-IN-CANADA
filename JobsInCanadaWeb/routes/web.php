<?php

use App\Http\Controllers\Admin\AuthController;
use App\Http\Controllers\Admin\CareerResourceController;
use App\Http\Controllers\Admin\CategoryController;
use App\Http\Controllers\Admin\CompanyController;
use App\Http\Controllers\Admin\DashboardController;
use App\Http\Controllers\Admin\JobListingController;
use App\Http\Controllers\Admin\LogoController;
use App\Http\Controllers\Admin\ProvinceController;
use App\Http\Controllers\Admin\SettingsController;
use Illuminate\Support\Facades\Route;

Route::get('/', fn () => redirect()->route('admin.dashboard'));

Route::get('/admin/login', [AuthController::class, 'showLogin'])->name('admin.login');
Route::post('/admin/login', [AuthController::class, 'login'])->name('admin.login.submit');

Route::post('/admin/logout', [AuthController::class, 'logout'])
    ->name('admin.logout')
    ->middleware('auth');

Route::middleware('admin')->prefix('admin')->name('admin.')->group(function () {
    Route::get('/', [DashboardController::class, 'index'])->name('dashboard');

    Route::resource('categories', CategoryController::class);
    Route::resource('companies', CompanyController::class);
    Route::post('jobs/import-json', [JobListingController::class, 'importJson'])->name('jobs.import-json');
    Route::resource('jobs', JobListingController::class);
    Route::resource('provinces', ProvinceController::class);
    Route::resource('logos', LogoController::class);
    Route::resource('career-resources', CareerResourceController::class);

    Route::get('settings', [SettingsController::class, 'index'])->name('settings.index');
    Route::put('settings', [SettingsController::class, 'update'])->name('settings.update');
});
