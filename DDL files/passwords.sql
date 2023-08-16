--------------------------------------------------------
--  File created - Wednesday-August-16-2023   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table PASSWORDS
--------------------------------------------------------

  CREATE TABLE "USSD"."PASSWORDS" 
   (	"CUS_ID" NUMBER(3,0), 
	"PASSWORD" NUMBER(7,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
REM INSERTING into USSD.PASSWORDS
SET DEFINE OFF;
Insert into USSD.PASSWORDS (CUS_ID,PASSWORD) values (111,1570016);
Insert into USSD.PASSWORDS (CUS_ID,PASSWORD) values (112,1631584);
--------------------------------------------------------
--  Constraints for Table PASSWORDS
--------------------------------------------------------

  ALTER TABLE "USSD"."PASSWORDS" MODIFY ("CUS_ID" NOT NULL ENABLE);
  ALTER TABLE "USSD"."PASSWORDS" MODIFY ("PASSWORD" NOT NULL ENABLE);
