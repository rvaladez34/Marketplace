package com.intuit.cg.backendtechassessment.repository;

public class SqlQueries {

    //Create Schema `Marketplace`
    public static String CREATE_MARKETPLACE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS `Marketplace`";
    public static String USE_MARKETPLACE = "USE `Marketplace`";

    //Create Table `Marketplace`.`Project`
    public static String CREATE_PROJECT_TABLE = "CREATE TABLE IF NOT EXISTS `Marketplace`.`Project` (`ProjectId` INT NOT NULL AUTO_INCREMENT,`seller_name` VARCHAR(255) NULL,`seller_email` VARCHAR(255) NULL,`max_budget` DECIMAL(19,4) NULL,`closing_date` DATE NULL,`description` VARCHAR(255) NULL,PRIMARY KEY (`ProjectId`))";

    //Create Table `Marketplace`.`Bid`
    public static String CREATE_BID_TABLE = "CREATE TABLE IF NOT EXISTS `Marketplace`.`Bid` (`BidId` INT NOT NULL AUTO_INCREMENT,`buyer_name` VARCHAR(255) NULL,`buyer_email` VARCHAR(255) NULL,`bid_amount` DECIMAL(19,4) NULL,`auto_bid` DECIMAL(19,4) NULL,`ProjectId` INT NOT NULL,PRIMARY KEY (`BidId`, `ProjectId`),CONSTRAINT `ProjectId` FOREIGN KEY (`ProjectId`) REFERENCES `Marketplace`.`Project` (`ProjectId`) ON DELETE NO ACTION ON UPDATE NO ACTION)";

    public static String SELLER_NAME = "seller_name";
    public static String SELLER_EMAIL = "seller_email";
    public static String MAX_BUDGET = "max_budget";
    public static String CLOSING_DATE = "closing_date";
    public static String DESCRIPTION = "description";

    public static String INSERT_PROJECT = "INSERT INTO Project (" + String.join(",", SELLER_NAME, SELLER_EMAIL, MAX_BUDGET, CLOSING_DATE, DESCRIPTION) + ") ";
    public static String PROJECT_VALUES = "VALUES ('" + String.join("','", SELLER_NAME, SELLER_EMAIL, MAX_BUDGET, CLOSING_DATE, DESCRIPTION)+"')";
    public static String GET_PROJECT = "SELECT * FROM Project";


    public static String PROJECT_ID = "ProjectId";
    public static String BUYER_NAME = "buyer_name";
    public static String BUYER_EMAIL = "buyer_email";
    public static String BID_AMOUNT = "bid_amount";
    public static String AUTO_BID = "auto_bid";

    public static String INSERT_BID = "INSERT INTO Bid (" + String.join(",", PROJECT_ID, BUYER_NAME, BUYER_EMAIL, BID_AMOUNT, AUTO_BID)+") ";
    public static String BID_VALUES = "VALUES ('" + String.join("','", PROJECT_ID, BUYER_NAME, BUYER_EMAIL, BID_AMOUNT)+"',"+AUTO_BID+")";
    public static String GET_BIDS = "SELECT * FROM Bid WHERE ProjectId=" + PROJECT_ID + " ORDER BY " + BID_AMOUNT + " ASC";

}