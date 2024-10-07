package com.ace.estore.inventory.entity.resourcing;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Non Related table. To maintain stock data for item by retail store staffs.
 * 1. inStockValue needs to be updated(+-) if any customer orders the item
 * 2. fulfilledForOrder status needs to be updated once once order is placed
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item_stock")
@IdClass(ItemStockId.class)
public class ItemStock {

	@Id
	private Integer itemId;
	@Id
	private String storeNumber;

	private Integer stockQuantity;
	/*
	 * To check if item amount is less than threshold value of 10. For purpose of
	 * replenishment. Have a scheduler every hour that'll email store team to
	 * replenish the item
	 */
	private Integer thresholdLimit;

	@OneToMany(targetEntity = StockUpdateDetails.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "item_id", referencedColumnName = "item_id"),
			@JoinColumn(name = "store_number", referencedColumnName = "store_number") })
	private List<StockUpdateDetails> updateDetails;
}
