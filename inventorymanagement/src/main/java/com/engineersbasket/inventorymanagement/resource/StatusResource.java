package com.engineersbasket.inventorymanagement.resource;

import com.engineersbasket.inventorymanagement.dao.StatusDao;
import com.engineersbasket.inventorymanagement.dao.response.FailedDao;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import com.engineersbasket.inventorymanagement.service.StatusService;
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
@RequestMapping("/status")
public class StatusResource {

    public final Logger log = LoggerFactory.getLogger(StatusResource.class);

    @Autowired
    StatusService statusService;
    @Autowired
    UserService userService;

    @GetMapping("/getAll")
    public String getAllStatus(Model model){
        log.info("get all status requested");
        List<StatusDao> all = statusService.getAll();
        log.info("get all status successfully retrieved");
        model.addAttribute("status",all);
        return "statusGetAllResponse";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("statusDao",new StatusDao());
        return "statusCreateRequest";
    }

    @PostMapping("/create")
    public String addStatus(@ModelAttribute StatusDao statusDao,Model model){
        log.info("create status requested with status {} by user {}",statusDao.getName(),userService.getUser());
        try {
            statusDao = statusService.addStatus(statusDao);
            log.info("create status successful");
            model.addAttribute("statusDao",statusDao);
            return "statusCreateResponse";
        }catch(DataIntegrityViolationException e){
            log.error("create status failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.duplicateStatus, Messages.duplicateErrorMessage
                    + statusDao.getName());
            model.addAttribute("var",fa);
        }catch (Exception e) {
            log.error("create status failed due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }

        return "Failure";
    }

    @GetMapping("/update")
    public String update(Model model){
        model.addAttribute("statusDao",new StatusDao());
        return "statusUpdateRequest";
    }



   // @PutMapping("/update/{id}")
    @PostMapping("/update")
    public String updateStatus(@ModelAttribute StatusDao statusDao,Model model){
        log.info("update status requested id {} by user {}",statusDao.getId(),userService.getUser());
        try {
            StatusDao updatedStatus = statusService.update(statusDao);
            log.info("update status successful");
            model.addAttribute("var",updatedStatus.getId());
            return "updateSuccess";
        } catch(DataIntegrityViolationException e){
            log.error("update status failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.duplicateStatus, Messages.duplicateErrorMessage
                    + statusDao.getName());
            model.addAttribute("var",fa);
        }catch (ResourceNotFound e) {
            log.error("update status failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        } catch (Exception e){
            log.error("update status failed due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }
        return "Failure";
    }

    @GetMapping("/deleteById")
    public String deleteById(@RequestParam("id") Integer id,Model model){
        log.info("Delete status with id {} requested by user {}",id,userService.getUser());
        try{
            statusService.deleteById(id);
            log.info("Delete successful");
            return "DeleteSuccess";
        }catch (Exception e){
            log.error("Delete failed due to {}",e);
            FailedDao failedDao = Messages.unknownError;
            model.addAttribute("var",failedDao);
            return "Failure";
        }
    }

//    @GetMapping("/update")
//    public String update(Model model){
//        model.addAttribute("statusDao",new StatusDao());
//        return "statusUpdateRequest";
//    }

}
