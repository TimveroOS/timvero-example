-- v8.2 migration: PaymentTransaction.orderId is now a column; trace becomes TEXT.
-- Adds the column nullable first, backfills BORROWER rows from credit_id, then enforces NOT NULL.

alter table if exists payment_transaction
    add column if not exists order_id varchar(255);

update payment_transaction
set order_id = credit_id::text
where order_id is null and credit_id is not null;

update payment_transaction
set order_id = id::text
where order_id is null;

alter table if exists payment_transaction
    alter column order_id set not null;

alter table if exists payment_transaction
    alter column trace type text using trace::text;

alter table if exists aud_payment_transaction
    add column if not exists order_id varchar(255);

alter table if exists aud_payment_transaction
    alter column trace type text using trace::text;
