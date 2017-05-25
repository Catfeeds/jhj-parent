
//计算欠款
SELECT * FROM `org_staffs` WHERE name = '孙红英'

还款总金额  = 834.00
SELECT sum(order_pay) FROM `org_staff_detail_pay` WHERE staff_id = 204 and order_type = 4

订单现金支付总金额 = 714.00 

select sum(order_pay) from order_prices where order_id in (select order_id from order_dispatchs where staff_id = 204 and dispatch_status = 1) and pay_type = 6

订单加时总金额 = 45

select sum(order_pay) from order_price_ext where  order_id in (select order_id from order_dispatchs where staff_id = 204 and dispatch_status = 1) and order_ext_type = 1