package com.courses.senla.services;


import com.courses.senla.models.Discount;
import com.courses.senla.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DiscountService {

   Discount save(Discount discount);

   Page<Discount> getAll(Integer pageNo, Integer pageSize, String sortBy);

   List<Discount> getAll();

   Discount getById(Long id);

   Discount update(Discount discount, Long id);

   Page<Discount>getDiscountsByUserId(Integer pageNo, Integer pageSize, String sortBy,Long userId);

   void generateDiscounts(User user);

   List<Discount> getDiscountsByUserId(Long id);

   Double calculationPercentsSum(List<Discount> discounts);
}
