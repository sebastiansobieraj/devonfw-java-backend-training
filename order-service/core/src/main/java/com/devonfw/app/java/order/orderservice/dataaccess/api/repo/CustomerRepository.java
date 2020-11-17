package com.devonfw.app.java.order.orderservice.dataaccess.api.repo;

import static com.querydsl.core.alias.Alias.$;

import java.util.Iterator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.devonfw.app.java.order.orderservice.dataaccess.api.CustomerEntity;
import com.devonfw.app.java.order.orderservice.logic.api.to.CustomerSearchCriteriaTo;
import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * {@link DefaultRepository} for {@link CustomerEntity}
 */
public interface CustomerRepository extends DefaultRepository<CustomerEntity> {

  /**
   * @param criteria the {@link CustomerSearchCriteriaTo} with the criteria to search.
   * @return the {@link Page} of the {@link CustomerEntity} objects that matched the search. If no pageable is set, it
   *         will return a unique page with all the objects that matched the search.
   */
  default Page<CustomerEntity> findByCriteria(CustomerSearchCriteriaTo criteria) {

    CustomerEntity alias = newDslAlias();
    JPAQuery<CustomerEntity> query = newDslQuery(alias);

    Long id = criteria.getId();
    if (id != null) {
      query.where($(alias.getId()).eq(id));
    }
    String firstname = criteria.getFirstname();
    if (firstname != null && !firstname.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getFirstname()), firstname, criteria.getFirstnameOption());
    }
    String lastname = criteria.getLastname();
    if (lastname != null && !lastname.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getLastname()), lastname, criteria.getLastnameOption());
    }
    if (criteria.getPageable() == null) {
      criteria.setPageable(PageRequest.of(0, Integer.MAX_VALUE));
    } else {
      addOrderBy(query, alias, criteria.getPageable().getSort());
    }

    return QueryUtil.get().findPaginated(criteria.getPageable(), query, true);
  }

  @Transactional
  @Modifying
  public Long removeById(Long id);

  /**
   * Add sorting to the given query on the given alias
   *
   * @param query to add sorting to
   * @param alias to retrieve columns from for sorting
   * @param sort specification of sorting
   */
  public default void addOrderBy(JPAQuery<CustomerEntity> query, CustomerEntity alias, Sort sort) {

    if (sort != null && sort.isSorted()) {
      Iterator<Order> it = sort.iterator();
      while (it.hasNext()) {
        Order next = it.next();
        switch (next.getProperty()) {
          case "id":
            if (next.isAscending()) {
              query.orderBy($(alias.getId().toString()).asc());
            } else {
              query.orderBy($(alias.getId().toString()).desc());
            }
            break;
          case "firstname":
            if (next.isAscending()) {
              query.orderBy($(alias.getFirstname()).asc());
            } else {
              query.orderBy($(alias.getFirstname()).desc());
            }
            break;
          case "lastname":
            if (next.isAscending()) {
              query.orderBy($(alias.getLastname()).asc());
            } else {
              query.orderBy($(alias.getLastname()).desc());
            }
            break;
          default:
            throw new IllegalArgumentException("Sorted by the unknown property '" + next.getProperty() + "'");
        }
      }
    }
  }

}