import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import CourseDetails from './pages/CourseDetails'
import StudentProfile from './pages/StudentProfile'
import './App.css'

function App() {
  return (
    <Router>
      <div className="app">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/course/:courseId" element={<CourseDetails />} />
          <Route path="/course/:courseId/student/:studentId" element={<StudentProfile />} />
        </Routes>
      </div>
    </Router>
  )
}

export default App
