--------------------------------------------------------
--  File created - Wednesday-August-16-2023   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table PROVIDER
--------------------------------------------------------

  CREATE TABLE "USSD"."PROVIDER" 
   (	"NAME" VARCHAR2(30 BYTE), 
	"MENU" NUMBER(1,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
REM INSERTING into USSD.PROVIDER
SET DEFINE OFF;
Insert into USSD.PROVIDER (NAME,MENU) values ('TELETALK',1);
Insert into USSD.PROVIDER (NAME,MENU) values ('AIRTEL',2);
Insert into USSD.PROVIDER (NAME,MENU) values ('ROBI',3);
Insert into USSD.PROVIDER (NAME,MENU) values ('BANGLALINK',4);
Insert into USSD.PROVIDER (NAME,MENU) values ('GRAMEENPHONE',5);
--------------------------------------------------------
--  Constraints for Table PROVIDER
--------------------------------------------------------

  ALTER TABLE "USSD"."PROVIDER" MODIFY ("NAME" NOT NULL ENABLE);
