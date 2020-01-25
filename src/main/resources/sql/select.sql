select ac.*, SUM(tr.AMOUNT)
from ACCOUNTS ac
    inner join TRANSFERS tr on ac.ID = tr.SOURCE_ID
where tr.TRANSFER_TIME >= '2019-01-01'
group by ac.ID
having SUM(tr.AMOUNT) > 1000;