<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('job_listings', function (Blueprint $table) {
            $table->id();
            $table->string('title');
            $table->string('slug')->unique();
            $table->foreignId('company_id')->nullable()->constrained('companies')->nullOnDelete();
            $table->foreignId('category_id')->nullable()->constrained('categories')->nullOnDelete();
            $table->string('salary')->nullable();
            $table->string('salary_period')->default('year');
            $table->string('location')->nullable();
            $table->string('province')->nullable();
            $table->string('job_type')->default('Full-Time');
            $table->boolean('is_remote')->default(false);
            $table->boolean('is_new')->default(false);
            $table->integer('applicants')->default(0);
            $table->boolean('is_featured')->default(false);
            $table->string('apply_url')->nullable();
            $table->text('description')->nullable();
            $table->json('skills')->nullable();
            $table->string('company_logo')->nullable();
            $table->string('company_logo_label')->nullable();
            $table->json('applicant_avatars')->nullable();
            $table->timestamp('posted_at')->nullable();
            $table->boolean('is_active')->default(true);
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('job_listings');
    }
};
