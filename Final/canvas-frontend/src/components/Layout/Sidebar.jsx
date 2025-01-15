import { NavLink, useLocation } from 'react-router-dom';
import '../../styles/Dashboard.css';

const Sidebar = () => {

    const location = useLocation();

    const isActive = (path) => {
        return location.pathname.startsWith(path);
    };

    const isDisabled = (path) => {
        return path !== '/';
    };

    const NavItem = ({ to, children }) => {
        const activeClass = isActive(to) ? 'nav-item-active' : '';
        const disabledClass = isDisabled(to) ? 'nav-item-disabled' : '';

        return (
            <NavLink
                to={to}
                className={`nav-item ${activeClass} ${disabledClass}`}
                onClick={(e) => {
                    if (isDisabled(to)) {
                        e.preventDefault();
                    }
                }}
                end={to === '/'}
            >
                {children}
            </NavLink>
        );
    };

    return (
        <aside className="dashboard-sidebar">
            <nav className="sidebar-nav">
                <NavItem to="/">Home</NavItem>
                <NavItem to="/student">Student</NavItem>
                <NavItem to="/ai-analysis">AI Analysis</NavItem>
            </nav>
        </aside>
    );
};

export default Sidebar;
