
//计算欠款
SELECT * FROM `org_staffs` WHERE name = '孙红英'

还款总金额  = 834.00
SELECT sum(order_pay) FROM `org_staff_detail_pay` WHERE staff_id = 204 and order_type = 4

订单现金支付总金额 = 714.00 

select sum(order_pay) from order_prices where order_id in (select order_id from order_dispatchs where staff_id = 204 and dispatch_status = 1) and pay_type = 6

订单加时总金额 = 45

select sum(order_pay) from order_price_ext where  order_id in (select order_id from order_dispatchs where staff_id = 204 and dispatch_status = 1) and order_ext_type = 1


//查询派工常用.
select order_id as '订单号', staff_id as '员工ID', staff_name as '员工姓名', 
 from_unixtime(service_date) as '服务开始时间', 
 service_hours as '服务时长(小时)',
 from_unixtime(service_date + 3600 * service_hours) as '服务结束时间',
 from_unixtime(service_date + 3600 * service_hours + 3600 * 1.5) as '服务延后时间'

 from order_dispatchs where 1=1 


                        and staff_id in
                         (  
                                11
                         , 
                                24
                         , 
                                50
                         , 
                                53
                         , 
                                73
                         , 
                                88
                         , 
                                100
                         , 
                                121
                         , 
                                128
                         , 
                                129
                         , 
                                136
                         , 
                                141
                         , 
                                142
                         , 
                                145
                         , 
                                147
                         , 
                                152
                         , 
                                169
                         , 
                                178
                         , 
                                199
                         , 
                                207
                         , 
                                215
                         , 
                                220
                         , 
                                221
                         , 
                                222
                         , 
                                223
                         , 
                                226
                         , 
                                228
                         ) 
                 

                 
                        and dispatch_status = 1
                 
                 
                             
            and (
                (service_date >= 1498005000  and service_date <= 1498021200 )
                or
                ((service_date+ service_hours*3600 + 1.5 * 3600) >= 1498005000 and (service_date+ service_hours*3600 + 1.5 * 3600) <= 1498021200)
            )