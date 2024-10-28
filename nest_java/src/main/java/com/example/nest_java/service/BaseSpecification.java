package com.example.nest_java.service;

import com.example.nest_java.dto.FilterQueryDTO;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseSpecification<E> implements Specification<E> {
    private final Map<String, ArrayList<String>> filters;

    private final Map<String, ArrayList<String>> skip;

 //   private final Map<String, Object> extend;

    private final String fullTextSearch;

    private final Map<String, String> where;

    public BaseSpecification(FilterQueryDTO filterQueryDTO) {

        this.filters = filterQueryDTO.getFilter();
        this.skip = filterQueryDTO.getSkip();
  //      this.extend = filterQueryDTO.getExtend();
        this.fullTextSearch = filterQueryDTO.getFullTextSearch();
        this.where = filterQueryDTO.getWhere();
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.isNull(root.get("deletedAt")));
        predicates.add(criteriaBuilder.isNull(root.get("disableAt")));

        //Adding Where Conditions
        if (where != null && !where.isEmpty()) {
            where.forEach((key, value) -> {
                if (key.split("_").length == 1) {
                    predicates.add(criteriaBuilder.equal(root.get(key), value));
                } else {
                    Join<Object, Object> objectJoin = root.join(key.split("_")[0], JoinType.INNER);
                    predicates.add(criteriaBuilder.equal(objectJoin.get(key.split("_")[1]), value));
                }
            });
        }


        // Adding Filters Conditions (IN ? BETWEEN)
        if (filters != null && !filters.isEmpty()) {
            filters.forEach((key, value) -> {
                if (((isInstant(value.get(0)) && isInstant(value.get(1))) || (isBigDecimal(value.get(0)) && isBigDecimal(value.get(1)))) && value.size() == 2) {
                    predicates.add(criteriaBuilder.between(root.get(key), value.get(0), value.get(1)));

                } else {
                    predicates.add(root.get(key).in(value));
                }
            });
        }

        // Adding Skip Conditions (NOT IN / BETWEEN)
        if (skip != null && !skip.isEmpty()) {
            skip.forEach((key, value) -> {
                if (((isInstant(value.get(0)) && isInstant(value.get(1))) || (isBigDecimal(value.get(0)) && isBigDecimal(value.get(1)))) && value.size() == 2) {
                    predicates.add(criteriaBuilder.not(criteriaBuilder.between(root.get(key), value.get(0), value.get(1))));
                } else {
                    predicates.add(criteriaBuilder.not(root.get(key).in(value)));

                }
            });
        }
        if (fullTextSearch != null && !fullTextSearch.isEmpty()) {
            Predicate orPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(root.get("name"), "%" + fullTextSearch + "%"),
                    criteriaBuilder.like(root.get("description"), "%" + fullTextSearch + "%")
            );

            predicates.add(orPredicate);
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private static boolean isInstant(String input) {

        try {
            Instant.parse(input);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean isBigDecimal(String input) {

        try {
            new BigDecimal(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
