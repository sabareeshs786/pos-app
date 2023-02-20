package com.increff.posapp.dto;

import com.increff.posapp.model.PosDaySalesData;
import com.increff.posapp.model.PosDaySalesForm;
import com.increff.posapp.pojo.OrderPojo;
import com.increff.posapp.pojo.PosDaySalesPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.OrderItemService;
import com.increff.posapp.service.OrderService;
import com.increff.posapp.service.PosDaySalesService;
import com.increff.posapp.util.Converter;
import com.increff.posapp.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class PosDaySalesDto {
	@Autowired
	private PosDaySalesService posDaySalesService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;

	@Scheduled(cron = "0 0 0 * * ?")
	public void updatePosDaySalesTable() throws ApiException {
		LocalDateTime startDateTime = LocalDateTime.now()
				.minusDays(1L).withHour(0).withMinute(0).withSecond(0);
		ZoneId zoneId = ZoneId.of("Asia/Kolkata");
		ZonedDateTime startDateTimeZoned = ZonedDateTime.of(startDateTime, zoneId);

		LocalDateTime endDateTime = LocalDateTime.now()
				.minusDays(1L).withHour(23).withMinute(59).withSecond(59);
		ZonedDateTime endDateTimeZoned = ZonedDateTime.of(endDateTime, zoneId);

		List<OrderPojo> orderPojoList = orderService.getByInterval(startDateTimeZoned, endDateTimeZoned);

		PosDaySalesPojo pojo = new PosDaySalesPojo();
		pojo.setDate(startDateTimeZoned);
		pojo.setInvoicedOrdersCount(orderPojoList.size());
		pojo.setInvoicedItemsCount(0);
		pojo.setTotalRevenue(0.0);
		for(OrderPojo p: orderPojoList){
			pojo.setInvoicedItemsCount((int) (pojo.getInvoicedItemsCount() + orderItemService.getTotalInvoicedQuantity(p.getId())));
			pojo.setTotalRevenue(pojo.getTotalRevenue() + orderItemService.getTotalCost(p.getId()));
		}
		posDaySalesService.add(pojo);
	}

	public List<PosDaySalesData> getAll(){
		List<PosDaySalesData> posDaySalesDataList = new ArrayList<>();
		List<PosDaySalesPojo> pojos = posDaySalesService.getAll();
		for(PosDaySalesPojo p: pojos){
			posDaySalesDataList.add(Converter.convertToPosDaySalesData(p));
		}
		return posDaySalesDataList;
	}

	public List<PosDaySalesData> getData(PosDaySalesForm form) throws ApiException {
		validate(form);
		ZoneId zoneId = ZoneId.of("Asia/Kolkata");
		ZonedDateTime zonedDateTimeStart = ZonedDateTime.of(form.getStartDate(), zoneId);
		ZonedDateTime zonedDateTimeEnd = ZonedDateTime.of(form.getEndDate(), zoneId);

		List<PosDaySalesData> posDaySalesDataList = new ArrayList<>();
		List<PosDaySalesPojo> pojos = posDaySalesService.getByInterval(zonedDateTimeStart,
				zonedDateTimeEnd);
		for(PosDaySalesPojo p: pojos){
			posDaySalesDataList.add(Converter.convertToPosDaySalesData(p));
		}
		return posDaySalesDataList;
	}

	private void validate(PosDaySalesForm form) throws ApiException {
		if(form.getStartDate().toLocalDate().isBefore(LocalDate.now().minusYears(2L))){
			throw new ApiException("Enter a date within 2 years");
		}
		if(form.getEndDate().toLocalDate().isAfter(LocalDate.now())){
			throw new ApiException("End date should be today's date or a date before today");
		}
	}
}
