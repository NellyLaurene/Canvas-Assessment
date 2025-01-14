import  { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import StudentList from '../components/StudentList';
import '../styles/CourseDetails.css';

const CourseDetails = () => {
  const { courseId } = useParams();
  const navigate = useNavigate();
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStudents = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/students?courseId=${courseId}`);
        const data = await response.json();
        setStudents(data);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching students:', error);
        setLoading(false);
      }
    };

    fetchStudents();
  }, [courseId]);

  if (loading) {
    return <div className="loading">Loading students...</div>;
  }

  return (
      <div className="course-details-container">
        <button className="back-button" onClick={() => navigate('/')}>
          ← Back to Courses
        </button>
        <StudentList students={students} courseId={courseId} />
      </div>
  );
};

export default CourseDetails;
