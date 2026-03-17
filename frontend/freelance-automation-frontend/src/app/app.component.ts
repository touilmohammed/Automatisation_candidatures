import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './shared/layout/header/header.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent],
  template: `
    <app-header></app-header>
    <main class="page-container">
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [`
    .page-container {
      max-width: 1100px;
      margin: 0 auto;
      padding: 32px 24px;
    }
  `]
})
export class AppComponent {}