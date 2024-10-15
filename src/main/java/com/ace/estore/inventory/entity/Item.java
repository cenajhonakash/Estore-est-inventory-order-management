package com.ace.estore.inventory.entity;

/*
 * This Item signifies it is available at retail shop not Store.
 */
import java.time.LocalDateTime;
import java.util.List;

import com.ace.estore.inventory.entity.resourcing.ItemStock;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inventory_item")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer itemId;

	private String name;
	private String brand;

	@Column(length = 10000)
	private String description;

	@Column(nullable = false)
	private Double discountPercent;

	@Column(nullable = false)
	private Double price;

	@Column(nullable = false)
	private Integer quantity; // available quantity

	private LocalDateTime addedDate;

	private LocalDateTime updateDate;

	private String image;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", updatable = false, insertable = true, nullable = false)
	private ItemCategory category;

	@OneToMany(targetEntity = ItemStock.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	List<ItemStock> itemStock;

	@OneToMany(targetEntity = OrderItem.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	List<OrderItem> orderedItem;

	@PrePersist
	private void setAddedDate() {
		this.addedDate = LocalDateTime.now();
	}

	@PreUpdate
	private void setUpdateDate() {
		this.updateDate = LocalDateTime.now();
	}
}
