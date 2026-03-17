import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const appRoutes: Routes = [
  {
    path: '',
    redirectTo: 'jobs',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'profile',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/profile/profile/profile.component').then(m => m.ProfileComponent)
  },
  {
    path: 'cv',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/cv/cv/cv.component').then(m => m.CvComponent)
  },
  {
    path: 'jobs',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/jobs/jobs-list/jobs-list.component').then(m => m.JobsListComponent)
  },
  {
    path: 'jobs/:id',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/jobs/job-detail/job-detail.component').then(m => m.JobDetailComponent)
  },
  {
    path: 'applications/:id',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/applications/application-detail/application-detail.component').then(m => m.ApplicationDetailComponent)
  },
  {
    path: 'notifications',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/notifications/notifications/notifications.component').then(m => m.NotificationsComponent)
  },
  {
    path: 'audit',
    canActivate: [authGuard],
    loadComponent: () =>
        import('./features/audit/audit/audit.component').then(m => m.AuditComponent)
  },
  {
    path: '**',
    redirectTo: 'jobs'
  },
];