import { useState, useEffect } from 'react';
import LoadingSpinner from '../Common/LoadingSpinner.jsx';
import ErrorMessage from '../Common/ErrorMessage.jsx';
import StudentTable from './StudentTable.jsx';

const StudentList = () => {
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const abortController = new AbortController();

        const fetchStudents = async () => {
            try {
                const response = await fetch('http://localhost:8081/api/canvas/all-students', {
                    signal: abortController.signal
                });
                if (!response.ok) {
                    throw new Error('Failed to fetch students');
                }
                const data = await response.json();
                if (!abortController.signal.aborted) {
                    setStudents(data);
                }
            } catch (error) {
                if (error.name === 'AbortError') {
                    return; // Ignore abort errors
                }
                console.error('Error fetching students:', error);
                setError(error.message);
            } finally {
                if (!abortController.signal.aborted) {
                    setLoading(false);
                }
            }
        };

        fetchStudents();

        return () => {
            abortController.abort();
        };
    }, []);

    if (loading) return <LoadingSpinner />;
    if (error) return <ErrorMessage error={error} />;

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-6">Students</h1><br/>
            <StudentTable students={students} />
        </div>
    );
};

export default StudentList;
