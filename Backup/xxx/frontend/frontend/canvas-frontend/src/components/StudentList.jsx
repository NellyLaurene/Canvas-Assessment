import 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/StudentList.css';

// eslint-disable-next-line react/prop-types
const StudentList = ({ students, courseId }) => {
  const navigate = useNavigate();

  const handleStudentClick = (studentId) => {
    navigate(`/course/${courseId}/student/${studentId}`);
  };

  return (
      <div className="student-list-container">
        <h2>Students Enrolled</h2>
        <div className="table-container">
          <table className="students-table">
            <thead>
            <tr>
              <th>Name</th>
              <th>Email</th>
              <th>Action</th>
            </tr>
            </thead>
            <tbody>
            {/* eslint-disable-next-line react/prop-types */}
            {students.map((student) => (
                <tr key={student.id}>
                  <td>{student.name}</td>
                  <td>{student.email || 'No Email Provided'}</td>
                  <td>
                    <button
                        className="view-details-btn"
                        onClick={() => handleStudentClick(student.id)}
                    >
                      View Report
                    </button>
                  </td>
                </tr>
            ))}
            </tbody>
          </table>
        </div>
      </div>
  );
};

export default StudentList;
