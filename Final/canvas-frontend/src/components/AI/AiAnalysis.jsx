import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
    PieChart, Pie, Cell
} from 'recharts';
import LoadingSpinner from '../Common/LoadingSpinner.jsx';
import ErrorMessage from '../Common/ErrorMessage.jsx';
import '../../styles/AIAnalysis.css';

const AiAnalysis = () => {
    const [analysisData, setAnalysisData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { studentId } = useParams();

    useEffect(() => {
        const abortController = new AbortController();

        const fetchAnalysis = async () => {
            try {
                const response = await fetch(`http://localhost:8081/api/ai/analysis/${studentId}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch analysis');
                }
                const data = await response.json();

                if (!abortController.signal.aborted) {
                    setAnalysisData(data);
                }

            } catch (error) {
                console.error('Error fetching analysis:', error);
                setError(error.message);
            } finally {
                if (!abortController.signal.aborted) {
                    setLoading(false);
                }
            }
        };

        fetchAnalysis();

        return () => {
            abortController.abort();
        };
    }, [studentId]);

    if (loading) return <LoadingSpinner />;
    if (error) return <ErrorMessage error={error} />;
    if (!analysisData) return null;

    const COLORS = ['#53916f', '#457a5d', '#6aad87', '#386349'];

    // Prepare data for grade distribution chart
    const gradeData = (analysisData.performanceMetrics.courseGrades || []).map(course => ({
        name: course.courseName,
        grade: course.grade
    }));

    // Prepare data for course distribution pie chart
    const courseDistData = Object.entries(analysisData.engagementAnalysis.courseDistribution || [])
        .map(([name, value]) => ({ name, value }));

    // Helper function for risk level color
    const getRiskLevelColor = (level) => {
        const colors = {
            'High': 'red',
            'Medium': 'orange',
            'Low': 'green'
        };
        return colors[level] || 'gray';
    };

    return (
        <div className="ai-analysis-container">
            {/* Student Info Header */}
            <div className="student-info-section">
                <h1>{analysisData.studentInfo.name}&#39;s Academic Analysis</h1>
                <p className="email">{analysisData.studentInfo.email}</p>
            </div>

            {/* Performance Overview */}
            <div className="metrics-grid">
                <div className="metric-card">
                    <h3>Average Grade</h3>
                    <div className="metric-value">{analysisData.performanceMetrics.averageGrade.toFixed(1)}</div>
                </div>
                <div className="metric-card">
                    <h3>Total Courses</h3>
                    <div className="metric-value">{analysisData.engagementAnalysis.totalEnrolledCourses}</div>
                </div>
                <div className="metric-card">
                    <h3>Risk Level</h3>
                    <div className="metric-value" style={{ color: getRiskLevelColor(analysisData.riskAssessment.riskLevel) }}>
                        {analysisData.riskAssessment.riskLevel}
                    </div>
                </div>
            </div>

            {/* Charts Section */}
            <div className="charts-section">
                <div className="chart-container">
                    <h3>Grade Distribution</h3>
                    <BarChart width={500} height={300} data={gradeData}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="name" />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Bar dataKey="grade" fill="#53916f" />
                    </BarChart>
                </div>

                <div className="chart-container">
                    <h3>Course Distribution</h3>
                    <PieChart width={400} height={300}>
                        <Pie
                            data={courseDistData}
                            cx={200}
                            cy={150}
                            outerRadius={100}
                            fill="#8884d8"
                            dataKey="value"
                            label
                        >
                            {courseDistData.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                            ))}
                        </Pie>
                        <Tooltip />
                        <Legend />
                    </PieChart>
                </div>
            </div>

            {/* Risk Assessment */}
            <div className="risk-section">
                <h3>Risk Factors</h3>
                <ul className="risk-factors">
                    {analysisData.riskAssessment.factors.map((factor, index) => (
                        <li key={index}>{factor}</li>
                    ))}
                </ul>
            </div>

            {/* Recommendations */}
            <div className="recommendations-section">
                <h3>Recommendations</h3>
                <ul className="recommendations">
                    {analysisData.recommendations.map((recommendation, index) => (
                        <li key={index}>{recommendation}</li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default AiAnalysis;
