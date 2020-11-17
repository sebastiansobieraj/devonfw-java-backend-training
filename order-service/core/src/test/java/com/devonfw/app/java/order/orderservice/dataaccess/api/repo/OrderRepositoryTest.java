package com.devonfw.app.java.order.orderservice.dataaccess.api.repo;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.devonfw.app.java.order.general.common.api.OrderStatus;
import com.devonfw.app.java.order.orderservice.dataaccess.api.CustomerEntity;
import com.devonfw.app.java.order.orderservice.dataaccess.api.ItemEntity;
import com.devonfw.app.java.order.orderservice.dataaccess.api.OrderEntity;
import com.devonfw.module.test.common.base.ComponentTest;

/**
 * @author SSOBIERA
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class OrderRepositoryTest extends ComponentTest {

  public static LocalDate SUCCESS_DATE = LocalDate.of(2019, Month.MARCH, 15);

  public static LocalDate FAILURE_DATE = LocalDate.of(2020, Month.DECEMBER, 1);

  @Inject
  private OrderRepository orderRepository;

  @Inject
  private CustomerRepository customerRepository;

  @Inject
  private ItemRepository itemRepository;

  @Test
  public void shouldFindAllOrders() {

    // when
    List<OrderEntity> foundOrders = this.orderRepository.findAll();

    // then
    assertThat(foundOrders).hasSize(1);
  }

  @Test
  public void shouldFindAllByCreationDateAndStatus() {

    // when
    Page<OrderEntity> foundOrders = this.orderRepository.findAllByCreationDateAndStatus(SUCCESS_DATE,
        OrderStatus.SERVED, PageRequest.of(0, 3));

    // then
    assertThat(foundOrders.getContent()).hasSize(1);
    assertThat(foundOrders.getContent()).extracting("creationDate").containsExactly(SUCCESS_DATE);
  }

  @Test
  public void shouldNotFindAllByCreationDateAndStatus() {

    // when
    Page<OrderEntity> foundOrders = this.orderRepository.findAllByCreationDateAndStatus(FAILURE_DATE,
        OrderStatus.SERVED, PageRequest.of(0, 3));

    // then
    assertThat(foundOrders.getContent()).hasSize(0);
  }

  @Test
  public void shouldCreateOrderWithTwoOrderPositionsAndOwnerSet() {

    // given
    CustomerEntity owner = new CustomerEntity();
    owner.setFirstname("Andrzej");
    owner.setLastname("Strzelba");

    ItemEntity firstItem = new ItemEntity();
    firstItem.setName("pasta");
    firstItem.setPrice(150.0);

    ItemEntity secondItem = new ItemEntity();
    secondItem.setName("pierogi");
    secondItem.setPrice(355.0);

    Set<ItemEntity> orderedPositions = new HashSet<>();
    orderedPositions.add(firstItem);
    orderedPositions.add(secondItem);

    OrderEntity order = new OrderEntity();
    order.setOwner(owner);
    order.setCreationDate(LocalDate.of(2019, 3, 15));
    order.setStatus(OrderStatus.SERVED);
    order.setOrderPositions(orderedPositions);

    this.customerRepository.save(owner);
    this.itemRepository.saveAll(orderedPositions);

    // when
    OrderEntity createdOrder = this.orderRepository.save(order);

    // then
    assertThat(createdOrder).isNotNull();
    assertThat(createdOrder.getOrderPositions()).hasSize(2);
    assertThat(createdOrder.getOwner()).isNotNull();
    assertThat(createdOrder.getOwner().getFirstname()).isEqualTo(owner.getFirstname());
    assertThat(createdOrder.getOwner().getLastname()).isEqualTo(owner.getLastname());
  }

}
