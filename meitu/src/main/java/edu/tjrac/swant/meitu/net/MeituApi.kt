package edu.tjrac.swant.meitu.net

import edu.tjrac.swant.meitu.bean.*
import retrofit2.http.*
import rx.Observable
import java.util.*

/**
 * Created by wpc on 2019/9/4.
 */
interface MeituApi {

    @GET("/v1/api/config/splash")
    fun getSplashInfo(): Observable<BR<ArrayList<SplashInfo>>>

    @POST("/v1/api/account/tokenlogin")
    fun tokenLogin(): Observable<BR<User>>

    @FormUrlEncoded
    @POST("/v1/api/account/login")
    fun login(@Field("account") account: String,
              @Field("pwd") pwd: String): Observable<BR<User>>


    //    @FormUrlEncoded
    @GET("/v1/api/model/list")
    fun getModelList(@Query("pageSize") pageSize: Int,
                     @Query("pageNo") pageNo: Int): Observable<BR<ArrayList<Model>>>


    @GET("/v1/api/model")
    fun getModelInfo(@Query("id") model_id: Int): Observable<BR<ModelDetail>>


    @GET("v1/api/like")
    fun like(@QueryMap map: HashMap<String, String>): Observable<BR<Int>>


    @GET("/v1/api/colum/list")
    fun getColumList(@Query("pageSize") pageSize: Int,
                     @Query("pageNo") pageNo: Int,
                     @Query("tag") tag: String?): Observable<BR<ArrayList<Colum>>>

    @GET("/v1/api/tag/hot")
    fun getHotTags(): Observable<BR<ArrayList<Tags>>>


    @POST("/v1/api/like/models")
    fun getFollowModelList(): Observable<BR<ArrayList<Like>>>

    @POST("/v1/api/like/colums")
    fun getFavouriteColumsList(): Observable<BR<ArrayList<Like>>>



    @GET("/v1/api/feedback/list")
    fun getFeedbackList(@Query("pageNo") pageNo: Int,
                        @Query("pageSize") pageSize: Int): Observable<BR<ArrayList<Feedback>>>

    @GET("/v1/api/colum")
    fun getColumDetails(@Query("model_id") modelid: Int?,
                       @Query("colum_id") id: Int?): Observable<BR<Colum>>

    @GET("/v1/api/home")
    fun getHomeData() : Observable<BR<HomeBean>>
}
