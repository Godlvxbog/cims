DROP TABLE IF EXISTS `user`;
CREATE TABLE `wenda2`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `password` VARCHAR(256) NOT NULL DEFAULT '',
  `salt` VARCHAR(45) NOT NULL DEFAULT '',
  `head_url` VARCHAR(256) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),

  UNIQUE INDEX `name_UNIQUE` (`name` ASC));

DROP TABLE IF EXISTS question;
CREATE TABLE `wenda2`.`question` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(256) NOT NULL,
  `content` TEXT NULL,
  `user_id` INT NOT NULL,
  `create_date` DATETIME NOT NULL,
  `comment_count` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `create_date` (`create_date` ASC));


DROP TABLE IF EXISTS login_ticket;
CREATE TABLE `wenda2`.`login_ticket` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `ticket` VARCHAR(128) NOT NULL,
  `expired` DATETIME NOT NULL,
  `status` INT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `ticket` (`ticket` ASC));
