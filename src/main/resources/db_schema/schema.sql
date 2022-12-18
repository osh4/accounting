/*Currencies*/
CREATE TABLE currencies
(
    pk           BIGINT
        CONSTRAINT currencies_pk PRIMARY KEY,
    f_name       VARCHAR(100) NOT NULL,
    f_isocode    VARCHAR(3)   NOT NULL,
    f_isocodenum DECIMAL(3, 0)
);

CREATE TABLE currency_rates
(
    pk       BIGINT
        CONSTRAINT currency_rates_pk PRIMARY KEY,
    f_source BIGINT         NOT NULL REFERENCES currencies (pk),
    f_target BIGINT         NOT NULL REFERENCES currencies (pk),
    f_date   DATE           NOT NULL,
    f_rate   DECIMAL(10, 2) NOT NULL
);

/*Accounts for money*/

CREATE TABLE account_types
(
    pk            BIGINT
        CONSTRAINT account_types_pk PRIMARY KEY,
    f_name        VARCHAR(100) NOT NULL,
    f_description TEXT
);

CREATE TABLE accounts
(
    pk             BIGINT
        CONSTRAINT accounts_pk PRIMARY KEY,
    f_name         VARCHAR(100) NOT NULL,
    f_account_type BIGINT       NOT NULL REFERENCES account_types (pk),
    f_currency     BIGINT       NOT NULL REFERENCES currencies (pk),
    f_user         BIGINT       NOT NULL REFERENCES users (pk),
    f_money_limit  DECIMAL(10, 2)
);

/*Transactions of money*/

CREATE TABLE transaction_types
(
    pk            BIGINT
        CONSTRAINT transaction_types_pk PRIMARY KEY,
    f_name        VARCHAR(100) NOT NULL,
    f_description TEXT
);

CREATE TABLE transactions
(
    pk               BIGINT
        CONSTRAINT transactions_pk PRIMARY KEY,
    f_date           DATE           NOT NULL,
    f_currency       BIGINT         NOT NULL REFERENCES currencies (pk),
    f_source_account BIGINT         NOT NULL REFERENCES accounts (pk),
    f_target_account BIGINT         NOT NULL REFERENCES accounts (pk),
    f_amount         DECIMAL(10, 2) NOT NULL,
    f_type           BIGINT         NOT NULL REFERENCES transaction_types (pk)
);

/*Planning*/

CREATE TABLE planning_periods
(
    pk            BIGINT
        CONSTRAINT planning_periods_pk PRIMARY KEY,
    f_name        VARCHAR(100) NOT NULL,
    f_description TEXT,
    f_date_part   INT          NOT NULL,
    f_count       INT          NOT NULL
);

CREATE TABLE financial_plans
(
    pk            BIGINT
        CONSTRAINT financial_plans_pk PRIMARY KEY,
    f_name        VARCHAR(100)   NOT NULL,
    f_first_day   DATE           NOT NULL,
    f_money_limit DECIMAL(10, 2) NOT NULL,
    f_period      BIGINT         NOT NULL REFERENCES planning_periods (pk),
    f_parent      BIGINT REFERENCES financial_plans (pk)
);

CREATE TABLE fp_entries
(
    pk            BIGINT
        CONSTRAINT fp_entries_pk PRIMARY KEY,
    f_plan        BIGINT         NOT NULL REFERENCES financial_plans (pk),
    f_name        VARCHAR(100)   NOT NULL,
    f_description TEXT,
    f_date        DATE           NOT NULL,
    f_amount      DECIMAL(10, 2) NOT NULL
);

CREATE TABLE settings
(
    pk      BIGINT
        CONSTRAINT settings_pk PRIMARY KEY,
    f_group VARCHAR(100) NOT NULL,
    f_key   VARCHAR(100) NOT NULL,
    f_type  VARCHAR(100) NOT NULL,
    f_value TEXT
);

/*Users*/
CREATE TABLE users
(
    pk          BIGINT
        CONSTRAINT users_pk PRIMARY KEY,
    f_login     VARCHAR(100) NOT NULL,
    f_firstname VARCHAR(100),
    f_lastname  VARCHAR(100),
    f_password  VARCHAR(250) NOT NULL
);

CREATE TABLE user_roles
(
    pk     BIGINT
        CONSTRAINT user_roles_pk PRIMARY KEY,
    f_name VARCHAR(100) NOT NULL

);

CREATE TABLE users_roles_rel
(
    pk     BIGINT
        CONSTRAINT users_roles_rel_pk PRIMARY KEY,
    f_user BIGINT NOT NULL REFERENCES users (pk),
    f_role BIGINT NOT NULL REFERENCES user_roles (pk)
);

