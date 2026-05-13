import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';

// A React alkalmazás gyökéreleme
const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);

// Az alkalmazás elindítása
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
