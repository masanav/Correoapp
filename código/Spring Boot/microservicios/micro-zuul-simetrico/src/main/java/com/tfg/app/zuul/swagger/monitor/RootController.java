package com.tfg.app.zuul.swagger.monitor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
   @GetMapping ("/api/documentacion")
   public String swaggerUi() {
       return "redirect:/api/documentacion/swagger-ui/index.html";
   }
}
