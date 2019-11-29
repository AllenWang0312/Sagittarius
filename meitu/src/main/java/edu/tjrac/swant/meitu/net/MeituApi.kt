package edu.tjrac.swant.meitu.net

import com.google.gson.JsonObject
import edu.tjrac.swant.meitu.bean.*
import edu.tjrac.swant.meitu.bean.stuct.LoginRespon
import retrofit2.http.*
import rx.Observable
import java.util.*

/**
 * Created by wpc on 2019/9/4.
 */
interface MeituApi {

    @GET("/v1/api/config/splash")
    fun getSplashInfo(): Observable<BR<ArrayList<SplashInfo>>>

    @FormUrlEncoded
    @POST("/v1/api/account/login")
    fun login(@Field("account") account: String,
              @Field("pwd") pwd: String,
              @Field("device") device: String): Observable<BR<LoginRespon>>

    @FormUrlEncoded
    @POST("/v1/api/account/tokenlogin")
    fun tokenLogin(@Field("device") device: String): Observable<BR<LoginRespon>>


    @FormUrlEncoded
    @POST("/v1/api/account/register")
    fun register(@Field("account") account: String,
                 @Field("pwd") pwd: String): Observable<BR<Any>>

    //    @FormUrlEncoded
    @GET("/v1/api/model/list")
    fun getModelList(@Query("pageSize") pageSize: Int,
                     @Query("pageNo") pageNo: Int): Observable<BR<ArrayList<Model>>>


    @GET("/v1/api/model")
    fun getModelInfo(@Query("id") model_id: Int): Observable<BR<ModelDetail>>


    @GET("v1/api/like")
    fun like(@QueryMap map: HashMap<String, String>): Observable<BR<Int>>


    @GET("/v1/api/album/list")
    fun getColumList(@Query("pageSize") pageSize: Int,
                     @Query("pageNo") pageNo: Int,
                     @Query("tag") tag: String?): Observable<BR<ArrayList<Album>>>

    @GET("/v1/api/tag/hot")
    fun getHotTags(): Observable<BR<ArrayList<Tags>>>


    @POST("/v1/api/like/models")
    fun getFollowModelList(): Observable<BR<ArrayList<Like>>>

    @POST("/v1/api/like/albums")
    fun getFavouriteColumsList(): Observable<BR<ArrayList<Like>>>


    @GET("/v1/api/feedback/list")
    fun getFeedbackList(@Query("pageNo") pageNo: Int,
                        @Query("pageSize") pageSize: Int): Observable<BR<ArrayList<Feedback>>>

    @GET("/v1/api/album")
    fun getColumDetails(@Query("model_id") modelid: Int?,
                        @Query("colum_id") id: Int?): Observable<BR<Album>>


    @GET("/v1/api/m/tabs")
    fun getTabs(@Query("gender") gender: String?,
                @Query("pageNo") pageNo: Int): Observable<BR<ArrayList<Tab>>>

    @POST("/v1/api/m/tabs/follow")
    fun followTabs(@Body selected: ArrayList<Tab>?): Observable<BR<JsonObject>>

    @GET("/v1/api/m/tabs/followed")
    fun followedTabs(): Observable<BR<ArrayList<Tab>>>


    @GET("/v1/api/m/home")
    fun getHomeData(@Query("contry") contry: String?): Observable<BR<HomeBean>>


    @GET("/v1/api/m/home/zone")
    fun getZoneHistroy(@Query("year") year: String,
                       @Query("month") month: String,
                       @Query("pageNo") pageNo: Int,
                       @Query("pageSize") pageSize: Int): Observable<BR<ArrayList<Zone>>>
}
