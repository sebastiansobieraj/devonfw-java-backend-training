package com.devonfw.app.java.order.orderservice.logic.api.to;

import java.time.LocalDate;

import com.devonfw.app.java.order.general.common.api.OrderStatus;
import com.devonfw.app.java.order.general.common.api.to.AbstractSearchCriteriaTo;

/**
 * {@link SearchCriteriaTo} to find instances of {@link com.devonfw.app.java.order.orderservice.common.api.Order}s.
 */
public class OrderSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private Long id;

  private LocalDate creationDate;

  private Long ownerId;

  private Double price;

  private OrderStatus status;

  /**
   * @return idId
   */

  public Long getId() {

    return id;
  }

  /**
   * @param id setter for id attribute
   */

  public void setId(Long id) {

    this.id = id;
  }

  /**
   * @return creationDateId
   */

  public LocalDate getCreationDate() {

    return creationDate;
  }

  /**
   * @param creationDate setter for creationDate attribute
   */

  public void setCreationDate(LocalDate creationDate) {

    this.creationDate = creationDate;
  }

  /**
   * getter for ownerId attribute
   * 
   * @return ownerId
   */

  public Long getOwnerId() {

    return ownerId;
  }

  /**
   * @param owner setter for owner attribute
   */

  public void setOwnerId(Long ownerId) {

    this.ownerId = ownerId;
  }

  /**
   * @return priceId
   */

  public Double getPrice() {

    return price;
  }

  /**
   * @param price setter for price attribute
   */

  public void setPrice(Double price) {

    this.price = price;
  }

  /**
   * @return statusId
   */

  public OrderStatus getStatus() {

    return status;
  }

  /**
   * @param status setter for status attribute
   */

  public void setStatus(OrderStatus status) {

    this.status = status;
  }

}
