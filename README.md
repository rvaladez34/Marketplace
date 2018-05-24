# backend-tech-assessment

Skeleton project for Backend Technical Assessment.

Includes
--------
How to run: mvn spring-boot:run

API Documentation
--------

Please find all API requirements in the link below

https://documenter.getpostman.com/view/137574/intuit-projects-and-bids/RW86MANY

If link does not work the documentation is in /resource/API_Documentation.postman_collection.json

Assignment Review
--------

Exercise Difficulty
Answer: Moderate

How did you feel about the exercise itself?
Answer: 8

How do you feel about coding an exercise as a step in the interview process?
Answer: 8

What would you change in the exercise and/or process?
Answer: If you want to see more coding samples and less design work, then more of the application framework should be given


Database tables
------------

Creating two simple tables to hold data for Projects and Bids with one to many relationship.

-- -----------------------------------------------------
-- Table `Marketplace`.`Project`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Marketplace`.`Project` (
  `ProjectId` INT NOT NULL AUTO_INCREMENT,
  `seller_name` VARCHAR(255) NULL,
  `seller_email` VARCHAR(255) NULL,
  `max_budget` DECIMAL(19,4) NULL,
  `closing_date` DATE NULL,
  `description` VARCHAR(255) NULL,
  PRIMARY KEY (`ProjectId`));

-- -----------------------------------------------------
-- Table `Marketplace`.`Bid`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Marketplace`.`Bid` (
  `BidId` INT NOT NULL AUTO_INCREMENT,
  `buyer_name` VARCHAR(255) NULL,
  `buyer_email` VARCHAR(255) NULL,
  `bid_amount` DECIMAL(19,4) NULL,
  `auto_bid` DECIMAL(19,4) NULL,
  `ProjectId` INT NOT NULL,
  PRIMARY KEY (`BidId`, `ProjectId`),
  CONSTRAINT `ProjectId`
    FOREIGN KEY (`ProjectId`)
    REFERENCES `Marketplace`.`Project` (`ProjectId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)