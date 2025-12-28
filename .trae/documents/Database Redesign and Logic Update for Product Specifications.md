I will redesign the database and update the code to support the requirement that multiple products (SKUs) can share the same `product_code` (representing a product model like "Huawei PC") but have different `category_id` (Specifications like "Black", "White").

### 1. Database Schema Changes (`src/main/resources/db/schema.sql`)
I will rewrite `schema.sql` with the following changes:
- **`tb_product`**: Remove the `UNIQUE` constraint on `code`. This allows multiple rows to have the same product code (e.g., Huawei PC - Black, Huawei PC - White).
- **`tb_category` (Specification Table)**: 
  - Rename the table comment to clarify it is the Specification table.
  - Add `product_code` column to link specifications to a product family.
  - Remove the global `UNIQUE` constraint on `name`.
  - Add a composite `UNIQUE` constraint on `(product_code, name)` so that "Black" can exist for both "Huawei PC" and "Apple PC", but only once per product code.

### 2. Entity Updates
- **`Category.java`**: Add `private String productCode;` field to match the new database column.

### 3. Service Logic Updates
- **`CategoryService` (Specification Logic)**:
  - Update `createCategory`: Validate uniqueness of specification name **scoped to `productCode`** instead of globally.
- **`ProductService`**:
  - Update `deleteProductById`: Implement cascading deletion. When a product (SKU) is deleted, delete its corresponding specification (Category) as well.
  - Add `deleteProductByCode`: Implement the new requirement.
    - Find all products with the given `code`.
    - Delete all these products.
    - Delete all specifications (`tb_category`) associated with this `product_code`.
  - Fix `getProductByCode`: Ensure it handles multiple results gracefully (returning the first match) since `code` is no longer unique.

### 4. Controller Updates
- **`ProductController`**: 
  - Add the new endpoint `DELETE /product/code/{code}` to expose the `deleteProductByCode` functionality.

*Note: I will interpret "Create/Delete Brand" in your request as a typo for "Create/Delete Category (Specification)", as the context strictly discusses Product and Specification tables.*
