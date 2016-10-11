
DROP TABLE IF EXISTS message;
CREATE TABLE `wenda2`.`message` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `from_id` INT NULL,
  `to_id` INT NULL,
  `conversation_id` VARCHAR(64) NOT NULL,
  `content` VARCHAR(256) NULL,
  `created_date` DATETIME NOT NULL,
  `has_read` INT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `conversation_index` (`conversation_id` ASC),
  INDEX `created_date` (`created_date` ASC));
