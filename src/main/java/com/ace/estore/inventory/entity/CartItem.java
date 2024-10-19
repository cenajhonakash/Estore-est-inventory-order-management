package com.ace.estore.inventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cart_item")
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cartItemId;

	private Integer quantity;

	@Transient
	private Double itemTotal;

	/*
	 * This field needs to be used if any cart item is not available. Use case: Want
	 * to strike off items in UI at cart level
	 */
	@Transient
	private Boolean requestQuantityNotAvailable = Boolean.TRUE;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", updatable = false, nullable = false)
	private Item item;
}
