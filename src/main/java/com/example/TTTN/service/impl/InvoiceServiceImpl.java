package com.example.TTTN.service.impl;

import com.example.TTTN.entity.*;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.InvoiceDto;
import com.example.TTTN.repository.*;
import com.example.TTTN.service.InvoiceService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public InvoiceDto createInvoice(InvoiceDto invoiceDto) {
        Order order = orderRepository.findById(invoiceDto.getOrderId()).orElseThrow(()
                -> new ResourceNotFoundException("Order", "id", String.valueOf(invoiceDto.getOrderId())));

        Partner partner = partnerRepository.findById(invoiceDto.getPartnerId()).orElseThrow(()
                -> new ResourceNotFoundException("Partner", "id", String.valueOf(invoiceDto.getPartnerId())));

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
    public InvoiceDto updateInvoice(long invoiceId, InvoiceDto invoiceDto) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(()
                -> new ResourceNotFoundException("Invoice", "id", String.valueOf(invoiceId)));

        Partner partner = partnerRepository.findById(invoiceDto.getPartnerId()).orElseThrow(()
                -> new ResourceNotFoundException("Partner", "id", String.valueOf(invoiceDto.getPartnerId())));

        InvoiceType invoiceType = invoiceTypeRepository.findById(invoiceDto.getInvoiceTypeId()).orElseThrow(()
                -> new ResourceNotFoundException("Invoice type", "id", String.valueOf(invoiceDto.getInvoiceTypeId())));

        Order order = orderRepository.findById(invoiceDto.getOrderId()).orElseThrow(()
                -> new ResourceNotFoundException("Order", "id", String.valueOf(invoiceDto.getOrderId())));

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
        invoice.setPartner(partner);
        invoice.setInvoiceType(invoiceType);
        invoice.setOrder(order);

        invoiceRepository.save(invoice);

        return mapToDto(invoice);
    }

    @Override
    public void deleteInvoiceById(long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(()
                -> new ResourceNotFoundException("Partner", "id", String.valueOf(invoiceId)));
        invoiceRepository.delete(invoice);
    }
}
