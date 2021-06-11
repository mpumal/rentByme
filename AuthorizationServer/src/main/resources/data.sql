INSERT INTO oauth_client_details (client_id, client_secret, web_server_redirect_uri, scope, access_token_validity,
refresh_token_validity, resource_ids, authorized_grant_types, additional_information) VALUES
('oriental', '{bcrypt}$2y$12$4LLQ2RV1jfHKLSJPlQjokO4nsilrAN1QkmxflUZMJfddzCNQQW3tC', 'http://localhost:8080/code',
'READ,WRITE', '3600', '10000', 'inventory,payment', 'authorization_code,password,refresh_token,implicit', '{}');

 INSERT INTO permission (name) VALUES
 ('create_profile'),
 ('read_profile'),
 ('update_profile'),
 ('delete_profile'),
 ('add_cars'),
 ('update_cars'),
 ('delete_cars'),
 ('read_cars'),
 ('create_booking');

 INSERT INTO role (name) VALUES
 ('ROLE_ADMIN'),('ROLE_USER');

 INSERT INTO permission_role (permission_id, role_id) VALUES
     (1, 1),  /* create -> ROLE_ADMIN */
     (2, 1),  /* read -> ROLE_ADMIN */
     (3, 1),  /* update -> ROLE_ADMIN */
     (4, 1),  /* delete -> ROLE_ADMIN */
     (1, 2),  /* create -> ROLE_USER */
     (2, 2),  /* read -> ROLE_USER */
     (3, 2),  /* update -> ROLE_USER */
     (4, 2),  /* delete -> ROLE_USER */
     (5, 1),  /* add -> ROLE_ADMIN */
     (6, 1),  /* update -> ROLE_ADMIN */
     (7, 1),  /* delete -> ROLE_ADMIN */
     (8, 1),  /* read -> ROLE_ADMIN */
     (8, 2),  /* read -> ROLE_USER */
     (9, 2);  /* create -> ROLE_USER */

/*INSERT INTO user (id, username, password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES
('1', 'manoj','{bcrypt}$2a$10$YmBtJn2wYMkAdg0yYV2KTODWExj.CCD/evbP7qghRqUk7JZqlLa0K', 'oriental@no.com', '1', '1', '1', '1');
INSERT INTO user (id, username, password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES
('2', 'manoj', '{bcrypt}$2a$10$LM7x8rrtZepYCA0koJtacekt0vrndvLMrfB3yCkbFFn6oZTKsi8Li','manoj@no.com', '1', '1', '1', '1');*/

/*INSERT INTO ROLE_USER (ROLE_ID, USER_ID) VALUES*/
/*(1, 1),*/  /* oriental - ADMIN */
/*(2, 2);*/  /* manoj - USER */
