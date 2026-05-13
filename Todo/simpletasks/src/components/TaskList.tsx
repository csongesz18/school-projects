import { Task } from "../types";
import TaskItem from "./TaskItem";
import "../styles/TaskItem.css";
import "../styles/TaskList.css";

interface TaskListProps {
  // A megjelenítendő feladatok listája
  tasks: Task[];

  // Feladat kész / nem kész állapot váltása
  onToggle: (id: number) => void;

  // Feladat törlése
  onDelete: (id: number) => void;

  // Feladat szövegének szerkesztése
  onEdit: (id: number, newTitle: string) => void;
}

function TaskList({ tasks, onToggle, onDelete, onEdit }: TaskListProps) {
  return (
    <ul className="task-list">
      {/* Minden feladat külön TaskItem komponensként jelenik meg */}
      {tasks.map((task) => (
        <TaskItem
          key={task.id}
          task={task}
          onToggle={onToggle}
          onDelete={onDelete}
          onEdit={onEdit}
        />
      ))}
    </ul>
  );
}

export default TaskList;
