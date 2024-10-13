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
import jakarta.persistence.OneToMany;
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
@Table(name = "customer_order")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderId;

	@Column(nullable = false)
	private String status;

	private String paymentStatus;
	private String refundStatus;
	private LocalDateTime createdDate;

	@Column(nullable = false)
	private String userId;

	/*
	 * need to add billing & delivery address in user-profile service.
	 * userAddressCode = userId + addresId
	 * {"phone":"","state":"","zip-code":"","billingAddress":"","email":""}
	 */
	@Column(columnDefinition = "JSON", nullable = false)
	private String userDetails;

	@OneToMany(targetEntity = OrderItem.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private List<OrderItem> orderItems;

	@PrePersist
	private void setCreatedDate() {
		this.createdDate = LocalDateTime.now();
	}
}
