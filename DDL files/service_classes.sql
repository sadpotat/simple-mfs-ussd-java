--------------------------------------------------------
--  File created - Wednesday-August-16-2023   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table SERVICE_CLASSES
--------------------------------------------------------

  CREATE TABLE "USSD"."SERVICE_CLASSES" 
   (	"SERVICE_ID" VARCHAR2(20 BYTE), 
	"CLASS" VARCHAR2(40 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
REM INSERTING into USSD.SERVICE_CLASSES
SET DEFINE OFF;
Insert into USSD.SERVICE_CLASSES (SERVICE_ID,CLASS) values ('trns_cout','Services.CashOut');
Insert into USSD.SERVICE_CLASSES (SERVICE_ID,CLASS) values ('trns_send','Services.SendMoney');
Insert into USSD.SERVICE_CLASSES (SERVICE_ID,CLASS) values ('trns_recharge','Services.MobileRecharge');
Insert into USSD.SERVICE_CLASSES (SERVICE_ID,CLASS) values ('trns_pay','Services.Payment');
Insert into USSD.SERVICE_CLASSES (SERVICE_ID,CLASS) values ('trns_bill','Services.BillPay');
Insert into USSD.SERVICE_CLASSES (SERVICE_ID,CLASS) values ('trns_emi','Services.EMIPayment');
Insert into USSD.SERVICE_CLASSES (SERVICE_ID,CLASS) values ('info_balance','Services.SendBalance');
Insert into USSD.SERVICE_CLASSES (SERVICE_ID,CLASS) values ('info_statement','Services.SendStatement');
Insert into USSD.SERVICE_CLASSES (SERVICE_ID,CLASS) values ('pin_change','Services.PINChange');
