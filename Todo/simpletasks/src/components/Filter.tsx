import "../styles/Filter.css";

interface FilterProps {
  // A jelenleg kiválasztott szűrő (all / active / completed)
  currentFilter: string;

  // A szülő komponens felé jelezzük, hogy a felhasználó új szűrőt választott
  onChange: (filter: string) => void;
}

function Filter({ currentFilter, onChange }: FilterProps) {
  return (
    <div className="filter-container">
      
      {/* Összes feladat megjelenítése */}
      <button
        className={`filter-button ${currentFilter === "all" ? "active" : ""}`}
        onClick={() => onChange("all")}
      >
        Összes
      </button>

      {/* Csak az aktív (nem pipált) feladatok */}
      <button
        className={`filter-button ${currentFilter === "active" ? "active" : ""}`}
        onClick={() => onChange("active")}
      >
        Aktív
      </button>

      {/* Csak a kész (pipált) feladatok */}
      <button
        className={`filter-button ${currentFilter === "completed" ? "active" : ""}`}
        onClick={() => onChange("completed")}
      >
        Kész
      </button>

    </div>
  );
}

export default Filter;
