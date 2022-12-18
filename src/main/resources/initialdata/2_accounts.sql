INSERT INTO account_types (pk, f_name, f_description)
VALUES (1, 'card', ''),
       (2, 'cash', ''),
       (3, 'creditCard', '');

INSERT INTO accounts (pk, f_name, f_account_type, f_currency, f_money_limit)
VALUES (1, 'MainCard', 2, 1, 1),
       (2, 'Wallet', 2, 2, 1),
       (3, 'SecondaryCard', 2, 3, 1);
