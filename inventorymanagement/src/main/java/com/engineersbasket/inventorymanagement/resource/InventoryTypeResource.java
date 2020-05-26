package com.engineersbasket.inventorymanagement.resource;

import com.engineersbasket.inventorymanagement.dao.InventoryTypeDao;
import com.engineersbasket.inventorymanagement.dao.response.FailedDao;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import com.engineersbasket.inventorymanagement.service.InventoryTypeService;
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
@RequestMapping("/inventory/type")
public class InventoryTypeResource {

    public final Logger log = LoggerFactory.getLogger(InventoryTypeResource.class);

    @Autowired
    InventoryTypeService inventoryTypeService;
    @Autowired
    UserService userService;

    @GetMapping("/getAll")
    public String getAll(Model model){
        log.info("get all inventory type requested");
        List<InventoryTypeDao> all = inventoryTypeService.getAll();
        log.info("get all inventory type retrieved successfully");
        model.addAttribute("inventoryType",all);
        return "inventoryTypeGetAllResponse";
    }

    @GetMapping("/create")
    public String get(Model model){
        model.addAttribute("inventoryTypeDao",new InventoryTypeDao());
        return "inventoryTypeCreateRequest";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute InventoryTypeDao inventoryTypeDao, Model model){
        log.info("create inventory type requested with name {} by user {}", inventoryTypeDao.getName(),userService.getUser());
        try {
            inventoryTypeDao = inventoryTypeService.create(inventoryTypeDao);
            log.info("create inventory type successful");
            model.addAttribute("inventoryTypeDao", inventoryTypeDao);
            return "inventoryTypeCreateResponse";
        }  catch(DataIntegrityViolationException e){
            log.error("create inventory type failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.duplicateinventoryType, Messages.duplicateErrorMessage +
                    inventoryTypeDao.getName());
            model.addAttribute("var",fa);
        }catch (Exception e) {
            log.error("create inventory type failed due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }

        return "Failure";
        //return StatusDao.builder().id(inventoryTypeDao.getId()).name(inventoryTypeDao.getName()).build();
    }

    @GetMapping("/update")
    public String update(Model model){
        model.addAttribute("inventoryTypeDao",new InventoryTypeDao());
        return "inventoryTypeUpdateRequest";
    }


    @PostMapping("/update")
    public String updateInventory(@ModelAttribute InventoryTypeDao inventoryTypeDao, Model model){
        log.info("update inventory type requested with name {} by user {}", inventoryTypeDao.getName(),userService.getUser());
        try {
            inventoryTypeService.update(inventoryTypeDao);
            log.info("update inventory type successful");
            model.addAttribute("var", inventoryTypeDao.getId());
            return "updateSuccess";
        } catch(DataIntegrityViolationException e){
            log.error("update inventory type failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.duplicateStatus, Messages.duplicateErrorMessage
                    + inventoryTypeDao.getName());
            model.addAttribute("var",fa);
        }catch (ResourceNotFound e) {
            log.error("update inventory type failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        } catch (Exception e){
            log.error("update inventory type failed due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }
        return "Failure";
    }

    @GetMapping("/deleteById")
    public String deleteById(@RequestParam("id") Integer id,Model model){
        log.info("Delete inventory type with id {} requested by user {}",id,userService.getUser());
        try{
            inventoryTypeService.deleteById(id);
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
