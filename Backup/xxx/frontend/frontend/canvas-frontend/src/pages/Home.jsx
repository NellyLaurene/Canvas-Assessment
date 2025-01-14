import  { useState, useEffect } from 'react';
import CourseCard from '../components/CourseCard';
import '../styles/Home.css';

const Home = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCourses = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/courses');
        const data = await response.json();
        setCourses(data);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching courses:', error);
        setLoading(false);
      }
    };

    fetchCourses();
  }, []);

  if (loading) {
    return <div className="loading">Loading courses...</div>;
  }

  return (
      <div className="home-container">
        <h1>Available Courses</h1>
        <div className="courses-grid">
          {courses.map((course) => (
              <CourseCard key={course.id} course={course} />
          ))}
        </div>
      </div>
  );
};

export default Home;
