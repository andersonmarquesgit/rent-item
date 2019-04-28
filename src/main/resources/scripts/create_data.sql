INSERT INTO tb_item_type(id, description) VALUES ('1', 'Tipo de Item 01');
INSERT INTO tb_item_type(id, description) VALUES ('2', 'Tipo de Item 02');
INSERT INTO tb_item_type(id, description) VALUES ('3', 'Tipo de Item 03');

INSERT INTO tb_item(id, description, price, item_type_id) VALUES ('1', 'Item Qualquer 1', 150, '1');
INSERT INTO tb_item(id, description, price, item_type_id) VALUES ('2', 'Item Qualquer 2', 320, '1');

INSERT INTO tb_customer(id, name, phone, email) VALUES ('1', 'Maria Guedes', '011988120987', 'mguedes@gmail.com');
INSERT INTO tb_customer(id, name, phone, email) VALUES ('2', 'Felipe Melo', '011985678394', 'felipem@gmail.com');