package com.increff.posapp.model;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class SchedulerData{
	private ZonedDateTime date;
	private Integer invoicedOrdersCount;
	private Integer invoicedItemsCount;
	private Double totalRevenue;
}
