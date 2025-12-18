I will implement the Order Creation logic with OpenFeign and Seata integration.

**1. Mall-Common Updates**

* Create `InventoryDeductDTO` to transfer stock deduction data.

* Create `InventoryClient` (OpenFeign) to call Inventory Service.

* Create `CartClient` (OpenFeign) to call Cart Service.

**2. Inventory Service Updates**

* Add `deductStock` method in `InventoryService` and `InventoryServiceImpl`.

* Add `PUT /inventory/deduct` endpoint in `InventoryController` to handle stock deduction requests.

**3. Cart Service Updates**

* Add `removeByProductIds` method in `CartItemService` and `CartItemServiceImpl`.

* Add `DELETE /cart/clear` endpoint in `CartItemController` to remove purchased items.

**4. Order Service Updates**

* Update `Order` entity to include a transient `orderItems` list.

* Enable Feign Clients in `OrderServiceApplication`.

* Implement `createOrder` logic in `OrderServiceImpl`:

  * Save Order and OrderItems.

  * Call Inventory Service to deduct stock.

  * Call Cart Service to remove items.

  * (Seata `@GlobalTransactional` is already present).

**5. Verification**

* I will verify the code structure and ensure all necessary dependencies and logic are correctly implemented.

