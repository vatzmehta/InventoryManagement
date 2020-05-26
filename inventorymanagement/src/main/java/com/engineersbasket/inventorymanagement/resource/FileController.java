package com.engineersbasket.inventorymanagement.resource;


import com.engineersbasket.inventorymanagement.dao.InvoicesDao;
import com.engineersbasket.inventorymanagement.service.InvoiceService;
import com.engineersbasket.inventorymanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
public class FileController {

    public final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    InvoiceService invoiceService;
    @Autowired
    UserService userService;

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam(value = "file")MultipartFile file,Model model){
        log.trace("upload file requested by user {}",userService.getUser());
        try {
            InvoicesDao invoicesDao = invoiceService.storeFile(file);
            log.trace("file uploaded successfully");
            model.addAttribute("file12",invoicesDao);
            return "fileUploadSuccess";
        } catch (Exception e) {
           log.error("file upload unsuccessful due to {}",e);
        }
        return "Failure";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id){
        log.info("file download with id {} requested by user {}",id,userService.getUser());
        try {
            InvoicesDao invoicesDao = invoiceService.getFile(id);
            log.info("file retrieved successfully");
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(invoicesDao.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + invoicesDao.getFileName() + "\"")
                    .body(new ByteArrayResource(invoicesDao.getData()));
        } catch (Exception e) {
            log.error("file download failed due to {}",e);
        }
        return ResponseEntity.notFound().build();


    }

}
