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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "item_category")
public class ItemCategory {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer categoryId;

	@Column(name = "category_title", length = 60, nullable = false, unique = true)
	private String title;

	@Column(name = "category_desc", length = 500)
	private String description;

	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	private String coverImage;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Item> items;

	@PrePersist
	protected void setCreateDate() {
		this.createdDate = LocalDateTime.now();
	}

	@PreUpdate
	protected void setUpdatedDate() {
		this.updatedDate = LocalDateTime.now();
	}
}
