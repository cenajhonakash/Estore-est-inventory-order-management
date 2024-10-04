package com.ace.estore.inventory.entity.resourcing;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class ItemStockId implements Serializable {

	@Column(name = "item_id", nullable = false)
	private Integer itemId;
	@Column(name = "store_number", nullable = false)
	private Integer storeNumber;
}
