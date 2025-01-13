import  { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import "../styles/StudentDetails.css";

const StudentDetails = () => {
  const { id } = useParams();
  const [student, setStudent] = useState(null);
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

    const fetchStudentDetails = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/students/${id}?courseId=${courseId}`);
        if (!response.ok) throw new Error("Failed to fetch student details");
        const data = await response.json();
        setStudent(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchStudentDetails();
  }, [id, courseId]);

  if (loading) {
    return <div className="loading">Loading student details...</div>;
  }

  if (error) {
    return <div className="error">Error: {error}</div>;
  }

  return (
      <div className="student-details-container">
        <div className="header">
          <Link to="/" className="back-button">
            ← Back to Students
          </Link>
          <h1>{student.name}&#39;s Report</h1>
        </div>

        <div className="student-profile">
          <div className="profile-avatar">{student.name.charAt(0)}</div>
          <div className="profile-info">
            <h2>{student.name}</h2>
            <p>{student.email}</p>
          </div>
        </div>

        <div className="courses-container">
          <h2>Enrolled Courses</h2>
          <div className="courses-grid">
            {student.courses.map((course, index) => (
                <div key={index} className="course-card">
                  <h3>{course.name}</h3>
                  <div className="course-stats">
                    <div className="stat">
                      <span className="label">Grade</span>
                      <span className="value">{course.grade}%</span>
                    </div>
                    <div className="stat">
                      <span className="label">Rank</span>
                      <span className="value">
                    {course.rank}/{course.totalStudents}
                  </span>
                    </div>
                  </div>
                </div>
            ))}
          </div>
        </div>
      </div>
  );
};

export default StudentDetails;
