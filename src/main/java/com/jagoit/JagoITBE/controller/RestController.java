package com.jagoit.JagoITBE.controller;

import com.jagoit.JagoITBE.helper.ActivityConstant;
import com.jagoit.JagoITBE.helper.ErrorConstant;
import com.jagoit.JagoITBE.helper.HelperConstant;
import com.jagoit.JagoITBE.helper.Utils;
import com.jagoit.JagoITBE.model.*;
import com.jagoit.JagoITBE.service.*;

import org.json.JSONException;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Tuple;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")

public class RestController {

    private static final Logger logger = LogManager.getLogger(RestController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private ChatService chatService;


    @RequestMapping(method = RequestMethod.POST, headers = {"operation=login"})
    public Map<String, Object> Login(@RequestBody Map<String, Object> requestBody){
        MDC.put("processID", Utils.generate_process_id());
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        logger.debug(">> Login");
        String email = requestBody.get("email").toString();
        String password = requestBody.get("password").toString();
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(requestBody));
            User user = userService.getUser(email);
            if(user != null){
                if(user.getUser_password().equals(password)){
                    output_schema.put("user_id", user.getUser_id());
                    output_schema.put("name", user.getUser_name());
                    output_schema.put("email", user.getEmail());
                    output_schema.put("role", HelperConstant.USER_ROLE);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                    output_schema.put("birth_date", dateFormat.format(user.getBirth_date()));
                    output_schema.put("gender", user.getGender());
                    output_schema.put("phone_number", user.getPhone_number());
                    if(user.getProfile_picture() != null){
                        output_schema.put("profile_picture", Base64.getEncoder().encodeToString(user.getProfile_picture()));
                    }else{
                        output_schema.put("profile_picture", null);
                    }
                    if(user.getBank_name() != null){
                        output_schema.put("bank_name", user.getBank_name());
                    }else{
                        output_schema.put("bank_name", null);
                    }
                    if(user.getAccount_number() != null){
                        output_schema.put("account_number", user.getAccount_number());
                    }else{
                        output_schema.put("account_number", null);
                    }

                    output_schema.put("balance", user.getBalance());
                    String isSubscription = user.getIs_subscription();
                    if(isSubscription.equals("Y")){
                        Date date = new Date();
                        Date dateStart = new Date(user.getStart_date_subscription().getTime());
                        Date dateEnd = new Date(user.getEnd_date_subscription().getTime());

                        Calendar calendarStart = Calendar.getInstance();
                        Calendar calendarEnd = Calendar.getInstance();
                        calendarStart.setTime(dateStart);
                        calendarEnd.setTime(dateEnd);
                        long timeMili = date.getTime();
                        long startMili = calendarStart.getTimeInMillis();
                        long endMili = calendarEnd.getTimeInMillis();
                        if(!(timeMili > startMili && timeMili < endMili)){
                            // update
                            userService.updateSubscription(user.getUser_id(), "N");
                            output_schema.put("is_subscription", "N");
                            output_schema.put("start_date_subscription", user.getStart_date_subscription());
                            output_schema.put("end_date_subscription", user.getEnd_date_subscription());
                        }else{
                            SimpleDateFormat dateFormatSubscription = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                            output_schema.put("is_subscription", user.getIs_subscription());
                            output_schema.put("start_date_subscription", dateFormatSubscription.format(dateStart));
                            output_schema.put("end_date_subscription", dateFormatSubscription.format(dateEnd));
                        }
                    }else{
                        output_schema.put("is_subscription", user.getIs_subscription());
                        output_schema.put("start_date_subscription", user.getStart_date_subscription());
                        output_schema.put("end_date_subscription", user.getEnd_date_subscription());
                    }
                }else{
                    error_message = "email atau password tidak sesuai";
                    error_code = "JI-0002";
                }

            }else{
                Consultant consultant = userService.getActiveConsultant(email);
                if(consultant != null){
                    if(consultant.getUser_password().equals(password)){
                        output_schema.put("user_id", consultant.getUser_id());
                        output_schema.put("name", consultant.getUser_name());
                        output_schema.put("email", consultant.getEmail());
                        output_schema.put("role", HelperConstant.USER_CONSULTANT);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                        output_schema.put("birth_date", dateFormat.format(consultant.getBirth_date()));
                        output_schema.put("gender", consultant.getGender());
                        output_schema.put("phone_number", consultant.getPhone_number());
                        if(consultant.getProfile_picture() != null){
                            output_schema.put("profile_picture", Base64.getEncoder().encodeToString(consultant.getProfile_picture()));
                        }else{
                            output_schema.put("profile_picture", null);
                        }
                        if(consultant.getBank_name() != null){
                            output_schema.put("bank_name", consultant.getBank_name());
                        }else{
                            output_schema.put("bank_name", null);
                        }
                        if(consultant.getAccount_number() != null){
                            output_schema.put("account_number", consultant.getAccount_number());
                        }else{
                            output_schema.put("account_number", null);
                        }

                        output_schema.put("balance", consultant.getBalance());
                        output_schema.put("price", consultant.getPrice());
                        output_schema.put("rating", consultant.getRating());
                        output_schema.put("last_education", consultant.getLast_education());
                        output_schema.put("address", consultant.getAddress());
                        output_schema.put("worktime", consultant.getWorktime());
                        output_schema.put("category", consultant.getCategory());
                        output_schema.put("doc_picture_1", consultant.getDoc_picture_1());
                        output_schema.put("doc_picture_2", consultant.getDoc_picture_2());
                        output_schema.put("doc_picture_3", consultant.getDoc_picture_3());
                        output_schema.put("doc_picture_4", consultant.getDoc_picture_4());
                        output_schema.put("doc_picture_5", consultant.getDoc_picture_5());
                    }else{
                        error_message = "email atau password tidak sesuai";
                        error_code = "JI-0002";
                    }
                }else{
                    Admin admin = userService.getAdmin(email);
                    if(admin != null){
                        if(admin.getUser_password().equals(password)){
                            output_schema.put("user_id", admin.getUser_id());
                            output_schema.put("name", admin.getUser_name());
                            output_schema.put("email", admin.getEmail());
                            output_schema.put("role", HelperConstant.USER_ADMIN);
                            output_schema.put("gender", admin.getGender());
                            output_schema.put("phone_number", admin.getPhone_number());
                            if(admin.getProfile_picture() != null){
                                output_schema.put("profile_picture", Base64.getEncoder().encodeToString(admin.getProfile_picture()));
                            }else{
                                output_schema.put("profile_picture", null);
                            }
                        }else{
                            error_message = "email atau password tidak sesuai";
                            error_code = "JI-0002";
                        }
                    }else{
                        error_message = "user tidak terdaftar";
                        error_code = "JI-0001";
                    }
                }
            }
        }catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=register"})
    public Map<String, Object> Register(@RequestBody Map<String, Object> requestBody){
        MDC.put("processID", Utils.generate_process_id());
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        logger.debug(">> register");
        try{
            String email = requestBody.get("email").toString();
            logger.debug("Input: " + Utils.generateInputRESTSimple(requestBody));
            if(userService.getUser(email) == null && userService.getConsultant(email) == null && userService.getAdmin(email) == null){
                String role = requestBody.get("role").toString();
                if(role.equals("consultee")){
                    User user = this.userService.registerUser(requestBody);
                    output_schema.put("user_id", user.getUser_id());
                    ExecutorService executor= Executors.newFixedThreadPool(2);
                    executor.execute(userService.sendEmailRegister(requestBody));
                }else if(role.equals("consultant")){
                    Consultant consultant = this.userService.registerConsultant(requestBody);
                    output_schema.put("user_id", consultant.getUser_id());
                }
            }else{
                error_message = "email sudah terdaftar";
                error_code = "JI-0003";
            }


        }catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=withdrawal"})
    public Map<String, Object> Withdrawal(@RequestBody Map<String, Object> requestBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> withdrawal");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(requestBody));
            String role = requestBody.get("role").toString();
            requestBody.put("activity", ActivityConstant.ACTIVITY_WITHDRAWAL_CODE);
            if(role.equals("consultee")){
                User isUpdate = userService.updateUserBalance(-(Integer) requestBody.get("nominal"), requestBody.get("user_id").toString());
                if(isUpdate != null){
                    BalanceTransaction balanceTransaction = balanceService.Withdrawal(requestBody);
                    if(balanceTransaction != null){
                        logger.debug("berhasil catat di log");
                        output_schema.put("id", balanceTransaction.getId());
                    }else{
                        logger.debug("gagal simpan");
                        error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                        error_code = ErrorConstant.GENERAL_ERROR_CODE;
                    }
                }else{
                    logger.debug("gagal kurangin saldo");
                    error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                    error_code = ErrorConstant.GENERAL_ERROR_CODE;
                }
            }else{
                Consultant isUpdate = userService.updateConsultantBalance(-(Integer) requestBody.get("nominal"), requestBody.get("user_id").toString());
                if(isUpdate != null){
                    BalanceTransaction balanceTransaction = balanceService.Withdrawal(requestBody);
                    if(balanceTransaction != null){
                        logger.debug("berhasil catat di log");
                        output_schema.put("id", balanceTransaction.getId());
                    }else{
                        logger.debug("gagal simpan");
                        error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                        error_code = ErrorConstant.GENERAL_ERROR_CODE;
                    }
                }else{
                    logger.debug("gagal kurangin saldo");
                    error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                    error_code = ErrorConstant.GENERAL_ERROR_CODE;
                }
            }



        }catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=topup"})
    public Map<String, Object> TopUp(@RequestBody Map<String, Object> requestBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> topup");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(requestBody));
            requestBody.put("activity", ActivityConstant.ACTIVITY_TOPUP_CODE);
            BalanceTransaction balanceTransaction = balanceService.Topup(requestBody);
            if(balanceTransaction != null){
                logger.debug("berhasil simpan");
                output_schema.put("id", balanceTransaction.getId());
            }else{
                logger.debug("gagal simpan");
                error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                error_code = ErrorConstant.GENERAL_ERROR_CODE;
            }

        }catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=payment"})
    public Map<String, Object> Payment(@RequestBody Map<String, Object> requestBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> payment");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(requestBody));
            if(requestBody.get("activity").equals("consultation")){
                requestBody.put("activity", ActivityConstant.ACTIVITY_PAY_CONSULTATION_CODE);
                User isUpdate = userService.updateUserBalance(-(Integer) requestBody.get("nominal"), requestBody.get("user_id").toString());
                if(isUpdate != null){
                    BalanceTransaction balanceTransaction = balanceService.Payment(requestBody);
                    if(balanceTransaction != null){
                        logger.debug("berhasil simpan");
                        output_schema.put("id", balanceTransaction.getId());
                    }else{
                        logger.debug("gagal simpan");
                        error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                        error_code = ErrorConstant.GENERAL_ERROR_CODE;
                    }
                }
            }else{
                requestBody.put("activity", ActivityConstant.ACTIVITY_PAY_SUBSCRIPTION_CODE);
                User isUpdate = userService.updateUserBalance(-(Integer) requestBody.get("nominal"), requestBody.get("user_id").toString());
                if(isUpdate != null){
                    BalanceTransaction balanceTransaction = balanceService.Payment(requestBody);
                    if(balanceTransaction != null){
                        logger.debug("berhasil simpan");
                        output_schema.put("id", balanceTransaction.getId());
                    }else{
                        logger.debug("gagal simpan");
                        error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                        error_code = ErrorConstant.GENERAL_ERROR_CODE;
                    }
                }
            }

        }catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);
        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=approveBalanceTransaction"})
    public Map<String, Object> ApproveBalanceTransaction(@RequestBody Map<String, Object> requestBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> approveBalanceTransaction");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(requestBody));
            String transaction_id = requestBody.get("transaction_id").toString();
            Integer nominal = Integer.parseInt(requestBody.get("nominal").toString());
            String user_id = requestBody.get("user_id").toString();
            String activity = requestBody.get("activity").toString();
            String user_id_admin = requestBody.get("user_id_admin").toString();
            if(activity.equals("topup")){ // topup
                BalanceTransaction balanceTransaction = balanceService.updateTransaction(HelperConstant.STATUS_DONE, transaction_id,
                        user_id_admin);
                if(balanceTransaction != null){
                    User isUpdate = userService.updateUserBalance(nominal, user_id);
                    if(isUpdate != null){
                        if(balanceTransaction != null){
                            logger.debug("berhasil update balance");
                            output_schema.put("id", balanceTransaction.getId());
                        }else{
                            logger.debug("gagal update balance");
                            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                            error_code = ErrorConstant.GENERAL_ERROR_CODE;
                        }
                    }
                }else{
                    logger.debug("gagal update transaction");
                    error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                    error_code = ErrorConstant.GENERAL_ERROR_CODE;
                }


            }else{ // withdrawal
                BalanceTransaction balanceTransaction = balanceService.updateTransaction(HelperConstant.STATUS_DONE, transaction_id,
                        user_id_admin);
                if(balanceTransaction != null) {
                    // berhasil update
                }else{
                    error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                    error_code = ErrorConstant.GENERAL_ERROR_CODE;
                }
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=rejectBalanceTransaction"})
    public Map<String, Object> RejectBalanceTransaction(@RequestBody Map<String, Object> requestBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> RejectBalanceTransaction");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(requestBody));
            String transaction_id = requestBody.get("transaction_id").toString();
            String activity = requestBody.get("activity").toString();
            String user_id_admin = requestBody.get("user_id_admin").toString();

            if(activity.equals("topup")){ // topup
                BalanceTransaction balanceTransaction = balanceService.updateTransaction(HelperConstant.STATUS_REJECTED, transaction_id,
                        user_id_admin);
                if(balanceTransaction != null){

                }else{
                    logger.debug("gagal update transaction");
                    error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                    error_code = ErrorConstant.GENERAL_ERROR_CODE;
                }

            }else{ // withdrawal
                BalanceTransaction balanceTransaction = balanceService.updateTransaction(HelperConstant.STATUS_REJECTED, transaction_id,
                        user_id_admin);
                if(balanceTransaction != null) {
                    if(userService.getUserData(balanceTransaction.getUser_id()) != null){
                        User user = this.userService.updateUserBalance(balanceTransaction.getNominal(), balanceTransaction.getUser_id());
                        if(user != null){
                            // berhasil balikin duitnya
                            logger.debug("berhasil update user");
                        }else{
                            logger.debug("gagal update user");
                            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                            error_code = ErrorConstant.GENERAL_ERROR_CODE;
                        }
                    }else{
                        Consultant consultant = this.userService.updateConsultantBalance(balanceTransaction.getNominal(), balanceTransaction.getUser_id());
                        if(consultant != null){
                            // berhasil balikin duitnya
                            logger.debug("berhasil update user");
                        }else{
                            logger.debug("gagal update user");
                            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                            error_code = ErrorConstant.GENERAL_ERROR_CODE;
                        }
                    }


                }else{
                    error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                    error_code = ErrorConstant.GENERAL_ERROR_CODE;
                }
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getCourses"})
    public Map<String, Object> GettAllCourseByCategory(@RequestParam("category_id") String category_id){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getAllCategoryCourse");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + category_id);
            List<Course> listCourses = this.courseService.getAllCourseByCategoryId(category_id);
            int size = listCourses.size();;

            if(size != 0){
                List<Object> listOutput = new ArrayList<>();
                DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                for(int i = 0; i < size; i++){
                    Map<String, Object> outputMap = new HashMap<>();
                    outputMap.put("course_id", listCourses.get(i).getCourse_id());
                    outputMap.put("course_name", listCourses.get(i).getCourse_name());
                    outputMap.put("created_date", dateFormat.format(listCourses.get(i).getCreated_date()));
                    outputMap.put("course_picture", Base64.getEncoder().encodeToString(listCourses.get(i).getCourse_picture()));
                    outputMap.put("count", listCourses.get(i).getCount());
                    listOutput.add(outputMap);
                }
                logger.debug("ada data course");
                output_schema.put("courses", listOutput);
            }else{
                logger.debug("tidak ada data course");
                error_message = "Tidak ada Data";
                error_code = "JI-xxxx";
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=insertCourse"})
    public Map<String, Object> InsertCourse(@RequestBody Map<String, Object> requestBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> insertCourse");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(requestBody));
            Course course = this.courseService.insertCourse(requestBody);
            if(course != null){
                logger.debug("berhasil insert course");
                output_schema.put("id", course.getCourse_id());
            }else{
                logger.debug("gagal insert course");
                error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                error_code = ErrorConstant.GENERAL_ERROR_CODE;
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=insertCourseDetail"})
    public Map<String, Object> InsertCourseDetail(@RequestBody Map<String, Object> requestBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> insertCourseDetail");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(requestBody));
            CourseDetail courseDetail = this.courseService.insertCourseDetail(requestBody);
            if(courseDetail != null){
                logger.debug("berhasil insert course detail");
                output_schema.put("id", courseDetail.getCourse_detail_id());
            }else{
                logger.debug("gagal insert course detail");
                error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                error_code = ErrorConstant.GENERAL_ERROR_CODE;
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getCourseDetail"})
    public Map<String, Object> GetCourseDetail(@RequestParam("course_id") String course_id) throws JSONException {
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getCourseDetail");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + course_id);
            List<CourseDetail> listCourseDetail = this.courseService.getCourseDetailByCourseId(course_id);
            int size = listCourseDetail.size();;

            if(size != 0){
                List<Object> listOutput = new ArrayList<>();
                for(int i = 0; i < size; i++){
                    Map<String, Object> outputMap = new HashMap<>();
                    outputMap.put("course_detail_id", listCourseDetail.get(i).getCourse_detail_id());
                    outputMap.put("course_detail_name", listCourseDetail.get(i).getCourse_detail_name());
                    outputMap.put("course_detail_type", listCourseDetail.get(i).getCourse_detail_type());
                    if(listCourseDetail.get(i).getCourse_detail_type().equalsIgnoreCase("PDF")){
                        outputMap.put("course_detail_content", Base64.getEncoder().encodeToString(listCourseDetail.get(i).getCourse_detail_content_byte()));
                        outputMap.put("course_detail_assessment", listCourseDetail.get(i).getCourse_detail_content_assessment());
                    }else if(listCourseDetail.get(i).getCourse_detail_type().equalsIgnoreCase("VIDEO")){
                        outputMap.put("course_detail_content", Utils.trimUrlVideo(listCourseDetail.get(i).getCourse_detail_content_text()));
                        outputMap.put("course_detail_assessment", listCourseDetail.get(i).getCourse_detail_content_assessment());
                    }else{
                        outputMap.put("course_detail_assessment", listCourseDetail.get(i).getCourse_detail_content_assessment());
                        outputMap.put("course_detail_content", null);
                    }
                    outputMap.put("course_detail_desc", listCourseDetail.get(i).getCourse_detail_desc());
                    outputMap.put("is_subscription", listCourseDetail.get(i).getIs_subscription());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                    outputMap.put("created_date", dateFormat.format(listCourseDetail.get(i).getCreated_date()));
                    outputMap.put("updated_date", dateFormat.format(listCourseDetail.get(i).getUpdated_date()));
                    listOutput.add(outputMap);
                }
                logger.debug("ada data course");
                output_schema.put("course_detail", listOutput);
            }else{
                logger.debug("tidak ada data course detail");
                error_message = "Tidak ada Data";
                error_code = "JI-xxxx";
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + Utils.generateInputRESTSimple(parent_schema));

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=editProfileUser"})
    public Map<String, Object> EditProfileUser(@RequestBody Map<String, Object> requestBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> editProfileUser");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(requestBody));
            User user = userService.editProfileUser(requestBody);

            if(user != null){
                logger.debug("berhasil edit profile");
            }else{
                logger.debug("gagal edit profile");
                error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                error_code = ErrorConstant.GENERAL_ERROR_CODE;
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=editProfileConsultant"})
    public Map<String, Object> EditProfileConsultant(@RequestBody Map<String, Object> requestBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> editProfileConsultant");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(requestBody));
            Consultant consultant = userService.editProfileConsultant(requestBody);

            if(consultant != null){
                logger.debug("berhasil edit profile");
            }else{
                logger.debug("gagal edit profile");
                error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                error_code = ErrorConstant.GENERAL_ERROR_CODE;
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getAllConsultant"})
    public Map<String, Object> GetAllConsultant() throws JSONException {
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getAllConsultant");

        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{

            List<Consultant> listConsultant = userService.getAllConsultant();
            int size = listConsultant.size();
            if(size != 0){
                List<Object> output = new ArrayList<>();
                Map<String, Object> outputMap = new HashMap<>();
                for(int i = 0; i < size; i++){
                    outputMap.put("id", listConsultant.get(i).getUser_id());
                    outputMap.put("name", listConsultant.get(i).getUser_name());
                    outputMap.put("email", listConsultant.get(i).getEmail());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                    outputMap.put("birth_date", dateFormat.format(listConsultant.get(i).getBirth_date()));
                    outputMap.put("gender", listConsultant.get(i).getGender());
                    outputMap.put("phone_number", listConsultant.get(i).getPhone_number());
                    outputMap.put("price", listConsultant.get(i).getPrice());
                    if(listConsultant.get(i).getProfile_picture() != null){
                        outputMap.put("profile_picture", Base64.getEncoder().encodeToString(listConsultant.get(i).getProfile_picture()));
                    }else{
                        outputMap.put("profile_picture", null);
                    }
                    if(listConsultant.get(i).getBank_name() != null){
                        outputMap.put("bank_name", listConsultant.get(i).getBank_name());
                    }else{
                        outputMap.put("bank_name", null);
                    }
                    if(listConsultant.get(i).getAccount_number() != null){
                        outputMap.put("account_number", listConsultant.get(i).getAccount_number());
                    }else{
                        outputMap.put("account_number", null);
                    }
                    outputMap.put("rating", listConsultant.get(i).getRating());
                    outputMap.put("last_education", listConsultant.get(i).getLast_education());
                    outputMap.put("address", listConsultant.get(i).getAddress());
                    outputMap.put("worktime", listConsultant.get(i).getWorktime());
                    outputMap.put("category", listConsultant.get(i).getCategory());
                    outputMap.put("ocupation", listConsultant.get(i).getOcupation());
                    outputMap.put("doc_picture_1", listConsultant.get(i).getDoc_picture_1());
                    outputMap.put("doc_picture_2", listConsultant.get(i).getDoc_picture_2());
                    outputMap.put("doc_picture_3", listConsultant.get(i).getDoc_picture_3());
                    outputMap.put("doc_picture_4", listConsultant.get(i).getDoc_picture_4());
                    outputMap.put("doc_picture_5", listConsultant.get(i).getDoc_picture_5());
                    output.add(outputMap);
                    outputMap = new HashMap<>();
                }
                output_schema.put("consultants", output);
            }else{
                logger.debug("tidak ada data");
                error_message = "Tidak ada Data";
                error_code = "JI-xxxx";
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
//        logger.debug("Output: " + Utils.generateInputRESTSimple(parent_schema));

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getPendingConsultant"})
    public Map<String, Object> GetPendingConsultant(){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getPendingConsultant");

        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            List<Consultant> listConsultant = userService.getPendingConsultant();
            int size = listConsultant.size();
            if(size != 0){
                Map<String, Object> outputMap = new HashMap<>();
                List<Object> outputList = new ArrayList<>();
                for(int i = 0; i < size; i++) {
                    outputMap.put("name", listConsultant.get(i).getUser_name());
                    outputMap.put("email", listConsultant.get(i).getEmail());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                    outputMap.put("birth_date", dateFormat.format(listConsultant.get(i).getBirth_date()));
                    outputMap.put("gender", listConsultant.get(i).getGender());
                    outputMap.put("phone_number", listConsultant.get(i).getPhone_number());
                    outputMap.put("profile_picture", Base64.getEncoder().encodeToString(listConsultant.get(i).getProfile_picture()));
                    outputMap.put("balance", listConsultant.get(i).getBalance());
                    outputMap.put("created_date", dateFormat.format(listConsultant.get(i).getCreated_date()));
                    outputMap.put("price", listConsultant.get(i).getPrice());
                    outputMap.put("rating", listConsultant.get(i).getRating());
                    outputMap.put("last_education", listConsultant.get(i).getLast_education());
                    outputMap.put("address", listConsultant.get(i).getAddress());
                    outputMap.put("worktime", listConsultant.get(i).getWorktime());
                    outputMap.put("ocupation", listConsultant.get(i).getOcupation());
                    outputMap.put("category", listConsultant.get(i).getCategory());
                    outputMap.put("doc_picture_1", listConsultant.get(i).getDoc_picture_1());
                    outputMap.put("doc_picture_2", listConsultant.get(i).getDoc_picture_2());
                    outputMap.put("doc_picture_3", listConsultant.get(i).getDoc_picture_3());
                    outputMap.put("doc_picture_4", listConsultant.get(i).getDoc_picture_4());
                    outputMap.put("doc_picture_5", listConsultant.get(i).getDoc_picture_5());
                    outputList.add(outputMap);
                    outputMap = new HashMap<>();
                }

                output_schema.put("consultants", outputList);
            }else{
                logger.debug("tidak ada data");
                error_message = "Tidak ada Data";
                error_code = "JI-xxxx";
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getTopupTransaction"})
    public Map<String, Object> GetTopupTransction(){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getTopUpTransaction");

        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            List<BalanceTransaction> listTransaction = balanceService.getTopupTransaction();
            int size = listTransaction.size();
            Map<String, Object> outputMap = new HashMap<>();
            List<Object> output = new ArrayList<>();
            if(size != 0){
                for(int i = 0; i < size; i++){
                    outputMap = new HashMap<>();
                    outputMap.put("id", listTransaction.get(i).getId());
                    outputMap.put("user_id", listTransaction.get(i).getUser_id());
                    outputMap.put("status", listTransaction.get(i).getStatus());
                    outputMap.put("nominal", listTransaction.get(i).getNominal());
                    outputMap.put("notes", listTransaction.get(i).getNotes());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                    outputMap.put("created_date", dateFormat.format(listTransaction.get(i).getCreated_date()));
                    if(listTransaction.get(i).getUser_id_admin() != null){
                        Admin admin = userService.getAdminData(listTransaction.get(i).getUser_id_admin());
                        outputMap.put("reviewer_name", admin.getUser_name());
                    }else{
                        outputMap.put("reviewer_name", "");
                    }

                    if(listTransaction.get(i).getUpdated_date() != null){
                        outputMap.put("updated_date", dateFormat.format(listTransaction.get(i).getUpdated_date()));
                    }else{
                        outputMap.put("updated_date", listTransaction.get(i).getUpdated_date());
                    }
                    outputMap.put("proof_picture", listTransaction.get(i).getProof_picture());

                    User user = userService.getUserData(listTransaction.get(i).getUser_id());
                    if(user != null){
                        outputMap.put("user_name", user.getUser_name());
                    }else{
                        Consultant consultant = userService.getConsultantData(listTransaction.get(i).getUser_id());
                        outputMap.put("user_name", consultant.getUser_name());
                    }

                    output.add(outputMap);
                }

                output_schema.put("lists", output);
            }else{
                logger.debug("tidak ada data");
                error_message = "Tidak ada Data";
                error_code = "JI-xxxx";
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getWithdrawalTransaction"})
    public Map<String, Object> GetWithdrawalTransction(){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getWithdrawalTransaction");

        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            List<BalanceTransaction> listTransaction = balanceService.getWithdrawalTransaction();
            int size = listTransaction.size();
            Map<String, Object> outputMap = new HashMap<>();
            List<Object> output = new ArrayList<>();
            if(size != 0){
                for(int i = 0; i < size; i++){
                    outputMap = new HashMap<>();
                    outputMap.put("id", listTransaction.get(i).getId());
                    outputMap.put("user_id", listTransaction.get(i).getUser_id());
                    outputMap.put("status", listTransaction.get(i).getStatus());
                    outputMap.put("nominal", listTransaction.get(i).getNominal());
                    outputMap.put("notes", listTransaction.get(i).getNotes());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                    outputMap.put("created_date", dateFormat.format(listTransaction.get(i).getCreated_date()));
                    if(listTransaction.get(i).getUser_id_admin() != null){
                        Admin admin = userService.getAdminData(listTransaction.get(i).getUser_id_admin());
                        outputMap.put("reviewer_name", admin.getUser_name());
                    }else{
                        outputMap.put("reviewer_name", "");
                    }
                    if(listTransaction.get(i).getUpdated_date() != null){
                        outputMap.put("updated_date", dateFormat.format(listTransaction.get(i).getUpdated_date()));
                    }else{
                        outputMap.put("updated_date", listTransaction.get(i).getUpdated_date());
                    }

                    User user = userService.getUserData(listTransaction.get(i).getUser_id());
                    if(user != null){
                        outputMap.put("user_name", user.getUser_name());
                        outputMap.put("bank_name", user.getBank_name());
                        outputMap.put("account_number", user.getAccount_number());
                    }else{
                        Consultant consultant = userService.getConsultantData(listTransaction.get(i).getUser_id());
                        outputMap.put("user_name", consultant.getUser_name());
                        outputMap.put("bank_name", consultant.getBank_name());
                        outputMap.put("account_number", consultant.getAccount_number());
                    }

                    output.add(outputMap);
                }

                output_schema.put("lists", output);
            }else{
                logger.debug("tidak ada data");
                error_message = "Tidak ada Data";
                error_code = "JI-xxxx";
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=reviewConsultant"})
    public Map<String, Object> ReviewConsultant(@RequestBody Map<String, Object> reqeustBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> reviewConsultant");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(reqeustBody));
            String email = reqeustBody.get("email").toString();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("email", email);
            paramMap.put("notes", reqeustBody.get("notes").toString());
            if(reqeustBody.get("activity").equals(HelperConstant.APPROVE_ACTIVITY)){ // approve
                paramMap.put("activity", HelperConstant.APPROVE_ACTIVITY);
                userService.approveConsultant(email);
                ExecutorService executor= Executors.newFixedThreadPool(2);
                executor.execute(userService.sendEmail(paramMap));

            }else{ // reject
                paramMap.put("activity", HelperConstant.REJECT_ACTIVITY);
                userService.rejectConsultant(email);
                ExecutorService executor= Executors.newFixedThreadPool(2);
                executor.execute(userService.sendEmail(paramMap));
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=insertUserCourseActivity"})
    public Map<String, Object> InsertUserCourseActivity(@RequestBody Map<String, Object> reqeustBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> insertUserCourseActivity");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(reqeustBody));
            UserCourseActivity userCourseActivity = courseService.insertUserCourseActivity(reqeustBody);
            if(userCourseActivity != null){
                logger.debug("berhasil insert");
            }else{
                logger.debug("gagal insert");
            }

        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getUserCourseActivity"})
    public Map<String, Object> GetUserCourseActivity(@RequestParam("user_id") String user_id, @RequestParam("course_id") String course_id ){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getUserCourseActivity");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: {user_id=" + user_id + ", course_id=" + course_id + "}");
            List<UserCourseActivity> userCourseActivities = courseService.getUserCourseActivity(user_id, course_id);
            int size = userCourseActivities.size();
            if(size != 0){
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                Map<String, Object> output = new HashMap<>();
                List<Object> listDetail = new ArrayList<>();
                output_schema.put("user_id", userCourseActivities.get(0).getUser_id());
                output_schema.put("course_id", userCourseActivities.get(0).getCourse_id());

                for(int i = 0; i < size; i++){
                    output.put("course_detail_id", userCourseActivities.get(i).getCourse_detail_id());
                    output.put("score", userCourseActivities.get(i).getScore());
                    output.put("created_date", dateFormat.format(userCourseActivities.get(i).getCreated_date()));
                    listDetail.add(output);
                    output = new HashMap<>();
                }
                output_schema.put("detail", listDetail);
                logger.debug("ada data");
            }else{
                logger.debug("gak ada data");
                error_message = "Tidak ada data";
                error_code = "JI-xxxx";
            }

        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.PUT, headers = {"operation=updateCourseViewCount"})
    public Map<String, Object> updateCourseViewCount(@RequestParam("course_id") String course_id ){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> updateCourseViewCount");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + course_id);
            if(courseService.updateCourseViewCount(course_id) != null){
                logger.debug("berhasil update");
            }else{
                logger.debug("gagal update");
                error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                error_code = ErrorConstant.GENERAL_ERROR_CODE;
            }

        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=updateScore"})
    public Map<String, Object> updateScore(@RequestBody Map<String, Object> reqeustBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> updateScore");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(reqeustBody));
            if(courseService.updateScore(reqeustBody) != null){
                logger.debug("berhasil update");
            }else{
                logger.debug("gagal update");
                error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                error_code = ErrorConstant.GENERAL_ERROR_CODE;
            }

        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=consultation"})
    public Map<String, Object> Consultation(@RequestBody Map<String, Object> reqeustBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> consultation");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(reqeustBody));
            ConsultationTransaction consultationTransaction = consultationService.createConsultation(reqeustBody);
            if(consultationTransaction != null){
                ExecutorService executor= Executors.newFixedThreadPool(2);
                executor.execute(consultationService.checkStatusConsultation(consultationTransaction.getConsultation_id(), reqeustBody.get("consultationType").toString()));
                output_schema.put("id", consultationTransaction.getConsultation_id());
            }else{
                error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                error_code = ErrorConstant.GENERAL_ERROR_CODE;
            }

        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getUpcomingConsultation"})
    public Map<String, Object> getUpcomingConsultation(@RequestParam("user_id") String user_id){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getUpcomingConsultation");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: { user_id= " + user_id  + " }");
            List<ConsultationTransaction> consultationTransactions = consultationService.getUpcomingConsultation(user_id);
            int size = consultationTransactions.size();
            if(size != 0){
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                Map<String, Object> output = new HashMap<>();
                List<Object> listOutput = new ArrayList<>();
                for(int i = 0; i < size; i++){
                    output.put("id", consultationTransactions.get(i).getConsultation_id());
                    output.put("userIdConsultee", consultationTransactions.get(i).getUser_id_consultee());
                    output.put("userIdConsultant", consultationTransactions.get(i).getUser_id_consultant());
                    output.put("consultationFee", consultationTransactions.get(i).getConsultation_fee());
                    output.put("adminFee", consultationTransactions.get(i).getAdmin_fee());
                    output.put("consultationType", consultationTransactions.get(i).getConsultation_type());
                    output.put("consultationMethod", consultationTransactions.get(i).getConsultation_method());
                    output.put("consultationDate", dateFormat.format(consultationTransactions.get(i).getConsultation_date()));

                    output.put("status", consultationTransactions.get(i).getStatus());

                    if(consultationTransactions.get(i).getChat_id() != null){
                        output.put("chatId", consultationTransactions.get(i).getChat_id());
                    }
                    User consulteeData = userService.getUserData(consultationTransactions.get(i).getUser_id_consultee());
                    output.put("consulteeName", consulteeData.getUser_name());
                    output.put("profilePicture", Base64.getEncoder().encodeToString(consulteeData.getProfile_picture()));
                    listOutput.add(output);
                    output = new HashMap<>();
                }

                output_schema.put("lists", listOutput);
//                logger.debug("ada data");
            }else{
//                logger.debug("gak ada data");
                error_message = "Tidak ada data";
                error_code = "JI-xxxx";
            }

        } catch (Exception e){
//            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getListConsultation"})
    public Map<String, Object> getListConsultation(@RequestParam("user_id") String user_id){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getListConsultation");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: { user_id= " + user_id  + " }");
            List<ConsultationTransaction> consultationTransactions = consultationService.getListConsultation(user_id);
            int size = consultationTransactions.size();
            if(size != 0){
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                Map<String, Object> output = new HashMap<>();
                List<Object> listOutput = new ArrayList<>();
                for(int i = 0; i < size; i++){
                    output.put("id", consultationTransactions.get(i).getConsultation_id());
                    output.put("userIdConsultee", consultationTransactions.get(i).getUser_id_consultee());
                    output.put("userIdConsultant", consultationTransactions.get(i).getUser_id_consultant());
                    output.put("consultationFee", consultationTransactions.get(i).getConsultation_fee());
                    output.put("adminFee", consultationTransactions.get(i).getAdmin_fee());
                    output.put("consultationType", consultationTransactions.get(i).getConsultation_type());
                    output.put("consultationMethod", consultationTransactions.get(i).getConsultation_method());
                    output.put("consultationDate", dateFormat.format(consultationTransactions.get(i).getConsultation_date()));
                    output.put("status", consultationTransactions.get(i).getStatus());
                    if(consultationTransactions.get(i).getChat_id() != null){
                        output.put("chatId", consultationTransactions.get(i).getChat_id());
                    }
                    Consultant consultantData = userService.getConsultantData(consultationTransactions.get(i).getUser_id_consultant());
                    output.put("consultantName", consultantData.getUser_name());
                    output.put("profilePicture", Base64.getEncoder().encodeToString(consultantData.getProfile_picture()));
                    listOutput.add(output);
                    output = new HashMap<>();
                }

                output_schema.put("lists", listOutput);
                logger.debug("ada data");
            }else{
                logger.debug("gak ada data");
                error_message = "Tidak ada data";
                error_code = "JI-xxxx";
            }

        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.PUT, headers = {"operation=approveConsultation"})
    public Map<String, Object> approveConsultation(@RequestParam("id") String id ){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> approveConsultation");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + id);
            ConsultationTransaction consultationTransaction = consultationService.updateStatusConsultation(id, HelperConstant.APPROVE_ACTIVITY);
            if(consultationTransaction != null){
                ExecutorService executor = Executors.newFixedThreadPool(3);
                if(consultationTransaction.getConsultation_type().equals("now")){
                    executor.execute(consultationService.updateDoneOnProrgress(id));
                }
                String user_id_consultee = consultationTransaction.getUser_id_consultee();
                String user_id_consultant = consultationTransaction.getUser_id_consultant();
                User consultee = userService.getUserData(user_id_consultee);
                Consultant consultant = userService.getConsultantData(user_id_consultant);
                String email_consultee = consultee.getEmail();
                String email_consultant = consultant.getEmail();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("email_consultee", email_consultee);
                paramMap.put("consultee_name", consultee.getUser_name());
                paramMap.put("email_consultant", email_consultant);
                paramMap.put("consultant_name", consultant.getUser_name());
                paramMap.put("consultation_id", consultationTransaction.getConsultation_id());
                paramMap.put("consultation_type", consultationTransaction.getConsultation_type());
                paramMap.put("consultation_date", consultationTransaction.getConsultation_date());
                paramMap.put("activity", "approve");
                executor = Executors.newFixedThreadPool(2);
                executor.execute(consultationService.sendEmail(paramMap));
            }else{
                error_message = "Konsultasi sudah dibatalkan";
                error_code = "JI-XXXX";
            }

        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.PUT, headers = {"operation=rejectConsultation"})
    public Map<String, Object> rejectConsultation(@RequestParam("id") String id ){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> approveConsultation");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + id);
            ConsultationTransaction consultationTransaction = consultationService.updateStatusConsultation(id, HelperConstant.REJECT_ACTIVITY);
            if(consultationTransaction != null){
                String user_id_consultee = consultationTransaction.getUser_id_consultee();
                logger.debug("user_id: " + user_id_consultee);
                User user = userService.updateUserBalance((consultationTransaction.getConsultation_fee() + consultationTransaction.getAdmin_fee()), user_id_consultee);
                if(user != null){
                    logger.debug("berhasil update balance");
                }else{
                    logger.debug("gagal update balance");
                }
            }else{
                error_message = "Konsultasi sudah dibatalkan";
                error_code = "JI-XXXX";
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.PUT, headers = {"operation=updateUserSubscription"})
    public Map<String, Object> UpdateUserSubscription(@RequestParam("id") String id ){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> updateUserSubscription");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + id);
            if(userService.updateSubscription(id, "Y") != null){
                logger.debug("berhasil update");
            }else{
                logger.debug("gagal update");
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.PUT, headers = {"operation=deleteCourseDetail"})
    public Map<String, Object> DeleteCourseDetail(@RequestParam("course_detail_id") String id ){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> deleteCourseDetail");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + id);
            courseService.deleteCourseDetail(id);
            courseService.deleteUserActivityDetail(id);
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=updateChatIdConsultation"})
    public Map<String, Object> UpdateChatIdConsultation(@RequestBody Map<String, Object> reqeustBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> updateChatIdConsultation");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(reqeustBody));
            String chat_id = reqeustBody.get("chatId").toString();
            String consultation_id = reqeustBody.get("consultationId").toString();

            ConsultationTransaction consultationTransaction = consultationService.updateConsultation(consultation_id, chat_id);
            if(consultationTransaction == null){
                error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                error_code = ErrorConstant.GENERAL_ERROR_CODE;
            }

        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=createChat"})
    public Map<String, Object> CreateChat(@RequestBody Map<String, Object> reqeustBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> createChat");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(reqeustBody));
            String user_id_consultee = reqeustBody.get("userIdConsultee").toString();
            String user_id_consultant = reqeustBody.get("userIdConsultant").toString();

            Chat chat = chatService.createChat(user_id_consultee, user_id_consultant);
            if(chat != null){
                output_schema.put("chat_id", chat.getChat_id());
            }

        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getUserById"})
    public Map<String, Object> GetUserById(@RequestParam("user_id") String user_id,
                                           @RequestParam("role") String role) throws JSONException {
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getUserById");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + user_id);
            if(role.equals("consultee")){
                User userDetail = this.userService.getUserData(user_id);
                if(userDetail != null) {
                    output_schema.put("user_detail", userDetail);
                }else{
                    logger.debug("tidak ada data user");
                    error_message = "Tidak ada Data";
                    error_code = "JI-xxxx";
                }
            }else{
                Consultant consultant = this.userService.getConsultantData(user_id);
                if(consultant != null) {
                    output_schema.put("user_detail", consultant);
                }else{
                    logger.debug("tidak ada data user");
                    error_message = "Tidak ada Data";
                    error_code = "JI-xxxx";
                }
            }

           

        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);
//        logger.debug("Output: " + Utils.generateInputRESTSimple(parent_schema));

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getCourseDetailById"})
    public Map<String, Object> GetCourseDetailById(@RequestParam("course_detail_id") String id){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getCourseDetailById");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + id);
            CourseDetail courseDetail = this.courseService.getCourseDetailById(id);
            logger.debug("ada data");
            if(courseDetail != null) {
                Map<String, Object> outputMap = new HashMap<>();
                outputMap.put("course_detail_id", courseDetail.getCourse_detail_id());
                outputMap.put("course_detail_name", courseDetail.getCourse_detail_name());
                outputMap.put("course_detail_type", courseDetail.getCourse_detail_type());
                if(courseDetail.getCourse_detail_type().equalsIgnoreCase("PDF")){
                    outputMap.put("course_detail_content", Base64.getEncoder().encodeToString(courseDetail.getCourse_detail_content_byte()));
                    outputMap.put("course_detail_assessment", courseDetail.getCourse_detail_content_assessment());
                }else if(courseDetail.getCourse_detail_type().equalsIgnoreCase("VIDEO")){
                    outputMap.put("course_detail_content", courseDetail.getCourse_detail_content_text());
                    outputMap.put("course_detail_assessment", courseDetail.getCourse_detail_content_assessment());
                }else{
                    outputMap.put("course_detail_assessment", courseDetail.getCourse_detail_content_assessment());
                    outputMap.put("course_detail_content", null);
                }
                outputMap.put("course_detail_desc", courseDetail.getCourse_detail_desc());
                outputMap.put("is_subscription", courseDetail.getIs_subscription());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                outputMap.put("created_date", dateFormat.format(courseDetail.getCreated_date()));
                output_schema.put("course_detail", outputMap);

            }else{
                logger.debug("tidak ada data course detail");
                error_message = "Tidak ada Data";
                error_code = "JI-xxxx";
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=editCourseDetail"})
    public Map<String, Object> EditCourseDetail(@RequestBody Map<String, Object> requestBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> editCourseDetail");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(requestBody));
            CourseDetail courseDetail = this.courseService.editCourseDetail(requestBody);
            if(courseDetail != null){
                logger.debug("berhasil edit course detail");
            }else{
                logger.debug("gagal insert course detail");
                error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                error_code = ErrorConstant.GENERAL_ERROR_CODE;
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getHistoryPayment"})
    public Map<String, Object> GetHistoryPayment(@RequestParam("user_id") String user_id){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getHistoryPayment");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + user_id);
            List<BalanceTransaction> listTransaction = this.balanceService.getHistoryPayment(user_id);
            int size = listTransaction.size();;

            if(size != 0){
                List<Object> listOutput = new ArrayList<>();
                DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                for(int i = 0; i < size; i++){
                    Map<String, Object> outputMap = new HashMap<>();
                    outputMap.put("id", listTransaction.get(i).getId());
                    outputMap.put("nominal", listTransaction.get(i).getNominal());
                    outputMap.put("status", listTransaction.get(i).getStatus());
                    outputMap.put("created_date", dateFormat.format(listTransaction.get(i).getCreated_date()));
                    outputMap.put("activity", listTransaction.get(i).getTransaction_detail().getTransaction_name());
                    listOutput.add(outputMap);
                }
                logger.debug("ada data");
                output_schema.put("lists", listOutput);
            }else{
                logger.debug("tidak ada data course");
                error_message = "Tidak ada Data";
                error_code = "JI-xxxx";
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getHistoryConsultation"})
    public Map<String, Object> GetHistoryConsultation(@RequestParam("user_id") String user_id,
                                                      @RequestParam("role") String role){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getHistoryConsultation");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + user_id + " role: " + role);
            List<Tuple> consultationTransaction = this.consultationService.getHistoryConsultation(user_id, role);
            int size = consultationTransaction.size();;

            if(size != 0){
                List<Object> listOutput = new ArrayList<>();
                DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                for(int i = 0; i < size; i++){
                    Map<String, Object> outputMap = new HashMap<>();
                    outputMap.put("id", consultationTransaction.get(i).get("id"));
                    outputMap.put("consultation_type", consultationTransaction.get(i).get("consultation_type"));
                    outputMap.put("status", consultationTransaction.get(i).get("status"));
                    outputMap.put("consultation_date", dateFormat.format(consultationTransaction.get(i).get("consultation_date")));
                    outputMap.put("consultation_fee", consultationTransaction.get(i).get("consultation_fee"));
                    outputMap.put("chat_id", consultationTransaction.get(i).get("chat_id"));
                    outputMap.put("admin_fee", consultationTransaction.get(i).get("admin_fee"));
                    outputMap.put("consultation_method", consultationTransaction.get(i).get("consultation_method"));
                    outputMap.put("user_name", consultationTransaction.get(i).get("user_name"));
                    outputMap.put("rating", consultationTransaction.get(i).get("rating"));
                    outputMap.put("is_reported_consultee", consultationTransaction.get(i).get("is_reported_consultee"));
                    outputMap.put("is_reported_consultant", consultationTransaction.get(i).get("is_reported_consultant"));
                    outputMap.put("profilePicture", Base64.getEncoder().encodeToString((byte[]) consultationTransaction.get(i).get("profile_picture")));

                    listOutput.add(outputMap);
                }
                logger.debug("ada data");
                output_schema.put("lists", listOutput);
            }else{
                logger.debug("tidak ada data course");
                error_message = "Tidak ada Data";
                error_code = "JI-xxxx";
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.PUT, headers = {"operation=updateRating"})
    public Map<String, Object> UpdateRating(@RequestParam("id") String id,
                                            @RequestParam("rating") Double rating){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> updateRating");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + id);
            ConsultationTransaction consultationTransaction = consultationService.updateRating(id, rating);
            if(consultationTransaction != null){
                userService.updateRating(consultationTransaction.getUser_id_consultant(), rating);
            }else{
                error_message = "gagal update rating";
                error_code = "JI-XXXX";
            }

        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"operation=reportConsultation"})
    public Map<String, Object> ReportConsultation(@RequestBody Map<String, Object> requestBody){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> reportConsultation");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + Utils.generateInputRESTSimple(requestBody));
            ConsultationTransaction consultationTransaction = consultationService.reportConsultation(requestBody);
            if(consultationTransaction != null){
                Map<String, Object> paramMap = new HashMap<>();
                User consultee = userService.getUserData(consultationTransaction.getUser_id_consultee());
                User consultant = userService.getUserData(consultationTransaction.getUser_id_consultant());
                paramMap.put("id", consultationTransaction.getConsultation_id());
                paramMap.put("userNameConsultee", consultee.getUser_name());
                paramMap.put("emailConsultee", consultee.getEmail());
                paramMap.put("phoneNumberConsultee", consultee.getPhone_number());
                paramMap.put("userNameConsultant", consultant.getUser_name());
                paramMap.put("emailConsultant", consultant.getEmail());
                paramMap.put("phoneNumberConsultant", consultant.getPhone_number());
                paramMap.put("role", requestBody.get("role"));
                paramMap.put("notes", requestBody.get("notes"));

                ExecutorService executor= Executors.newFixedThreadPool(2);
                executor.execute(consultationService.sendEmailReportConsultation(paramMap));
            }else{
                error_message = "gagal update flag";
                error_code = "JI-XXXX";
            }

        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + parent_schema);

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getConsultationById"})
    public Map<String, Object> GetConsultationById(@RequestParam("consultation_id") String consultation_id) throws JSONException {
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getConsultationById");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + consultation_id);
            Tuple consultationTransaction = consultationService.getConsultationById(consultation_id);
            if(consultationTransaction != null){
                Map<String, Object> output = new HashMap<>();
                output.put("id", consultationTransaction.get("id"));
                output.put("user_id_consultee", consultationTransaction.get("user_id_consultee"));
                output.put("user_id_consultant", consultationTransaction.get("user_id_consultant"));
                output.put("consultation_type", consultationTransaction.get("consultation_type"));
                output.put("consultation_fee", consultationTransaction.get("consultation_fee"));
                output.put("admin_fee", consultationTransaction.get("admin_fee"));
                output.put("consultation_date", consultationTransaction.get("consultation_date"));
                output.put("consultation_method", consultationTransaction.get("consultation_method"));
                output.put("rating", consultationTransaction.get("rating"));
                output.put("consultee_name", consultationTransaction.get("consultee_name"));
                output.put("consultee_email", consultationTransaction.get("consultee_email"));
                output.put("consultee_phone_number", consultationTransaction.get("consultee_phone_number"));
                output.put("consultee_profile_picture", consultationTransaction.get("consultee_profile_picture"));
                output.put("consultant_name", consultationTransaction.get("consultant_name"));
                output.put("consultant_email", consultationTransaction.get("consultant_email"));
                output.put("consultant_phone_number", consultationTransaction.get("consultant_phone_number"));
                output.put("consultant_profile_picture", consultationTransaction.get("consultant_profile_picture"));
                output_schema.put("data", output);
            }else{
                logger.debug("error");
                error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                error_code = ErrorConstant.GENERAL_ERROR_CODE;
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + Utils.generateInputRESTSimple(parent_schema));

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getConsultationByChatId"})
    public Map<String, Object> GetConsultationByChatId(@RequestParam("chat_id") String chat_id) throws JSONException {
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getConsultationById");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + chat_id);
            ConsultationTransaction consultationTransaction = consultationService.getConsultationByChatId(chat_id);
            if(consultationTransaction != null){
                output_schema.put("data", consultationTransaction);
            }else{
                logger.debug("error");
                error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                error_code = ErrorConstant.GENERAL_ERROR_CODE;
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + Utils.generateInputRESTSimple(parent_schema));

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.PUT, headers = {"operation=finishConsultation"})
    public Map<String, Object> FinishConsultation(@RequestParam("consultation_id") String consultation_id) throws JSONException {
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> finishConsultation");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{
            logger.debug("Input: " + consultation_id);
            ConsultationTransaction consultationTransaction = consultationService.finishConsultation(consultation_id);
            if(consultationTransaction != null){

            }else{
                logger.debug("error");
                error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                error_code = ErrorConstant.GENERAL_ERROR_CODE;
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + Utils.generateInputRESTSimple(parent_schema));

        return parent_schema;
    }

    @RequestMapping(method = RequestMethod.GET, headers = {"operation=getConsultationByDate"})
    public Map<String, Object> GetConsultationByDate(@RequestParam("start_date") String start_date,
                                                     @RequestParam("end_date") String end_date) throws JSONException {
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getConsultationByDate");
        Map<String, Object> output_schema = new HashMap<>();
        Map<String, Object> parent_schema = new HashMap<>();
        Map<String, Object> error_schema = new HashMap<>();
        String error_code = ErrorConstant.SUCCESS_ERROR_CODE;
        String error_message = ErrorConstant.SUCCESS_ERROR_MESSAGE;
        try{

            logger.debug("Input: " + start_date + " - " + end_date); // format yyyy-MM-dd
            List<Tuple> consultationTransaction = consultationService.getConsultationByDate(start_date, end_date);
            if(consultationTransaction != null){
                int length = consultationTransaction.size();
                List<Object> outputList = new ArrayList<>();
                for(int i = 0; i < length; i++){
                    Map<String, Object> output = new HashMap<>();
                    output.put("id", consultationTransaction.get(i).get("id"));
                    output.put("user_id_consultee", consultationTransaction.get(i).get("user_id_consultee"));
                    output.put("user_id_consultant", consultationTransaction.get(i).get("user_id_consultant"));
                    output.put("consultation_type", consultationTransaction.get(i).get("consultation_type"));
                    output.put("consultation_fee", consultationTransaction.get(i).get("consultation_fee"));
                    output.put("admin_fee", consultationTransaction.get(i).get("admin_fee"));
                    output.put("consultation_date", consultationTransaction.get(i).get("consultation_date"));
                    output.put("consultation_method", consultationTransaction.get(i).get("consultation_method"));
                    output.put("rating", consultationTransaction.get(i).get("rating"));
                    output.put("consultee_name", consultationTransaction.get(i).get("consultee_name"));
                    output.put("consultee_email", consultationTransaction.get(i).get("consultee_email"));
                    output.put("consultee_phone_number", consultationTransaction.get(i).get("consultee_phone_number"));
                    output.put("consultee_profile_picture", consultationTransaction.get(i).get("consultee_profile_picture"));
                    output.put("consultant_name", consultationTransaction.get(i).get("consultant_name"));
                    output.put("consultant_email", consultationTransaction.get(i).get("consultant_email"));
                    output.put("consultant_phone_number", consultationTransaction.get(i).get("consultant_phone_number"));
                    output.put("consultant_profile_picture", consultationTransaction.get(i).get("consultant_profile_picture"));
                    outputList.add(output);
                }
                output_schema.put("data", outputList);
            }else{
                logger.debug("error");
                error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
                error_code = ErrorConstant.GENERAL_ERROR_CODE;
            }
        } catch (Exception e){
            logger.debug(e);
            error_message = ErrorConstant.GENERAL_ERROR_MESSAGE;
            error_code = ErrorConstant.GENERAL_ERROR_CODE;
        }

        error_schema.put("error_code", error_code);
        error_schema.put("error_message", error_message);
        parent_schema.put("error_schema", error_schema);

        if(error_code.equals(ErrorConstant.SUCCESS_ERROR_CODE)){
            parent_schema.put("output_schema", output_schema);
        }
        logger.debug("Output: " + Utils.generateInputRESTSimple(parent_schema));

        return parent_schema;
    }

    
}
