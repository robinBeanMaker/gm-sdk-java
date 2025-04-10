# TestApi

All URIs are relative to *https://localhost:9090*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getInfo**](TestApi.md#getInfo) | **GET** /openApi/test/info |  |


<a id="getInfo"></a>
# **getInfo**
> ResponseResultTestVO getInfo(name, age)



获取基本信息

### Example
```java
// Import classes:
import com.trustasia.tagm.core.ApiClient;
import com.trustasia.tagm.core.ApiException;
import com.trustasia.tagm.core.Configuration;
import com.trustasia.tagm.core.auth.*;
import com.trustasia.tagm.core.models.*;
import com.trustasia.tagm.api.TestApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://localhost:9090");
    
    TestApi apiInstance = new TestApi(defaultClient);
    Object name = null; // Object | 名称
    Object age = null; // Object | 年龄
    try {
      ResponseResultTestVO result = apiInstance.getInfo(name, age);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling TestApi#getInfo");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **name** | [**Object**](.md)| 名称 | |
| **age** | [**Object**](.md)| 年龄 | |

### Return type

[**ResponseResultTestVO**](ResponseResultTestVO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **401** | Unauthorized |  -  |
| **400** | Bad Request |  -  |
| **403** | Forbidden |  -  |
| **429** | Too Many Requests |  -  |
| **500** | Internal Server Error |  -  |
| **200** | OK |  -  |

