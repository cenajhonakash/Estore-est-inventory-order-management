package com.ace.estore.inventory.service.impl.itemAndStock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace.estore.inventory.dto.ItemStockDto;
import com.ace.estore.inventory.dto.StockUpdateDetailsDto;
import com.ace.estore.inventory.dto.mapper.ItemStockDtoMapper;
import com.ace.estore.inventory.dto.mapper.StockUpdateDetailsMapper;
import com.ace.estore.inventory.dto.request.order.OrderDetailsStockUpdateDto;
import com.ace.estore.inventory.entity.Item;
import com.ace.estore.inventory.entity.resourcing.ItemStock;
import com.ace.estore.inventory.entity.resourcing.ItemStockId;
import com.ace.estore.inventory.entity.resourcing.StockUpdateDetails;
import com.ace.estore.inventory.entity.resourcing.StockUpdateDetails.StockUpdateDetailsBuilder;
import com.ace.estore.inventory.exception.GeneralException;
import com.ace.estore.inventory.exception.ResourceExistsException;
import com.ace.estore.inventory.exception.ResourceNotFoundException;
import com.ace.estore.inventory.exception.ValidationException;
import com.ace.estore.inventory.repository.ItemRepository;
import com.ace.estore.inventory.repository.resourcing.ItemStockRepository;
import com.ace.estore.inventory.service.ItemStockService;
import com.ace.estore.inventory.service.helper.InventoryHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ItemStockServiceImpl implements ItemStockService {

	@Autowired
	private ItemStockRepository itemStockRepository;
	@Autowired
	private ItemRepository itemRepo;
	@Autowired
	private InventoryHelper helper;
	@Autowired
	private ItemStockDtoMapper stockMapper;
	@Autowired
	private StockUpdateDetailsMapper stockUpdateMapper;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public ItemStockDto addItemStock(ItemStockDto itemStockDto)
			throws ResourceExistsException, ResourceNotFoundException, GeneralException, ValidationException {
		helper.validateMandatoryAttributesForNewStock(itemStockDto);
		Optional<ItemStock> itemStock = itemStockRepository
				.findById(new ItemStockId(itemStockDto.getItemId(), itemStockDto.getStoreNumber()));
		if (itemStock.isPresent()) {
			log.info("Stock details already present for item: {} & store: {}", itemStockDto.getItemId(),
					itemStockDto.getStoreNumber());
			throw new ResourceExistsException("Stock details already existing for store: "
					+ itemStockDto.getStoreNumber() + " & item: " + itemStockDto.getItemId());
		}
		Optional<Item> item = itemRepo.findById(itemStockDto.getItemId());
		if (item.isEmpty()) {
			throw new ResourceNotFoundException(
					"Cannot add stock details as item information not present for item id: {}"
							+ itemStockDto.getItemId());
		}
		ItemStock itemStockToSave = helper.buildItemStockEntity(itemStockDto);
		itemStockToSave
				.setUpdateDetails(buildStockUpdateDetailsForNewItem(itemStockToSave, itemStockDto));
		ItemStock stock = itemStockRepository.save(itemStockToSave);
		return buildItemStockResponseDto(stock);
	}

	private List<StockUpdateDetails> buildStockUpdateDetailsForNewItem(ItemStock itemStockToSave,
			ItemStockDto itemStockDto) {
		List<StockUpdateDetails> updateDetailList = new ArrayList<>();
		updateDetailList.add(StockUpdateDetails.builder().credit(itemStockDto.getStockQuantity())
				.newStockValue(itemStockDto.getStockQuantity())
				.updatedByUser(itemStockDto.getUpdateDetail().getUpdatedByUser())
//				.stock(itemStockToSave)
				.build());
		return updateDetailList;
	}


	@Override
	public ItemStockDto updateItemStock(ItemStockDto itemStockDto, Integer itemId, String store)
			throws GeneralException, ResourceNotFoundException {
		ItemStock itemStock = itemStockRepository.findById(new ItemStockId(itemId, store))
				.orElseThrow(() -> new ResourceNotFoundException(
						"Stock details not found for store: " + store + " & item: " + itemId));
		if (Objects.nonNull(itemStockDto.getStockQuantity())
				&& itemStockDto.getStockQuantity() != itemStock.getStockQuantity()) {
			if (itemStockDto.getStockQuantity() > itemStock.getStockQuantity()) {// increase ItemStock.stockQuantity &
																					// in stockUpdateDetails

			}
			if (itemStockDto.getStockQuantity() < itemStock.getStockQuantity()) {// decrease ItemStock.stockQuantity

			}
			itemStock.setStockQuantity(itemStockDto.getStockQuantity());
		}
		if (Objects.nonNull(itemStockDto.getThresholdLimit())
				&& itemStockDto.getThresholdLimit() != itemStock.getThresholdLimit()) {
			itemStock.setThresholdLimit(itemStockDto.getThresholdLimit());
		}
		try {
			itemStock.setUpdateDetails(buildStockUpdateDetails(itemStock.getUpdateDetails(), itemStockDto));
		} catch (JsonProcessingException e) {
			throw new GeneralException("Error while writing orderDetails: " + itemStockDto.getUpdateDetail()
					+ "due to: {}" + e.getMessage());
		}
		return buildItemStockResponseDto(itemStock);
	}

	private List<StockUpdateDetails> buildStockUpdateDetails(List<StockUpdateDetails> updateDetails,
			ItemStockDto itemStockDto) throws JsonProcessingException {

		StockUpdateDetailsDto updateDetailDto = itemStockDto.getUpdateDetail();
		List<StockUpdateDetails> updateList = new ArrayList<>();
		StockUpdateDetailsBuilder stockUpdateBuilder = StockUpdateDetails.builder()
				.updatedByUser(updateDetailDto.getUpdatedByUser())
				.updatedForOrder(Objects.nonNull(updateDetailDto.getOrderDetails())
						? objectMapper.writeValueAsString(updateDetailDto.getOrderDetails())
						: null);

		if (Objects.nonNull(updateDetails)) {
			if (Objects.nonNull(updateDetailDto.getUpdatedByUser())
					&& Objects.isNull(updateDetailDto.getOrderDetails())) {
				// stockUpdateBuilder.newStockValue();
			}
			updateDetails.add(null);
		} else {
		}
		return null;
	}

	@Override
	public List<ItemStockDto> getItemStock(Integer itemId, String storeNumber) throws ResourceExistsException {
		List<ItemStock> stockDetails = null;
		if (Objects.isNull(storeNumber) && Objects.isNull(itemId)) {// both null then get all data
			stockDetails = itemStockRepository.findAll();
		} else if (Objects.isNull(storeNumber) && Objects.nonNull(itemId)) {// getByStore
			stockDetails = itemStockRepository.findAllByItemId(itemId);
		} else if (Objects.isNull(itemId) && Objects.nonNull(storeNumber)) {// getByItem
			stockDetails = itemStockRepository.findAllByStoreNumber(storeNumber);
		} else {
			Optional<ItemStock> itemStock = itemStockRepository.findById(new ItemStockId(itemId, storeNumber));
			if (itemStock.isEmpty()) {
				throw new ResourceExistsException(
						"Stock details already existing for store: " + storeNumber + " & item: " + itemId);
			}
			stockDetails = Arrays.asList(itemStock.get());
		}
		return stockDetails.stream().map(stock -> buildItemStockResponseDto(stock)).collect(Collectors.toList());
	}

	private ItemStockDto buildItemStockResponseDto(ItemStock stock) {
		ItemStockDto itemStockDto = stockMapper.entityToDto(stock);
		if (stock.getUpdateDetails() != null && !stock.getUpdateDetails().isEmpty()) {
			List<StockUpdateDetailsDto> orderUpdateDetails = stock.getUpdateDetails().stream().map(updateDetails -> {
				try {
					StockUpdateDetailsDto stockUpdateDetailsDto = stockUpdateMapper.entityToDto(updateDetails);
					stockUpdateDetailsDto.setOrderDetails(
							Objects.nonNull(updateDetails.getUpdatedForOrder()) ? objectMapper.readValue(
									updateDetails.getUpdatedForOrder(), OrderDetailsStockUpdateDto.class) : null);
					return stockUpdateDetailsDto;
				} catch (JsonProcessingException e) {
					log.error("Error while converting order details: {} for stock:{} due to: {}",
							updateDetails.getUpdatedForOrder(), updateDetails.getId(), e.getMessage());
					return null;
				}
			}).filter(dto -> Objects.nonNull(dto)).collect(Collectors.toList());
			itemStockDto.setOrderUpdateDetails(orderUpdateDetails);
		}
		return itemStockDto;
	}

}
