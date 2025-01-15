import { useNavigate } from 'react-router-dom';
import '../../styles/Dashboard.css';

const Header = () => {
    const navigate = useNavigate();

    return (
        <header className="dashboard-header">
            <div className="header-left">
                <h1 className="header-title">Canvas Dashboard</h1>
            </div>
            <div className="header-right">
                <button className="profile-button">
                    <span className="profile-avatar">N</span>
                    <span className="profile-name">Nelly</span>
                </button>
            </div>
        </header>
    );
};

export default Header;
