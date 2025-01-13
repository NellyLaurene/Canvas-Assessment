import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "../styles/Home.css";

const Home = () => {
  const [courses, setCourses] = useState([]); // List of courses
  const [selectedCourseId, setSelectedCourseId] = useState(null); // Selected courseId
  const [students, setStudents] = useState([]); // Students of the selected course
  const [loading, setLoading] = useState(false); // Loading state
  const [error, setError] = useState(null); // Error state

  // Fetch courses from the backend
  useEffect(() => {
    const fetchCourses = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/courses"); // Backend endpoint for published courses
        if (!response.ok) throw new Error("Failed to fetch courses");
        const data = await response.json();
        setCourses(data); // Set the list of courses
      } catch (err) {
        setError(err.message);
      }
    };

    fetchCourses();
  }, []);

  // Fetch students when a course is selected
  useEffect(() => {
    if (!selectedCourseId) return;

    const fetchStudents = async () => {
      setLoading(true);
      try {
        const response = await fetch(`http://localhost:8080/api/students?courseId=${selectedCourseId}`);
        if (!response.ok) throw new Error("Failed to fetch students");
        const data = await response.json();
        setStudents(data); // Set the students for the selected course
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchStudents();
  }, [selectedCourseId]);

  if (error) {
    return <div className="error">Error: {error}</div>;
  }

  return (
      <div className="home-container">
        <h1>Courses</h1>
        <div className="courses-grid">
          {courses.map((course) => (
              <button
                  key={course.id}
                  className={`course-card ${selectedCourseId === course.id ? "selected" : ""}`}
                  onClick={() => setSelectedCourseId(course.id)}
              >
                <h2>{course.name}</h2>
                <p>Code: {course.courseCode}</p>
              </button>
          ))}
        </div>

        {loading ? (
            <div className="loading">Loading students...</div>
        ) : selectedCourseId && students.length > 0 ? (
            <div className="students-grid">
              <h2>Students List</h2>
              {students.map((student) => (
                  <Link to={`/student/${student.id}`} key={student.id} className="student-card">
                    <div className="student-avatar">{student.name.charAt(0)}</div>
                    <div className="student-info">
                      <h3>{student.name}</h3>
                      <p>{student.email}</p>
                    </div>
                  </Link>
              ))}
            </div>
        ) : (
            <div className="no-students">No students found for the selected course.</div>
        )}
      </div>
  );
};

export default Home;
