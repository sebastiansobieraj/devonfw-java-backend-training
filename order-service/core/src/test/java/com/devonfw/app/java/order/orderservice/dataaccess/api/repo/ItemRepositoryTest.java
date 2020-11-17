package com.devonfw.app.java.order.orderservice.dataaccess.api.repo;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.devonfw.app.java.order.orderservice.dataaccess.api.ItemEntity;
import com.devonfw.app.java.order.orderservice.logic.api.to.ItemSearchCriteriaTo;
import com.devonfw.module.test.common.base.ComponentTest;

/**
 * @author SSOBIERA
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ItemRepositoryTest extends ComponentTest {

  public static String ITALY = "Italy";

  public static String SPAGHETTI_BOLOGNESE = "spaghetti bolognese";

  public static String SPAGHETTI_CARBONARA = "spaghetti carbonara";

  public static Double PRICE_250 = 250.0;

  @Inject
  private ItemRepository itemRepository;

  @Test
  public void shouldFindAllItems() {

    // when
    List<ItemEntity> foundItems = this.itemRepository.findAll();

    // then
    assertThat(foundItems).hasSize(1);
    assertThat(foundItems.get(0).getName()).isEqualTo("spaghetti bolognese");
  }

  @Test
  public void shouldFindByAllCriteria() {

    // given
    ItemSearchCriteriaTo criteria = new ItemSearchCriteriaTo();
    criteria.setDescription(ITALY);
    criteria.setName(SPAGHETTI_BOLOGNESE);
    criteria.setPrice(PRICE_250);
    criteria.setPageable(PageRequest.of(0, 3));

    // when
    Page<ItemEntity> foundItems = this.itemRepository.findByCriteria(criteria);

    // then
    assertThat(foundItems.getContent()).hasSize(1);
    assertThat(foundItems.getContent()).extracting("name").containsExactly(SPAGHETTI_BOLOGNESE);
  }

  @Test
  public void shouldNotFindByName() {

    // given
    ItemSearchCriteriaTo criteria = new ItemSearchCriteriaTo();
    criteria.setName(SPAGHETTI_CARBONARA);
    criteria.setPageable(PageRequest.of(0, 3));

    // when
    Page<ItemEntity> foundItems = this.itemRepository.findByCriteria(criteria);

    // then
    assertThat(foundItems.getContent()).hasSize(0);
  }

  @Test
  public void shouldFindItemsByName() {

    // given
    ItemSearchCriteriaTo item = new ItemSearchCriteriaTo();
    item.setName("spaghetti bolognese");

    // when
    Page<ItemEntity> foundItem = this.itemRepository.findByCriteria(item);

    // then
    assertThat(foundItem).hasSize(1);
    assertThat(foundItem.getContent().get(0).getName()).isEqualTo("spaghetti bolognese");
  }

  @Test
  public void shouldFindByNameAndDescription() {

    // given
    ItemSearchCriteriaTo criteria = new ItemSearchCriteriaTo();
    criteria.setDescription(ITALY);
    criteria.setName(SPAGHETTI_BOLOGNESE);
    criteria.setPageable(PageRequest.of(0, 3));

    // when
    Page<ItemEntity> foundItems = this.itemRepository.findByCriteria(criteria);

    // then
    assertThat(foundItems.getContent()).hasSize(1);
    assertThat(foundItems.getContent()).extracting("description").containsExactly(ITALY);
  }

  @Test
  public void shouldFindByPrice() {

    // given
    ItemSearchCriteriaTo criteria = new ItemSearchCriteriaTo();
    criteria.setPrice(PRICE_250);
    criteria.setPageable(PageRequest.of(0, 3));

    // when
    Page<ItemEntity> foundItems = this.itemRepository.findByCriteria(criteria);

    // then
    assertThat(foundItems.getContent()).hasSize(1);
    assertThat(foundItems.getContent()).extracting("price").containsExactly(PRICE_250);
  }

  @Test
  public void shouldFindByNameAsc() {

    // when
    Page<ItemEntity> foundItems = this.itemRepository.findAndSortAllByName(SPAGHETTI_BOLOGNESE.toUpperCase(),
        PageRequest.of(0, 3));

    // then
    assertThat(foundItems.getContent()).hasSize(1);
    assertThat(foundItems.getContent()).extracting("name").containsExactly(SPAGHETTI_BOLOGNESE);
  }

  @Test
  public void shouldUpdateItemsPriceByGivenName() {

    // given
    ItemSearchCriteriaTo criteria = new ItemSearchCriteriaTo();
    criteria.setName(SPAGHETTI_BOLOGNESE);
    criteria.setDescription(ITALY);
    criteria.setPrice(PRICE_250);

    ItemEntity criteria2 = new ItemEntity();
    criteria2.setName("pallea");
    criteria2.setPrice(150.0);
    criteria2.setDescription("Spain");
    this.itemRepository.save(criteria2);

    // when
    List<ItemEntity> criteriaToBeUpdated = this.itemRepository.findByName("spaghetti bolognese");
    criteriaToBeUpdated.get(0).setPrice(criteria.getPrice() + 100.00);
    this.itemRepository.saveAll(criteriaToBeUpdated);

    // then
    List<ItemEntity> changedItems = this.itemRepository.findByName("spaghetti bolognese");
    assertThat(changedItems).extracting("price").containsOnly(350.0);

    List<ItemEntity> notChangedItems = this.itemRepository.findByName(criteria2.getName());
    assertThat(notChangedItems).extracting("price").containsOnly(150.0);
  }

}
