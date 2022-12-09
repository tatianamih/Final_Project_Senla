package com.courses.senla.services.impl;

import com.courses.senla.exceptions.LimitPrecentException;
import com.courses.senla.exceptions.ResourceNotFoundException;
import com.courses.senla.models.Discount;
import com.courses.senla.models.User;
import com.courses.senla.repositories.DiscountRepository;
import com.courses.senla.services.DiscountService;
import com.courses.senla.services.GenerateId;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Log4j2
@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    @Value("${limit.percent.sum}")
    Double limitPercentsSum;

    @Autowired
    public DiscountServiceImpl(DiscountRepository repository) {
        this.discountRepository = repository;
    }

    @Transactional
    @Override
    public Page<Discount> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        log.info("get all discounts on page {}", pageNo);

        return discountRepository.findAll(paging);
    }

    @Override
    public List<Discount> getAll() {
        log.info("get all discounts");

        return discountRepository.findAll();
    }

    @Override
    public Discount getById(Long id) {
        log.info("get a discount by id {}", id);

        return discountRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("discount not found by id = ", id));
    }

    @Override
    public Discount update(Discount discount, Long id) {
        Discount updatedDiscount = getById(id);
        updatedDiscount.setName(discount.getName());
        updatedDiscount.setPercent(discount.getPercent());
        updatedDiscount.setDeleted(discount.getDeleted());
        updatedDiscount.setUser(discount.getUser());
        log.info("update discount -- {}", discount.getName());

        return discountRepository.save(updatedDiscount);
    }

    @Override
    public Page<Discount> getDiscountsByUserId(Integer pageNo, Integer pageSize, String sortBy, Long userId) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Discount> discountPage = discountRepository.findDiscountsByUser_Id(paging, userId);
        log.debug("get all discounts on page {} by user id {} ", pageNo, userId);
        return discountPage;
    }

    @Transactional
    @Override
    public Discount save(Discount discount) {
        List<Discount> discounts = getDiscountsByUserId(discount.getUser().getId());
        if (!checkPercentsSum(discounts, discount, limitPercentsSum)) {
            log.error("added percent is more then user's max percent sum ");
            throw new LimitPrecentException("added percent is more then user's max percent sum {} ",
                    calculationPercentsSum(discounts));
        } else {
            GenerateId generateId = () -> (long) discountRepository.findAll().size() + 1;
            discount.setId(generateId.generateId());
            discount.setDeleted(false);
            discount.setCreated(LocalDateTime.now());
            log.info("discount was saved");
            return discountRepository.save(discount);
        }
    }

    @Override
    public void generateDiscounts(User user) {
        Discount discount = new Discount();
        discount.setName("discount");
        discount.setPercent(0.00);
        discount.setDeleted(false);
        discount.setCreated(LocalDateTime.now());
        discountRepository.save(discount);
        discount.setUser(user);
    }

    @Override
    public List<Discount> getDiscountsByUserId(Long id) {
        return discountRepository.findDiscountsByUser_Id(id);
    }

    @Override
    public Double calculationPercentsSum(List<Discount> discounts) {
        return discounts.stream()
                .filter(d -> !d.getDeleted())
                .map(Discount::getPercent)
                .collect(Collectors.toList())
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private Boolean checkPercentsSum(List<Discount> discounts, Discount newDiscount, Double limitPercentsSum) {
        Double percentSum = calculationPercentsSum(discounts);
        return percentSum + newDiscount.getPercent() <= limitPercentsSum;
    }
}


