import { loadTasks, saveTasks } from "./utils/storage";
import { useState, useEffect } from "react";
import Filter from "./components/Filter";
import Header from "./components/Header";
import AddTaskForm from "./components/AddTaskForm";
import TaskList from "./components/TaskList";
import { Task } from "./types";
import "./styles/App.css";

function App() {
  // A feladatok listája (localStorage-ból betöltve)
  const [tasks, setTasks] = useState<Task[]>(() => loadTasks());

  // A kiválasztott szűrő típusa
  const [filter, setFilter] = useState<string>("all");

  // Fontosság szerinti rendezés be/ki kapcsolása
  const [sortByImportance, setSortByImportance] = useState(false);

  // Új feladat hozzáadása
  const addTask = (title: string, importance?: "avg" | "low" | "medium" | "high") => {
    const newTask: Task = {
      id: Date.now(),
      title,
      completed: false,
      importance: importance,
    };
    setTasks([...tasks, newTask]);
  };

  // Feladat kész állapotának átváltása
  const toggleTask = (id: number) => {
    setTasks(
      tasks.map((task) =>
        task.id === id ? { ...task, completed: !task.completed } : task
      )
    );
  };

  // Egy feladat törlése
  const deleteTask = (id: number) => {
    setTasks(tasks.filter((task) => task.id !== id));
  };

  // Minden feladat törlése
  const deleteAllTasks = () => {
    setTasks([]);
  };

  // Feladat szövegének szerkesztése
  const editTask = (id: number, newTitle: string) => {
    setTasks(
      tasks.map((task) =>
        task.id === id ? { ...task, title: newTitle } : task
      )
    );
  };

  // A feladatok mentése localStorage-ba minden változáskor
  useEffect(() => {
    saveTasks(tasks);
  }, [tasks]);

  // ⭐ A megjelenített lista kiszámítása (szűrés)
  let displayedTasks =
    filter === "all"
      ? tasks
      : filter === "active"
      ? tasks.filter((t) => !t.completed)
      : tasks.filter((t) => t.completed);

  // ⭐ Rendezés fontosság szerint (csak az aktívak)
  if (sortByImportance) {
    const order = { high: 3, medium: 2, low: 1, avg: 0 };

    const activeTasks = displayedTasks.filter(t => !t.completed);
    const completedTasks = displayedTasks.filter(t => t.completed);

    // A fontosabb feladatok kerüljenek előre
    activeTasks.sort((a, b) => {
      return order[b.importance || "avg"] - order[a.importance || "avg"];
    });

    displayedTasks = [...activeTasks, ...completedTasks];
  }

  return (
    <div className="app-container">
      <Header />

      {/* Feladat hozzáadása */}
      <AddTaskForm onAdd={addTask} />

      {/* Szűrők + Rendezés + Mindent törlés */}
      <div className="top-bar">
        <Filter currentFilter={filter} onChange={setFilter} />

        {/* Rendezés gomb */}
        <button
          className={`sort-btn ${sortByImportance ? "active" : ""}`}
          onClick={() => setSortByImportance(!sortByImportance)}
        >
          Rendezés
        </button>

        {/* Összes törlése */}
        <button onClick={deleteAllTasks} className="clear-all-button">
          Összes törlése
        </button>
      </div>

      {/* Feladatok listája */}
      <TaskList
        tasks={displayedTasks}
        onToggle={toggleTask}
        onDelete={deleteTask}
        onEdit={editTask}
      />
    </div>
  );
}

export default App;
