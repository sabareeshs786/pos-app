package com.increff.posapp.dto;

import com.increff.posapp.model.SchedulerData;
import com.increff.posapp.pojo.OrderPojo;
import com.increff.posapp.pojo.PosDaySalesPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.OrderItemService;
import com.increff.posapp.service.OrderService;
import com.increff.posapp.service.SchedulerService;
import com.increff.posapp.util.Converter;
import com.increff.posapp.util.DateTimeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
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
public class SchedulerDto {

	private static final Logger logger = Logger.getLogger(SchedulerDto.class);
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;

	@Scheduled(fixedRate = 60000*60*24)
	public void updateScheduler() throws ApiException {
		LocalDateTime localDateTime = LocalDateTime.now();
		ZoneId zoneId = ZoneId.of("Asia/Kolkata");
		ZonedDateTime date = ZonedDateTime.of(localDateTime, zoneId);

		if(schedulerService.getAll().size() == 0){
			logger.info("Scheduler is null");
			List<OrderPojo> orderPojoList = orderService.getAll();
			List<LocalDate> localDateList = orderPojoList.stream().map(SchedulerDto::toLocalDate).collect(Collectors.toList());
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
						p.setInvoicedItemsCount((int) (p.getInvoicedItemsCount() + orderItemService.getSumOfInvoicedQuantityByOrderId(pojo.getId())));
						p.setTotalRevenue(p.getTotalRevenue() + orderItemService.getTotalCostByOrderId(pojo.getId()));
					}
					schedulerService.add(p);
				}
			}
		}
		else {
			logger.info("Scheduler is not null");
			ZonedDateTime lastDateTime = schedulerService.getLastDateTime().plusSeconds(1);
			ZonedDateTime zonedDateTimeNow = ZonedDateTime.now();
			List<OrderPojo> orderPojoList = orderService.getByInterval(lastDateTime, zonedDateTimeNow);
			PosDaySalesPojo pojo = new PosDaySalesPojo();
			pojo.setZonedDateTime(zonedDateTimeNow);
			pojo.setInvoicedOrdersCount(orderPojoList.size());
			pojo.setInvoicedItemsCount(0);
			pojo.setTotalRevenue(0.0);
			for(OrderPojo p: orderPojoList){
				pojo.setInvoicedItemsCount((int) (pojo.getInvoicedItemsCount() + orderItemService.getSumOfInvoicedQuantityByOrderId(p.getId())));
				pojo.setTotalRevenue(pojo.getTotalRevenue() + orderItemService.getTotalCostByOrderId(p.getId()));
			}
			schedulerService.add(pojo);
			logger.info("Scheduler updated");
		}
	}

	public List<SchedulerData> getAll(){
		logger.info("SchedulerData started executing");
		List<SchedulerData> schedulerDataList = new ArrayList<>();
		List<PosDaySalesPojo> pojos = schedulerService.getAll();
		logger.info("Received all pojos: "+pojos.toString());
		for(PosDaySalesPojo p: pojos){
			schedulerDataList.add(Converter.convertToSchedulerData(p));
		}
		logger.info("Scheduler Data: "+schedulerDataList.toString());
		logger.info("Returning data");
		return schedulerDataList;
	}

//	public Page<SchedulerData> getAll(Integer page, Integer size){
//		Page<PosDaySalesPojo> posDaySalesPojoPage = schedulerService.getAllByPage(page, size);
//		List<SchedulerData> schedulerDataList = new ArrayList<>();
//		List<PosDaySalesPojo> pojos = posDaySalesPojoPage.getContent();
//		for(PosDaySalesPojo p: pojos){
//			schedulerDataList.add(Converter.convertToSchedulerData(p));
//		}
//		return new PageImpl<>(schedulerDataList, PageRequest.of(page, size), posDaySalesPojoPage.getTotalElements());
//	}
	private static LocalDate toLocalDate(OrderPojo orderPojo){
		return orderPojo.getTime().toLocalDate();
	}

//	public Page<SchedulerData> getData(LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size){
//		if(page == null && size == null){
//
//		}
//		if(startDate != null && endDate!= null)
//	}
}
