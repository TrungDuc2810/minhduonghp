package com.example.TTTN.service.impl;

import com.example.TTTN.entity.Partner;
import com.example.TTTN.entity.Payment;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PaymentDto;
import com.example.TTTN.repository.PartnerRepository;
import com.example.TTTN.repository.PaymentRepository;
import com.example.TTTN.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private final PartnerRepository partnerRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, ModelMapper modelMapper, PartnerRepository partnerRepository) {
        this.paymentRepository = paymentRepository;
        this.modelMapper = modelMapper;
        this.partnerRepository = partnerRepository;
    }

    private PaymentDto mapToDto(Payment payment) {
        return modelMapper.map(payment, PaymentDto.class);
    }

    private Payment mapToEntity(PaymentDto paymentDto) {
        return modelMapper.map(paymentDto, Payment.class);
    }

    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        Payment payment = mapToEntity(paymentDto);
        return mapToDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentDto getPaymentById(long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(()
                -> new ResourceNotFoundException("Payment", "id", String.valueOf(paymentId)));
        return mapToDto(payment);
    }

    @Override
    public ListResponse<PaymentDto> getAllPayments(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<Payment> payments = paymentRepository.findAll(pageRequest);

        List<Payment> listOfPayments = payments.getContent();

        List<PaymentDto> content = listOfPayments.stream().map(this::mapToDto).toList();

        ListResponse<PaymentDto> paymentResponse = new ListResponse<>();
        paymentResponse.setContent(content);
        paymentResponse.setPageNo(payments.getNumber());
        paymentResponse.setPageSize(payments.getSize());
        paymentResponse.setTotalPages(payments.getTotalPages());
        paymentResponse.setTotalElements((int) payments.getTotalElements());
        paymentResponse.setLast(payments.isLast());

        return paymentResponse;
    }

    @Override
    public ListResponse<PaymentDto> getPaymentsByPartnerId(long partnerId, int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public PaymentDto updatePayment(long paymentId, PaymentDto paymentDto) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(()
                -> new ResourceNotFoundException("Payment", "id", String.valueOf(paymentId)));

        Partner partner = partnerRepository.findById(paymentDto.getPartnerId()).orElseThrow(()
                -> new ResourceNotFoundException("Partner", "id", String.valueOf(paymentDto.getPartnerId())));

        payment.setPaymentAmount(paymentDto.getPaymentAmount());
        payment.setPartner(partner);

        paymentRepository.save(payment);

        return mapToDto(payment);
    }

    @Override
    public void deletePaymentById(long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(()
                -> new ResourceNotFoundException("Partner", "id", String.valueOf(paymentId)));
        paymentRepository.delete(payment);
    }
}
