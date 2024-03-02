package com.app.bigprize.Network;

import com.app.bigprize.Async.Models.Big_Api_Response;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Big_ApiInterface {
    @FormUrlEncoded
    @POST("GHYHTGEOOEW")
    Call<Big_Api_Response> getHomeData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("DBDGHFGTHNJK")
    Call<Big_Api_Response> getMoreAppsData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("BJDKIDJFKIKJKKDORI")
    Call<Big_Api_Response> saveMinesweeper(@Header("Token") String token, @Header("Secret") String random, @Field("details") String details);

    @FormUrlEncoded
    @POST("GFHGHUHJHJ")
    Call<Big_Api_Response> loginUser(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);
    @FormUrlEncoded
    @POST("BBTBGFFFFBBX")
    Call<Big_Api_Response> getBrainData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);
    @FormUrlEncoded
    @POST("SSAAQQUUOOMMGG")
    Call<Big_Api_Response> saveBrainsData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);
    @FormUrlEncoded
    @POST("QHGDHDJTBXJMSKSPH")
    Call<Big_Api_Response> getCardsData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);
    @FormUrlEncoded
    @POST("KSISKKSISIQJNDOPS")
    Call<Big_Api_Response> saveCardsData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);



    @FormUrlEncoded
    @POST("GBNHJMKLOOPI")
    Call<Big_Api_Response> getUserProfile(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);



    @FormUrlEncoded
    @POST("EditProfileData")
    Call<Big_Api_Response> editUserProfile(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);



    @FormUrlEncoded
    @POST("GADUIOGFHFH")
    Call<Big_Api_Response> getNotificationData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);



    @FormUrlEncoded
    @POST("DFGHFGJGHITRTRR")
    Call<Big_Api_Response> getFAQ(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);
    @FormUrlEncoded
    @POST("GDUDNHDHJDYJKBDF")
    Call<Big_Api_Response> getMinesweeper(@Header("Token") String token, @Header("Secret") String random, @Field("details") String details);


    @Multipart
    @POST("NHMJKLOPB")
    Call<Big_Api_Response> submitFeedback(@Part("details") RequestBody details, @Part MultipartBody.Part Image);


    @FormUrlEncoded
    @POST("NSHPSBNCHSLGVDEHQRR")
    Call<Big_Api_Response> getSpinData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);



    @FormUrlEncoded
    @POST("YBQDVSLPBGZZHGSGGKMNS")
    Call<Big_Api_Response> saveSpinData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);



    @FormUrlEncoded
    @POST("TYHJNFKKAJD")
    Call<Big_Api_Response> getPointHistory(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);



    @FormUrlEncoded
    @POST("lIONESSCVBC")
    Call<Big_Api_Response> getInviteAsync(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);



    @FormUrlEncoded
    @POST("FGBNHJMUIPJJ")
    Call<Big_Api_Response> getTaskOfferList(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);


    @FormUrlEncoded
    @POST("TYSLSTGSWJSHYHYH")
    Call<Big_Api_Response> shareTaskOffer(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);



    @FormUrlEncoded
    @POST("TYHJUIQWCVFG")
    Call<Big_Api_Response> getTaskDetails(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);


    @Multipart
    @POST("TYGBVCFDSFGG")
    Call<Big_Api_Response> taskImageUpload(@Part("details") RequestBody details, @Part MultipartBody.Part Image);
    @FormUrlEncoded
    @POST("GEURFRJIGMDAUN")
    Call<Big_Api_Response> getColorData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);


    @FormUrlEncoded
    @POST("HLSIJNMAMOWIM")
    Call<Big_Api_Response> saveColorData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);


    @FormUrlEncoded
    @POST("YJKFGHFGHXD")
    Call<Big_Api_Response> getWithdrawalType(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("YHJDFNHJGH")
    Call<Big_Api_Response> getWithdrawTypeList(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);



    @FormUrlEncoded
    @POST("QETBGJOLPJ")
    Call<Big_Api_Response> redeemPoints(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("DFGDFHDFHFG")
    Call<Big_Api_Response> saveDailyBonus(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @GET
    Call<JsonObject> callApi(@Url String Value,//http://surgex.media-412.com/click
                             @Query("pid") String pid,//9
                             @Query("offer_id") String o_id,//1548
                             @Query("sub5") String installed_package_id,
                             @Query("sub3") String gaid,
                             @Query("sub2") String current_app_package_id,
                             @Query("sub1") String unique_click_id,//device_id
                             @Query("sub7") String ip_address);

    @FormUrlEncoded
    @POST("PAFKAVANOHATO")
    Call<JsonObject> savePackageTracking(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("TSILNEERSDRAWERTEG")
    Call<Big_Api_Response> getRewardScreenData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("NDJHATRNLMFBYS")
    Call<Big_Api_Response> getWatchVideoList(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("GSMJDQRTDMGM")
    Call<Big_Api_Response> saveWatchVideo(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("YHNVBFGDFGGHK")
    Call<Big_Api_Response> getGiveAwayList(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("QWNIOASOPPYDEWQVNST")
    Call<Big_Api_Response> saveGiveAway(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("PJDGQHBXUDPNCYDQGIBSLI")
    Call<Big_Api_Response> getScratchcard(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("OODNDGFGSSESBSIVDK")
    Call<Big_Api_Response> saveScratchcard(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("INXAERHNBBJSTIOLJ")
    Call<Big_Api_Response> getLuckyNumber(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("SANDYONDKHFITMIAA")
    Call<Big_Api_Response> saveLuckyNumberContest(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("YETRRGPMSADGPIM")
    Call<Big_Api_Response> getLuckyNumberHistory(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("WhatsappLogin")
    Call<Big_Api_Response> loginWithWhatsApp(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);



    @FormUrlEncoded
    @POST("RBYWQNSIPIUUNANNAQKL")
    Call<Big_Api_Response> getImagePuzzleData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("WQVBERREHAZXXGWOPUH")
    Call<Big_Api_Response> saveImagePuzzleData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);


    @FormUrlEncoded
    @POST("NEJFYFNRJDGFNDIO")
    Call<Big_Api_Response> getAlphabetData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);
    @FormUrlEncoded
    @POST("JDFLHJUFIUJQEUO")
    Call<Big_Api_Response> saveAlphabetData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);


    @FormUrlEncoded
    @POST("OJSAETCXJHDSPQGBDHS")
    Call<Big_Api_Response> getQuizData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("IRUBNTTAQZZSDDFFSJGHH")
    Call<Big_Api_Response> saveQuizData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("DDFGDHDYNBCGGKSBDF")
    Call<Big_Api_Response> getQuizHistory(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("AKELMBNIEYCAUY")
    Call<Big_Api_Response> getDailyRewardList(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("LSISBCHDIDJP")
    Call<Big_Api_Response> saveDailyReward(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("TGHNMFDGHGFGFSD")
    Call<Big_Api_Response> deleteAccount(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("getGamesList")
    Call<Big_Api_Response> getGamesList(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("getGamesDetails")
    Call<Big_Api_Response> getGamesDetails(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("getChartData")
    Call<Big_Api_Response> getChartData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);



    @FormUrlEncoded
    @POST("getRevealGameData")
    Call<Big_Api_Response> getRevealGameData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("SaveRevealGame")
    Call<Big_Api_Response> saveRevealGame(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("getWatchWebsiteData")
    Call<Big_Api_Response> getWatchWebsiteData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("saveWatchWebsite")
    Call<Big_Api_Response> saveWatchWebsite(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("getNumberPuzzleData")
    Call<Big_Api_Response> getNumberPuzzleData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("saveNumberPuzzle")
    Call<Big_Api_Response> saveNumberPuzzle(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("getFlipCardCalculateNumbersData")
    Call<Big_Api_Response> getFlipCardCalculateNumbersData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("saveFlipCardCalculateNumbers")
    Call<Big_Api_Response> saveFlipCardCalculateNumbers(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("EHKJNHHMKKKDHBNKN")
    Call<Big_Api_Response> getTextTypingData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("PJUNHDFHHHDMHYDVA")
    Call<Big_Api_Response> saveTextTyping(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);



    @FormUrlEncoded
    @POST("JDFHDFJDIQMPNHN")
    Call<Big_Api_Response> getMilestonesData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("HJDISGSKIHQRVMS")
    Call<Big_Api_Response> saveMilestone(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("BHDFGFNJFGSKMHGCV")
    Call<Big_Api_Response> getSingleMilestoneData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);



    @FormUrlEncoded
    @POST("LNVTRUNALWQBMZJ")
    Call<Big_Api_Response> getCountData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("HJDCJFEJUFNMKAHN")
    Call<Big_Api_Response> saveCountData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("TRYHYSBDJDGCGCHKHHHD")
    Call<Big_Api_Response> getWordSorting(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("ETGHDJDKJOBDHTTSU")
    Call<Big_Api_Response> saveWordSorting(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);




    @FormUrlEncoded
    @POST("DFGJKDFJLD")
    Call<Big_Api_Response> getAdjoeLeaderboardData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("MDBIRTUNXLAZE")
    Call<Big_Api_Response> saveDiceData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);


    @FormUrlEncoded
    @POST("DGDGDFGTGBNHJ")
    Call<Big_Api_Response> getAdjoeLeaderboardHistory(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("NOPSSTEVAMUBBS")
    Call<Big_Api_Response> getDiceData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);
    @FormUrlEncoded
    @POST("TRJSAISBSGNJPOJGFS")
    Call<Big_Api_Response> saveDailyTarget(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);
    @FormUrlEncoded
    @POST("DFGDFGDFGBCVB")
    Call<Big_Api_Response> getWalletBalance(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);
    @FormUrlEncoded
    @POST("FGHFHFHDG")
    Call<Big_Api_Response> saveQuickTask(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("RBQUBWRSBSKSG")
    Call<Big_Api_Response> validateUpiId(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("QJJQJSHDYNSJSG")
    Call<Big_Api_Response> scanAndPay(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);
    @FormUrlEncoded
    @POST("QTGQGTBSRGSWHRVS")
    Call<Big_Api_Response> getPaymentStatus(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);
    @FormUrlEncoded
    @POST("QUHSYBBSHYSHSA")
    Call<Big_Api_Response> getTicketDetails(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);
    @FormUrlEncoded
    @POST("BSDISDUHUI")
    Call<Big_Api_Response> paymentDetails(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);
}
