DELETE FROM orders;
DELETE FROM order_status;
DELETE FROM rating;
DELETE FROM products;
DELETE FROM cities;
DELETE FROM users;
DELETE FROM roles;
DELETE FROM user_states;

ALTER SEQUENCE orders_order_id_seq RESTART WITH 1;
ALTER SEQUENCE products_product_id_seq RESTART WITH 1;
ALTER SEQUENCE users_user_id_seq RESTART WITH 1;

INSERT INTO roles(id, name) VALUES (0, 'ADMIN');
INSERT INTO roles(id, name) VALUES (1, 'USER');

INSERT INTO user_states(id, name) VALUES (0, 'CONFIRMED');
INSERT INTO user_states(id, name) VALUES (1, 'NOT_CONFIRMED');
INSERT INTO user_states(id, name) VALUES (2, 'BANNED');
INSERT INTO user_states(id, name) VALUES (3, 'DELETED');

INSERT INTO order_status (id, name) VALUES (0, 'OPEN');
INSERT INTO order_status (id, name) VALUES (1, 'SHIPPED');
INSERT INTO order_status (id, name) VALUES (2, 'COMPLETED');
INSERT INTO order_status (id, name) VALUES (3, 'CANCELLED');

--adminer
INSERT INTO users(email, password, username, role, state, reg_date) VALUES ('admin@mrkt.com', '$2a$10$ochHQYL3ZyNoh/eXr.JoveYVklpqFMoBaHgOszqi0Ai8CKRhclNLe', 'Admin', 0, 0, '2017-01-17 17:33:56.509000');
--foxyassword
INSERT INTO users(email, password, username, role, state, reg_date, user_rating, rate_count, contacts) VALUES ('MaxergGom@gmail.com',
                                                                                                           '$2a$10$jaz/SoW1r88RNAP2zxmKKevHHidLyOMwG4grS5r6nUqxePEwG8pba', 'МаксерShop', 1, 0, '2017-10-24 17:22:52.9804000', 3.235, 53, '+79352061863');
--nyanyaga
INSERT INTO users(email, password, username, role, state, reg_date, user_rating, rate_count, contacts) VALUES ('alizzka@gmail.com',
                                                                                                           '$2a$10$TQaY3LgOHaQhZ9aA6J4HU.SFcrRbYobI33vWMzlgACnOvhMAInbOW', 'Элис', 1, 0, '2017-06-17 17:33:56.509000', 5, 1, '+79306193613');
--itsbusinessok
INSERT INTO users(email, password, username, role, state, reg_date, user_rating, rate_count, contacts) VALUES ('lamiro@gmail.com',
                                                                                                           '$2a$10$VYFKDy/iqe/NjJm7zbaqg.hzX/gsJL2H96ui4VEyj5DFbk48CE2gm', 'Lamiro', 1, 0, '2017-10-21 17:38:41.462000', 4.6, 10, '+79239610638');
--matilda
INSERT INTO users(email, password, username, role, state, reg_date, user_rating, rate_count, contacts) VALUES ('Nikolar@gmail.com',
                                                                                                           '$2a$10$wOo3xd2lHch386LuBcoErurKx8Os8CRdonvTw3dcoDDe.bQ3GkQH.', 'Николай', 1, 0, '2017-10-03 17:26:52.9804000', 3.235, 53, '+9693610629');
--windowsphone
INSERT INTO users(email, password, username, role, state, reg_date, user_rating, rate_count, contacts) VALUES ('andrewyan@gmail.com',
                                                                                                           '$2a$10$74jn2NEC3eioOz9xKmjuhOHFwEAmLchY85KkEHi5ocDGvPEBINwjy', 'Григорий Ревизов', 1, 0, '2017-05-25 17:31:53.885000', 0.0, 0, '+79106139301');
INSERT INTO cities(id, name) VALUES (1, 'Moscow');
INSERT INTO cities(id, name) VALUES (2, 'Saint-Petersburg');

INSERT INTO products (name, description, image_url, type, city, price, date_created, seller_id, available)
VALUES ('Iphone X', 'Brand new phone from Apple. Now even more expensive!', NULL , 1, 1, 0.2755, '2017-10-22 15:25:15.000000', 3, true);
INSERT INTO products (name, description, image_url, type, city, price, date_created, seller_id, available)
VALUES ('AUDI Q7', 'Luxury auto to anticipate your every need.', NULL , 4, 1, 4.13, '2017-10-25 13:27:12.000000', 2, true);
INSERT INTO products (name, description, image_url, type, city, price, date_created, seller_id, available)
VALUES ('Fidget spinner', 'Not only for autists.', NULL , 2, 2, 0.0008249, '2017-10-24 16:34:51.000000', 2, true);
INSERT INTO products (name, description, image_url, type, city, price, date_created, seller_id, available)
VALUES ('Halloween mask', 'Make your friends shit with bricks!', NULL , 2, 2, 0.0002749, '2017-10-23 02:00:05.000000', 4, true);
INSERT INTO products (name, description, image_url, type, city, price, date_created, seller_id, available)
VALUES ('Flat in Moscow', 'Euroremont.', NULL , 5, 1, 55, '2017-10-25 21:05:36.000000', 6, true);

INSERT INTO orders (product_id, buyer_id, seller_id, status, date_start, date_end)
VALUES (1, 4, 3, 3, '2017-10-27 17:36:36.000000', '2017-10-28 15:39:12.000000');
INSERT INTO orders (product_id, buyer_id, seller_id, status, date_start, date_end)
VALUES (4, 5, 4, 2, '2017-10-27 07:34:04.000000', '2017-10-29 05:23:46.000000');
INSERT INTO orders (product_id, buyer_id, seller_id, status, date_start, date_end)
VALUES (3, 2, 2, 0, '2017-10-27 11:19:12.000000', NULL);
INSERT INTO orders (product_id, buyer_id, seller_id, status, date_start, date_end)
VALUES (5, 4, 6, 0, '2017-10-27 14:05:57.000000', NULL);
INSERT INTO orders (product_id, buyer_id, seller_id, status, date_start, date_end)
VALUES (2, 3, 2, 0, '2017-10-27 20:01:45.000000', NULL);

INSERT INTO wallets(address, balance, our) VALUES ('2NB4JdwTRicrTN67G2oPXCehwPpxjNvfNDM', 0, TRUE);
INSERT INTO wallets(address, balance, our) VALUES ('2Muq5DXg49XvTCAwTJvAFAT3xays1TXejh8', 0, TRUE);
INSERT INTO wallets(address, balance, our) VALUES ('2MwpQucpRBHFzgYdnP8hr3FFYjiqnjRquij', 0, TRUE);
INSERT INTO wallets(address, balance, our) VALUES ('2MxUgXpbpMPm6x6dE7gUxTdZv9zYAK5jL7t', 0, TRUE);
INSERT INTO wallets(address, balance, our) VALUES ('2N4Ryfqoo6B8Bw6GUCoVrbQNMBZ9TpEv89m', 0, TRUE);