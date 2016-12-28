SELECT distinct user_id FROM `order_cards` WHERE order_status = 1


select * from users where id not in (SELECT distinct user_id FROM `order_cards` WHERE order_status = 1
) and rest_money > 0


select mobile as 手机号, rest_money as 余额, from_unixtime(add_time) as 注册时间,
from_unixtime(update_time) as 最后更新时间 from users where id not in (SELECT distinct user_id FROM `order_cards` WHERE order_status = 1 ) and rest_money > 0



//10.20之后充值的用户
select * from user_id where id in (
select distinct user_id from order_cards where add_time >= 1476892800 and order_status = 1)

//找出10.29之后充值的用户，之前有充值记录的
select * from order_cards where order_status = 1 and add_time < 1476892800  and user_id in (select distinct user_id from order_cards where add_time >= 1476892800 and order_status = 1) 
