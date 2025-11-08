package com.example.adspay.services

import com.example.adspay.models.common.BaseResponse
import com.example.adspay.models.transfer.*
import retrofit2.http.*
import com.example.adspay.constant.ApiPath

interface TransferInterface {

    @POST(ApiPath.Transfer.Internal.INQUIRY)
    suspend fun inquiryNumber(
        @Header("Authorization") bearer: String,
        @Body payload: InquiryPayload
    ): BaseResponse<DataInquiry>

    @POST(ApiPath.Transfer.Internal.TRANSFER)
    suspend fun internalTransfer(
        @Header("Authorization") bearer: String,
        @Body payload: TransferPayload
    ): BaseResponse<DataTransfer>

    @GET(ApiPath.Transfer.Bank.GET_LIST_BANK)
    suspend fun getBankList(
        @Header("Authorization") bearer: String,
    ): BaseResponse<List<GetBankDataResponse>>

    @POST(ApiPath.Transfer.Bank.INQUIRY)
    suspend fun inquiryBank(
        @Header("Authorization") bearer: String,
        @Body payload: InquiryBankPayload
    ): BaseResponse<DataInquiryBank>

    @POST(ApiPath.Transfer.Bank.PREVIEW)
    suspend fun previewTransferBank(
        @Header("Authorization") bearer: String,
        @Body payload: TransferBankPreviewPayload
    ): BaseResponse<DataPreviewBankTransfer>

    @POST(ApiPath.Transfer.Bank.TRANSFER)
    suspend fun transferBank(
        @Header("Authorization") bearer: String,
        @Body payload: TransferBankPayload
    ): BaseResponse<DataBankTransfer>
}
