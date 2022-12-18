INSERT INTO planning_periods (pk, f_name, f_description, f_date_part, f_count)
VALUES (1, 'day', '', '', 30);

INSERT INTO financial_plans(pk, f_name, f_first_day, f_money_limit, f_period, f_parent)
VALUES (1, 'Monthly plan', 25, 200000, 1, NULL);

INSERT INTO fp_entries(pk, f_plan, f_name, f_description, f_date, f_amount)
VALUES (1, 1, 'Everyday sum', '', '2021-01-25', 200000);
