package com.example.TTTN.service.impl;

import com.example.TTTN.entity.*;
import com.example.TTTN.exception.BadRequestException;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.WarehouseTransactionDto;
import com.example.TTTN.repository.*;
import com.example.TTTN.service.WarehouseTransactionService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseTransactionServiceImpl implements WarehouseTransactionService {
    private final WarehouseTransactionRepository warehouseTransactionRepository;
    private final ModelMapper modelMapper;
    private final StatusRepository statusRepository;
    private final OrderRepository orderRepository;
    private final WarehouseProductRepository warehouseProductRepository;
    private final ProductRepository productRepository;
    private final WarehouseTransactionTypeRepository warehouseTransactionTypeRepository;
    private final WarehouseRepository warehouseRepository;
    private final ImportBatchRepository importBatchRepository;
    private final TransactionBatchRepository transactionBatchRepository;

    public WarehouseTransactionServiceImpl(WarehouseTransactionRepository warehouseTransactionRepository,
                                           ModelMapper modelMapper,
                                           StatusRepository statusRepository,
                                           OrderRepository orderRepository,
                                           WarehouseProductRepository warehouseProductRepository,
                                           ProductRepository productRepository, WarehouseTransactionTypeRepository warehouseTransactionTypeRepository, WarehouseRepository warehouseRepository, ImportBatchRepository importBatchRepository, TransactionBatchRepository transactionBatchRepository) {
        this.warehouseTransactionRepository = warehouseTransactionRepository;
        this.modelMapper = modelMapper;
        this.statusRepository = statusRepository;
        this.orderRepository = orderRepository;
        this.warehouseProductRepository = warehouseProductRepository;
        this.productRepository = productRepository;
        this.warehouseTransactionTypeRepository = warehouseTransactionTypeRepository;
        this.warehouseRepository = warehouseRepository;
        this.importBatchRepository = importBatchRepository;
        this.transactionBatchRepository = transactionBatchRepository;
    }

    private WarehouseTransactionDto mapToDto(WarehouseTransaction warehouseTransaction) {
        return modelMapper.map(warehouseTransaction, WarehouseTransactionDto.class);
    }

    private WarehouseTransaction mapToEntity(WarehouseTransactionDto warehouseTransactionDto) {
        return modelMapper.map(warehouseTransactionDto, WarehouseTransaction.class);
    }

    @Override
    @Transactional
    public WarehouseTransactionDto createWarehouseTransaction(WarehouseTransactionDto dto) {
        WarehouseTransaction wt = mapToEntity(dto);

        WarehouseTransaction savedWt = warehouseTransactionRepository.save(wt);

        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", String.valueOf(dto.getOrderId())));
        Status status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Status", "id", String.valueOf(dto.getStatusId())));
        WarehouseTransactionType type = warehouseTransactionTypeRepository.findById(dto.getTransactionTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction Type", "id", String.valueOf(dto.getTransactionTypeId())));
        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(dto.getWarehouseId())));

        double totalRevenue = order.getTotalMoney();
        double totalCost = 0;

        boolean isImport = type.getName().equalsIgnoreCase("Nhập");
        boolean isExport = type.getName().equalsIgnoreCase("Xuất");
        String statusName = status.getName();

        for (OrderDetail detail : order.getOrderDetails()) {
            Product product = productRepository.findById(detail.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", String.valueOf(detail.getProduct().getId())));
            int quantity = detail.getQuantity();

            WarehouseProduct wp = warehouseProductRepository.findByWarehouseIdAndProductId(warehouse.getId(), product.getId());

            if (isImport && statusName.equalsIgnoreCase("Đã hoàn thành")) {
                double unitCost = detail.getUnit_price();

                ImportBatch batch = importBatchRepository.findByProductAndWarehouseAndUnitCost(product, warehouse, unitCost);

                if (batch != null) {
                    batch.setQuantityRemaining(batch.getQuantityRemaining() + quantity);
                } else {
                    batch = new ImportBatch();
                    batch.setProduct(product);
                    batch.setWarehouse(warehouse);
                    batch.setUnitCost(unitCost);
                    batch.setQuantityRemaining(quantity);
                }
                importBatchRepository.save(batch);

                wp.setQuantity(wp.getQuantity() + quantity);
                warehouseProductRepository.save(wp);

                product.setQuantity(product.getQuantity() + quantity);
                productRepository.save(product);
            }

            if (isExport && (statusName.equalsIgnoreCase("Đã hoàn thành")
                    || statusName.equalsIgnoreCase("Đang xử lý"))) {
                validateStock(warehouse.getId(), product.getId(), quantity);

                List<ImportBatch> batches = importBatchRepository
                        .findByProductAndWarehouseOrderByImportDateAsc(product, warehouse);

                int totalExportQuantity = quantity;
                for (ImportBatch batch : batches) {
                    int available = batch.getQuantityRemaining();
                    if (available <= 0) continue;

                    int deducted = Math.min(available, totalExportQuantity);
                    batch.setQuantityRemaining(available - deducted);
                    importBatchRepository.save(batch);

                    TransactionBatch tb = new TransactionBatch();
                    tb.setWarehouseTransaction(wt);
                    tb.setImportBatch(batch);
                    tb.setQuantityDeducted(deducted);
                    transactionBatchRepository.save(tb);

                    totalCost += deducted * batch.getUnitCost();
                    totalExportQuantity -= deducted;
                }

                wp.setQuantity(wp.getQuantity() - quantity);
                warehouseProductRepository.save(wp);

                product.setQuantity(product.getQuantity() - quantity);
                productRepository.save(product);
            }
        }

        if (isExport && (statusName.equalsIgnoreCase("Đã hoàn thành"))) {
            double profit = totalRevenue - totalCost;
            order.setProfitMoney(profit);
            orderRepository.save(order);
        }

        return mapToDto(savedWt);
    }

    public void validateStock(long warehouseId, long productId, int requiredQuantity) {
        WarehouseProduct wp = warehouseProductRepository.findByWarehouseIdAndProductId(warehouseId, productId);
        Warehouse wh = warehouseRepository.findById(warehouseId).orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(warehouseId)));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "id", String.valueOf(productId)));

        int available = (wp != null) ? wp.getQuantity() : 0;

        if (available < requiredQuantity) {
            throw new BadRequestException(wh.getName().toUpperCase() + " không đủ số lượng cho sản phẩm " + product.getName().toUpperCase()
                    + ". Vui lòng tạo chuyển kho trước hoặc nhập thêm số lượng.");
        }
    }

    @Override
    @Transactional
    public WarehouseTransactionDto updateWarehouseTransaction(long id, WarehouseTransactionDto dto) {
        WarehouseTransaction wt = warehouseTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse transaction", "id", String.valueOf(id)));
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", String.valueOf(dto.getOrderId())));
        Status newStatus = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Status", "id", String.valueOf(dto.getStatusId())));
        WarehouseTransactionType type = warehouseTransactionTypeRepository.findById(dto.getTransactionTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction Type", "id", String.valueOf(dto.getTransactionTypeId())));
        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(dto.getWarehouseId())));

        String oldStatusName = wt.getStatus().getName();
        String newStatusName = newStatus.getName();

        boolean isImport = type.getName().equalsIgnoreCase("Nhập");
        boolean isExport = type.getName().equalsIgnoreCase("Xuất");

        double totalRevenue = order.getTotalMoney();
        double totalCost = 0;

        for (OrderDetail detail : order.getOrderDetails()) {
            Product product = productRepository.findById(detail.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", String.valueOf(detail.getProduct().getId())));
            int quantity = detail.getQuantity();

            WarehouseProduct wp = warehouseProductRepository.findByWarehouseIdAndProductId(warehouse.getId(), product.getId());

            // NHẬP: Đang xử lý -> Đã hoàn thành
            if (isImport && oldStatusName.equalsIgnoreCase("Đang xử lý") && newStatusName.equalsIgnoreCase("Đã hoàn thành")) {
                double unitCost = detail.getUnit_price();

                ImportBatch batch = importBatchRepository.findByProductAndWarehouseAndUnitCost(product, warehouse, unitCost);
                if (batch != null) {
                    batch.setQuantityRemaining(batch.getQuantityRemaining() + quantity);
                } else {
                    batch = new ImportBatch();
                    batch.setProduct(product);
                    batch.setWarehouse(warehouse);
                    batch.setUnitCost(unitCost);
                    batch.setQuantityRemaining(quantity);
                }
                importBatchRepository.save(batch);

                wp.setQuantity(wp.getQuantity() + quantity);
                warehouseProductRepository.save(wp);
                product.setQuantity(product.getQuantity() + quantity);
                productRepository.save(product);
            }

            // XUẤT: Đang xử lý -> Không thành công
            if (isExport && oldStatusName.equalsIgnoreCase("Đang xử lý") && newStatusName.equalsIgnoreCase("Không thành công")) {
                List<TransactionBatch> usedBatches = transactionBatchRepository.findByWarehouseTransaction(wt);
                for (TransactionBatch tb : usedBatches) {
                    ImportBatch batch = tb.getImportBatch();
                    batch.setQuantityRemaining(batch.getQuantityRemaining() + tb.getQuantityDeducted());
                    importBatchRepository.save(batch);
                }
                transactionBatchRepository.deleteAll(usedBatches);

                wp.setQuantity(wp.getQuantity() + quantity);
                warehouseProductRepository.save(wp);
                product.setQuantity(product.getQuantity() + quantity);
                productRepository.save(product);
            }

            // XUẤT: Không thành công -> Đã hoàn thành hoặc Đang xử lý
            if (isExport && oldStatusName.equalsIgnoreCase("Không thành công")
                    && (newStatusName.equalsIgnoreCase("Đã hoàn thành") || newStatusName.equalsIgnoreCase("Đang xử lý"))) {
                validateStock(warehouse.getId(), product.getId(), quantity);

                List<ImportBatch> batches = importBatchRepository
                        .findByProductAndWarehouseOrderByImportDateAsc(product, warehouse);
                int totalExportQuantity = quantity;
                for (ImportBatch batch : batches) {
                    int available = batch.getQuantityRemaining();
                    if (available <= 0) continue;
                    int deducted = Math.min(available, totalExportQuantity);
                    batch.setQuantityRemaining(available - deducted);
                    importBatchRepository.save(batch);

                    TransactionBatch tb = new TransactionBatch();
                    tb.setWarehouseTransaction(wt);
                    tb.setImportBatch(batch);
                    tb.setQuantityDeducted(deducted);
                    transactionBatchRepository.save(tb);

                    totalCost += deducted * batch.getUnitCost();
                    totalExportQuantity -= deducted;
                }

                wp.setQuantity(wp.getQuantity() - quantity);
                warehouseProductRepository.save(wp);
                product.setQuantity(product.getQuantity() - quantity);
                productRepository.save(product);
            }
        }

        // XUẤT: Đang xử lý -> Đã hoàn thành
        if (isExport && oldStatusName.equalsIgnoreCase("Đang xử lý") && newStatusName.equalsIgnoreCase("Đã hoàn thành")) {
            // Tính totalCost từ TransactionBatch đã có
            totalCost = transactionBatchRepository.findByWarehouseTransaction(wt)
                    .stream()
                    .mapToDouble(tb -> tb.getQuantityDeducted() * tb.getImportBatch().getUnitCost())
                    .sum();
        }

        // Xử lý profit cho đơn xuất
        if (isExport) {
            if (newStatusName.equalsIgnoreCase("Không thành công")) {
                order.setProfitMoney(0);
            } else if (newStatusName.equalsIgnoreCase("Đã hoàn thành") || newStatusName.equalsIgnoreCase("Đang xử lý")) {
                // Tính lại profit dựa trên totalRevenue và totalCost
                double profit = totalRevenue - totalCost;
                order.setProfitMoney(profit);
            }
            orderRepository.save(order);
        }

        wt.setStatus(newStatus);
        wt.setCreatedBy(dto.getCreatedBy());
        wt.setParticipant(dto.getParticipant());
        wt.setStorekeeper(dto.getStorekeeper());
        wt.setAccountant(dto.getAccountant());

        return mapToDto(warehouseTransactionRepository.save(wt));
    }

    @Override
    public ListResponse<WarehouseTransactionDto> getAllWarehouseTransactions(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<WarehouseTransaction> warehouseTransactions = warehouseTransactionRepository.findAll(pageRequest);

        List<WarehouseTransaction> listOfWarehouseTransactions = warehouseTransactions.getContent();

        List<WarehouseTransactionDto> content = listOfWarehouseTransactions.stream().map(this::mapToDto).toList();

        ListResponse<WarehouseTransactionDto> response = new ListResponse<>();
        response.setContent(content);
        response.setPageNo(warehouseTransactions.getNumber());
        response.setPageSize(warehouseTransactions.getSize());
        response.setTotalElements((int) warehouseTransactions.getTotalElements());
        response.setTotalPages(warehouseTransactions.getTotalPages());
        response.setLast(warehouseTransactions.isLast());

        return response;
    }

    @Override
    public WarehouseTransactionDto getWarehouseTransaction(long id) {
        WarehouseTransaction warehouseTransaction = warehouseTransactionRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(id)));

        return mapToDto(warehouseTransaction);
    }

    @Override
    public void deleteWarehouseTransaction(long id) {
        WarehouseTransaction warehouseTransaction = warehouseTransactionRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(id)));
        warehouseTransactionRepository.delete(warehouseTransaction);
    }
}
