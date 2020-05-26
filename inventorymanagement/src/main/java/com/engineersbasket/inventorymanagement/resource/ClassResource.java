package com.engineersbasket.inventorymanagement.resource;

import com.engineersbasket.inventorymanagement.dao.ClassDao;
import com.engineersbasket.inventorymanagement.dao.DeployDao;
import com.engineersbasket.inventorymanagement.dao.response.DeployGetAllResponse;
import com.engineersbasket.inventorymanagement.dao.response.FailedDao;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import com.engineersbasket.inventorymanagement.service.ClassService;
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
@RequestMapping("/class")
public class ClassResource {

    public final Logger log = LoggerFactory.getLogger(ClassResource.class);

    @Autowired
    ClassService classService;
    @Autowired
    UserService userService;

    @GetMapping("/getAll")
    public String getAll(Model model){
        log.trace("Get all classes requested");
        List<ClassDao> all = classService.getAll();
        log.info("All classes retrieval successful");
        model.addAttribute("classes",all);
        return "classGetAllResponse";

    }
    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("classDao",new ClassDao());
        return "classCreateRequest";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ClassDao classDao,Model model){
        log.info("Create Class with classname {} by user {}",classDao.getClassName(),userService.getUser());
        try {
            classDao = classService.create(classDao);
            log.info("Class created successfully with id {}",classDao.getId());
            model.addAttribute("classDao",classDao);
            return "classCreateResponse";
        }catch(DataIntegrityViolationException e){
            log.error("Create class failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.duplicateClass, Messages.duplicateErrorMessage +
                    classDao.getClassName());
            model.addAttribute("var",fa);
        }catch (Exception e) {
           log.error("Unknown Error: {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }

        return "Failure";
    }

    @GetMapping("/deploy")
    public String deploy(Model model){
        model.addAttribute("deployDao",new DeployDao());
        return "classDeployRequest";
    }

    @PostMapping("/deploy")
    public String deploy(@ModelAttribute DeployDao deployDao,Model model){
        try {
            log.info("Deploying {} to class {} by user {}",deployDao.getSerialNumber(),deployDao.getClassName(),userService.getUser());
            boolean b  = classService.deploy(deployDao);
            log.info("Successfully deployed {}",deployDao.getSerialNumber());
            return "classDeployResponse";
        } catch (ResourceNotFound e) {
            log.error("Deploy failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        }
        return "Failure";
    }

    @GetMapping("deploy/getAll")
    public String deployGetAll(Model model){
        log.info("Get all deployed inventory requested");
        List<DeployGetAllResponse> all = classService.getAllDeploy();
        model.addAttribute("all",all);
        log.info("Successfully retrieved all the deployed inventory");
        return "deployGetAllResponse";

    }

    @GetMapping("/update")
    public String update(Model model){
        model.addAttribute("classDao",new ClassDao());
        return "classUpdateRequest";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute ClassDao classDao,Model model){
        log.info("Update requested for {} by user {}",classDao.getId(),userService.getUser());
        try {
            classService.update(classDao);
            model.addAttribute("var",classDao.getId());
            return "updateSuccess";
        } catch(DataIntegrityViolationException e){
            log.error("update class failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.duplicateClass, Messages.duplicateErrorMessage +
                    classDao.getClassName());
            model.addAttribute("var",fa);
        }catch (ResourceNotFound e) {
            log.error("Update Request failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",fa);
        } catch (Exception e){
            log.error("Update Request failed due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }
        return "Failure";
    }

    @GetMapping("/deleteById")
    public String deleteById(@RequestParam("id") Integer id,Model model){
        log.info("Delete class with id {} requested by user {}",id,userService.getUser());
        try{
            classService.deleteById(id);
            log.info("Delete successful");
            return "DeleteSuccess";
        }catch (Exception e){
            log.error("Delete failed due to {}",e);
            FailedDao failedDao = Messages.unknownError;
            model.addAttribute("var",failedDao);
            return "Failure";
        }
    }

    @GetMapping("/deployed")
    public String deployedClass(Model model){
        model.addAttribute("classDao",new ClassDao());
        return "classDeployedRequest";
    }

    @PostMapping("/deployed")
    public String deployedClass(@ModelAttribute ClassDao classDao, Model model){
        log.trace("Get deployed inventory requested with classname {}",classDao.getClassName());
        try {
        List<DeployGetAllResponse> all = classService.getDeployedClass(classDao.getClassName());
        log.info("All deployed class inventory retrieval successful");
        model.addAttribute("all",all);
        return "classDeployedResponse";
        } catch (ResourceNotFound e) {
            log.error("Get deployed inventory with classname {} failed due to {}",classDao.getClassName(),e);
           model.addAttribute("var",new FailedDao(e.getCode(),e.getMessage()));
        }  catch (Exception e){
            log.error("Get deployed inventory with classname {} failed due to {}",classDao.getClassName(),e);
            model.addAttribute("var",Messages.unknownError);
        }
       return "Failure";

    }

//    public String getUser(){
//        try {
//            return userService.getCurrentUser().getUserName();
//        } catch (Exception e) {
//            return "--Unknown--";
//        }
//    }

//    @GetMapping("deploy/searchByStatus")
//    public String getByStatus(Model model,@RequestParam("inputURL") String status){
//
//        try {
//            List<DeployGetAllResponse> inventoryResponseDao = classService.getByStatus(status);
//            model.addAttribute("all",inventoryResponseDao);
//            return "deployGetAllResponse";
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }



}
