
INSERT INTO T_Account (account_id, account_number, balance)
VALUES (100, 1000, 5000);

INSERT INTO T_Operation (operation_id, amount, date, type)
VALUES (100, 1000, '2021-01-16', 'DEPOSIT'),
       (101, 400, '2021-02-01', 'WITHDRAWAL');

INSERT INTO T_Account_Operation_list (account_account_id, operation_list_operation_id)
VALUES (100, 100),
       (100, 101);