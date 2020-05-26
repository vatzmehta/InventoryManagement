package com.engineersbasket.inventorymanagement.resource;

import com.engineersbasket.inventorymanagement.dao.PaymentMethodDao;
import com.engineersbasket.inventorymanagement.dao.PaymentStatusDao;
import com.engineersbasket.inventorymanagement.dao.response.FailedDao;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import com.engineersbasket.inventorymanagement.service.PaymentService;
import com.engineersbasket.inventorymanagement.service.UserService;
import com.engineersbasket.inventorymanagement.utils.ErrorCodes;
import com.engineersbasket.inventorymanagement.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentResource {

    public final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    @Autowired
    PaymentService paymentService;
    @Autowired
    UserService userService;


    @GetMapping("/method/getAll")
    public String getAllMethod(Model model){
        log.info("get all payment method requested");
        List<PaymentMethodDao> all = paymentService.getAllMethods();
        log.info("get all payment method successfully retrieved");
        model.addAttribute("var","Methods");
        model.addAttribute("all",all);
        return "paymentGetAllResponse";
    }

    @GetMapping("/method/create")
    public String createMethod(Model model){
        model.addAttribute("paymentMethodDao",new PaymentMethodDao());
        return "paymentMethodCreateRequest";
    }

    @PostMapping("/method/create")
    public String addMethod(@ModelAttribute PaymentMethodDao paymentMethodDao, Model model){
        log.info("create payment method requested with status {} by user {}",paymentMethodDao.getName(),userService.getUser());
        try {
            paymentMethodDao = paymentService.addPaymentMethod(paymentMethodDao);
            log.info("create payment method successful");
            model.addAttribute("paymentMethodDao",paymentMethodDao);
            return "paymentCreateResponse";
        }catch(DataIntegrityViolationException e){
            log.error("create payment method failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.duplicatePaymentMethod, Messages.duplicateErrorMessage
                    + paymentMethodDao.getName());
            model.addAttribute("var",fa);
        }catch (Exception e) {
            log.error("create payment method failed due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }

        return "Failure";
    }

    @GetMapping("/method/update")
    public String updateMethod(Model model){
        model.addAttribute("paymentMethodDao",new PaymentMethodDao());
        return "paymentMethodUpdateRequest";
    }



    // @PutMapping("/update/{id}")
    @PostMapping("/method/update")
    public String updateMethod(@ModelAttribute PaymentMethodDao paymentMethodDao,Model model){
        log.info("update payment method requested id {} by user {}",paymentMethodDao.getId(),userService.getUser());
        try {
            PaymentMethodDao updatedStatus = paymentService.updatePaymentMethod(paymentMethodDao);
            log.info("update payment method successful");
            model.addAttribute("var",updatedStatus.getId());
            return "updateSuccess";
        } catch(DataIntegrityViolationException e){
            log.error("update payment method failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.duplicatePaymentMethod, Messages.duplicateErrorMessage
                    + paymentMethodDao.getName());
            model.addAttribute("var",fa);
        }catch (ResourceNotFound e) {
            log.error("update payment method failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        } catch (Exception e){
            log.error("update payment method failed due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }
        return "Failure";
    }

    @GetMapping("/method/deleteById")
    public String deleteMethodById(@RequestParam("id") Integer id, Model model){
        log.info("Delete payment method with id {} requested by user {}",id,userService.getUser());
        try{
            paymentService.deletePaymentMethodById(id);
            log.info("Delete successful");
            return "DeleteSuccess";
        }catch (Exception e){
            log.error("Delete failed due to {}",e);
            FailedDao failedDao = Messages.unknownError;
            model.addAttribute("var",failedDao);
            return "Failure";
        }
    }

    @GetMapping("/status/getAll")
    public String getAllStatus(Model model){
        log.info("get all payment method requested");
        List<PaymentStatusDao> all = paymentService.getAllStatus();
        log.info("get all payment method successfully retrieved");
        model.addAttribute("all",all);
        model.addAttribute("var","Status");
        return "paymentGetAllResponse";
    }

    @GetMapping("/status/create")
    public String createStatus(Model model){
        model.addAttribute("paymentStatusDao",new PaymentStatusDao());
        return "paymentStatusCreateRequest";
    }

    @PostMapping("/status/create")
    public String addStatus(@ModelAttribute PaymentStatusDao paymentStatusDao, Model model){
        log.info("create payment status requested with status {} by user {}",paymentStatusDao.getName(),userService.getUser());
        try {
            paymentStatusDao = paymentService.addPaymentStatus(paymentStatusDao);
            log.info("create payment status successful");
            model.addAttribute("paymentStatusDao",paymentStatusDao);
            return "paymentStatusCreateResponse";
        }catch(DataIntegrityViolationException e){
            log.error("create payment status failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.duplicatePaymentStatus, Messages.duplicateErrorMessage
                    + paymentStatusDao.getName());
            model.addAttribute("var",fa);
        }catch (Exception e) {
            log.error("create payment status failed due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }

        return "Failure";
    }

    @GetMapping("/status/update")
    public String updateStatus(Model model){
        model.addAttribute("paymentStatusDao",new PaymentStatusDao());
        return "paymentStatusUpdateRequest";
    }



    // @PutMapping("/update/{id}")
    @PostMapping("/status/update")
    public String updateStatus(@ModelAttribute PaymentStatusDao paymentStatusDao, Model model){
        log.info("update payment status requested id {} by user {}",paymentStatusDao.getId(),userService.getUser());
        try {
            PaymentStatusDao updatedStatus = paymentService.updatePaymentStatus(paymentStatusDao);
            log.info("update payment status successful");
            model.addAttribute("var",updatedStatus.getId());
            return "updateSuccess";
        } catch(DataIntegrityViolationException e){
            log.error("update payment status failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.duplicateStatus, Messages.duplicateErrorMessage
                    + paymentStatusDao.getName());
            model.addAttribute("var",fa);
        }catch (ResourceNotFound e) {
            log.error("update payment status failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        } catch (Exception e){
            log.error("update payment status failed due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }
        return "Failure";
    }

    @GetMapping("/status/deleteById")
    public String deleteById(@RequestParam("id") Integer id, Model model){
        log.info("Delete payment status with id {} requested by user {}",id,userService.getUser());
        try{
            paymentService.deletePaymentStatusById(id);
            log.info("Delete successful");
            return "DeleteSuccess";
        }catch (Exception e){
            log.error("Delete failed due to {}",e);
            FailedDao failedDao = Messages.unknownError;
            model.addAttribute("var",failedDao);
            return "Failure";
        }
    }

}
