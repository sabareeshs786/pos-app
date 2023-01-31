package com.increff.posapp.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "brand", uniqueConstraints = { @UniqueConstraint(columnNames = { "brand", "category" }) })
public class BrandPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "brand", nullable = false)
	private String brand;
	@Column(name = "category", nullable = false)
	private String category;

	@Override
	public String toString() {
		return "BrandPojo [id=" + id + ", brand=" + brand + ", category=" + category + "]";
	}
}
