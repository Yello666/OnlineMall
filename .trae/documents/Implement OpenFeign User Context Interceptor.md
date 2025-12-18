I will implement an OpenFeign Request Interceptor in the common module to propagate user information to downstream microservices.

**Implementation Steps:**

1.  **Modify `DefaultFeignConfig.java`**:
    -   Located at `mall-common/src/main/java/com/emily/mall/common/config/DefaultFeignConfig.java`.
    -   Add a new `@Bean` of type `RequestInterceptor`.
    -   Inside the interceptor's `apply` method:
        -   Retrieve the current user's ID, username, and role from `UserContextHolder`.
        -   If these values exist, add them to the Feign request headers (`X-User-Id`, `X-User-Name`, `X-User-Role`) to ensure downstream services can retrieve them via their `UserContextInterceptor`.

**Verification:**
-   Since this is a common component change, I will verify the code structure and syntax.
-   I will verify that the header names match those expected by `UserContextInterceptor` (`X-User-Id`, `X-User-Name`, `X-User-Role`).
