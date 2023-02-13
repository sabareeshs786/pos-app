package com.increff.posapp.dto;

import com.increff.posapp.model.PosDaySalesData;
import com.increff.posapp.pojo.OrderPojo;
import com.increff.posapp.pojo.PosDaySalesPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.OrderItemService;
import com.increff.posapp.service.OrderService;
import com.increff.posapp.service.PosDaySalesService;
import com.increff.posapp.util.Converter;
import com.increff.posapp.util.DateTimeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class PosDaySalesDto {

	private static final Logger logger = Logger.getLogger(PosDaySalesDto.class);
	@Autowired
	private PosDaySalesService posDaySalesService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;

	@Scheduled(fixedRate = 60000*60*24)
	public void updateScheduler() throws ApiException {
		LocalDateTime localDateTime = LocalDateTime.now();
		ZoneId zoneId = ZoneId.of("Asia/Kolkata");
		ZonedDateTime date = ZonedDateTime.of(localDateTime, zoneId);

		if(posDaySalesService.getAll().size() == 0){
			logger.info("Nothing in pos_day_sales");
			List<OrderPojo> orderPojoList = orderService.getAll();
			List<LocalDate> localDateList = orderPojoList.stream().map(PosDaySalesDto::toLocalDate).collect(Collectors.toList());
			Set<LocalDate> localDateSet = new TreeSet<>(localDateList);
			if(orderPojoList.size() > 0){
				logger.info("Orders greater than zero");
				for(LocalDate localDate: localDateSet){
					PosDaySalesPojo p = new PosDaySalesPojo();
					p.setZonedDateTime(DateTimeUtil.getZonedDateTimeStart(localDate, "Asia/Kolkata"));
					List<OrderPojo> orderPojos = orderService.getByInterval(DateTimeUtil.getZonedDateTimeStart(localDate, "Asia/Kolkata"), DateTimeUtil.getZonedDateTimeEnd(localDate, "Asia/Kolkata"));
					p.setInvoicedOrdersCount(orderPojos.size());
					p.setInvoicedItemsCount(0);
					p.setTotalRevenue(0.0);
					for(OrderPojo pojo: orderPojos) {
						p.setInvoicedItemsCount((int) (p.getInvoicedItemsCount() + orderItemService.getTotalInvoicedQuantity(pojo.getId())));
						p.setTotalRevenue(p.getTotalRevenue() + orderItemService.getTotalCost(pojo.getId()));
					}
					posDaySalesService.add(p);
				}
			}
		}
		else {
			logger.info("Pos Day Sales tables has some elements");
			ZonedDateTime lastDateTime = posDaySalesService.getLastDateTime().plusSeconds(1);
			ZonedDateTime zonedDateTimeNow = ZonedDateTime.now();
			List<OrderPojo> orderPojoList = orderService.getByInterval(lastDateTime, zonedDateTimeNow);
			PosDaySalesPojo pojo = new PosDaySalesPojo();
			pojo.setZonedDateTime(zonedDateTimeNow);
			pojo.setInvoicedOrdersCount(orderPojoList.size());
			pojo.setInvoicedItemsCount(0);
			pojo.setTotalRevenue(0.0);
			for(OrderPojo p: orderPojoList){
				pojo.setInvoicedItemsCount((int) (pojo.getInvoicedItemsCount() + orderItemService.getTotalInvoicedQuantity(p.getId())));
				pojo.setTotalRevenue(pojo.getTotalRevenue() + orderItemService.getTotalCost(p.getId()));
			}
			posDaySalesService.add(pojo);
			logger.info("Scheduler updated");
		}
	}

	public List<PosDaySalesData> getAll(){
		logger.info("PosDaySalesData started executing");
		List<PosDaySalesData> posDaySalesDataList = new ArrayList<>();
		List<PosDaySalesPojo> pojos = posDaySalesService.getAll();
		logger.info("Received all pojos: "+pojos.toString());
		for(PosDaySalesPojo p: pojos){
			posDaySalesDataList.add(Converter.convertToSchedulerData(p));
		}
		logger.info("Data: "+ posDaySalesDataList.toString());
		logger.info("Returning data");
		return posDaySalesDataList;
	}

//	public Page<PosDaySalesData> getAll(Integer page, Integer size){
//		Page<PosDaySalesPojo> posDaySalesPojoPage = posDaySalesService.getAllByPage(page, size);
//		List<PosDaySalesData> schedulerDataList = new ArrayList<>();
//		List<PosDaySalesPojo> pojos = posDaySalesPojoPage.getContent();
//		for(PosDaySalesPojo p: pojos){
//			schedulerDataList.add(Converter.convertToSchedulerData(p));
//		}
//		return new PageImpl<>(schedulerDataList, PageRequest.of(page, size), posDaySalesPojoPage.getTotalElements());
//	}
	private static LocalDate toLocalDate(OrderPojo orderPojo){
		return orderPojo.getTime().toLocalDate();
	}

//	public Page<PosDaySalesData> getData(LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size){
//		if(page == null && size == null){
//
//		}
//		if(startDate != null && endDate!= null)
//	}
}
