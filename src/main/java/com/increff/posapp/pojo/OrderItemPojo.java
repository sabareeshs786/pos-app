package com.increff.posapp.pojo;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orderitem", uniqueConstraints = { @UniqueConstraint(columnNames = { "orderId", "productId" }) })
@Getter
@Setter
public class OrderItemPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "orderId", nullable = false)
	private Integer orderId;
	@Column(name = "productId", nullable = false)
	private Integer productId;
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	@Column(name = "sellingPrice", nullable = false)
	private Double sellingPrice;

	@Override
	public String toString() {
		return "OrderItemPojo [id=" + id + ", orderId=" + orderId + ", productId=" + productId + ", quantity="
				+ quantity + ", sellingPrice=" + sellingPrice + "]";
	}

}
