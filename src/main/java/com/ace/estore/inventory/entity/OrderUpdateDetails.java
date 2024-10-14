package com.ace.estore.inventory.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_update")
public class OrderUpdateDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderUpdateId;

	private String status; // Cancellation by customer
	private LocalDateTime needDeliveryDate; // delivery date rescheduling by customer
	@Column(columnDefinition = "JSON")
	private String userDetails; // delivery address change by customer

	private LocalDateTime updatedTime;

	@PrePersist
	private void setUpdatedTime() {
		this.updatedTime = LocalDateTime.now();
	}
}
