DROP TABLE IF EXISTS CartItem;

CREATE TABLE IF NOT EXISTS CartItem (
   userId BIGINT NOT NULL,
   productId BIGINT NOT NULL,
   quantity INT NOT NULL,
   CONSTRAINT PK_CartItem PRIMARY KEY (userId, productId)
);
