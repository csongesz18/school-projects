import "../styles/TaskItem.css";
import { useState, useEffect, useRef } from "react";
import { Task } from "../types";

interface TaskItemProps {
  // Egy konkrét feladat adatai
  task: Task;

  // Kattintásra a feladat kész / nem kész állapotát változtatja
  onToggle: (id: number) => void;

  // A feladat törlése
  onDelete: (id: number) => void;

  // A feladat szövegének módosítása
  onEdit: (id: number, newTitle: string) => void;
}

function TaskItem({ task, onToggle, onDelete, onEdit }: TaskItemProps) {
  // Szerkesztő mód be/ki
  const [isEditing, setIsEditing] = useState(false);

  // A feladat szövegének aktuális értéke szerkesztés közben
  const [text, setText] = useState(task.title);

  // Input mezőhöz referencia a fókusz automatikus beállításához
  const inputRef = useRef<HTMLInputElement | null>(null);

  // Ha szerkesztés indul → automatikusan fókuszt kap az input
  useEffect(() => {
    if (isEditing && inputRef.current) {
      inputRef.current.focus();
    }
  }, [isEditing]);

  // Enter → mentés, Escape → szerkesztés megszakítása
  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === "Enter") finishEditing();

    if (e.key === "Escape") {
      setIsEditing(false);
      setText(task.title);
    }
  };

  // Szerkesztés befejezése és frissítés a szülő komponens felé
  const finishEditing = () => {
    if (text.trim() !== "") {
      onEdit(task.id, text);
    } else {
      setText(task.title);
    }
    setIsEditing(false);
  };

  return (
    <li
      className={`task-item 
        ${task.completed ? "completed-task" : ""}
        ${
          !task.completed
            ? task.importance === "low"
              ? "importance-low"
              : task.importance === "medium"
              ? "importance-medium"
              : task.importance === "high"
              ? "importance-high"
              : ""
            : ""
        }
      `}
    >
      {/* BAL OLDAL – checkbox és szöveg együtt */}
      <div className="left-section">
        
        {/* A feladat készre jelölése */}
        <div
          className={`checkbox ${task.completed ? "checked" : ""}`}
          onClick={() => onToggle(task.id)}
        ></div>

        {/* Szerkesztő mező vagy sima szöveg – attól függően, hogy editing módban vagyunk-e */}
        {isEditing ? (
          <input
            ref={inputRef}
            className="edit-input"
            value={text}
            onChange={(e) => setText(e.target.value)}
            onBlur={finishEditing}
            onKeyDown={handleKeyDown}
          />
        ) : (
          <span
            className={`task-title ${task.completed ? "completed" : ""}`}
            onDoubleClick={() => setIsEditing(true)}
          >
            {task.title}
          </span>
        )}
      </div>

      {/* JOBB OLDAL – Törlés gomb */}
      <button className="delete-btn" onClick={() => onDelete(task.id)}>
        Törlés
      </button>
    </li>
  );
}

export default TaskItem;
