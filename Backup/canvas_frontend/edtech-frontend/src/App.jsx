import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import StudentDetails from './pages/StudentDetails';
import './styles/App.css';

function App() {
  return (
    <Router>
      <div className="app">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/student/:id" element={<StudentDetails />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
