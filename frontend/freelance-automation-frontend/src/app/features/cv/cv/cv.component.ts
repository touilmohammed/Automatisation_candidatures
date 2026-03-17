import { CommonModule, DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { CvService } from '../../../core/services/cv.service';
import { CvResponse } from '../../../core/models/cv.models';

@Component({
  selector: 'app-cv',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './cv.component.html',
  styleUrls: ['./cv.component.css']
})
export class CvComponent implements OnInit {
  private cvService = inject(CvService);

  isLoading = false;
  isUploading = false;

  message = '';
  errorMessage = '';

  currentCv: CvResponse | null = null;
  extractedText = '';
  selectedFileName = '';

  ngOnInit(): void {
    this.loadCvData();
  }

  loadCvData(): void {
    this.isLoading = true;
    this.message = '';
    this.errorMessage = '';

    this.cvService.getMyCv().subscribe({
      next: (cv) => {
        this.currentCv = cv;
        this.loadText();
      },
      error: (err) => {
        this.isLoading = false;
        if (err?.status !== 404) {
          this.errorMessage = err?.error?.error ?? 'Impossible de charger le CV';
        }
      }
    });
  }

  loadText(): void {
    this.cvService.getMyCvText().subscribe({
      next: (res) => {
        this.extractedText = res.extractedText ?? '';
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        if (err?.status !== 404) {
          this.errorMessage = err?.error?.error ?? 'Impossible de charger le texte extrait';
        }
      }
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) {
      return;
    }

    this.selectedFileName = file.name;
    this.upload(file);
  }

  upload(file: File): void {
    this.isUploading = true;
    this.message = '';
    this.errorMessage = '';

    this.cvService.upload(file).subscribe({
      next: (cv) => {
        this.currentCv = cv;
        this.message = 'CV uploadé avec succès.';
        this.isUploading = false;
        this.loadText();
      },
      error: (err) => {
        this.isUploading = false;
        this.errorMessage = err?.error?.error ?? 'Erreur lors de l’upload du CV';
      }
    });
  }
}