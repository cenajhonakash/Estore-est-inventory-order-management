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
import com.ace.estore.inventory.dto.request.inventory.ItemStockRequestDto;
import com.ace.estore.inventory.dto.request.inventory.StockUpdateDetailsRequestDto;
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
	public ItemStockDto addItemStock(ItemStockRequestDto itemStockDto)
			throws ResourceExistsException, ResourceNotFoundException, GeneralException, ValidationException {
		helper.validateMandatoryAttributesForNewStock(itemStockDto);
		Optional<ItemStock> itemStock = itemStockRepository
				.findById(new ItemStockId(itemStockDto.itemId(), itemStockDto.storeNumber()));
		if (itemStock.isPresent()) {
			log.info("Stock details already present for item: {} & store: {}", itemStockDto.itemId(),
					itemStockDto.storeNumber());
			throw new ResourceExistsException("Stock details already existing for store: "
					+ itemStockDto.storeNumber() + " & item: " + itemStockDto.itemId());
		}
		Optional<Item> item = itemRepo.findById(itemStockDto.itemId());
		if (item.isEmpty()) {
			throw new ResourceNotFoundException(
					"Cannot add stock details as item information not present for item id: {}"
							+ itemStockDto.itemId());
		}
		ItemStock itemStockToSave = ItemStock.builder().itemId(itemStockDto.itemId())
				.storeNumber(itemStockDto.storeNumber())
				.thresholdLimit(itemStockDto.thresholdLimit())
				.isLive(itemStockDto.enable().equals(Boolean.FALSE) ? false : true).build();
		itemStockToSave.setUpdateDetails(buildStockUpdateDetailsForNewItem(itemStockToSave, itemStockDto));
		itemStockToSave.setStockQuantity(itemStockToSave.getUpdateDetails()
				.get(itemStockToSave.getUpdateDetails().size() - 1).getNewStockValue());
		ItemStock stock = itemStockRepository.save(itemStockToSave);
		return buildItemStockResponseDto(stock);
	}

	private List<StockUpdateDetails> buildStockUpdateDetailsForNewItem(ItemStock itemStockToSave,
			ItemStockRequestDto itemStockDto) {
		List<StockUpdateDetails> updateDetailList = new ArrayList<>();
		updateDetailList.add(StockUpdateDetails.builder().credit(itemStockDto.updateDetail().credit())
				.newStockValue(itemStockDto.updateDetail().credit())
				.updatedByUser(itemStockDto.updateDetail().updatedByUser())
				.build());
		return updateDetailList;
	}

	@Override
	public ItemStockDto updateItemStock(ItemStockRequestDto itemStockDto)
			throws GeneralException, ResourceNotFoundException, ValidationException {

		helper.validateMandatoryAttributesForNewStock(itemStockDto);
		ItemStock itemStock = itemStockRepository
				.findById(new ItemStockId(itemStockDto.itemId(), itemStockDto.storeNumber()))
				.orElseThrow(() -> new ResourceNotFoundException(
						"Stock details not found for store: " + itemStockDto.storeNumber() + " & item: "
								+ itemStockDto.itemId()));
		if (itemStock.getIsLive().equals(Boolean.FALSE)) {
			if (itemStockDto.enable().equals(Boolean.TRUE))
				itemStock.setIsLive(Boolean.TRUE);
			else
				throw new ValidationException("Cannot update stock as it is not live");
		}

		if (Objects.nonNull(itemStockDto.thresholdLimit())
				&& itemStockDto.thresholdLimit() != itemStock.getThresholdLimit()) {// threshold limit update
			itemStock.setThresholdLimit(itemStockDto.thresholdLimit());
		}

		if (Objects.nonNull(itemStockDto.updateDetail().updatedByUser())
				|| Objects.nonNull(itemStockDto.updateDetail().orderDetails())) {// qty update
			try {
				itemStock.getUpdateDetails().add(buildStockUpdateDetails(itemStock.getUpdateDetails(), itemStockDto));
			} catch (JsonProcessingException e) {
				throw new GeneralException("Error while writing orderDetails: " + itemStockDto.updateDetail()
						+ "due to: {}" + e.getMessage());
			}
			itemStock.setStockQuantity(
					itemStock.getUpdateDetails().get(itemStock.getUpdateDetails().size() - 1).getNewStockValue());
		}
		ItemStock updatedStock = itemStockRepository.save(itemStock);
		return buildItemStockResponseDto(updatedStock);
	}

	private StockUpdateDetails buildStockUpdateDetails(List<StockUpdateDetails> updateDetails,
			ItemStockRequestDto itemStockDto) throws JsonProcessingException, ValidationException {

		StockUpdateDetailsRequestDto updateDetailDto = itemStockDto.updateDetail();

		if (Objects.nonNull(updateDetailDto.updatedByUser()) && Objects.nonNull(updateDetailDto.orderDetails()))
			throw new ValidationException("Stock cannot be updated by an order & admin in same transaction");

		if (Objects.nonNull(updateDetailDto.debit()) && Objects.nonNull(updateDetailDto.credit()))
			throw new ValidationException("Cannot perform debit & credit in same transaction.");

		StockUpdateDetailsBuilder stockUpdateDetailsBuilder = StockUpdateDetails.builder();
		Integer currentStockValue = updateDetails.get(updateDetails.size() - 1).getNewStockValue();
		if (Objects.nonNull(updateDetailDto.updatedByUser())) { // Admin is updating manually

			if (Objects.nonNull(updateDetailDto.debit())) {
				stockUpdateDetailsBuilder.debit(updateDetailDto.debit());
				stockUpdateDetailsBuilder.newStockValue(
						currentStockValue - updateDetailDto.debit()); // updating debited new stock value
			} else {
				stockUpdateDetailsBuilder.credit(updateDetailDto.credit());
				stockUpdateDetailsBuilder.newStockValue(
						currentStockValue + updateDetailDto.credit()); // updating credited new stock value
			}
			stockUpdateDetailsBuilder.updatedByUser(updateDetailDto.updatedByUser());
		} else if (Objects.nonNull(updateDetailDto.orderDetails())) {// Stock update(Debit) came via customer order
			stockUpdateDetailsBuilder.debit(updateDetailDto.orderDetails().fulfilledQty());
			stockUpdateDetailsBuilder.newStockValue(currentStockValue - updateDetailDto.orderDetails().fulfilledQty());
			stockUpdateDetailsBuilder
					.updatedForOrder(objectMapper.writeValueAsString(updateDetailDto.orderDetails())).build();
		}
		return stockUpdateDetailsBuilder.build();
	}


	@Override
	public List<ItemStockDto> getItemStock(Integer itemId, String storeNumber) throws ResourceNotFoundException {
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
				throw new ResourceNotFoundException(
						"Stock details not found for store: " + storeNumber + " & item: " + itemId);
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
