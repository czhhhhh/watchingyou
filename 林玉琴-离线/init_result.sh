#!/bin/bash
# -*- coding: utf-8 -*-

yesterday=$(date -d "1 days ago" +%Y-%m-%d)
echo ${yesterday}


hdfs dfs -rm -r /home/hive/result


hive -e"




use bankwarning;





CREATE EXTERNAL TABLE IF NOT EXISTS atm_nativedata(
logType string,
UUID string,
transactionType string,
nodeTimestamp string,
nodeNameAndProvince map<string, string>,
transactionDirectionAndGender map<string, string>,
age map<string, int>, 
amount map<string, int>)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\054'
MAP KEYS TERMINATED BY '\075'
LOCATION '/home/hive/ATM_NativeData';

CREATE EXTERNAL TABLE IF NOT EXISTS fx_nativedata(
logType string,
UUID string,
transactionType string,
nodeTimestamp string,
nodeNameAndProvince map<string, string>,
transactionDirectionAndCurrency map<string, string>,
purpose map<string, string>,
amount map<string, int>,
age map<string, int>)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\054'
MAP KEYS TERMINATED BY '\075'
LOCATION '/home/hive/FX_NativeData';





LOAD DATA INPATH '/flume/2021-08-02/ATM/' OVERWRITE into table atm_nativedata;

LOAD DATA INPATH '/flume/2021-08-02/FX/' OVERWRITE into table fx_nativedata;





CREATE EXTERNAL TABLE IF NOT EXISTS atm_temporarydata(
logType string,
UUID string,
transactionType string,
nodeTimestamp string,
nodeNameAndProvince map<string, string>,
transactionDirectionAndGender map<string, string>,
age map<string, int>, 
amount map<string, int>)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\054'
MAP KEYS TERMINATED BY '\075'
LOCATION '/home/hive/ATM_TemporaryData';

CREATE EXTERNAL TABLE IF NOT EXISTS fx_temporarydata(
logType string,
UUID string,
transactionType string,
nodeTimestamp string,
nodeNameAndProvince map<string, string>,
transactionDirectionAndCurrency map<string, string>,
purpose map<string, string>,
amount map<string, int>,
age map<string, int>)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\054'
MAP KEYS TERMINATED BY '\075'
LOCATION '/home/hive/FX_TemporaryData';







insert overwrite table atm_temporarydata
select * from atm_nativedata
where atm_nativedata.uuid 
in (select uuid from atm_nativedata group by uuid having count(uuid) = 7);

insert overwrite table fx_temporarydata
select * from fx_nativedata
where fx_nativedata.uuid 
in (select uuid from fx_nativedata group by uuid having count(uuid) = 9);





CREATE EXTERNAL TABLE IF NOT EXISTS atm_partitiondata(
logType string,
UUID string,
transactionType string,
nodeTimestamp string,
nodeNameAndProvince map<string, string>,
transactionDirectionAndGender map<string, string>,
age map<string, int>, 
amount map<string, int>)
PARTITIONED BY (logtypes string)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\054'
MAP KEYS TERMINATED BY '\075'
LOCATION '/home/hive/ATM_PartitionData';

CREATE EXTERNAL TABLE IF NOT EXISTS fx_partitiondata(
logType string,
UUID string,
transactionType string,
nodeTimestamp string,
nodeNameAndProvince map<string, string>,
transactionDirectionAndCurrency map<string, string>,
purpose map<string, string>,
amount map<string, int>,
age map<string, int>)
PARTITIONED BY (logtypes string)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\054'
MAP KEYS TERMINATED BY '\075'
LOCATION '/home/hive/FX_PartitionData';





insert overwrite table atm_partitiondata partition(logtypes)
select tmpa.*, tmpa.logtype 
from atm_temporarydata tmpa;

insert overwrite table fx_partitiondata partition(logtypes)
select tmpt.*, tmpt.logtype 
from fx_temporarydata tmpt;





drop table result;





CREATE EXTERNAL TABLE IF NOT EXISTS result(
agg_name string, 
agg_date string, 
volume int, 
transaction_type string, 
agg_attr string,
count int)
PARTITIONED BY (parms string)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\054'
LOCATION '/home/hive/result';





from atm_partitiondata
insert overwrite table result partition(parms = 'ATMFXamount0')
select 'amountRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'ATM001' as transaction_type, 
'0-100' as agg_attr,
COUNT(amount['金额']) as count
where amount['金额'] between 0 and 100 
and logtypes = 'BusinesslogATM'

insert overwrite table result partition(parms = 'ATMFXamount101')
select 'amountRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'ATM001' as transaction_type, 
'101-500' as agg_attr,
COUNT(amount['金额']) as count
where amount['金额'] between 101 and 500 
and logtypes = 'BusinesslogATM'

insert overwrite table result partition(parms = 'ATMFXamount501')
select 'amountRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'ATM001' as transaction_type, 
'501-1000' as agg_attr,
COUNT(amount['金额']) as count
where amount['金额'] between 501 and 1000 
and logtypes = 'BusinesslogATM'

insert overwrite table result partition(parms = 'ATMFXamount1001')
select 'amountRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'ATM001' as transaction_type, 
'1001-10000' as agg_attr,
COUNT(amount['金额']) as count
where amount['金额'] between 1001 and 10000 
and logtypes = 'BusinesslogATM'

insert overwrite table result partition(parms = 'ATMFXamount10001')
select 'amountRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'ATM001' as transaction_type, 
'10001' as agg_attr,
COUNT(amount['金额']) as count
where amount['金额'] > 10000 
and logtypes = 'BusinesslogATM'

insert overwrite table result partition(parms = 'ATMFXprovince')
select 'location' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'ATM001' as transaction_type, 
nodeNameAndProvince['省份'] as agg_attr,
COUNT(amount['金额']) as count
where nodeNameAndProvince['省份'] like concat('%', '省') 
or nodeNameAndProvince['省份'] like concat('%', '市') 
and logtypes = 'BusinesslogATM'
group by nodeNameAndProvince;



from fx_partitiondata
insert into table result partition(parms = 'ATMFXamount0')
select 'amountRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'FX001' as transaction_type, 
'0-100' as agg_attr,
COUNT(amount['金额']) as count
where amount['金额'] between 0 and 100 
and logtypes = 'BusinesslogFX'

insert into table result partition(parms = 'ATMFXamount101')
select 'amountRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'FX001' as transaction_type, 
'101-500' as agg_attr,
COUNT(amount['金额']) as count
where amount['金额'] between 101 and 500 
and logtypes = 'BusinesslogFX'

insert into table result partition(parms = 'ATMFXamount501')
select 'amountRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'FX001' as transaction_type, 
'501-1000' as agg_attr,
COUNT(amount['金额']) as count
where amount['金额'] between 501 and 1000 
and logtypes = 'BusinesslogFX'

insert into table result partition(parms = 'ATMFXamount1001')
select 'amountRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'FX001' as transaction_type, 
'1001-10000' as agg_attr,
COUNT(amount['金额']) as count
where amount['金额'] between 1001 and 10000 
and logtypes = 'BusinesslogFX'

insert into table result partition(parms = 'ATMFXamount10001')
select 'amountRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'FX001' as transaction_type, 
'10001' as agg_attr,
COUNT(amount['金额']) as count
where amount['金额'] > 10000 
and logtypes = 'BusinesslogFX'

insert into table result partition(parms = 'ATMFXprovince')
select 'location' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'FX001' as transaction_type, 
nodeNameAndProvince['省份'] as agg_attr,
COUNT(amount['金额']) as count
where nodeNameAndProvince['省份'] like concat('%', '省') 
or nodeNameAndProvince['省份'] like concat('%', '市') 
and logtypes = 'BusinesslogFX'
group by nodeNameAndProvince

insert overwrite table result partition(parms = 'FXcurrency')
select 'currency' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'FX001' as transaction_type, 
transactionDirectionAndCurrency['货币种类'] as agg_attr,
COUNT(amount['金额']) as count
where transactionDirectionAndCurrency['货币种类'] like concat('%', '元') 
or transactionDirectionAndCurrency['货币种类'] like concat('%', '币') 
or transactionDirectionAndCurrency['货币种类'] like concat('%', '镑') 
and logtypes = 'BusinesslogFX'
group by transactionDirectionAndCurrency

insert overwrite table result partition(parms = 'FXpurpose')
select 'purpose' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'FX001' as transaction_type, 
purpose['换汇目的'] as agg_attr,
COUNT(amount['金额']) as count
where purpose['换汇目的'] = '其他'
or purpose['换汇目的'] = '就医' 
or purpose['换汇目的'] = '教育' 
or purpose['换汇目的'] = '旅游' 
or purpose['换汇目的'] = '留学' 
and logtypes = 'BusinesslogFX'
group by purpose

insert overwrite table result partition(parms = 'FXage18')
select 'ageRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'FX001' as transaction_type, 
'18-25' as agg_attr,
COUNT(amount['金额']) as count
where age['年龄'] between 18 and 25
and logtypes = 'BusinesslogFX'

insert overwrite table result partition(parms = 'FXage26')
select 'ageRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'FX001' as transaction_type, 
'26-35' as agg_attr,
COUNT(amount['金额']) as count
where age['年龄'] between 26 and 35
and logtypes = 'BusinesslogFX'

insert overwrite table result partition(parms = 'FXage36')
select 'ageRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'FX001' as transaction_type, 
'36-45' as agg_attr,
COUNT(amount['金额']) as count
where age['年龄'] between 36 and 45
and logtypes = 'BusinesslogFX'

insert overwrite table result partition(parms = 'FXage46')
select 'ageRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'FX001' as transaction_type, 
'46-60' as agg_attr,
COUNT(amount['金额']) as count
where age['年龄'] between 46 and 60
and logtypes = 'BusinesslogFX'

insert overwrite table result partition(parms = 'FXage61')
select 'ageRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'FX001' as transaction_type, 
'61-80' as agg_attr,
COUNT(amount['金额']) as count
where age['年龄'] between 61 and 80
and logtypes = 'BusinesslogFX'

insert overwrite table result partition(parms = 'FXage81')
select 'ageRange' as agg_name, 
'${yesterday}' as agg_date,
SUM(amount['金额']) as volume, 
'FX001' as transaction_type, 
'81' as agg_attr,
COUNT(amount['金额']) as count
where age['年龄'] > 80
and logtypes = 'BusinesslogFX';





insert overwrite table result partition(parms = 'NULL')
select agg_name,
agg_date,
0 as volume,
transaction_type,
agg_attr,
count as count
from result
where volume is null;





select * from result;
"