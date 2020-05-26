package com.engineersbasket.inventorymanagement.resource;

import com.engineersbasket.inventorymanagement.db.User;
import com.engineersbasket.inventorymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @GetMapping
    public String welcome(Model model) throws Exception {
        StringBuilder sb = new StringBuilder();
        User currentUser = userService.getCurrentUser();
        sb.append("Hello "+currentUser.getUserName());
        if(currentUser.getUnreadNotificationCount() != null && currentUser.getUnreadNotificationCount() > 0) {
           sb.append(", you have " + currentUser.getUnreadNotificationCount() + " Unread Notifications");
        }
        model.addAttribute("hello",sb.toString());
        return "welcome";
    }

    @GetMapping("/deleteById")
    public String deleteById(){
        return "deleteById";
    }

//    @GetMapping("/error")
//    public String error(HttpServletRequest request, Model model){
//        Integer code = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
//
//            FailedDao failedDao = new FailedDao(code,exception.toString());
//            model.addAttribute("var",failedDao);
//
//
//
//        return "Failure";
//
//    }
}
