package com.ace.estore.inventory.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
@Table(name = "order_item")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderItemId;

	private Integer quantity; // updatable entity by customer by increasing/decreasing

	private Double actualPrice;

	private Double discount;

	private Double salePrice;

	private String status; // updatable entity by customer like CANCEL ITEM

	@Column(name = "deliveryDate")
	private LocalDateTime promisedDeliveryDate; // To be calculated based on distance OR defaultValue is +5 currentDate

	@Column(name = "needDeliveryDate")
	private LocalDateTime needDeliveryDate; // updatable entity by customer

	@ManyToOne
	@JoinColumn(name = "order_id", updatable = false)
	private Order order;

	@OneToMany(targetEntity = OrderUpdateDetails.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "order_item_id")
	private List<OrderUpdateDetails> orderUpdates;

	@OneToOne
	@JoinColumn(name = "product_id", updatable = true)
	private Item item;

	@PrePersist
	private void setNeedDeliveryDate() {
		if (Objects.isNull(this.needDeliveryDate))
			this.needDeliveryDate = LocalDateTime.now().plusDays(5);
		if (Objects.isNull(this.promisedDeliveryDate))
			this.promisedDeliveryDate = this.needDeliveryDate;

	}
}
