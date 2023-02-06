use miniautorizador;

DROP TABLE IF EXISTS card_transaction;

DROP TABLE IF EXISTS card;

CREATE TABLE card(
	id INT(10) NOT NULL AUTO_INCREMENT,
	number VARCHAR(16) NOT NULL,
	password VARCHAR(20) NOT NULL,
	balance DECIMAL(19,4) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE card_transaction(
	id INT(10) NOT NULL AUTO_INCREMENT,
	card_id INT NOT NULL,
	amount DECIMAL(19,4) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (card_id) REFERENCES card(id)
);
