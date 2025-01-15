import PropTypes from 'prop-types';
import '../../styles/ErrorMessage.css';

const ErrorMessage = ({ error }) => (
    <div className="error-container">
        <div className="error-message">Error: {error}</div>
        <button
            onClick={() => window.location.reload()}
            className="retry-button"
        >
            Retry
        </button>
    </div>
);

ErrorMessage.propTypes = {
    error: PropTypes.string.isRequired
};

export default ErrorMessage;
