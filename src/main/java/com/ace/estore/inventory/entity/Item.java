package com.ace.estore.inventory.entity;

/*
 * This Item signifies it is available at retail shop not Store.
 */
import java.time.LocalDateTime;

import com.ace.estore.inventory.entity.resourcing.ItemStock;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
    private Integer productId;

    private String name;
	private String brand;

    @Column(length = 10000)
    private String description;

	@Column(nullable = false)
    private Double price;

	@Column(nullable = false)
	private Double discountPercent;

	@Column(nullable = false)
    private Integer quantity;

    private LocalDateTime addedDate;

	private LocalDateTime updateDate;

	private String image;
    
	@OneToOne
	@JoinColumns({ @JoinColumn(name = "item_id", referencedColumnName = "item_id"),
			@JoinColumn(name = "store_number", referencedColumnName = "store_number") })
	private ItemStock stock;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", updatable = false, insertable = true)
    private  ItemCategory category;

	@PrePersist
	private void setAddedDate() {
		this.addedDate = LocalDateTime.now();
	}

	@PreUpdate
	private void setUpdateDate() {
		this.updateDate = LocalDateTime.now();
	}
}
