create table if not exists user_states
(
  id   integer not null
    constraint states_id_pk
    primary key,
  name varchar(15)
);

create unique index if not exists states_id_uindex
  on user_states (id);


create table if not exists cities
(
  id   bigserial   not null
    constraint cities_pkey
    primary key,
  name varchar(20) not null
);

create unique index if not exists cities_name_uindex
  on cities (name);

create unique index if not exists cities_id_uindex
  on cities (id);

create table if not exists order_status
(
  id   integer     not null
    constraint order_status_id_pk
    primary key,
  name varchar(15) not null
);

create unique index if not exists order_status_id_uindex
  on order_status (id);

create unique index if not exists order_status_name_uindex
  on order_status (name);

create table if not exists roles
(
  id   integer not null
    constraint roles_id_pk
    primary key,
  name varchar(10)
);

create unique index if not exists roles_id_uindex
  on roles (id);

create table if not exists products
(
  id           bigserial not null
    constraint products_pkey
    primary key,
  name         varchar(255),
  description  varchar(255),
  date_created timestamp,
  available    boolean,
  seller       bigint
    constraint products_users_user_id_fk
    references users,
  city         integer
    constraint products_cities_id_fk
    references cities,
  price        numeric(15, 10),
  image        varchar(255)
);

create table if not exists orders
(
  product_id integer
    constraint orders_product_id_fk
    references products,
  buyer_id   integer
    constraint orders_buyer_fk
    references users,
  status     integer
    constraint orders_order_status_id_fk
    references order_status,
  price      double precision
    constraint price_not_negative
    check (price >= ((0) :: numeric) :: double precision),
  id         serial not null
    constraint order_order_id_pk
    primary key,
  date_start timestamp,
  date_end   timestamp,
  seller_id  bigint,
  date_ship  timestamp,
  constraint end_after_start
  check (date_end >= date_start)
);

create unique index if not exists order_order_id_uindex
  on orders (id);

create unique index if not exists order_order_id_uindex
  on orders (id);

create table if not exists wallets
(
  id      integer      not null
    constraint wallets_pkey
    primary key,
  address varchar(255) not null,
  balance numeric(15, 10),
  our     boolean      not null
);

create table if not exists transaction_states
(
  id   smallserial not null
    constraint transaction_states_pkey
    primary key,
  name varchar(15)
);

create unique index if not exists transaction_states_id_uindex
  on transaction_states (id);

INSERT INTO transaction_states (id, name) SELECT 0, 'Sent'
  WHERE NOT EXISTS(SELECT id FROM transaction_states WHERE id = 0);

INSERT INTO transaction_states (id, name) SELECT 1, 'Received'
  WHERE NOT EXISTS(SELECT id FROM transaction_states WHERE id = 1);

INSERT INTO transaction_states (id, name) SELECT 2, 'Sent failed'
  WHERE NOT EXISTS(SELECT id FROM transaction_states WHERE id = 2);

INSERT INTO transaction_states (id, name) SELECT 3, 'Receive failed'
  WHERE NOT EXISTS(SELECT id FROM transaction_states WHERE id = 3);

create table if not exists roles
(
  id   integer not null
    constraint roles_id_pk
    primary key,
  name varchar(10)
);

INSERT INTO roles (id, name) SELECT 0, 'ADMIN'
  WHERE NOT EXISTS(SELECT id FROM roles WHERE id = 0);

INSERT INTO roles (id, name) SELECT 1, 'USER'
                             WHERE NOT EXISTS(SELECT id FROM roles WHERE id = 1);

create unique index if not exists roles_id_uindex
  on roles (id);

create table if not exists user_states
(
  id   integer not null
    constraint states_id_pk
    primary key,
  name varchar(15)
);

create unique index if not exists states_id_uindex
  on user_states (id);

create table if not exists order_status
(
  id   integer     not null
    constraint order_status_id_pk
    primary key,
  name varchar(15) not null
);

create unique index if not exists order_status_id_uindex
  on order_status (id);

create unique index if not exists order_status_name_uindex
  on order_status (name);

create table if not exists cities
(
  id   bigserial   not null
    constraint cities_pkey
    primary key,
  name varchar(20) not null
);

create unique index if not exists cities_name_uindex
  on cities (name);

create unique index if not exists cities_id_uindex
  on cities (id);

create table if not exists files
(
  id                 bigserial not null
    constraint files_pkey
    primary key,
  original_file_name varchar(255),
  size               bigint,
  storage_file_name  varchar(255),
  type               varchar(255),
  url                varchar(255)
);

create table if not exists wallets
(
  id      integer      not null
    constraint wallets_pkey
    primary key,
  address varchar(255) not null,
  balance numeric(15, 10),
  our     boolean      not null
);

create table if not exists users
(
  id         bigserial    not null
    constraint users_pkey
    primary key,
  email      varchar(25)  not null
    constraint uk_r43af9ap4edm43mmtq01oddj6
    unique,
  password   varchar(255) not null,
  username   varchar(255)
    constraint uk_3g1j96g94xpk3lpxl2qbl985x
    unique,
  role       integer
    constraint users_roles_id_fk
    references roles,
  reg_date   timestamp,
  state      integer         default 1
    constraint users_states_id_fk
    references user_states,
  email_link varchar(36),
  contacts   varchar(100),
  balance    numeric(15, 10) default 0,
  wallet_id  bigint
    constraint users_wallets_id_fk
    references wallets
);

create table if not exists products
(
  id           bigserial not null
    constraint products_pkey
    primary key,
  name         varchar(255),
  description  varchar(255),
  date_created timestamp,
  available    boolean,
  seller_id       bigint
    constraint products_users_user_id_fk
    references users,
  city         integer
    constraint products_cities_id_fk
    references cities,
  price        numeric(15, 10),
  image        varchar(255)
);

create table if not exists orders
(
  product    integer
    constraint fk2exrvqyqnr3x6gwmh4wid01xf
    references products
    constraint orders_product_id_fk
    references products,
  buyer      integer
    constraint fkljvc97l19m7cnlopv8535hijx
    references users
    constraint orders_buyer_fk
    references users,
  status     integer
    constraint orders_order_status_id_fk
    references order_status,
  price      double precision
    constraint price_not_negative
    check (price >= ((0) :: numeric) :: double precision),
  id         serial not null
    constraint order_order_id_pk
    primary key,
  date_start timestamp,
  date_end   timestamp,
  seller     bigint
    constraint fkdmy44tbeq18goy7qq2cy9wut4
    references users,
  date_ship  timestamp,
  constraint end_after_start
  check (date_end >= date_start)
);

create unique index if not exists order_order_id_uindex
  on orders (id);

create table if not exists rating
(
  id       bigint not null
    constraint rating_pkey
    primary key,
  mark     smallint,
  text     varchar(255),
  receiver bigint
    constraint fkh4jtuk2i03xqiucfal6nrqgmb
    references users,
  sender   bigint
    constraint fkjly61wyx6jt4vcw1a4of4wtxu
    references users
);

create table if not exists transaction_states
(
  id   smallserial not null
    constraint transaction_states_pkey
    primary key,
  name varchar(15)
);

create unique index if not exists transaction_states_id_uindex
  on transaction_states (id);

INSERT INTO transaction_states (id, name)
  SELECT
    0,
    'SENT'
  WHERE NOT EXISTS(SELECT id
                   FROM transaction_states
                   WHERE id = 0);

INSERT INTO transaction_states (id, name)
  SELECT
    1,
    'RECEIVED'
  WHERE NOT EXISTS(SELECT id
                   FROM transaction_states
                   WHERE id = 1);

INSERT INTO transaction_states (id, name)
  SELECT
    2,
    'SENT_FAILED'
  WHERE NOT EXISTS(SELECT id
                   FROM transaction_states
                   WHERE id = 2);

INSERT INTO transaction_states (id, name)
  SELECT
    3,
    'RECEIVE_FAILED'
  WHERE NOT EXISTS(SELECT id
                   FROM transaction_states
                   WHERE id = 3);

create table if not exists transactions
(
  id          bigint          not null
    constraint transactions_pkey
    primary key,
  amount      numeric(15, 10) not null,
  date        timestamp       not null,
  fee         numeric(15, 10),
  state       integer
    constraint transactions_transaction_states_id_fk
    references transaction_states
    on update cascade,
  txid        varchar(255),
  user_id     bigint
    constraint transactions_users_id_fk
    references users,
  from_wallet integer
    constraint transactions_wallets_id_fk
    references wallets,
  to_wallet   integer
    constraint transactions_wallets_id_fk_2
    references wallets
);

CREATE TABLE IF NOT EXISTS persistent_logins (
  username  varchar(64) not null,
  series    varchar(64) not null,
  token     varchar(64) not null,
  last_used timestamp   not null,
  PRIMARY KEY (series)
);

create table if not exists money_history
(
  id          serial  not null,
  sender_id   integer not null
    constraint money_history_users_id_fk
    references users
    on update cascade,
  receiver_id integer
    constraint money_history_users_id_fk_2
    references users
    on update cascade,
  amount      integer not null
);
