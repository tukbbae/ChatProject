-- =================================================================================================
-- Account
-- =================================================================================================
INSERT INTO account(username, email, activate_date, is_password_changed, last_login_date, login_fail_count, password, password_change_date, register_date, restrict_code, role_code) VALUES ('admin', 'indra818@gmail.com', '2015-05-24', '1', '2015-05-24', '0', '$2a$10$1.HbRzNUe.DwOlonbXLwyO8ETQdlYX7WRGfNo.DxmJVZ8bwlWNn4S', '2015-05-24', '2015-05-24', 'ASC002', 'SYSADMIN');

-- =================================================================================================
-- User
-- =================================================================================================
INSERT INTO user(email, name, token, device_type, updated_date) VALUES ('tukbbae@gmail.com', '이현민', 'eB3rSJz-l9E:APA91bGv305OcuKBWt279DGUGJ7QsMFAiIsuCf3UDdaH4jAtJzS_KVney35FUDgN26bXT-umtS9k3LEZglgETdKKWO4fDQ1D5NVCCVDvUdvGV49JEaeNecK0o8zFKzvBrwr974ZhEcRx', 'A', '2016-08-09');
INSERT INTO user(email, name, token, device_type, updated_date) VALUES ('user1@gmail.com', '유저1', 'e2S9zTlQL6Y:APA91bHVjB-nsRZ7-npxeCYDbedX15fdXsTQEPCqz313tT2SXB-N8XwKRMSkVbndCah9EOZaB6dD0GuyfDRsyLSWbo0p1YXEkyt4FHvwRZV6B2DjWZ2kFhvSDvArUde9aeg-kjFHW2HD', 'A', '2016-08-09');
INSERT INTO user(email, name, token, device_type, updated_date) VALUES ('user2@gmail.com', '유저2', 'e2S9zTlQL6Y:APA91bHVjB-nsRZ7-npxeCYDbedX15fdXsTQEPCqz313tT2SXB-N8XwKRMSkVbndCah9EOZaB6dD0GuyfDRsyLSWbo0p1YXEkyt4FHvwRZV6B2DjWZ2kFhvSDvArUde9aeg-kjFHW2HD', 'A', '2016-08-09');


