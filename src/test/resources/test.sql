SELECT * FROM message;
select *,count(id) as cnt from 
(select * from message order by created_date desc) tt
group by conversation_id order by created_date desc
limit 0,3;

-- #这是message中消息显示的sql语句，很复杂有木有，注意学习，
-- 下面开始写messageDao