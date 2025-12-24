-- Review table
CREATE TABLE tb_reviews (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    user_id CHAR(36) NOT NULL,
    order_id CHAR(36) NOT NULL,
    product_id CHAR(36) NOT NULL,
    score DECIMAL(3, 1) NOT NULL,
    content TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES tb_users(id),
    FOREIGN KEY (order_id) REFERENCES tb_orders(id)
);

CREATE INDEX idx_reviews_user ON tb_reviews(user_id);
CREATE INDEX idx_reviews_product ON tb_reviews(product_id);
