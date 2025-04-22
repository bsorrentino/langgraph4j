-- Drop tables in reverse order of dependency if they exist, using CASCADE for FKs
DROP TABLE IF EXISTS issue CASCADE;
DROP TABLE IF EXISTS project CASCADE;

-- Create the project table
CREATE TABLE project (
    project_id BIGSERIAL PRIMARY KEY, -- Use BIGSERIAL for auto-incrementing 64-bit integer
    name VARCHAR(255) NOT NULL UNIQUE, -- Project name, must be unique and not empty
    description TEXT,                  -- Optional longer description
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(), -- Timestamp with timezone when the project was created
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()  -- Timestamp with timezone when the project was last updated (can be managed by triggers later if needed)
);

-- Create the issue table
CREATE TABLE issue (
    issue_id BIGSERIAL PRIMARY KEY,     -- Use BIGSERIAL for auto-incrementing 64-bit integer
    project_id BIGINT NOT NULL,         -- Foreign key referencing the project table
    title VARCHAR(255) NOT NULL,        -- Issue title, must not be empty
    description TEXT,                   -- Optional longer description of the issue
    status VARCHAR(50) NOT NULL CHECK (status IN ('Open', 'In Progress', 'Resolved', 'Closed', 'Reopened')), -- Status must be one of these values
    priority VARCHAR(50) NOT NULL CHECK (priority IN ('Low', 'Medium', 'High', 'Critical')), -- Priority must be one of these values
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(), -- Timestamp with timezone when the issue was created
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(), -- Timestamp with timezone when the issue was last updated

    -- Define the foreign key constraint
    CONSTRAINT fk_project
        FOREIGN KEY(project_id)
        REFERENCES project(project_id)
        ON DELETE CASCADE -- If a project is deleted, delete all its associated issues
);

-- Optional: Add indexes for performance, especially on foreign keys and frequently queried columns
CREATE INDEX idx_issue_project_id ON issue(project_id);
CREATE INDEX idx_issue_status ON issue(status);
CREATE INDEX idx_issue_priority ON issue(priority);

-- Insert sample data for projects
-- Note: The actual project_id values will be 1, 2, 3 if inserted sequentially on a fresh DB
INSERT INTO project (name, description) VALUES
('LangGraph4J Core', 'Core library development for LangGraph4J framework.'),
('Project Phoenix UI', 'Frontend development for the new Phoenix platform.'),
('Data Migration Initiative', 'Migrating legacy data to the new cloud infrastructure.');

-- Insert sample issues for Project 1 (LangGraph4J Core) - Assuming project_id = 1
INSERT INTO issue (project_id, title, description, status, priority) VALUES
(1, 'Implement StateGraph Checkpointing', 'Need to add functionality to save and restore graph state.', 'In Progress', 'High'),
(1, 'Refactor Agent Executor Node', 'Improve the logic and error handling in the main agent executor.', 'Open', 'Medium'),
(1, 'Add Support for Anthropic Models', 'Integrate Claude 3 models via the official API.', 'Open', 'Medium'),
(1, 'Improve Documentation for Prebuilt Tools', 'Expand examples and explanations for calculator, search, etc.', 'Open', 'Low'),
(1, 'Write Unit Tests for Graph Validation', 'Ensure graph structures are validated correctly before execution.', 'In Progress', 'High'),
(1, 'Fix Bug in Conditional Edge Routing', 'Edges sometimes route incorrectly based on state.', 'Open', 'Critical'),
(1, 'Explore Async Generator Support', 'Investigate using async generators for streaming output.', 'Open', 'Medium'),
(1, 'Optimize Memory Usage for Large Graphs', 'Reduce memory footprint when dealing with complex graphs.', 'Open', 'Medium'),
(1, 'Add Example: Multi-Agent Collaboration', 'Create a how-to guide for building collaborating agents.', 'Open', 'Low'),
(1, 'Release Version 1.0.0', 'Prepare and tag the first major release.', 'Open', 'High');

-- Insert sample issues for Project 2 (Project Phoenix UI) - Assuming project_id = 2
INSERT INTO issue (project_id, title, description, status, priority) VALUES
(2, 'Design Login Page Mockups', 'Create high-fidelity mockups for the user login screen.', 'Resolved', 'High'),
(2, 'Implement User Authentication Flow', 'Connect frontend login form to the backend auth service.', 'In Progress', 'Critical'),
(2, 'Develop Dashboard Widget Component', 'Create a reusable component for displaying key metrics.', 'In Progress', 'High'),
(2, 'Set up React Router Navigation', 'Implement client-side routing for different sections.', 'Open', 'Medium'),
(2, 'Integrate Charting Library', 'Add a library like Chart.js or D3 for data visualization.', 'Open', 'Medium'),
(2, 'Fix CSS Alignment on Settings Page', 'Elements are misaligned on smaller screen sizes.', 'Open', 'Low'),
(2, 'Implement User Profile Editing', 'Allow users to update their name, email, and password.', 'Open', 'Medium'),
(2, 'Write E2E Tests for Login', 'Use Cypress or Playwright to test the login functionality.', 'Open', 'Medium'),
(2, 'Optimize Bundle Size', 'Reduce the size of the JavaScript bundle for faster loading.', 'Open', 'Low'),
(2, 'Accessibility Audit (WCAG)', 'Ensure the UI meets WCAG 2.1 AA standards.', 'Open', 'High'),
(2, 'Create Reusable Button Component', 'Standardize button styles across the application.', 'Resolved', 'Low');

-- Insert sample issues for Project 3 (Data Migration Initiative) - Assuming project_id = 3
INSERT INTO issue (project_id, title, description, status, priority) VALUES
(3, 'Analyze Legacy Database Schema', 'Document the structure and relationships in the old DB.', 'Resolved', 'High'),
(3, 'Develop ETL Script for Users Table', 'Extract, transform, and load user data.', 'In Progress', 'Critical'),
(3, 'Develop ETL Script for Orders Table', 'Extract, transform, and load order data, handling dependencies.', 'In Progress', 'Critical'),
(3, 'Set up Cloud Database Instance', 'Provision and configure the target PostgreSQL instance in the cloud.', 'Resolved', 'High'),
(3, 'Define Data Validation Rules', 'Establish rules to ensure data integrity after migration.', 'Open', 'Medium'),
(3, 'Perform Test Migration Run 1', 'Execute the migration scripts in a staging environment.', 'Open', 'High'),
(3, 'Analyze Performance of ETL Scripts', 'Identify bottlenecks and optimize script performance.', 'Open', 'Medium'),
(3, 'Handle Data Cleansing for Addresses', 'Standardize and clean up address data during transformation.', 'Open', 'Medium'),
(3, 'Plan Production Cutover Strategy', 'Define the steps and timeline for the final migration.', 'Open', 'High'),
(3, 'Document Migration Process', 'Create comprehensive documentation for the entire process.', 'Open', 'Low');
