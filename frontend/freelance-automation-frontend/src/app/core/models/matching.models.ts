export interface MatchingResultResponse {
  jobId: number;
  score: number;
  matchedKeywords: string[];
  decision: string;
}