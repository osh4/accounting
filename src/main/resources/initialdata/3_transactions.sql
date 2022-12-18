INSERT INTO transaction_types (pk, f_name, f_description)
VALUES (1, 'income', ''),
       (2, 'outcome', '');

INSERT INTO transactions (pk, f_date, f_currency, f_source_account, f_target_account, f_amount, f_type)
VALUES (1, '2021-11-01', 2, 1, 2, 1.0, 1),
       (2, '2021-11-02', 2, 2, 1, -1.0, 2);
