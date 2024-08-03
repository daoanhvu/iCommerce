package com.icommerce.shopping.service.impl;

import com.icommerce.shopping.common.Constants;
import com.icommerce.shopping.dto.PageableContent;
import com.icommerce.shopping.dto.PageableRequest;
import com.icommerce.shopping.dto.ServiceResponse;
import com.icommerce.shopping.model.Product;
import com.icommerce.shopping.repository.ProductRepository;
import com.icommerce.shopping.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ServiceResponse<Product> getProductById(Long id) {
        ServiceResponse<Product> response = new ServiceResponse<>();
        Optional<Product> optProd = productRepository.findById(id);
        if(optProd.isEmpty()) {
            response.setServiceCode(Constants.SERVICE_CODE_NOT_FOUND);
            response.setServiceMessage("Product not found");
            response.setHttpStatus(HttpStatus.NOT_FOUND.value());
            LOG.debug("Product id {} not found.", id);
            return response;
        }
        response.setServiceCode(Constants.SERVICE_CODE_OK);
        response.setHttpStatus(HttpStatus.OK.value());
        response.setResult(optProd.get());
        return response;
    }

    public ServiceResponse<PageableContent<Product>> searchProduct(PageableRequest request) {
        ServiceResponse<PageableContent<Product>> result = new ServiceResponse<>();
        if(request.getSorts() != null && !(request.getSorts().isEmpty())) {
            List<Sort.Order> orders = request.getSorts().stream().map(o -> new Sort.Order(Sort.Direction.fromString(o.getOrder()), o.getFieldName())).collect(Collectors.toList());

            Page<Product> pageResult = productRepository.findAll((Specification<Product>) ((root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates = buildSearchPredicates(request.getQuery(), root, criteriaBuilder);
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }), PageRequest.of(request.getPage(), request.getSize(), Sort.by(orders)));
            PageableContent<Product> content = new PageableContent<>();
            content.setTotalElements(pageResult.getTotalElements());
            content.setTotalPages(pageResult.getTotalPages());
            content.setNumberOfElements(pageResult.getNumberOfElements());
            content.setPage(request.getPage());
            content.setSize(request.getSize());
            content.setContent(pageResult.getContent());
            result.setResult(content);
            result.setServiceCode(Constants.SERVICE_CODE_OK);
            result.setHttpStatus(HttpStatus.OK.value());
            return result;
        }

        Page<Product> pageResult = productRepository.findAll((Specification<Product>) ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = buildSearchPredicates(request.getQuery(), root, criteriaBuilder);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }), PageRequest.of(request.getPage(), request.getSize()));
        PageableContent<Product> content = new PageableContent<>();
        content.setTotalElements(pageResult.getTotalElements());
        content.setTotalPages(pageResult.getTotalPages());
        content.setNumberOfElements(pageResult.getNumberOfElements());
        content.setPage(request.getPage());
        content.setSize(request.getSize());
        content.setContent(pageResult.getContent());
        result.setResult(content);
        result.setServiceCode(Constants.SERVICE_CODE_OK);
        result.setHttpStatus(HttpStatus.OK.value());
        return result;
    }

    private List<Predicate> buildSearchPredicates(final HashMap<String, Object> query,
                                                  Root<Product> root,
                                                  CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if(query == null) {
            return predicates;
        }

        if(query.containsKey("brand")) {
            String brand = (String)query.get("brand");
            if(StringUtils.isNoneBlank(brand)) {
                Predicate branchPredicate = criteriaBuilder.equal(criteriaBuilder.lower(root
                        .get("brand")), brand.toLowerCase());
                predicates.add(criteriaBuilder.or(branchPredicate));
            }
        }

        if(query.containsKey("category")) {
            String category = (String)query.get("category");
            if(category != null && category.length() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }
        }

        if(query.containsKey("name")) {
            String name = (String)query.get("name");
            if(name != null && name.length() > 0) {
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root
                        .get("name")), "%" + name.toLowerCase() + "%");
                predicates.add(namePredicate);
            }
        }

        if(query.containsKey("price")) {
            Map<String, Object> price = (Map<String, Object>) query.get("price");
            if(price.containsKey("from")) {
                Double priceFrom = Double.parseDouble(price.get("from").toString());
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceFrom));
            }
            if(price.containsKey("to")) {
                Double priceTo = Double.parseDouble(price.get("to").toString());
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceTo));
            }
        }

        return predicates;
    }
}
