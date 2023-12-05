set schema 'public';

DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts
(
    id          varchar(64) PRIMARY KEY,
    name        varchar(64),
    description varchar(64),
    currency_id varchar(64),
    user_id     varchar(64)
);
DROP TABLE IF EXISTS currencies;

CREATE TABLE currencies
(
    id       varchar(200) PRIMARY KEY,
    iso_code varchar(255),
    name     varchar(255)
);

DROP TABLE IF EXISTS rates;

CREATE TABLE rates
(
    id        varchar(200) PRIMARY KEY,
    rate_date date,
    rate      decimal(10, 2),
    source_id varchar(200),
    target_id varchar(200)
);

DROP TABLE IF EXISTS settings;

CREATE TABLE settings
(
    key             varchar(200) PRIMARY KEY,
    setting_type_id varchar(200),
    value           varchar(200)
);

DROP TABLE IF EXISTS setting_types;

CREATE TABLE setting_types
(
    id         varchar(64) PRIMARY KEY,
    name       varchar(64),
    class_name varchar(200)
);

DROP TABLE IF EXISTS transactions;

CREATE TABLE transactions
(
    id                  varchar(200) PRIMARY KEY,
    transaction_date    date,
    amount              decimal(10, 2),
    description         varchar(200),
    transaction_type_id varchar(200),
    source_account_id   varchar(200),
    target_account_id   varchar(200)
);

DROP TABLE IF EXISTS transactions_types;

CREATE TABLE transactions_types
(
    id          varchar(200) PRIMARY KEY,
    name        varchar(200),
    description varchar(200)
);

DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id            varchar(200) PRIMARY KEY,
    email         varchar(200),
    name          varchar(200),
    password      varchar(200),
    roles         varchar(64)[],
    created_date  date,
    modified_date date
);

