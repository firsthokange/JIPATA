SELECT MAX(LOG_TIME), MIN(LOG_TIME),
(MAX(LOG_TIME)-MIN(LOG_TIME))*24*60*60
FROM CMES_APP_LOG
WHERE 
(LOG_LINE LIKE '%begin controller http://cmes.crownproperty.or.th/cpbesb/general/create_book.htm%'
OR
LOG_LINE LIKE '%end controller http://cmes.crownproperty.or.th/cpbesb/general/create_book.htm%')
AND LOG_TIME BETWEEN 
--TO_DATE('01-11-2016 00:00:01', 'dd-mm-yyyy hh24:mi:ss')
TO_DATE('30-12-2014 00:00:01', 'dd-mm-yyyy hh24:mi:ss')
AND
--TO_DATE('01-11-2016 23:59:59', 'dd-mm-yyyy hh24:mi:ss')
TO_DATE('30-12-2014 23:59:59', 'dd-mm-yyyy hh24:mi:ss')
GROUP BY THREAD_ID_ROUND