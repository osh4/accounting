set schema 'public';

INSERT INTO setting_types(id, name, class_name)
VALUES (1, 'String', 'java.lang.String'),
       (2, 'Integer', 'java.lang.Integer'),
       (3, 'Long', 'java.lang.Long');

INSERT INTO settings(key, setting_type_id, value)
VALUES ('default.page.size', 1, '3'),
       ('default.setting.type', 1, '1'),
       ('default.transaction.category', 1, '1'),
       ('default.target.account', 1, '3')
;

INSERT INTO transactions_types(id, name, description)
VALUES ('1', 'Приход', ''),
       ('2', 'Расход', '');

INSERT INTO transactions_categories(id, name, description, color)
VALUES ('1', 'Аренда', 'Аренда жилья', ''),
       ('2', 'Связь', '', ''),
       ('3', 'К/у Ереван', '', ''),
       ('4', 'К/у СПб', '', ''),
       ('5', 'Ипотека', '', ''),
       ('6', 'Сервисы', '', ''),
       ('7', 'Медицина', '', ''),
       ('8', 'Услуги', '', ''),
       ('9', 'Спорт', '', ''),
       ('10', 'Автомобиль', '', ''),
       ('11', 'Путешествия', '', ''),
       ('12', 'Онлайн покупки', '', ''),
       ('13', 'Прочее', '', '');

INSERT INTO currencies(id, iso_code, name, long_name)
VALUES ('1', '840', 'USD', 'United States Dollar'),
       ('2', '051', 'AMD', 'Armenian Drams'),
       ('3', '643', 'RUB', 'Russian Rubles'),
       ('4', '978', 'EUR', 'Euro');

-- Passwords: user - 123, admin - nimda
INSERT INTO users(id, email, name, password, roles, created_date, modified_date, enabled)
VALUES ('1', 'user@test.com', 'Test User', '{bcrypt}$2a$10$cNI1L04yzK5VbGpd4IvszekDGwwb9NOZ7b/hz6JxYgjXUgdX16Tx6', '{"USER"}', NOW(), NOW(), true),
       ('2', 'admin@test.com', 'Test Admin', '{bcrypt}$2a$10$d9L0E0mCNDcILQ6b/3gAfuEJvFypXM9Jm7ocF632QHMdlJ8XT0Tqq', '{"ADMIN"}', NOW(), NOW(), true);

INSERT INTO accounts(id, name, description, currency_id, user_id, amount)
VALUES ('1', 'Cash AMD', '', '2', '1', '1000'),
       ('2', 'Cash AMD income', '', '2', null, '0'),
       ('3', 'Cash AMD outcome', '', '2', null, '0')
;

insert into transactions (id, transaction_date, amount, description, transaction_type_id, transaction_category_id,
                          source_account_id, target_account_id)
values ('1', '01-11-2024', 20, 'Salary income AMD', '1', '13', '2', '1'),
       ('2', '01-11-2024', -10, 'Pay with cash AMD', '2', '13', '1', '3'),
       ('3', '01-11-2024', -5, 'Pay with cash AMD', '2', '13', '1', '3')
;