package com.example.universityairlines.repository

import com.example.universityairlines.model.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


object UserRepository {

    private val okHttpClient: OkHttpClient =
        OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS).build()

    private var retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl("https://universityairlines.altervista.org")
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create()).build()

    private val retrofitServiceLogin = retrofit.create(GetUserService::class.java)
    private val retrofitServiceRegistration = retrofit.create(SetUserService::class.java)
    private val retrofitServiceGetFlights = retrofit.create(GetFlightService::class.java)
    private val retrofitServiceGetAirports = retrofit.create(GetAirportsService::class.java)
    private val retrofitServiceGetPaymentInit =
        retrofit.create(GetPaymentInitialization::class.java)
    private val retrofitServiceGetPaymentConfirmation =
        retrofit.create(GetPaymentConfirmation::class.java)
    private val mapper = jacksonObjectMapper()

    suspend fun getUser(
        mail: String,
        password: String,
    ): ApiResult<LoginResponse> {
        val service: Call<LoginResponse> = retrofitServiceLogin.getUser(mail, password)
        return safeCall(service)
    }

    suspend fun setUser(
        mail: String, password: String, first_name: String, last_name: String
    ): ApiRegistrationResult<RegistrationResponse> {
        val service: Call<RegistrationResponse> =
            retrofitServiceRegistration.setUser(mail, password, first_name, last_name)
        return callRegistration(service)
    }

    suspend fun getFlights(
        origine: String,
        destinazione: String,
        dataDiPartenza: String,
        dataDiRitorno: String,
        numPasseggeri: String
    ): ApiResult<FlightsResponse> {
        val service: Call<FlightsResponse> =
            retrofitServiceGetFlights.getFlights(
                origine,
                destinazione,
                dataDiPartenza,
                dataDiRitorno,
                numPasseggeri
            )
        return safeCall(service)
    }

    suspend fun getAirports(
        code: String, name: String, citycode: String,
        city: String, countrycode: String, country: String, continent: String
    ): ApiResult<GetAirportResponse> {
        val service: Call<GetAirportResponse> =
            retrofitServiceGetAirports.getAirports(
                code,
                name,
                citycode,
                city,
                countrycode,
                country,
                continent
            )
        return safeCall(service)
    }

    suspend fun getPaymentInit(
        origine: String,
        destinazione: String,
        data_partenza: String,
        data_ritorno: String,
        num_passeggeri: String,
        prezzo_volo: String
    ): ApiResult<PaymentInitResponse> {
        val service: Call<PaymentInitResponse> =
            retrofitServiceGetPaymentInit.getPaymentInit(
                origine,
                destinazione,
                data_partenza,
                data_ritorno,
                num_passeggeri,
                prezzo_volo
            )
        return safeCall(service)
    }

    suspend fun getPaymentConfirmation(
        origine: String,
        destinazione: String,
        data_partenza: String,
        data_ritorno: String,
        totale_da_pagare: String,
        numero_carta: String,
        scadenza_carta: String,
        cvv_carta: String
    ): ApiResult<PaymentConfirmationResponse> {
        val service: Call<PaymentConfirmationResponse> =
            retrofitServiceGetPaymentConfirmation.getPaymentConfirmation(
                origine,
                destinazione,
                data_partenza,
                data_ritorno,
                totale_da_pagare,
                numero_carta,
                scadenza_carta,
                cvv_carta
            )
        return safeCall(service)
    }

    private suspend fun <R : Any, T : Call<R>> safeCall(service: T): ApiResult<R> =
        withContext(Dispatchers.IO) {
            val response: Response<R> = service.awaitResponse()

            if (response.isSuccessful) {
                ApiResult.Success(response.body()!!)
            } else {
                val error = try {
                    mapper.readValue(response.errorBody()!!.string(), ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                ApiResult.Failure(error)
            }
        }

    private suspend fun <R : Any, T : Call<R>> callRegistration(service: T): ApiRegistrationResult<R> =
        withContext(Dispatchers.IO) {
            val response: Response<R> = service.awaitResponse()

            if (response.isSuccessful) {
                ApiRegistrationResult.Success(response.body()!!)
            } else {
                ApiRegistrationResult.Failure(response.errorBody()!!)
            }
        }
}

// Datasource
interface GetUserService {
    @GET("/signin.php")
    fun getUser(
        @Query("mail") mail: String,
        @Query("password") password: String
    ): Call<LoginResponse>
}

interface SetUserService {
    @GET("/signup.php")
    fun setUser(
        @Query("mail") mail: String, @Query("password") password: String,
        @Query("first_name") first_name: String, @Query("last_name") last_name: String
    ): Call<RegistrationResponse>
}

interface GetFlightService {
    @GET("/get_flights.php")
    fun getFlights(
        @Query("origin") origine: String,
        @Query("destination") destinazione: String,
        @Query("departure_date") dataDiPartenza: String,
        @Query("return_date") dataDiRitorno: String,
        @Query("passengers_number") numPasseggeri: String
    ): Call<FlightsResponse>
}

interface GetAirportsService {
    @GET("/get_airports.php")
    fun getAirports(
        @Query("code") code: String,
        @Query("name") name: String,
        @Query("citycode") citycode: String,
        @Query("city") city: String,
        @Query("countrycode") countrycode: String,
        @Query("country") country: String,
        @Query("continent") continent: String
    ): Call<GetAirportResponse>
}

interface GetPaymentInitialization {
    @GET("/payment_init.php")
    fun getPaymentInit(
        @Query("origin") origine: String,
        @Query("destination") destinazione: String,
        @Query("departure_date") data_partenza: String,
        @Query("return_date") data_ritorno: String,
        @Query("passengers_number") num_passeggeri: String,
        @Query("flight_price") prezzo_volo: String
    ): Call<PaymentInitResponse>
}

interface GetPaymentConfirmation {
    @GET("/payment_confirmation.php")
    fun getPaymentConfirmation(
        @Query("origin") origine: String,
        @Query("destination") destinazione: String,
        @Query("departure_date") data_partenza: String,
        @Query("return_date") data_ritorno: String,
        @Query("total_to_pay") totale_da_pagare: String,
        @Query("card_number") numero_carta: String,
        @Query("card_expiration") scadenza_carta: String,
        @Query("card_cvv") cvv_carta: String
    ): Call<PaymentConfirmationResponse>
}
