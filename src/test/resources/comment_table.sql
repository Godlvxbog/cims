
DROP TABLE IF EXISTS comment;
CREATE TABLE `wenda2`.`comment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` TEXT NOT NULL,
  `user_id` INT NOT NULL,
  `entity_type` INT NOT NULL,
  `entity_id` INT NOT NULL,
  `created_date` DATETIME NOT NULL,
  `status` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `user_index` (`user_id` ASC),
  INDEX `entity_index` (`entity_id` ASC, `entity_type` ASC));

