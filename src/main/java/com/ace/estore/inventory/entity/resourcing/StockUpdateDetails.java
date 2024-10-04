package com.ace.estore.inventory.entity.resourcing;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
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
	private Integer updatedQuantity;// updatedForOrder.sourcedQty = updatedQuantity. [+ -] by admin
	private Integer newStockValue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "item_id", referencedColumnName = "item_id", insertable = false, updatable = false),
			@JoinColumn(name = "store_number", referencedColumnName = "store_number", insertable = false, updatable = false) })
	private ItemStock stock;

	@PrePersist
	private void setUpdatedDate() {
		this.updatedDate = LocalDateTime.now();
	}
}
