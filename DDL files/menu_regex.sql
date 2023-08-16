--------------------------------------------------------
--  File created - Wednesday-August-16-2023   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table MENU_REGEX
--------------------------------------------------------

  CREATE TABLE "USSD"."MENU_REGEX" 
   (	"MENU" VARCHAR2(20 BYTE), 
	"REGEX" VARCHAR2(20 BYTE), 
	"ERROR_MSG" VARCHAR2(50 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
REM INSERTING into USSD.MENU_REGEX
SET DEFINE OFF;
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('-1','.*','.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('1','[1-8]','Please enter a number between 1 and 8.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('11','[1-9]\d\d','Please enter a 3-digit A/C.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('11X','\d+(\.\d+)?','Please enter a valid numeric amount.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('11XX','\d{4}','Please enter a valid 4-digit PIN.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('12','[1-9]\d\d','Please enter a 3-digit A/C.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('12X','\d+(\.\d+)?','Please enter a valid numeric amount.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('12XX','.*','.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('12XXX','\d{4}','Please enter a valid 4-digit PIN.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('13','[1-5]','Please enter a number between 1 and 5.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('13X','[1-2]','Please enter a number between 1 and 2.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('13XX','01[1-9]\d{8}','Please enter a valid 11 digit mobile number.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('13XXX','\d+(\.\d+)?','Please enter a valid numeric amount.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('13XXXX','\d{4}','Please enter a valid 4-digit PIN.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('14','[1-9]\d\d','Please enter a 3-digit A/C.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('14X','\d+(\.\d+)?','Please enter a valid numeric amount.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('14XX','.*','.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('14XXX','\d{4}','Please enter a valid 4-digit PIN.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('15','[1-9]\d\d','Please enter a 3-digit A/C.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('15X','\d+(\.\d+)?','Please enter a valid numeric amount.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('15XX','\d{4}','Please enter a valid 4-digit PIN.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('16','[1-9]\d\d','Please enter a 3-digit A/C.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('16X','\d+(\.\d+)?','Please enter a valid numeric amount.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('16XX','.*','.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('16XXX','\d{4}','Please enter a valid 4-digit PIN.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('17','[1-3]','Please enter a number between 1 and 3.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('171','\d{4}','Please enter a valid 4-digit PIN.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('172','\d{4}','Please enter a valid 4-digit PIN.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('18','[1-2]','Please enter a number between 1 and 2.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('182','\d{4}','Please enter a valid 4-digit PIN.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('182X','\d{4}','Please enter a valid 4-digit PIN.');
Insert into USSD.MENU_REGEX (MENU,REGEX,ERROR_MSG) values ('182XX','\d{4}','Please enter a valid 4-digit PIN.');
