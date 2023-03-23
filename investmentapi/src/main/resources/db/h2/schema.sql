DROP TABLE product_investing IF EXISTS;


CREATE TABLE product_investing (
  product_id INTEGER AS IDENTITY PRIMARY KEY,
  accumulated_investing_amount INTEGER(11),
  investing_user_count  INTEGER(11)
);
CREATE INDEX user_count ON product_investing (investing_user_count);