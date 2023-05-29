# 网络相关问题

## @Part parameters can only be used with multipart encoding. (parameter #3)
```
@Multipart
@POST("v1.0/me/drive/items/{itemId}/createUploadSession")
fun upload(@Header("Authorization") accessToken: String, @Path("itemId") itemId: String,
    @Part metaData: MultipartBody.Part, @Part media: MultipartBody.Part): Observable<Response<ResponseBody>>
```
