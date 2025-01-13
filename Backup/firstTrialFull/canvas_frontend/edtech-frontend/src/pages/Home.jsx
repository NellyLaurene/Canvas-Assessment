import  { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "../styles/Home.css";

const Home = () => {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [courseId, setCourseId] = useState(null);

  useEffect(() => {
    const fetchCourseId = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/course"); // API to fetch courseId
        if (!response.ok) throw new Error("Failed to fetch courseId");
        const data = await response.json();
        setCourseId(data.courseId);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchCourseId();
  }, []);

  useEffect(() => {
    if (!courseId) return; // Wait for courseId to load

    const fetchStudents = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/students?courseId=${courseId}`);
        if (!response.ok) throw new Error("Failed to fetch students");
        const data = await response.json();
        setStudents(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchStudents();
  }, [courseId]);

  if (loading) {
    return <div className="loading">Loading students...</div>;
  }

  if (error) {
    return <div className="error">Error: {error}</div>;
  }

  return (
      <div className="home-container">
        <h1>Students List</h1>
        <div className="students-grid">
          {students.map((student) => (
              <Link to={`/student/${student.id}`} key={student.id} className="student-card">
                <div className="student-avatar">{student.name.charAt(0)}</div>
                <div className="student-info">
                  <h2>{student.name}</h2>
                  <p>{student.email}</p>
                </div>
              </Link>
          ))}
        </div>
      </div>
  );
};

export default Home;
