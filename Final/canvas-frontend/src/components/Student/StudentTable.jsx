import React, { useState, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import PropTypes from 'prop-types';
import '../../styles/StudentTable.css';

const StudentTable = ({ students }) => {
    const navigate = useNavigate();
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedCourse, setSelectedCourse] = useState('');

    // Get unique courses for dropdown
    const availableCourses = useMemo(() => {
        const coursesSet = new Set();
        students.forEach(student => {
            student.courses.forEach(course => {
                coursesSet.add(course.courseName);
            });
        });
        return Array.from(coursesSet);
    }, [students]);

    const formatCourses = (courses) => {
        if (!courses || courses.length === 0) return 'No courses';
        return courses.map(course => course.courseName).join(', ');
    };

    // Filter and search logic
    const filteredStudents = useMemo(() => {
        return students.filter(student => {
            const matchesSearch = student.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                student.email.toLowerCase().includes(searchTerm.toLowerCase());

            const matchesCourse = !selectedCourse ||
                student.courses.some(course => course.courseName === selectedCourse);

            return matchesSearch && matchesCourse;
        });
    }, [students, searchTerm, selectedCourse]);

    return (
        <div className="table-container">
            <div className="table-controls">
                <input
                    type="text"
                    placeholder="Search students..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="search-input"
                />
                <select
                    value={selectedCourse}
                    onChange={(e) => setSelectedCourse(e.target.value)}
                    className="course-filter"
                >
                    <option value="">All Courses</option>
                    {availableCourses.map(course => (
                        <option key={course} value={course}>
                            {course}
                        </option>
                    ))}
                </select>
            </div>
            <table className="students-table">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Courses</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {filteredStudents.map((student) => (
                    <tr key={student.id}>
                        <td>
                            <div className="student-name">
                                <div className="student-avatar">
                                    {student.name.charAt(0)}
                                </div>
                                <span>{student.name}</span>
                            </div>
                        </td>
                        <td>{student.email}</td>
                        <td>{formatCourses(student.courses)}</td>
                        <td>
                            <button
                                className="view-details-btn"
                                onClick={() => navigate(`/student/${student.id}`)}
                            >
                                View Report
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

StudentTable.propTypes = {
    students: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.string.isRequired,
            name: PropTypes.string.isRequired,
            email: PropTypes.string.isRequired,
            courses: PropTypes.arrayOf(
                PropTypes.shape({
                    courseId: PropTypes.string.isRequired,
                    courseName: PropTypes.string.isRequired
                })
            )
        })
    ).isRequired
};

export default StudentTable;
