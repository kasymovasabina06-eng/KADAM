-- RUN IN SSMS USING WINDOWS AUTHENTICATION, as an admin of the local instance.
-- Creates a table only if it does not already exist. Does not remove data.
USE java_platform;
GO
IF OBJECT_ID(N'dbo.app_users', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.app_users (
        id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        full_name NVARCHAR(120) NOT NULL,
        email NVARCHAR(190) NOT NULL UNIQUE,
        password_hash NVARCHAR(255) NOT NULL,
        role NVARCHAR(40) NOT NULL,
        city NVARCHAR(100) NULL,
        specialization NVARCHAR(160) NULL
    );
END;
GO
-- Grant the local application account only rights needed for this stage.
IF USER_ID(N'java_user') IS NULL
    CREATE USER java_user FOR LOGIN java_user;
GO
ALTER ROLE db_datareader ADD MEMBER java_user;
ALTER ROLE db_datawriter ADD MEMBER java_user;
GO
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'app_users';
GO
