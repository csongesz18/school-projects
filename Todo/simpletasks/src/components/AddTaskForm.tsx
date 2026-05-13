import { useState } from "react";
import "../styles/AddTaskForm.css";

interface AddTaskFormProps {
  // A szülő komponens felé visszaadjuk a létrehozott feladat címét és fontosságát
  onAdd: (title: string, importance?: "avg" | "low" | "medium" | "high") => void;
}

function AddTaskForm({ onAdd }: AddTaskFormProps) {
  // A felhasználó által beírt feladat szövege
  const [title, setTitle] = useState("");

  // A feladat kiválasztott fontossági szintje
  const [importance, setImportance] = useState<"avg" | "low" | "medium" | "high">("avg");

  // A form elküldése: új feladat hozzáadása
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    // Üres feladatot nem engedünk hozzáadni
    if (!title.trim()) return;

    // A szülő komponensnek átadjuk az adatokat
    onAdd(title, importance);

    // Mezők ürítése sikeres felvétel után
    setTitle("");
    setImportance("avg");
  };

  return (
    <form className="add-task-form" onSubmit={handleSubmit}>
      
      {/* Feladat szövegének beírása */}
      <input
        type="text"
        className="add-task-input"
        placeholder="Írd be a feladatot..."
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />

      {/* Fontosság kiválasztása legördülő listából */}
      <select
        className="importance-select"
        value={importance}
        onChange={(e) =>
          setImportance(e.target.value as "avg" | "low" | "medium" | "high")
        }
      >
        <option value="avg">⚪ Átlagos</option>
        <option value="low">🟡 Nem fontos</option>
        <option value="medium">🟠 Fontos</option>
        <option value="high">🔴 Nagyon fontos</option>
      </select>

      {/* Hozzáadás gomb */}
      <button type="submit" className="add-task-button">
        Hozzáadás
      </button>

    </form>
  );
}

export default AddTaskForm;
