export interface CvResponse {
  id: number;
  filename: string;
  mimeType: string;
  uploadedAt: string;
}

export interface CvTextResponse {
  filename: string;
  extractedText: string;
  textLength: number;
}