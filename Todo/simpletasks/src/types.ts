/** Feladat típus definiálása */
export interface Task {
  id: number;
  title: string;
  completed: boolean;
  importance?: "avg" | "low" | "medium" | "high";
}
