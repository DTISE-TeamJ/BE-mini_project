package com.example.BE_mini_project.events.repository;

import com.example.BE_mini_project.events.model.EventCategory;
import com.example.BE_mini_project.events.model.Events;
import com.example.BE_mini_project.events.model.TicketType;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class EventSpecification {
    public static Specification<Events> searchByKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) {
                return cb.conjunction();
            }
            String lowercaseKeyword = "%" + keyword.toLowerCase() + "%";

            Join<Events, EventCategory> categoryJoin = root.join("eventCategory", JoinType.LEFT);

            return cb.or(
                    cb.like(cb.lower(root.get("name")), lowercaseKeyword),
                    cb.like(cb.lower(root.get("location")), lowercaseKeyword),
                    cb.like(cb.lower(categoryJoin.get("name")), lowercaseKeyword),
                    cb.like(cb.lower(root.get("organization")), lowercaseKeyword)

            );
        };
    }

    public static Specification<Events> hasCategory(String categoryName) {
        return (root, query, cb) -> {
            if (categoryName == null || categoryName.isEmpty()) {
                return cb.conjunction();
            }
            Join<Events, EventCategory> categoryJoin = root.join("eventCategory", JoinType.INNER);
            return cb.equal(cb.lower(categoryJoin.get("name")), categoryName.toLowerCase());
        };
    }

    public static Specification<Events> hasLocation(String location) {
        return (root, query, cb) -> {
            if (location == null || location.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("location"), location);
        };
    }

    public static Specification<Events> dateRangeBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) {
                return cb.conjunction();
            }
            if (startDate == null) {
                return cb.lessThanOrEqualTo(root.get("startDate"), endDate);
            }
            if (endDate == null) {
                return cb.greaterThanOrEqualTo(root.get("endDate"), startDate);
            }
            return cb.and(
                    cb.greaterThanOrEqualTo(root.get("endDate"), startDate),
                    cb.lessThanOrEqualTo(root.get("startDate"), endDate)
            );
        };
    }

    public static Specification<Events> isNotDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    public static Specification<Events> priceRangeBetween(Double minPrice, Double maxPrice) {
        return (root, query, cb) -> {
            Join<Events, TicketType> ticketTypeJoin = root.join("ticketTypes", JoinType.LEFT);
            Subquery<Double> minPriceSubquery = query.subquery(Double.class);
            Root<Events> subRoot = minPriceSubquery.from(Events.class);
            Join<Events, TicketType> subTicketTypeJoin = subRoot.join("ticketTypes", JoinType.LEFT);
            minPriceSubquery.select(cb.min(subTicketTypeJoin.get("price")))
                    .where(cb.equal(subRoot.get("id"), root.get("id")));

            Predicate pricePredicate = cb.and(
                    minPrice == null ? cb.conjunction() : cb.greaterThanOrEqualTo(minPriceSubquery, minPrice),
                    maxPrice == null ? cb.conjunction() : cb.lessThanOrEqualTo(minPriceSubquery, maxPrice)
            );

            query.groupBy(root.get("id"));
            return pricePredicate;
        };
    }

    public static Specification<Events> sortByTicketPrice(Sort.Direction direction) {
        return (root, query, cb) -> {
            Subquery<Double> priceSubquery = query.subquery(Double.class);
            Root<Events> subRoot = priceSubquery.from(Events.class);
            Join<Events, TicketType> ticketTypeJoin = subRoot.join("ticketTypes", JoinType.LEFT);

            if (direction == Sort.Direction.DESC) {
                priceSubquery.select(cb.max(ticketTypeJoin.get("price")))
                        .where(cb.equal(subRoot.get("id"), root.get("id")));
            } else {
                priceSubquery.select(cb.min(ticketTypeJoin.get("price")))
                        .where(cb.equal(subRoot.get("id"), root.get("id")));
            }

            query.orderBy(direction == Sort.Direction.DESC ?
                    cb.desc(priceSubquery) : cb.asc(priceSubquery));

            return cb.conjunction();
        };
    }
}