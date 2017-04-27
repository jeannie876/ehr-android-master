package io.github.hkust1516csefyp43.easymed.utility;

import java.util.List;

import io.github.hkust1516csefyp43.easymed.pojo.LoginCredentials;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Attachment;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.BlockedDevice;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.BloodType;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Clinic;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Consultation;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Count;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Country;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Document;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.DocumentType;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Gender;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Keyword;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Medication;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.MedicationVariant;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Notification;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Patient;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Prescription;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.RelatedData;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Role;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.ServerStatus;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.ServerTime;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Suitcase;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Triage;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.User;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Visit;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Louis on 20/4/16.
 */
public interface v2API {

  interface attachments {
    @GET("attachments")
    Call<List<Attachment>> getAttachments(
        @Header("token") String token,
        @Query("cloudinary_url") String url,
        @Query("file_name") String name,
        @Query("user_id") String userId,
        @Query("offset") Integer offset,
        @Query("sort_by") String sortBy,
        @Query("limit") Integer limit
    );

    @GET("attachments/{id}")
    Call<Attachment> getAttachment(
        @Header("token") String token,
        @Path("id") String id
    );

    @POST("attachments/")
    Call<Attachment> addAttachment(
        @Header("token") String token,
        @Body Attachment attachment
    );

    @PUT("attachments/{id}")
    Call<Attachment> editAttachment(
        @Header("token") String token,
        @Path("id") String id,
        @Body Attachment attachment
    );

    @DELETE("attachments/{id}")
    Call<Attachment> deleteAttachment(
        @Header("token") String token,
        @Path("id") String id
    );
  }

  interface blockedDevices {
    @GET("blocked_devices")
    Call<List<BlockedDevice>> getBlockedDevices(
        @Header("token") String token,
        @Query("reporter_id") String reporterId,
        @Query("victim_id") String victimId,
        @Query("remark") String remark,
        @Query("offset") Integer offset,
        @Query("sort_by") String sortBy,
        @Query("limit") Integer limit
    );

    @GET("blocked_devices/{id}")
    Call<BlockedDevice> getBlockedDevice(
        @Header("token") String token,
        @Path("id") String id
    );

    @POST("blocked_devices/")
    Call<BlockedDevice> addBlockedDevice(
        @Header("token") String token,
        @Body BlockedDevice blockedDevice
    );

    @PUT("blocked_devices/{id}")
    Call<BlockedDevice> editBlockedDevice(
        @Header("token") String token,
        @Path("id") String id,
        @Body BlockedDevice blockedDevice
    );

    @DELETE("blocked_devices/{id}")
    Call<BlockedDevice> deleteBlockedDevice(
        @Header("token") String token,
        @Path("id") String id
    );
  }

  interface bloodTypes {
    @GET("blood_types")
    Call<List<BloodType>> getBloodTypes(
        @Header("token") String token,
        @Query("blood_type") String bloodType
    );

    @GET("blood_types/{id}")
    Call<BloodType> getBloodType(
        @Header("token") String token,
        @Path("id") String bloodTypeId
    );

    @POST("blood_types/")
    Call<BloodType> addBloodType(@Header("token") String token);

    @PUT("blood_types/{id}")
    Call<BloodType> editBloodType(@Header("token") String token);

    @DELETE("blood_types/{id}")
    Call<BloodType> deleteBloodType(@Header("token") String token);

  }

  interface clinics {
    /**
     * Get list of clinics without token >> clinic_id & english_name only
     * No param/header/query/body needed
     *
     * @return
     */
    @GET("clinics")
    Call<List<Clinic>> getSimplifiedClinics();

    /**
     * Get list of clinics
     *
     * @param token >> access token
     * @return
     */
    @GET("clinics")
    Call<List<Clinic>> getClinics(
        @Header("token") String token,
        @Query("country_id") String countryId,
        @Query("is_active") Boolean isActive,
        @Query("english_name") String englishName,
        @Query("native_name") String nativeName,
        @Query("latitude") Double latitude,
        @Query("longitude") Double longitude,
        @Query("create_timestamp") String createTimestamp,
        @Query("remark") String remark,
        @Query("is_global") Boolean isGlobal,
        @Query("suitcase_id") String suitcaseId,
        @Query("offset") Integer offset,
        @Query("sort_by") String sortBy,
        @Query("limit") Integer limit
    );

    @GET("clinics/{id}")
    Call<Clinic> getClinic(
        @Header("token") String token,
        @Path("id") String id
    );

  }

  interface countries {
    @GET("countries")
    Call<List<Country>> getCountries(
        @Header("token") String token,
        @Query("english_name") String englishName,
        @Query("native_name") String nativeName,
        @Query("phone_country_code") Integer phoneCountryCode,
        @Query("phone_number_syntax") String phoneNumberSyntax,
        @Query("emoji") String emoji,
        @Query("offset") Integer offset,
        @Query("sort_by") String sortBy,
        @Query("limit") Integer limit
    );

    @GET("countries/{id}")
    Call<Country> getCountry(
        @Header("token") String token,
        @Path("id") String id
    );

    @POST("countries/")
    Call<Country> addCountry(
        @Header("token") String token,
        @Body Country country
    );

    @PUT("countries/{id}")
    Call<Country> editCountry(
        @Header("token") String token,
        @Path("id") String id,
        @Body Country country
    );

    @DELETE("countries/{id}")
    Call<Country> deleteCountry(
        @Header("token") String token,
        @Path("id") String id
    );
  }

  interface documentTypes {
    @GET("document_types")
    Call<List<DocumentType>> getDocumentTypes(
        @Header("token") String token,
        @Query("type") String type,
        @Query("offset") Integer offset,
        @Query("sort_by") String sortBy,
        @Query("limit") Integer limit
    );

    @GET("document_types/{id}")
    Call<DocumentType> getDocumentType(
        @Header("token") String token,
        @Path("id") String id
    );

    @POST("document_types/")
    Call<DocumentType> addDocumentType(
        @Header("token") String token,
        @Body DocumentType documentType
    );

    @PUT("document_types/{id}")
    Call<DocumentType> editDocumentType(
        @Header("token") String token,
        @Path("id") String id,
        @Body DocumentType documentType
    );

    @DELETE("document_types/{id}")
    Call<DocumentType> deleteDocumentType(
        @Header("token") String token,
        @Path("id") String id
    );
  }

  interface genders {


    @GET("genders")
    Call<List<Gender>> getGenders(
        @Header("token") String token
    );

    @GET("gender/{id}")
    Call<Gender> getGender(
        @Header("token") String token,
        @Path("id") String id
    );

    @POST("genders/")
    Call<Gender> addGender(
        @Header("token") String token
    );

    @PUT("genders/{id}")
    Call<Gender> editGender(
        @Header("token") String token,
        @Path("id") String id,
        @Body Gender gender
    );

    @DELETE("genders/{id}")
    Call<Gender> deleteGender(
        @Header("token") String token,
        @Path("id") String id
    );
  }

  interface keywords {
    @GET("keywords")
    Call<List<Keyword>> getKeywords(
        @Header("token") String token,
        @Query("keyword") String keyword,
        @Query("chief_complain") Boolean cc,
        @Query("diagnosis") Boolean diagnosis,
        @Query("screening") Boolean screening,
        @Query("allergen") Boolean allergen,
        @Query("follow-up") Boolean followUp,
        @Query("advice") Boolean advice,
        @Query("education") Boolean education,
        @Query("general") Boolean general,
        @Query("medication_route") Boolean medicationRoute,
        @Query("medication_form") Boolean medicationForm,
        @Query("medication_frequency") Boolean medicationFrequency,
        @Query("unit") Boolean unit,
        @Query("investigation") Boolean investigation,
        @Query("relationship_type") Boolean relationshipType,
        @Query("red_flag") Boolean redFlag,
        @Query("offset") Integer offset,
        @Query("limit")Integer limit,
        @Query("sort_by")String sortBy
    );

    @GET("keywords/{id}")
    Call<Keyword> getKeyword(
        @Header("token") String token,
        @Path("id") String id
    );

    @POST("keywords/")
    Call<Keyword> addKeyword(
        @Header("token") String token,
        @Body Keyword keyword
    );

    @PUT("keywords/{id}")
    Call<Keyword> editKeyword(
        @Header("token") String token,
        @Path("id") String id,
        @Body Keyword keyword
    );

    @DELETE("keywords/{id}")
    Call<Keyword> deleteKeyword(@Header("token") String token);
  }

  interface notifications {
    @GET("notifications/")
    Call<List<Notification>> getMyNotifications(
        @Header("token") String token
    );

    @PUT("notifications/{id}")
    Call<Notification> editMyNotification(
        @Header("token") String token,
        @Path("id") String id,
        @Body Notification notification
    );

    @POST("notifications/")
    Call<Notification> createNotification(
        @Header("token") String token,
        @Body Notification notification
    );
  }

  interface patients {
    /**
     * TODO fill me up
     */
    @GET("patients/")
    Call<List<Patient>> getPatients(
        @Header("token") String token,
        @Query("clinic_id") String client,
        @Query("next_station") String nextStation,
        @Query("gender_id") String gender,
        @Query("phone_number_country_code") String phoneCountryCode,        //phone number is missing?
        @Query("email") String email,
        @Query("first_name") String firstName,
        @Query("middle_name") String middleName,
        @Query("last_name") String lastName,
        @Query("name") String name,
        @Query("visit_date") String visitDate,
        @Query("sort_by") String sortBy
    );

    @POST("patients")
    Call<Patient> addPatient(
        @Header("token") String token,
        @Body Patient patient
    );

    @PUT("patients/{id}")
    Call<Patient> editPatient(
        @Header("token") String token,
        @Path("id") String id,
        @Body Patient patient
    );
  }

  interface prescriptions {

    @GET("prescriptions")
    Call<List<Prescription>> getPrescriptions(
        @Header("token") String token,
        @Query("visit_id") String visitId,
        @Query("medication_id") String medicationId,
        @Query("consultation_id") String consultationId,
        @Query("prescription_detail") String prescriptionDetail,
        @Query("sort_by") String sortBy,
        @Query("limit") Integer limit,
        @Query("offset") Integer offset
    );

    @GET("prescriptions/{id}")
    Call<Prescription> getPrescription(
        @Header("token") String token,
        @Path("id") String id
    );

    @PUT("prescriptions/{id}")
    Call<Prescription> editPrescription(
        @Header("token") String token,
        @Path("id") String id,
        @Body Prescription prescription
    );

    @POST("prescriptions/")
    Call<Prescription> addPrescription(
        @Header("token") String token,
        @Body Prescription prescription
    );

    @DELETE("prescriptions/{id}")
    Call<Prescription> deletePrescription(
        @Header("token") String token,
        @Path("id") String id
    );
  }

  interface triages {
    /**
     * TODO fill me up
     *
     * @param token
     * @return
     */
    @GET("triages")
    Call<List<Triage>> getTriages(
        @Header("token") String token,
        @Query("visit_id") String visitId
    );

    @POST("triages")
    Call<Triage> addTriage(
        @Header("token") String token,
        @Body Triage triage
    );

    @PUT("triages/{id}")
    Call<Triage> editTriage(
        @Header("token") String token,
        @Body Triage triage,
        @Path("id") String id
    );
  }

  interface visits {
    /**
     * TODO fill me up
     *
     * @param token
     * @param sort_by
     * @return
     */
    @GET("visits")
    Call<List<Visit>> getVisits(
        @Header("token") String token,
        @Query("next_station") Integer nextStation,
        @Query("patient_id") String patientId,
        @Query("sort_by") String sort_by);

    @POST("visits")
    Call<Visit> addVisit(
        @Header("token") String token,
        @Body Visit visit
    );

    @PUT("visits/{id}")
    Call<Visit> editVisit(
        @Header("token") String token,
        @Body Visit visit,
        @Path("id") String id
    );

    @GET("visits/count")
    Call<Count> getVisitCount(
        @Header("token")  String token,
        @Query("visit_date_range_before") String beforeWhatDate,
        @Query("visit_date_range_after")  String afterWhatDate

    );
  }

  interface consultations {
    /**
     * TODO fill me up
     *
     * @param token
     * @return
     */
    @GET("consultations")
    Call<List<Consultation>> getConsultations(
        @Header("token") String token,
        @Query("visit_id") String visitId
    );

    @POST("consultations")
    Call<Consultation> addConsultation(
        @Header("token") String token,
        @Body Consultation consultation
    );

    @PUT("consultations/{id}")
    Call<Consultation> editConsultation(
        @Header("token") String token,
        @Body Consultation consultation,
        @Path("id") String id
    );
  }

  interface medications {
    @GET("medications")
    Call<List<Medication>> getMedications(
        @Header("token") String token,
        @Query("medication") String medication,
        @Query("user_id") String userId,
        @Query("suitcase_id") String suitcaseId,
        @Query("sort_by") String sortBy,
        @Query("limit") Integer limit,
        @Query("offset") Integer offset
    );

    @GET("medications/{id}")
    Call<Medication> getMedication(
      @Header("token")  String token,
      @Path("id")       String id
    );

    //PUT :id
    //POST
    @POST("medications")
    Call<Medication> addMedication(
        @Header("token") String token,
        @Body Medication medication
    );

    //DELETE :id
  }

  interface medication_variants {
    @GET("medication_variants")
    Call<List<MedicationVariant>> getMedicationVariants(
        @Header("token") String token,
        @Query("stock") Integer stock,
        @Query("suitcase_id") String suitcaseId
    );

    //GET :id
    //PUT :id
    //POST
    //DELETE :id
  }

  interface queries {
    @GET("queries")
    Call<List<io.github.hkust1516csefyp43.easymed.pojo.server_response.Query>> getQueries (
        @Header("token") String token
    );

    @POST("queries")
    Call<io.github.hkust1516csefyp43.easymed.pojo.server_response.Query> pushQuery (
        @Header("token") String token,
        @Body io.github.hkust1516csefyp43.easymed.pojo.server_response.Query query
    );
  }

  interface related_data {
    @GET("related_data")
    Call<List<RelatedData>> getRelatedDataPlural (
      @Header("token")              String token,
      @Query("consultation_id")     String consultationId,
      @Query("category")            Integer category,
      @Query("data")                String data,
      @Query("remark")              String remark,
      @Query("sort_by") String sortBy,
      @Query("limit") Integer limit,
      @Query("offset") Integer offset
    );

    @GET("related_data/{id}")
    Call<RelatedData> getRelatedData (
        @Header("token")      String token,
        @Path("id")           String id
    );

    @PUT("related_data/{id}")
    Call<RelatedData> editRelatedData (
        @Header("token")      String token,
        @Path("id")           String id,
        @Body                 RelatedData relatedData
    );

    @POST("related_data")
    Call<RelatedData> addRelatedData (
        @Header("token")      String token,
        @Body                 RelatedData relatedData
    );

    @DELETE("related_data/{id}")
    Call<RelatedData> deleteRelatedData (
        @Header("token")    String token,
        @Path("id")         String id
    );
  }

  interface documents {
    @GET("documents")
    Call<List<Document>> getDocuments (
        @Header("token")          String token,
        @Query("document_type")   String documentTypeId,
        @Query("patient_id")      String patientId,
        @Query("sort_by") String sortBy,
        @Query("limit") Integer limit,
        @Query("offset") Integer offset
    );

    @GET("documents/{id}")
    Call<Document> getDocument(
        @Header("token")      String token,
        @Path("id")           String id
    );

    @PUT("documents/{id}")
    Call<Document> editDocument(
        @Header("token")      String token,
        @Path("id")           String id,
        @Body                 Document document
    );

    @POST("documents")
    Call<Document> addDocument(
        @Header("token")      String token,
        @Body                 Document document
    );

    @DELETE("documents/{id}")
    Call<Document> deleteDocument(
        @Header("token")      String token,
        @Path("id")           String id
    );
  }

  interface login {
    @POST("login")
    Call<Object> login(@Body LoginCredentials loginCredentials);
  }

  interface signup {

  }

  interface roles{
    @GET("roles")
    Call<List<Role>> getRoles(
            @Header("token") String token
    );
  }

  interface users {
    //TODO fill me up
    @GET("users")
    Call<List<User>> getUsers(
        @Header("token") String token,
        @Query("username") String username
    );

    @GET("users/{id}")
    Call<User> getUser(
        @Header("token")  String token,
        @Path("id")       String id
    );

    @POST("users")
    Call<User> addUser(
        @Header("token")  String token,
        @Body             User user
    );
  }

  interface suitcases {
    @GET("suitcases")
    Call<List<Suitcase>> getSuitcases(
        @Header("token")    String token
    );

  }

  interface staticAPI {
    @GET("static/status")
    Call<ServerStatus> getStatus();

    @GET("static/shutdown")
    Call<Object> shutdown(
        @Header("token") String token
    );

    @GET("static/clock")
    Call<ServerTime> getServerTime();

    @PUT("static/clock")
    Call<ServerTime> setServerTime(
        @Header("token")  String token,
        @Body ServerTime serverTime
    );
  }
}