<?php

namespace Database\Seeders;

use App\Models\User;
use Illuminate\Database\Seeder;

class AdminSeeder extends Seeder
{
    public function run(): void
    {
        $email = env('ADMIN_EMAIL', 'admin@jobsincanada.ca');
        $password = env('ADMIN_PASSWORD', 'password');

        User::updateOrCreate(
            ['email' => $email],
            [
                'name' => 'Site Admin',
                'email' => $email,
                'password' => bcrypt($password),
                'is_admin' => true,
            ]
        );
    }
}
