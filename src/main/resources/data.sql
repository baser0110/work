CREATE TYPE permission AS ENUM ('NO', 'VIEW', 'FULL');

-- Create Profile Table
CREATE TABLE profile (
                         id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
                         name VARCHAR(255) NOT NULL UNIQUE,
                         ext_alarm_mng permission NOT NULL DEFAULT 'NO',
                         cell_stat_mng_single permission NOT NULL DEFAULT 'NO',
                         cell_stat_mng_batch permission NOT NULL DEFAULT 'NO',
                         accept_measurement permission NOT NULL DEFAULT 'NO',
                         user_mng permission NOT NULL DEFAULT 'NO',
                         vasily_tools permission NOT NULL DEFAULT 'NO'
);

-- Create User Table
CREATE TABLE app_user (
                      id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
                      username VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                      is_first_login  BOOLEAN NOT NULL DEFAULT TRUE,
                      profile_id VARCHAR(255) NOT NULL,
                      CONSTRAINT fk_profile FOREIGN KEY (profile_id) REFERENCES profile(id)
);

ALTER TABLE app_user DROP CONSTRAINT fk_profile,
    ADD CONSTRAINT fk_profile FOREIGN KEY (profile_id) REFERENCES profile(id) ON DELETE CASCADE;

ALTER TABLE profile ALTER COLUMN accept_measurement TYPE VARCHAR;
ALTER TABLE profile ALTER COLUMN cell_stat_mng_single TYPE VARCHAR;
ALTER TABLE profile ALTER COLUMN cell_stat_mng_batch TYPE VARCHAR;
ALTER TABLE profile ALTER COLUMN ext_alarm_mng TYPE VARCHAR;
ALTER TABLE profile ALTER COLUMN user_mng TYPE VARCHAR;
ALTER TABLE profile ALTER COLUMN vasily_tools TYPE VARCHAR;
-- ALTER TABLE profile
--     ALTER COLUMN ext_alarm_mng TYPE permission USING ext_alarm_mng::permission,
--     ALTER COLUMN cell_stat_mng_single TYPE permission USING cell_stat_mng_single::permission,
--     ALTER COLUMN cell_stat_mng_batch TYPE permission USING cell_stat_mng_batch::permission,
--     ALTER COLUMN accept_measurement TYPE permission USING accept_measurement::permission,
--     ALTER COLUMN user_mng TYPE permission USING user_mng::permission,
--     ALTER COLUMN vasily_tools TYPE permission USING vasily_tools::permission;



-- Insert Profiles
INSERT INTO profile (id, name, ext_alarm_mng, cell_stat_mng_single, cell_stat_mng_batch, accept_measurement, user_mng, vasily_tools)
VALUES (gen_random_uuid(), 'bss_team', 'FULL', 'FULL', 'FULL', 'FULL', 'FULL', 'FULL');
--
-- INSERT INTO profile (id, name, ext_alarm_mng, cell_stat_mng_single, cell_stat_mng_batch, accept_measurement, user_mng, vasily_tools)
-- VALUES (gen_random_uuid(), 'Viewer', 'VIEW', 'VIEW', 'VIEW', 'VIEW', 'VIEW', 'VIEW');
--
-- INSERT INTO profile (id, name, ext_alarm_mng, cell_stat_mng_single, cell_stat_mng_batch, accept_measurement, user_mng, vasily_tools)
-- VALUES (gen_random_uuid(), 'BasicUser', 'NO', 'VIEW', 'NO', 'VIEW', 'NO', 'VIEW');

-- Insert Users
INSERT INTO app_user (id, username, password, is_deleted, profile_id)
VALUES (gen_random_uuid(), 'fist_admin', '123', false, (SELECT id FROM profile WHERE name = 'bss_team'));
--
-- INSERT INTO app_user (id, username, password, is_deleted, profile_id)
-- VALUES (gen_random_uuid(), 'viewer', 'password123', false, (SELECT id FROM profile WHERE name = 'Viewer'));
--
-- INSERT INTO app_user (id, username, password, is_deleted, profile_id)
-- VALUES (gen_random_uuid(), 'basicuser', 'password123', false, (SELECT id FROM profile WHERE name = 'BasicUser'));

-- ALTER TABLE app_user
--     ADD COLUMN is_first_login  BOOLEAN NOT NULL DEFAULT True;