package com.engineersbasket.inventorymanagement.resource;

import com.engineersbasket.inventorymanagement.dao.FinancialRequestDao;
import com.engineersbasket.inventorymanagement.dao.InventoryDao;
import com.engineersbasket.inventorymanagement.dao.PaymentInputDao;
import com.engineersbasket.inventorymanagement.dao.response.FailedDao;
import com.engineersbasket.inventorymanagement.dao.response.FinancialDaoResponse;
import com.engineersbasket.inventorymanagement.dao.response.InventoryResponseDao;
import com.engineersbasket.inventorymanagement.dao.response.NewInventoryResponseDao;
import com.engineersbasket.inventorymanagement.exceptions.AllQuantityAlreadyPurchased;
import com.engineersbasket.inventorymanagement.exceptions.InvalidInventoryTypeException;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import com.engineersbasket.inventorymanagement.service.InventoryService;
import com.engineersbasket.inventorymanagement.service.UserService;
import com.engineersbasket.inventorymanagement.utils.ErrorCodes;
import com.engineersbasket.inventorymanagement.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    public static final String PRICE = "Price";
    public final Logger log = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    InventoryService inventoryService;
    @Autowired
    UserService userService;

    @GetMapping
    public String home() {
        return "inventoryHome";
    }

    @GetMapping("/getAll")
    public String getAllItems(Model model) {
        log.trace("get all Inventory requested");
        List<InventoryResponseDao> all = inventoryService.getAll();
        log.trace("all inventory successfully retrieved");
        model.addAttribute("entries", all);
        return "inventoryGetAllResponse";
    }

    @GetMapping("/create/withf")
    public String create(Model model) {
        model.addAttribute("inventoryDao", new InventoryDao());
        return "inventoryWithFileCreateRequest";
    }

//    @PostMapping("/create")
//    public InventoryDao create(@RequestBody InventoryDao inventoryDao) {
//        try {
//            return inventoryService.create(inventoryDao);
//        } catch (Exception e) {
//
//        }
//        return null;
//    }

    @PostMapping("/create/withf")
    public String createWithFileUpload(@ModelAttribute InventoryDao inventoryDao,
                                       @RequestParam(value = "file") MultipartFile file,
                                       Model model) {
        log.info("create inventory requested with serial number {} by user {}",inventoryDao.getSerialNumber(),userService.getUser());
        try {
            if(inventoryDao.getDepricatedValue() < 0){
                model.addAttribute("var",new FailedDao(ErrorCodes.invalidValue, PRICE + Messages.invalidData + inventoryDao.getDepricatedValue()));
                return "Failure";
            }
            inventoryDao = inventoryService.createWithFile(inventoryDao, file);
            log.info("Inventory successfully created");
            model.addAttribute("inventoryDao",inventoryDao);
            return "inventoryCreateWithFileResponse";
        } catch (DataIntegrityViolationException e) {
            log.error("create inventory failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.duplicateSerialNumber, Messages.duplicateErrorMessage +
                    inventoryDao.getSerialNumber());
            model.addAttribute("var", fa);
        } catch(EntityNotFoundException e ){
            log.error("create inventory failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.resourceNotFound,e.getMessage());
            model.addAttribute("var",fa);
        }catch (InvalidDataAccessApiUsageException e){
            log.error("create inventory failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.invalidValue,Messages.invalidData);
            model.addAttribute("var",fa);
        }catch(ResourceNotFound e){
            log.error("create inventory failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        }catch(AllQuantityAlreadyPurchased e){
            log.error("create inventory failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        }catch(InvalidInventoryTypeException e){
            log.error("create inventory failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        } catch (Exception e) {
            log.error("create inventory failed due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var", fa);
        }

        return "Failure";
    }

    @GetMapping("/update")
    public String update(Model model) {
        model.addAttribute("inventoryDao", new InventoryDao());
        return "inventoryUpdateRequest";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute  InventoryDao inventoryDao,Model model) {
        log.info("update inventory with id {} requested by user {}",inventoryDao.getId(),userService.getUser());
        try {
            if(inventoryDao.getDepricatedValue() != null && inventoryDao.getDepricatedValue() < 0){
                model.addAttribute("var",new FailedDao(ErrorCodes.invalidValue, PRICE + Messages.invalidData + inventoryDao.getDepricatedValue()));
                return "Failure";
            }
            inventoryService.update(inventoryDao);
            log.info("update inventory successful");
            model.addAttribute("var",inventoryDao.getId());
            return "updateSuccess";
        }catch (ResourceNotFound e){
            log.error("update inventory unsuccessful due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        } catch (Exception e) {
            log.error("update inventory unsuccessful due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }
        return "Failure";
    }

    @GetMapping("/searchBySerialNumber")
    public String getBySerialNumber(Model model, @RequestParam("inputURL") String serialNumber) {
        log.info("search by serialnumber requested with serialnumber {}",serialNumber);
        try {
            NewInventoryResponseDao inventoryResponseDao = inventoryService.getBySerialNumber(serialNumber);
            model.addAttribute("i", inventoryResponseDao);
            log.info("successfully retrieved by serial number");
            return "inventoryGetResponse";
        } catch (ResourceNotFound e) {
            log.info("search by serialnumber failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        }catch (Exception e){
            log.error("search by serialnumber unsuccessful due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }

        return "Failure";
    }

    @GetMapping("/searchById")
    public String getById(Model model, @RequestParam("inputURL") Integer id) {
        log.info("search by id requested with id {}",id);
        try {
            NewInventoryResponseDao inventoryResponseDao = inventoryService.searchById(id);
            log.info("search by id successfully retrieved");
            model.addAttribute("i", inventoryResponseDao);
            return "inventoryGetResponse";
        } catch (ResourceNotFound e) {
            log.info("search by id failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        }catch (Exception e){
            log.error("search by id failed due to  {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }

        return "Failure";
    }

    @GetMapping("/searchByStatus")
    public String getByStatus(Model model, @RequestParam("inputURL") String status) {
        log.info("search by status requested with status {}",status);
        List<InventoryResponseDao> all = null;
        try {
            all = inventoryService.getByStatus(status);
            log.info("search by status successfully retrieved");
            model.addAttribute("entries", all);
            return "inventoryGetAllResponse";
        } catch (ResourceNotFound e) {
            log.error("search by status failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        }catch (Exception e){
            log.error("search by status failed due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }
       return "Failure";

    }

    @GetMapping("/updateStatus")
    public String updateStatus(Model model){
        model.addAttribute("inventoryDao",new InventoryDao());
        return "inventoryUpdateStatus";
    }

    @PostMapping("/updateStatus")
    public String updateStatus(@ModelAttribute InventoryDao inventoryDao,Model model){
        log.info("update inventory status requested with serialnumber {} and status id {} by user {}",inventoryDao.getSerialNumber(),inventoryDao.getStatusId(),userService.getUser());
        try {
            inventoryService.updateStatus(inventoryDao);
            log.info("update inventory status successful");
            model.addAttribute("var",inventoryDao.getSerialNumber());
            return "updateSuccess";
        }catch (ResourceNotFound e){
            log.error("update inventory status failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        }catch (Exception e) {
            log.error("update inventory status failed due to {}",e);
         FailedDao fa = Messages.unknownError;
         model.addAttribute("var",fa);
        }
        return "Failure";
    }

    @GetMapping("/deleteById")
    public String deleteById(@RequestParam("id") Integer id,Model model){
        log.info("Delete inventory with id {} requested by user {}",id,userService.getUser());
        try{
            inventoryService.deleteById(id);
            log.info("Delete successful");
            return "DeleteSuccess";
        }catch (Exception e){
            log.error("Delete failed due to {}",e);
            FailedDao failedDao = Messages.unknownError;
            model.addAttribute("var",failedDao);
            return "Failure";
        }
    }

    @GetMapping("/financial/get")
    public String getFinancial(Model model) {
        model.addAttribute("financialRequestDao", new FinancialRequestDao());
        return "inventoryGetFinancialRequest";
    }

    @PostMapping("/financial/get")
    public String getFinancial(@ModelAttribute FinancialRequestDao financialRequestDao, Model model) {
        log.info("get financial Inventory requested");
        List<FinancialDaoResponse> all = inventoryService.getFinancial(financialRequestDao.getFromDate(),
                financialRequestDao.getToDate());
        log.info("financial inventory successfully retrieved");
        model.addAttribute("entries", all);
        return "inventoryGetFinancialResponse";
    }

    @GetMapping("/update/paymentMethod")
    public String updatePaymentMethod(Model model){
        model.addAttribute("paymentInputDao",new PaymentInputDao());
        return "inventoryUpdatePaymentMethod";
    }

    @GetMapping("/update/paymentStatus")
    public String updatePaymentStatus(Model model){
        model.addAttribute("paymentInputDao",new PaymentInputDao());
        return "inventoryUpdatePaymentStatus";
    }

    @PostMapping("/update/paymentMethod")
    public String updatePaymentMethod(@ModelAttribute PaymentInputDao paymentInputDao,Model model){
        log.info("update inventory payment method requested with purchase id {} and status id {} by user {}",paymentInputDao.getPurchaseId(),userService.getUser());
        try {
            inventoryService.updatePaymentMethod(paymentInputDao.getPurchaseId(),paymentInputDao.getId());
            log.info("update inventory payment method successful");
            model.addAttribute("var",paymentInputDao.getPurchaseId());
            return "updateSuccess";
        }catch (ResourceNotFound e){
            log.error("update inventory payment method failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        }catch (Exception e) {
            log.error("update inventory payment method failed due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }
        return "Failure";
    }

    @PostMapping("/update/paymentStatus")
    public String updatePaymentStatus(@ModelAttribute PaymentInputDao paymentInputDao,Model model){
        log.info("update inventory payment status requested with purchase id {} and status id {} by user {}",paymentInputDao.getPurchaseId(),userService.getUser());
        try {
            inventoryService.updatePaymentStatus(paymentInputDao.getPurchaseId(),paymentInputDao.getId());
            log.info("update inventory payment status successful");
            model.addAttribute("var",paymentInputDao.getPurchaseId());
            return "updateSuccess";
        }catch (ResourceNotFound e){
            log.error("update inventory payment status failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        }catch (Exception e) {
            log.error("update inventory payment status failed due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }
        return "Failure";
    }

    @GetMapping("/searchByPaymentStatus")
    public String getByPaymentStatus(Model model, @RequestParam("inputURL") String paymentStatus) {
        log.info("search by payment status requested with status {}",paymentStatus);
        try {
            List<NewInventoryResponseDao> inventoryResponseDao = inventoryService.getAllNewInventory(paymentStatus);
            log.info("search by payment status successfully retrieved");
            model.addAttribute("entries", inventoryResponseDao);
            return "inventoryGetByPaymentResponse";
        }catch (Exception e){
            log.error("search by payment status failed due to  {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }

        return "Failure";
    }



}

//TODO: 1. Appropriate Exceptions, 2. Multiple File Upload, 3. File upload with Inventory details in same page, 4. User Login
//TODO: 5.
