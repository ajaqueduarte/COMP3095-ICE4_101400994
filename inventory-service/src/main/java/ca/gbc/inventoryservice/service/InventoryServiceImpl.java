package ca.gbc.inventoryservice.service;


import ca.gbc.inventoryservice.dto.InventoryRequest;
import ca.gbc.inventoryservice.dto.InventoryResponse;
import ca.gbc.inventoryservice.model.Inventory;
import ca.gbc.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;


    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<InventoryRequest> requests) {

        //comment the bottom out later, just used it for showing how to use it
        //log.info("wait started");
        Thread.sleep(7000);
        //log.info("wait ended");
        //until here



        List<Inventory> availableInventory = inventoryRepository.findAllByInventoryRequests(requests);

        return requests.stream().map(request -> {

            boolean isInStock = availableInventory.stream()
                    .anyMatch(inventory -> inventory.getSkuCode().equals(request.getSkuCode())
                            && inventory.getQuantity() >= request.getQuantity());

            if (isInStock) {
                return InventoryResponse.builder()
                        .skuCode(request.getSkuCode())
                        .sufficientStock(true)
                        .build();
            } else {
                return InventoryResponse.builder()
                        .skuCode(request.getSkuCode())
                        .sufficientStock(false)
                        .build();
            }

        }).toList();

    }

}