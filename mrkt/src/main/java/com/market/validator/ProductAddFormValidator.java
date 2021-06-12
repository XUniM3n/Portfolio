package com.market.validator;

import com.market.form.ProductAddForm;
import com.market.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProductAddFormValidator implements Validator {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.getName().equals(ProductAddForm.class.getName());
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductAddForm form = (ProductAddForm) o;

        ValidationUtils.rejectIfEmpty(errors, "name", "empty.name", "Empty name");
        ValidationUtils.rejectIfEmpty(errors, "price", "empty.price", "Empty price");
        ValidationUtils.rejectIfEmpty(errors, "city", "empty.city", "Empty city");
        ValidationUtils.rejectIfEmpty(errors, "description", "empty.description", "Empty description");

        if (!cityRepository.findById(form.getCity()).isPresent())
            errors.reject("invalid.city", "No such city");

        if (form.getDescription().length() > 1000)
            errors.reject("invalid.description", "Description length should be less than 1000");
    }
}
