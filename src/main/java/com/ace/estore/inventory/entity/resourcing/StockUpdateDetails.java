package com.ace.estore.inventory.entity.resourcing;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item_stock_update_details")
public class StockUpdateDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/*
	 * If store admin updated the stock
	 */
	private Integer updatedByUser;
	/*
	 * Stock fulfilled for order track details
	 * {"orderNo":"","originalReqQty":"","sourcedQty":""}
	 */
	@Column(columnDefinition = "JSON")
	private String updatedForOrder;

	private LocalDateTime updatedDate;
	private Integer credit;// updatedForOrder.sourcedQty = updatedQuantity. Decrement if updatedForOrder is
	private Integer debit;// not null(assuming customer order fulfilled from Stock) OR

	private Integer newStockValue;

	@PrePersist
	private void setUpdatedDate() {
		this.updatedDate = LocalDateTime.now();
	}
}
