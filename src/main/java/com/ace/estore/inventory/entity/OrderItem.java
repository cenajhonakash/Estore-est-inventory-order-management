package com.ace.estore.inventory.entity;

import java.time.LocalDateTime;
import java.util.List;

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
    
    private Integer quantity;
    
    private Double discount;
    
    private Double price;
    
	private String status;

    @Column(name = "deliveryDate")
    private LocalDateTime promisedDeliveryDate;

	@ManyToOne
	@JoinColumn(name = "order_id", updatable = false)
	private Order order;

	@OneToMany(targetEntity = OrderUpdateDetails.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "order_item_id")
	private List<OrderUpdateDetails> orderUpdates;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Item item;
}
