### ✅ Root Cause of the Error

The error you're encountering is:

```
java.lang.IllegalStateException: No thread-bound request found:
Are you referring to request attributes outside of an actual web request, or processing a request outside of the originally receiving thread?
...
at org.springframework.cloud.config.server.environment.HttpRequestConfigTokenProvider.getToken(HttpRequestConfigTokenProvider.java:43)
```

---

### 🔍 What Is Causing It?

The **`HttpRequestConfigTokenProvider`** is **Spring Cloud Config Server’s default way of retrieving Vault tokens**. It
tries to extract the token from the current `HttpServletRequest` (specifically from an HTTP header) — which **only
exists during an actual incoming HTTP request**.

But certain internal operations like:

* **health checks** (e.g., `/actuator/health`)
* **background config loading**
* **application startup fetches from Vault**

… **are not bound to an HTTP request**, which means there is no `HttpServletRequest` on the current thread. So when
`HttpRequestConfigTokenProvider.getToken()` is called, it fails with:

> `"No thread-bound request found"`

---

### 🧠 Why This Happens by Default

Spring Cloud Config Server uses the following bean **by default** for Vault token resolution:

```java

@Bean
@ConditionalOnMissingBean(ConfigTokenProvider.class)
public ConfigTokenProvider configTokenProvider() {
    return new HttpRequestConfigTokenProvider();
}
```

So **unless you explicitly override it**, **it will always attempt to read tokens from HTTP headers** — even when no
HTTP request is present.

---

### 🔥 How to Fix It (Summary)

To stop this, you must provide your own `ConfigTokenProvider` bean that **returns a static token** instead of looking at
the HTTP request.

```java

@Bean
public ConfigTokenProvider configTokenProvider() {
    return request -> "root"; // use your static token
}
```

This prevents Spring from trying to read the token from the request at all, eliminating the exception.

---

### ✅ Bottom Line

| 🔍 What went wrong                                   | 🛠 How to fix it                            |
|------------------------------------------------------|---------------------------------------------|
| `HttpRequestConfigTokenProvider` used by default     | Provide your own `ConfigTokenProvider` bean |
| No HTTP request context during health/startup        | Avoid accessing request attributes manually |
| Vault token resolved via request header (by default) | Use static token programmatically instead   |

Let me know if you’d like a full self-contained config example.
