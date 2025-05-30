package com.example.TTTN.service.impl;

import com.example.TTTN.entity.*;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.*;
import com.example.TTTN.repository.*;
import com.example.TTTN.service.InvoiceService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ModelMapper modelMapper;
    private final PartnerRepository partnerRepository;
    private final InvoiceTypeRepository invoiceTypeRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              ModelMapper modelMapper,
                              PartnerRepository partnerRepository,
                              InvoiceTypeRepository invoiceTypeRepository, OrderRepository orderRepository, OrderStatusRepository orderStatusRepository) {
        this.invoiceRepository = invoiceRepository;
        this.modelMapper = modelMapper;
        this.partnerRepository = partnerRepository;
        this.invoiceTypeRepository = invoiceTypeRepository;
        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    private InvoiceDto mapToDto(Invoice invoice) {
        return modelMapper.map(invoice, InvoiceDto.class);
    }

    private Invoice mapToEntity(InvoiceDto invoiceDto) {
        return modelMapper.map(invoiceDto, Invoice.class);
    }

    @Override
    @Transactional
    public InvoiceDto createInvoice(InvoiceDto invoiceDto) {
        Order order = orderRepository.findById(invoiceDto.getOrderId()).orElseThrow(()
                -> new ResourceNotFoundException("Order", "id", String.valueOf(invoiceDto.getOrderId())));

        Partner partner = partnerRepository.findById(order.getPartner().getId()).orElseThrow(()
                -> new ResourceNotFoundException("Partner", "id", String.valueOf(order.getPartner().getId())));

        order.setPaidMoney(order.getPaidMoney() + invoiceDto.getMoneyAmount());
        partner.setDebt(partner.getDebt() - invoiceDto.getMoneyAmount());

        if (order.getTotalMoney() == order.getPaidMoney()) {
            order.setOrderStatus(orderStatusRepository.findByName("Đã thanh toán"));
        } else {
            order.setOrderStatus(orderStatusRepository.findByName("Thanh toán một phần"));
        }

        Invoice invoice = mapToEntity(invoiceDto);
        return mapToDto(invoiceRepository.save(invoice));
    }

    @Override
    public InvoiceDto getInvoiceById(long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(()
                -> new ResourceNotFoundException("Invoice", "id", String.valueOf(invoiceId)));
        return mapToDto(invoice);
    }

    @Override
    public InvoiceDetailsDto getInvoiceDetailsById(long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(()
                -> new ResourceNotFoundException("Invoice", "id", String.valueOf(invoiceId)));

        InvoiceDetailsDto invoiceDetailsDto = new InvoiceDetailsDto();
        invoiceDetailsDto.setId(invoice.getId());
        InvoiceTypeDto invoiceTypeDto = modelMapper.map(invoice.getInvoiceType(), InvoiceTypeDto.class);
        invoiceDetailsDto.setInvoiceType(invoiceTypeDto);
        invoiceDetailsDto.setMoneyAmount(invoice.getMoneyAmount());
        if (invoice.getPaymentType() != null) {
            invoiceDetailsDto.setPaymentType(invoice.getPaymentType().getLabel());
        } else {
            invoiceDetailsDto.setPaymentType("N/A");
        }
        invoiceDetailsDto.setCreatedAt(invoice.getCreatedAt());

        // Lấy thông tin đơn hàng
        if (invoice.getOrder() != null) {
            Order order = invoice.getOrder();
            OrderDto orderDto = modelMapper.map(order, OrderDto.class);
            invoiceDetailsDto.setOrder(orderDto);

            // Lấy thông tin đối tác
            if (order.getPartner() != null) {
                Partner partner = order.getPartner();
                PartnerDto partnerDto = modelMapper.map(partner, PartnerDto.class);
                invoiceDetailsDto.setPartner(partnerDto);
            }

            // Lấy chi tiết sản phẩm trong đơn hàng
            if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
                List<OrderItemDto> items = order.getOrderDetails().stream().map(detail -> {
                    OrderItemDto itemDto = new OrderItemDto();
                    itemDto.setProductId(detail.getProduct().getId());

                    // Lấy thông tin sản phẩm
                    try {
                        Product product = detail.getProduct();
                        itemDto.setProductName(product.getName());
                    } catch (Exception e) {
                        itemDto.setProductName("Sản phẩm " + detail.getProduct().getId());
                    }

                    itemDto.setQuantity(detail.getQuantity());
                    itemDto.setUnitPrice(detail.getUnit_price());
                    itemDto.setTotal(detail.getQuantity() * detail.getUnit_price());

                    return itemDto;
                }).collect(Collectors.toList());

                invoiceDetailsDto.setItems(items);
            }
        }

        return invoiceDetailsDto;

    }

    @Override
    public ListResponse<InvoiceDto> getAllInvoices(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<Invoice> invoices = invoiceRepository.findAll(pageRequest);

        List<Invoice> listOfInvoices = invoices.getContent();

        List<InvoiceDto> content = listOfInvoices.stream().map(this::mapToDto).toList();

        ListResponse<InvoiceDto> invoiceResponse = new ListResponse<>();
        invoiceResponse.setContent(content);
        invoiceResponse.setPageNo(invoices.getNumber());
        invoiceResponse.setPageSize(invoices.getSize());
        invoiceResponse.setTotalPages(invoices.getTotalPages());
        invoiceResponse.setTotalElements((int) invoices.getTotalElements());
        invoiceResponse.setLast(invoices.isLast());

        return invoiceResponse;
    }

    @Override
    public ListResponse<InvoiceDto> getInvoicesByPartnerId(long partnerId, int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public ListResponse<InvoiceDto> getInvoicesByInvoiceTypeId(long invoiceTypeId, int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    @Transactional
    public InvoiceDto updateInvoice(long invoiceId, InvoiceDto invoiceDto) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(()
                -> new ResourceNotFoundException("Invoice", "id", String.valueOf(invoiceId)));

        Order order = orderRepository.findById(invoiceDto.getOrderId()).orElseThrow(()
                -> new ResourceNotFoundException("Order", "id", String.valueOf(invoiceDto.getOrderId())));

        Partner partner = partnerRepository.findById(order.getPartner().getId()).orElseThrow(()
                -> new ResourceNotFoundException("Partner", "id", String.valueOf(order.getPartner().getId())));

        InvoiceType invoiceType = invoiceTypeRepository.findById(invoiceDto.getInvoiceTypeId()).orElseThrow(()
                -> new ResourceNotFoundException("Invoice type", "id", String.valueOf(invoiceDto.getInvoiceTypeId())));

        double oldMoneyAmount = invoice.getMoneyAmount();
        double newMoneyAmount = invoiceDto.getMoneyAmount();
        double restMoneyAmount = Math.abs(oldMoneyAmount - newMoneyAmount);
        if (newMoneyAmount > oldMoneyAmount) {
            partner.setDebt(partner.getDebt() - restMoneyAmount);
            order.setPaidMoney(order.getPaidMoney() + restMoneyAmount);
        } else {
            partner.setDebt(partner.getDebt() + restMoneyAmount);
            order.setPaidMoney(order.getPaidMoney() - restMoneyAmount);
        }

        if (order.getTotalMoney() == order.getPaidMoney()) {
            order.setOrderStatus(orderStatusRepository.findByName("Đã thanh toán"));
        } else {
            order.setOrderStatus(orderStatusRepository.findByName("Thanh toán một phần"));
        }

        invoice.setMoneyAmount(invoiceDto.getMoneyAmount());
        invoice.setInvoiceType(invoiceType);
        invoice.setOrder(order);

        if (invoiceDto.getPaymentType().equalsIgnoreCase("CASH")) {
            invoice.setPaymentType(PaymentType.CASH);
        } else {
            invoice.setPaymentType(PaymentType.TRANSFER);
        }

        invoiceRepository.save(invoice);

        return mapToDto(invoice);
    }

    @Override
    @Transactional
    public void deleteInvoiceById(long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", String.valueOf(invoiceId)));

        Order order = invoice.getOrder();
        order.setPaidMoney(order.getPaidMoney() - invoice.getMoneyAmount());
        String status = order.getPaidMoney() == 0.0 ? "Chưa thanh toán" :
                order.getPaidMoney() < order.getTotalMoney() ? "Thanh toán một phần" :
                        "Đã thanh toán";
        order.setOrderStatus(orderStatusRepository.findByName(status));

        Partner partner = order.getPartner();
        partner.setDebt(partner.getDebt() + invoice.getMoneyAmount());

        invoiceRepository.delete(invoice);
    }
}
