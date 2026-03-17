export interface ApplicationResponse {
  id: number;
  jobId: number;
  jobTitle: string;
  generatedCv: string;
  generatedCoverLetter: string;
  status: string;
  createdAt: string;
  reviewedAt: string | null;
  submittedAt: string | null;
  submissionMode: string | null;
}

export interface ApplicationActionResponse {
  applicationId: number;
  status: string;
}

export interface ApplicationSubmitResponse {
  applicationId: number;
  status: string;
  submissionMode: string;
  targetUrl: string;
  submittedAt: string;
}