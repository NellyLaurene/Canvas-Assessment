import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import '../styles/Home.css';

const Home = () => {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // TODO: Fetch students from Canvas API
    // Temporary mock data
    const mockStudents = [
      { id: 1, name: 'John Doe', email: 'john@example.com' },
      { id: 2, name: 'Jane Smith', email: 'jane@example.com' },
      // Add more mock data as needed
    ];
    setStudents(mockStudents);
    setLoading(false);
  }, []);

  if (loading) {
    return <div className="loading">Loading students...</div>;
  }

  return (
    <div className="home-container">
      <h1>Students List</h1>
      <div className="students-grid">
        {students.map((student) => (
          <Link 
            to={`/student/${student.id}`} 
            key={student.id} 
            className="student-card"
          >
            <div className="student-avatar">
              {student.name.charAt(0)}
            </div>
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
