-- =================================================================================================
-- Account
-- =================================================================================================
INSERT INTO account(username, email, activate_date, is_password_changed, last_login_date, login_fail_count, password, password_change_date, register_date, restrict_code, role_code) VALUES ('admin', 'tukbbae@gmail.com', '2015-05-24', '1', '2015-05-24', '0', '$2a$10$1.HbRzNUe.DwOlonbXLwyO8ETQdlYX7WRGfNo.DxmJVZ8bwlWNn4S', '2015-05-24', '2015-05-24', 'ASC002', 'SYSADMIN');

-- =================================================================================================
-- User
-- =================================================================================================
INSERT INTO user(email, name, token, device_type, updated_date) VALUES ('user1@gmail.com', '유저1', 'e2S9zTlQL6Y:APA91bHVjB-nsRZ7-npxeCYDbedX15fdXsTQEPCqz313tT2SXB-N8XwKRMSkVbndCah9EOZaB6dD0GuyfDRsyLSWbo0p1YXEkyt4FHvwRZV6B2DjWZ2kFhvSDvArUde9aeg-kjFHW2HD', 'A', '2016-08-09');


