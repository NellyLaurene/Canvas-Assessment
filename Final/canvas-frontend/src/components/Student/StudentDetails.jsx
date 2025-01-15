import  { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import '../../styles/StudentDetails.css';

const StudentDetails = () => {

  const navigate = useNavigate();
  const { studentId } = useParams();
  const [studentData, setStudentData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const abortController = new AbortController();

    const fetchStudentData = async () => {
      try {
        setLoading(true);
        const response = await fetch(`http://localhost:8081/api/canvas/student/${studentId}/details`, {
          signal: abortController.signal
        });

        if (!response.ok) {
          throw new Error('Failed to fetch student details');
        }

        const data = await response.json();

        // Transform the data to match the expected format
        const transformedData = {
          id: data.id,
          fullName: data.name,
          email: data.email || 'No email provided',
          courses: data.courseGrades.map(course => ({
            id: course.courseId,
            name: course.courseName,
            grade: calculateGrade(course.grade),
            rank: '-', // API doesn't provide rank, so we'll show a placeholder
            totalStudents: '-' // Same for total students
          }))
        };

        if (!abortController.signal.aborted) {
          setStudentData(transformedData);
        }

      } catch (error) {
        console.error('Error fetching student data:', error);
        setError('Failed to load student details');
      } finally {
        if (!abortController.signal.aborted) {
          setLoading(false);
        }
      }
    };

    fetchStudentData();

    return () => {
      abortController.abort();
    };
  }, [studentId]);

  // Helper function to convert numeric grade to letter grade
  const calculateGrade = (numericGrade) => {
    if (numericGrade >= 90) return 'A';
    if (numericGrade >= 80) return 'B';
    if (numericGrade >= 70) return 'C';
    if (numericGrade >= 60) return 'D';
    return 'F';
  };

  if (loading) {
    return (
        <div className="loading-container">
          <div className="loading-spinner"></div>
        </div>
    );
  }

  if (error) {
    return (
        <div className="error-container">
          <p>{error}</p>
          <button onClick={() => navigate('/')}>Back to Students</button>
        </div>
    );
  }

  if (!studentData) return <div>Student not found</div>;

  return (
      <div className="student-details-container">
        <button className="back-button" onClick={() => navigate('/')}>
          ← Back to Students
        </button>

        <div className="student-info">
          <h1>{studentData.fullName}</h1>
          <p className="student-email">{studentData.email}</p>
        </div>

        <div className="courses-section">
          <h2>Course Performance</h2>
          <table className="courses-table">
            <thead>
            <tr>
              <th>Course</th>
              <th>Grade</th>
              <th>Rank</th>
            </tr>
            </thead>
            <tbody>
            {studentData.courses.map((course) => (
                <tr key={course.id}>
                  <td>{course.name}</td>
                  <td className="grade">{course.grade}</td>
                  <td>{course.rank} of {course.totalStudents}</td>
                </tr>
            ))}
            </tbody>
          </table>
        </div>

        <button
            className="ai-analysis-button"
            onClick={() => navigate(`/ai-analysis/${studentId}`)}
        >
          View AI Analysis
        </button>
      </div>
  );
};

export default StudentDetails;
