
import PropTypes from 'prop-types';
import Header from '../Layout/Header.jsx';
import Sidebar from '../Layout/Sidebar.jsx';
import Footer from '../Layout/Footer.jsx';

const DashboardLayout = ({ children }) => {
    return (
        <div className="dashboard-container">
            <Header />
            <div className="dashboard-content">
                <Sidebar />
                <main className="main-content">
                    {children}
                </main>
            </div>
            <Footer />
        </div>
    );
};

DashboardLayout.propTypes = {
    children: PropTypes.node.isRequired,
};

export default DashboardLayout;
