import 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/CourseCard.css';

const CourseCard = ({ course }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/course/${course.id}`);
  };

  return (
    <div className="course-card" onClick={handleClick}>
      <h2>{course.name}</h2>
      <p className="course-id">ID: {course.id}</p>
    </div>
  );
};

export default CourseCard;
