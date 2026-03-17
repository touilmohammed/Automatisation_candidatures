import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProfileService } from '../../../core/services/profile.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  private fb = inject(FormBuilder);
  private profileService = inject(ProfileService);

  isLoading = false;
  isSaving = false;
  successMessage = '';
  errorMessage = '';

  form = this.fb.group({
    title: ['', [Validators.required]],
    skills: ['', [Validators.required]],
    yearsOfExperience: [0, [Validators.required, Validators.min(0)]],
    dailyRate: [0, [Validators.required, Validators.min(0)]],
    missionPreferences: ['', [Validators.required]]
  });

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.profileService.getMyProfile().subscribe({
      next: (profile) => {
        this.form.patchValue({
          title: profile.title ?? '',
          skills: profile.skills ?? '',
          yearsOfExperience: profile.yearsOfExperience ?? 0,
          dailyRate: profile.dailyRate ?? 0,
          missionPreferences: profile.missionPreferences ?? ''
        });
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        if (err?.status !== 404) {
          this.errorMessage = err?.error?.error ?? 'Impossible de charger le profil';
        }
      }
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSaving = true;
    this.errorMessage = '';
    this.successMessage = '';

    const payload = {
      title: this.form.value.title ?? '',
      skills: this.form.value.skills ?? '',
      yearsOfExperience: Number(this.form.value.yearsOfExperience ?? 0),
      dailyRate: Number(this.form.value.dailyRate ?? 0),
      missionPreferences: this.form.value.missionPreferences ?? ''
    };

    this.profileService.upsertMyProfile(payload).subscribe({
      next: () => {
        this.isSaving = false;
        this.successMessage = 'Profil enregistré avec succès.';
      },
      error: (err) => {
        this.isSaving = false;
        this.errorMessage = err?.error?.error ?? 'Impossible d’enregistrer le profil';
      }
    });
  }

  get title() {
    return this.form.get('title');
  }

  get skills() {
    return this.form.get('skills');
  }

  get yearsOfExperience() {
    return this.form.get('yearsOfExperience');
  }

  get dailyRate() {
    return this.form.get('dailyRate');
  }

  get missionPreferences() {
    return this.form.get('missionPreferences');
  }
}