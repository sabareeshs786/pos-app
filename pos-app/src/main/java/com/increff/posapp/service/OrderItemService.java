package com.increff.posapp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.increff.posapp.dao.OrderItemDao;
import com.increff.posapp.pojo.OrderItemPojo;
import com.increff.posapp.util.DoubleUtil;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderItemService {

	@Autowired
	private OrderItemDao orderItemDao;

	public void add(OrderItemPojo p) throws ApiException {
		normalize(p);
		validate(p);
		orderItemDao.insert(p);
	}

	public OrderItemPojo getById(Integer id) throws ApiException {
		return getCheckById(id);
	}

	   
	public List<OrderItemPojo> getByOrderId(Integer orderId) throws ApiException {
		return getCheckByOrderId(orderId);
	}

	public Page<OrderItemPojo> getPageByOrderId(Integer orderId, Integer page, Integer size) throws ApiException {
		getCheckByOrderId(orderId);
		return orderItemDao.getPageByOrderId(orderId, page, size);
	}

	public Long getSumOfInvoicedQuantityByOrderId(Integer orderId){
		return orderItemDao.getInvoicedQuantityByOrderId(orderId);
	}

	public Double getTotalCostByOrderId(Integer orderId){
		return orderItemDao.getTotalCostByOrderId(orderId);
	}

	public OrderItemPojo getByProductId(Integer productId) throws ApiException {
		return getCheckByProductId(productId);
	}

	   
	public List<OrderItemPojo> getByQuantity(Integer quantity) throws ApiException {
		return getCheckByQuantity(quantity);
	}

	   
	public List<OrderItemPojo> getBySellingPrice(Double sellingPrice) throws ApiException {
		return getCheckBySellingPrice(sellingPrice);
	}

	   
	public List<OrderItemPojo> getAll() {
		return orderItemDao.selectAll();
	}


	// For updation
	   
	public void updateById(Integer id, OrderItemPojo p) throws ApiException {
		normalize(p);
		validate(p);
		OrderItemPojo ex = getCheckById(id);
		ex.setOrderId(p.getOrderId());
		ex.setProductId(p.getProductId());
		ex.setQuantity(p.getQuantity());
		ex.setSellingPrice(p.getSellingPrice());
		orderItemDao.update(ex);
	}

	   
	public OrderItemPojo getCheckById(Integer id) throws ApiException {
		OrderItemPojo p = orderItemDao.selectById(id);
		if (p == null) {
			throw new ApiException("Band Category combination with given ID does not exit, id: " + id);
		}
		return p;
	}

	   
	public List<OrderItemPojo> getCheckByOrderId(Integer orderId) throws ApiException {
		List<OrderItemPojo> list = orderItemDao.selectByOrderId(orderId);
		if (list.isEmpty()) {
			throw new ApiException("Order Id doesn't exist");
		}
		return list;
	}

	   
	public OrderItemPojo getCheckByProductId(Integer productId) throws ApiException {
		OrderItemPojo p = orderItemDao.selectByProductId(productId);
		if (p == null) {
			throw new ApiException("Order with product Id doesnot exist");
		}
		return p;
	}

	   
	public List<OrderItemPojo> getCheckByQuantity(Integer quantity) throws ApiException {
		List<OrderItemPojo> list = orderItemDao.selectByQuantity(quantity);
		if (list.isEmpty()) {
			throw new ApiException("Order Id doesnot exist");
		}
		return list;
	}

	   
	public List<OrderItemPojo> getCheckBySellingPrice(Double sellingPrice) throws ApiException {
		List<OrderItemPojo> list = orderItemDao.selectBySellingPrice(sellingPrice);
		if (list.isEmpty()) {
			throw new ApiException("Order Id doesnot exist");
		}
		return list;
	}
	protected static void normalize(OrderItemPojo p) {
		p.setSellingPrice(DoubleUtil.round(p.getSellingPrice(), 2));
	}
	protected void validate(OrderItemPojo p) throws ApiException {
		if(p.getOrderId().toString().isEmpty()){
			throw new ApiException("Order Id can't be empty");
		}
		if(p.getProductId().toString().isEmpty()){
			throw new ApiException("Order Id can't be empty");
		}
		if(p.getQuantity().toString().isEmpty()){
			throw new ApiException("Order Id can't be empty");
		}
		if(p.getSellingPrice().toString().isEmpty()){
			throw new ApiException("Selling Price can't be empty");
		}
		if(p.getSellingPrice() <= 0.0){
			throw new ApiException("Selling price must be greater than zero");
		}
		if(p.getQuantity() <= 0){
			throw new ApiException("Quantity must be greater than zero");
		}
		if(p.getSellingPrice().isInfinite() || p.getSellingPrice().isNaN()){
			throw new ApiException("Selling price is not valid");
		}
	}
}
