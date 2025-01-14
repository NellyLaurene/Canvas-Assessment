import  { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import StudentDetails from '../components/StudentDetails';
import '../styles/StudentProfile.css';

const StudentProfile = () => {
  const { courseId, studentId } = useParams();
  const [studentData, setStudentData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStudentData = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/students/${studentId}?courseId=${courseId}`);
        const data = await response.json();
        setStudentData(data);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching student data:', error);
        setLoading(false);
      }
    };

    fetchStudentData();
  }, [courseId, studentId]);

  if (loading) {
    return <div className="loading">Loading student profile...</div>;
  }

  return (
      <div className="student-profile-container">
        <StudentDetails student={studentData} />
      </div>
  );
};

export default StudentProfile;
