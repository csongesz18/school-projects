import { Task } from "../types";

/* Kulcs a localStorage-ben a feladatok tárolásához */
const STORAGE_KEY = "simpletasks-data";

/**
 * Feladatok mentése a localStorage-ba.
 */
export function saveTasks(tasks: Task[]) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));
}

/**
 * Feladatok betöltése a localStorage-ból.
 */
export function loadTasks(): Task[] {
  const data = localStorage.getItem(STORAGE_KEY);
  if (!data) return [];
  try {
    return JSON.parse(data) as Task[];
  } catch {
    return [];
  }
}
