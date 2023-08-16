--------------------------------------------------------
--  File created - Wednesday-August-16-2023   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table RESPONSES
--------------------------------------------------------

  CREATE TABLE "USSD"."RESPONSES" 
   (	"MENU" VARCHAR2(20 BYTE), 
	"RES_STR" VARCHAR2(160 BYTE), 
	"TYPE" VARCHAR2(10 BYTE) DEFAULT 'static'
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
REM INSERTING into USSD.RESPONSES
SET DEFINE OFF;
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('18','Choose an option:__1. Forgot PIN__2. Change PIN','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('181','Please call 16167 and verify your account to reset PIN.','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('182','Enter old PIN:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('182X','Enter new PIN:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('182XX','Confirm new PIN:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('182XXX','/changepin','forward');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('1','Please select a service__1. Cash Out__2. Send Money__3. Mobile Recharge__4. Payment__5. Bill Pay__6. EMI Payment__7. Account Information__8. PIN Reset','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('15','Enter a Biller ID:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('15X','Enter bill amount:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('15XX','Enter PIN:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('15XXX','/transact','forward');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('12','Enter Recipient:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('12X','Enter amount to be sent:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('12XX','Enter reference:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('12XXX','Enter PIN:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('12XXXX','/transact','forward');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('13','Select an operator__1. Teletalk__2. Airtel__3. Grameenphone__4. Robi__5. Banglalink','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('13X','Select SIM type__1. Prepaid__2. Postpaid','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('13XX','Enter mobile number:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('13XXX','Enter amount:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('13XXXX','Enter PIN:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('13XXXXX','/recharge','forward');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('11','Enter an Uddokta A/C:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('11X','Enter amount:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('11XX','Enter PIN:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('11XXX','/transact','forward');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('14','Enter a merchant A/C:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('14X','Enter payment amount:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('14XX','Enter reference:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('14XXX','Enter PIN:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('14XXXX','/transact','forward');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('16','Enter EMI Recipient A/C number:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('16XX','Enter reference:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('16XXX','Enter PIN:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('16XXXX','/transact','forward');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('16X','Enter EMI payment amount:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('17','Choose a Service:__1. Balance Enquiry__2. Mini Statement__3. Helpline','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('173','Please call 16167 or visit www.something.com.bd for more information.','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('171','Enter PIN:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('172','Enter PIN:','static');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('171X','/balance','forward');
Insert into USSD.RESPONSES (MENU,RES_STR,TYPE) values ('172X','/statement','forward');
