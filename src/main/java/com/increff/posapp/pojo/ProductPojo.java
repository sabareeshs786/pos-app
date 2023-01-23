package com.increff.posapp.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter
public class ProductPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "barcode", unique = true, nullable = false)
	private String barcode;
	@Column(name = "brand_category", nullable = false)
	private Integer brandCategory;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "mrp", nullable = false)
	private Double mrp;

	@Override
	public String toString() {
		return "ProductPojo [id=" + id + ", barcode=" + barcode + ", brand_category=" + brandCategory + ", name="
				+ name + ", mrp=" + mrp + "]";
	}

}
