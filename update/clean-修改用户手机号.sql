
原来手机号
select * from users where mobile = '15810770179'

id = 6374

现在手机号

select * from users where mobile = '18600959566'
id = 4118


WHERE user_id in (4118, 6374)


//更新的数据

update user_coupons set user_id = 4118 where user_id = 6374;

update user_detail_pay set user_id = 4118, mobile = '18600959566' where user_id = 6374;

update user_pay_status set user_id = 4118, mobile = '18600959566' where user_id = 6374;

update orders set user_id = 4118, mobile = '18600959566', addr_id = 4627 where user_id = 6374;

update order_cards set user_id = 4118, mobile = '18600959566' where user_id = 6374;

update order_dispatchs set user_id = 4118, mobile = '18600959566' where user_id = 6374;

update order_dispatch_prices set user_id = 4118, mobile = '18600959566', addr_id = 4627 where user_id = 6374;

update order_log set user_id = 4118, mobile = '18600959566' where mobile = '15810770179';

update order_prices set user_id = 4118, mobile = '18600959566' where mobile = '15810770179';

update order_service_addons set user_id = 4118 where user_id = 6374;

update users set rest_money = 0, is_vip = 0 where user_id = 6374;

update users set rest_money = 680, is_vip = 1 where user_id = 4118;


