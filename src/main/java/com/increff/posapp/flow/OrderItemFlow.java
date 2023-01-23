package com.increff.posapp.flow;

import com.increff.posapp.pojo.OrderPojo;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class OrderItemFlow {
    public void update(Integer id){
        OrderPojo p = new OrderPojo();
        p.setTime(ZonedDateTime.now());
    }
}
