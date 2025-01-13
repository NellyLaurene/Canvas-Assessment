import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import '../styles/StudentDetails.css';

const StudentDetails = () => {
  const { id } = useParams();
  const [studentData, setStudentData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // TODO: Fetch student details from Canvas API
    // Temporary mock data
    const mockStudentData = {
      id,
      name: 'John Doe',
      email: 'john@example.com',
      courses: [
        {
          id: 1,
          name: 'Mathematics',
          grade: 85,
          rank: 5,
          totalStudents: 30
        },
        {
          id: 2,
          name: 'Physics',
          grade: 92,
          rank: 2,
          totalStudents: 25
        },
        // Add more mock courses as needed
      ]
    };
    setStudentData(mockStudentData);
    setLoading(false);
  }, [id]);

  if (loading) {
    return <div className="loading">Loading student details...</div>;
  }

  return (
    <div className="student-details-container">
      <div className="header">
        <Link to="/" className="back-button">
          ← Back to Students
        </Link>
        <h1>{studentData.name}'s Report</h1>
      </div>
      
      <div className="student-profile">
        <div className="profile-avatar">
          {studentData.name.charAt(0)}
        </div>
        <div className="profile-info">
          <h2>{studentData.name}</h2>
          <p>{studentData.email}</p>
        </div>
      </div>

      <div className="courses-container">
        <h2>Enrolled Courses</h2>
        <div className="courses-grid">
          {studentData.courses.map((course) => (
            <div key={course.id} className="course-card">
              <h3>{course.name}</h3>
              <div className="course-stats">
                <div className="stat">
                  <span className="label">Grade</span>
                  <span className="value">{course.grade}%</span>
                </div>
                <div className="stat">
                  <span className="label">Rank</span>
                  <span className="value">{course.rank}/{course.totalStudents}</span>
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
