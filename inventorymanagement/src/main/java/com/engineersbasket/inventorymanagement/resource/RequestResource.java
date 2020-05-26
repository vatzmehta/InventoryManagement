package com.engineersbasket.inventorymanagement.resource;

import com.engineersbasket.inventorymanagement.dao.PurchaseRequestDao;
import com.engineersbasket.inventorymanagement.dao.RequestNewInventoryDao;
import com.engineersbasket.inventorymanagement.dao.response.FailedDao;
import com.engineersbasket.inventorymanagement.dao.response.PurchaseRequestResponseDao;
import com.engineersbasket.inventorymanagement.dao.response.RequestNewInventoryResponseDao;
import com.engineersbasket.inventorymanagement.exceptions.QuantityMisMatchException;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import com.engineersbasket.inventorymanagement.exceptions.UserIdentificationException;
import com.engineersbasket.inventorymanagement.service.PurchaseRequestService;
import com.engineersbasket.inventorymanagement.service.RequestNewInventoryService;
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

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/request")
public class RequestResource {

    public final Logger log = LoggerFactory.getLogger(RequestNewInventoryDao.class);

    @Autowired
    RequestNewInventoryService requestNewInventoryService;

    @Autowired
    PurchaseRequestService purchaseRequestService;

    @Autowired
    UserService userService;

    @GetMapping("/getAll")
    public String getAll(Model model){
        log.trace("Get all request inventory requested");
        List<RequestNewInventoryResponseDao> all = requestNewInventoryService.getAll();
        log.info("All requests retrieval successful");
        model.addAttribute("var","All requests");
        model.addAttribute("requests",all);
        return "requestGetAllResponse";

    }
    
    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("requestNewInventoryDao",new RequestNewInventoryDao());
        return "requestCreateRequest";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute RequestNewInventoryDao requestNewInventory, Model model){
        log.info("request create requested with inventory type {} for class {} by user {}",
                requestNewInventory.getInventoryTypeId(),requestNewInventory.getRequestingClassName(),userService.getUser());
        if(requestNewInventory.getQuantity() < -1){
            log.error("request create failed due to entered invalid quantity value: {}","Quantity " + requestNewInventory.getQuantity());
            model.addAttribute("var",new FailedDao(ErrorCodes.invalidValue,Messages.invalidData +
                    requestNewInventory.getQuantity()));
            return "Failure";
        }

        try {
            requestNewInventoryService.create(requestNewInventory);
            model.addAttribute("var",requestNewInventory.getId());
            log.info("request create successful with id {}",requestNewInventory.getId());
            return "requestCreateResponse";
        } catch (ResourceNotFound e){
            log.error("request create failed due to {}",e);
            model.addAttribute("var",new FailedDao(e.getCode(),e.getMessage()));
        }catch (Exception e) {
            log.error("request create failed due to {}",e);
            model.addAttribute("var", Messages.unknownError);
        }
        return "Failure";
    }

    @GetMapping("/approveRequest")
    public String approve(Model model){
        model.addAttribute("purchaseRequestDao",new PurchaseRequestDao());
        return "approveRequest";
    }

    @GetMapping("/approve")
    public String approve(@RequestParam("id") Integer id, @RequestParam("quantity")  Integer quantity, Model model){
        log.info("purchase approve request for request id {} and quantity {} by user {}",id,quantity,userService.getUser());
        if(quantity < -1){
            log.error("approve request failed due to entered invalid quantity value: {}",quantity);
            model.addAttribute("var",new FailedDao(ErrorCodes.invalidValue,Messages.invalidData + quantity));
            return "Failure";
        }
        try {
            PurchaseRequestDao approve = purchaseRequestService.approve(id, quantity);
            log.info("purchase approve successful with id {}",approve.getId());
            model.addAttribute("approve",approve);
            return "purchaseRequestApproval";
        } catch(EntityNotFoundException e){
            log.error("purchase approval failed due to {}",e);
            model.addAttribute("var",new FailedDao(ErrorCodes.resourceNotFound,Messages.resourceNotFound + id));
        }catch (DataIntegrityViolationException e){
            log.error("purchase approval failed as request is already approved");
            model.addAttribute("var",new FailedDao(ErrorCodes.requestedAlreadyApproved, Messages.requestedAlreadyApproved));
        }catch (ResourceNotFound e){
            log.error("purchase approval failed due to {}",e);
            model.addAttribute("var",new FailedDao(e.getCode(),e.getMessage()));
        }catch (QuantityMisMatchException e){
            log.error("purchase approval failed due to {}",e);
            model.addAttribute("var",new FailedDao(e.getCode(),e.getMessage()));
        }catch (UserIdentificationException e){
            log.error("purchase approval failed due to {}",e);
            model.addAttribute("var",new FailedDao(e.getCode(),e.getMessage()));
        } catch (Exception e) {
            log.error("purchase approval failed due to {}",e);
            model.addAttribute("var", Messages.unknownError);
        }
        return "Failure";


    }

    @GetMapping("/approved/getAll")
    public String approvedGetAll(Model model){
        log.trace("Get all approved request inventory requested");
        List<PurchaseRequestDao> all = purchaseRequestService.getAll();
        log.info("All approved requests retrieval successful");
        model.addAttribute("requests",all);
        return "purchaserequestGetAllResponse";

    }

    @GetMapping("/purchaseOrder")
    public String purchaseOrder(Model model){
        log.trace("Get all purchase order requested");
        List<PurchaseRequestResponseDao> all = purchaseRequestService.getAllPurchaseOrder();
        log.info("Get all purchase order retrieval successful");
        model.addAttribute("requests",all);
        return "purchaseOrderGetAllResponse";
    }

    @GetMapping("/requiringApproval/getAll")
    public String requiringApproval(Model model){
        log.trace("requiring Approval request requested");
        List<RequestNewInventoryResponseDao> all = requestNewInventoryService.getNonApproved();
        log.info("All Non approved request retrieval successful");
        model.addAttribute("var","Pending Approval");
        model.addAttribute("requests",all);
        return "requiringApprovalGetAllResponse";

    }

    @GetMapping("/deleteById")
    public String deleteById(@RequestParam("id") Integer id,Model model){
        log.info("Delete request with id {} requested by user {}",id,userService.getUser());
        try{
            requestNewInventoryService.deleteById(id);
            log.info("Delete successful");
            return "DeleteSuccess";
        }catch (Exception e){
            log.error("Delete failed due to {}",e);
            FailedDao failedDao = Messages.unknownError;
            model.addAttribute("var",failedDao);
            return "Failure";
        }
    }

    @GetMapping("approved/deleteById")
    public String deletePurchaseById(@RequestParam("id") Integer id,Model model){
        log.info("Delete approved request with id {} requested by user {}",id,userService.getUser());
        try{
            purchaseRequestService.deleteById(id);
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
