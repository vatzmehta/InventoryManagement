package com.engineersbasket.inventorymanagement.resource;

import com.engineersbasket.inventorymanagement.dao.CreateUserRequestDao;
import com.engineersbasket.inventorymanagement.dao.EnableUserDao;
import com.engineersbasket.inventorymanagement.dao.UpdateUserDao;
import com.engineersbasket.inventorymanagement.dao.UserDao;
import com.engineersbasket.inventorymanagement.dao.response.FailedDao;
import com.engineersbasket.inventorymanagement.exceptions.IncorrectPassword;
import com.engineersbasket.inventorymanagement.exceptions.PasswordMisMatchException;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import com.engineersbasket.inventorymanagement.service.UserNotificationService;
import com.engineersbasket.inventorymanagement.service.UserService;
import com.engineersbasket.inventorymanagement.utils.ErrorCodes;
import com.engineersbasket.inventorymanagement.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserResource {

    public final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    UserService userService;
    @Autowired
    UserNotificationService userNotification;

    @GetMapping("/getAll")
    public String getAllUsers(Model model){
        log.info("get all users requested");
        List<UserDao> all = userService.getAll();
        log.info("get all users retrieved successfully");
        model.addAttribute("users",all);
        return "userGetAllResponse";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("createUserRequestDao",new CreateUserRequestDao());
        return "userCreateRequest";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute CreateUserRequestDao createUserRequestDao,Model model){
        log.info("create user requested with username {} by user {}",createUserRequestDao.getUserName(),userService.getUser());
        try {
            if(!Arrays.equals(createUserRequestDao.getPassword(),createUserRequestDao.getConfirmPassword())){
                throw new PasswordMisMatchException();
            }
            UserDao userDao = userService.create(createUserRequestDao);
            model.addAttribute("userDao",userDao);
            log.info("create user successful");
            return "userCreateResponse";
        }catch(DataIntegrityViolationException e){
            log.error("create user failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.duplicateUser, Messages.duplicateErrorMessage + createUserRequestDao.getUserName());
            model.addAttribute("var",fa);
        }catch(PasswordMisMatchException e){
            log.error("create user failed due to {}",e);
            model.addAttribute("var",new FailedDao(e.getCode(),e.getMessage()));
        }catch (Exception e) {
            log.error("create user failed due to {}",e);
            FailedDao fa = Messages.unknownError;
            model.addAttribute("var",fa);
        }

        return "Failure";
    }

    @GetMapping("/update")
    public String update(Model model){
        model.addAttribute("userDao",new UpdateUserDao());
        return "userUpdateRequest";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute UpdateUserDao userDao,Model model){
        log.info("change password / update user requested by user {} ", userService.getUser());
        try{
            userService.update(userDao);
            model.addAttribute("var",userDao.getUserName());
            log.info("change password / update user successful");
            return "updateSuccess";
        }catch (IncorrectPassword  e){
            log.error("change password / update user unsuccessful due to {}",e);
            FailedDao failedDao = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",failedDao);
        } catch (PasswordMisMatchException e){
            log.error("change password / update user unsuccessful due to {}",e);
            FailedDao failedDao = new FailedDao(e.getCode(),e.getMessage());
            model.addAttribute("var",failedDao);
        }catch(Exception e){
            log.error("change password / update user unsuccessful due to {}",e);
            FailedDao failedDao = Messages.unknownError;
            model.addAttribute("var",failedDao);
        }
        return "Failure";
    }

    @GetMapping("/enable")
    public String enable(Model model){
        model.addAttribute("enableUserDao",new EnableUserDao());
        return "userEnableRequest";
    }

    @PostMapping("/enable")
//    @PreAuthorize("hasRole(ADMIN)")
    public String enable(@ModelAttribute EnableUserDao userDao,Model model){
        log.info("enable / disable user requested for {} by {}",userDao.getUserName(),userService.getUser());
        try{
            userService.enableUser(userDao.getUserName(),userDao.getIsEnable());
            model.addAttribute("var",userDao.getUserName());
            log.info("enable / disable user successful");
            return "updateSuccess";
        } catch (UsernameNotFoundException e){
            log.error("enable / disable failed due to {}",e);
            FailedDao fa = new FailedDao(ErrorCodes.resourceNotFound, e.getMessage() );
            model.addAttribute("var", fa);
        }catch (ResourceNotFound e) {
            log.error("enable / disable failed due to {}",e);
            FailedDao fa = new FailedDao(e.getCode(), e.getMessage());
            model.addAttribute("var", fa);
        }catch (Exception e) {
            log.error("enable / disable failed due to {}",e);
            FailedDao failedDao = Messages.unknownError;
            model.addAttribute("var", failedDao);
        }
            return "Failure";
    }

    @GetMapping("notifications/getAll")
    public String getAllNotifications(Model model){
        log.info("get all user notifications");
        List<String> all = null;
        try {
            all = userNotification.getNotifications();
        } catch (Exception e) {
           log.error("get all notifications failed due to {}",e);
        }
        log.info("get all users notification successfully");
        model.addAttribute("n",all);
        return "userGetAllNotification"; //TODO:2
    }
}
