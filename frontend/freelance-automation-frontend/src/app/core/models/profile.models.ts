export interface ProfileResponse {
  id: number;
  email: string;
  title: string;
  skills: string;
  yearsOfExperience: number;
  dailyRate: number;
  missionPreferences: string;
}

export interface UpsertProfileRequest {
  title: string;
  skills: string;
  yearsOfExperience: number;
  dailyRate: number | string;
  missionPreferences: string;
}