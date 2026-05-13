import "../styles/Header.css";

function Header() {
  return (
    // Az alkalmazás felső címsora
    <header className="header-container">
      
      {/* Fő cím */}
      <h2 className="header-title">Feladatkezelő</h2>

      {/* Alcím / leírás */}
      <div className="header-subtitle">Teendőlista</div>

    </header>
  );
}

export default Header;
