-- Таблица продавцов
CREATE TABLE sellers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_info VARCHAR(255) NOT NULL,
    registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Таблица транзакций
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
    payment_type VARCHAR(20) NOT NULL CHECK (payment_type IN ('CASH', 'CARD', 'TRANSFER')),
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transaction_seller
        FOREIGN KEY (seller_id)
        REFERENCES sellers(id)
        ON DELETE CASCADE
);

-- Индексы
CREATE INDEX idx_sellers_registration_date ON sellers(registration_date);
CREATE INDEX idx_transactions_seller_id ON transactions(seller_id);
CREATE INDEX idx_transactions_date ON transactions(transaction_date);
CREATE INDEX idx_transactions_payment_type ON transactions(payment_type);

-- Комментарии
COMMENT ON TABLE sellers IS 'Таблица продавцов CRM системы';
COMMENT ON TABLE transactions IS 'Таблица транзакций продавцов';
COMMENT ON COLUMN sellers.name IS 'Имя продавца';
COMMENT ON COLUMN sellers.contact_info IS 'Контактная информация продавца';
COMMENT ON COLUMN transactions.amount IS 'Сумма транзакции (больше 0)';
COMMENT ON COLUMN transactions.payment_type IS 'Тип оплаты: CASH, CARD, TRANSFER';