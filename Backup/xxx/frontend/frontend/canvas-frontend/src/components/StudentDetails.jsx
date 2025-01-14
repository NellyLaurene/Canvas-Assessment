import 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/StudentDetails.css';

// eslint-disable-next-line react/prop-types
const StudentDetails = ({ student }) => {
  const navigate = useNavigate();

  if (!student) {
    return <div>No student data available.</div>; // Handles undefined `student`
  }

  // Extract student properties with fallback values
  const fullName = student.name || "Unknown Name";
  const studentId = student.id || "Unknown ID";
  const email = student.email || studentId; // Show student ID if email is not available
  const courses = student.courses || [];
  const course = courses.length > 0 ? courses[0] : {}; // Assuming we care about the first course for now
  const grade = course.grade || "N/A";
  const score = course.score || "N/A";

  // Position and total students fallback
  const position = course.position || "-";
  const totalStudents = course.total_students || "-";

  return (
      <div className="student-details-container">
        <button className="back-button" onClick={() => navigate(-1)}>
          ← Back to Students
        </button>

        <div className="student-header">
          <div className="avatar">{fullName.charAt(0)}</div>
          <div className="header-info">
            <h1>{fullName}</h1>
            <p className="student-email">Student ID: {email}</p>
          </div>
        </div>

        <div className="details-section">
          <div className="performance-details">
            <div className="info-row">
              <span className="label">Grade:</span>
              <span className="value grade">{grade}</span>
            </div>
            <div className="info-row">
              <span className="label">Score:</span>
              <span className="value score">{score}</span>
            </div>
            <div className="info-row">
              <span className="label">Position:</span>
              <span className="value position">
              {position} out of {totalStudents}
            </span>
            </div>
          </div>
        </div>
      </div>
  );
};

export default StudentDetails;
