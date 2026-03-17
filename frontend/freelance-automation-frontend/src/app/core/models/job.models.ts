export interface JobOffer {
  id: number;
  externalId: string;
  platform: string;
  title: string;
  description: string;
  budget: number;
  currency: string;
  location: string;
  remoteAllowed: boolean;
  jobUrl: string;
  postedAt: string;
  ingestedAt: string;
}