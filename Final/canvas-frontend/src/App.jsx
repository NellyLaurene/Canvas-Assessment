import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Students from './pages/Students'
import Student from "./pages/Student.jsx";
import AIAnalysis from './pages/AIAnalysis'
import './App.css'

function App() {
  return (
    <Router>
      <div className="app">
        <Routes>
          <Route path="/" element={<Students />} />
          <Route path="/student/:studentId" element={<Student />} />
          <Route path="/ai-analysis/:studentId" element={<AIAnalysis />} />
        </Routes>
      </div>
    </Router>
  )
}

export default App
