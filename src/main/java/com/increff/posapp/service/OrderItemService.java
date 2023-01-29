package com.increff.posapp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
		orderItemDao.insert(p);
	}

	public OrderItemPojo getById(Integer id) throws ApiException {
		return getCheckById(id);
	}

	   
	public List<OrderItemPojo> getByOrderId(Integer orderId) throws ApiException {
		return getCheckByOrderId(orderId);
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
			throw new ApiException("Order Id doesnot exist");
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
}
