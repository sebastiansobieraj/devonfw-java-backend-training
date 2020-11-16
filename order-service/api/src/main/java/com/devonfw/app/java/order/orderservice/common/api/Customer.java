package com.devonfw.app.java.order.orderservice.common.api;

import com.devonfw.app.java.order.general.common.api.ApplicationEntity;

public interface Customer extends ApplicationEntity {

  /**
   * @return idId
   */

  public Long getId();

  /**
   * @param id setter for id attribute
   */

  public void setId(Long id);

  /**
   * @return firstnameId
   */

  public String getFirstname();

  /**
   * @param firstname setter for firstname attribute
   */

  public void setFirstname(String firstname);

  /**
   * @return lastnameId
   */

  public String getLastname();

  /**
   * @param lastname setter for lastname attribute
   */

  public void setLastname(String lastname);

}
