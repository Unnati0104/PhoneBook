/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.capp.controller;
import com.mycompany.capp.command.LoginCommand;
import com.mycompany.capp.command.UserCommand;
import com.mycompany.capp.domain.User;
import com.mycompany.capp.exception.UserBlockedException;
import com.mycompany.capp.service.UserService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 *
 * @author Simran
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping(value={"/","/index"})  //default page
    public String index(Model m){
        m.addAttribute("command",new LoginCommand());
        return "index";  //JSP page resolved by -WEB-INF/view/index.jsp
    }
    
    @RequestMapping(value="/login", method= RequestMethod.POST) 
    
//    dispatcher servlet is working at the front who will receive request and read data from parameters and bind both parameters
            
    public String handleLogin(@ModelAttribute("command") LoginCommand cmd,Model m, HttpSession session){
        try{
       User loggedInUser= userService.login(cmd.getLoginName(),cmd.getPassword());
       if(loggedInUser==null){
//           user supplied wrong details
            m.addAttribute("err","Login Failed! Enter valid credentials.");
            return "index";
       }
            else{
//                  Success
//                    check the role and redirect to a apt dashboard 
                    if(loggedInUser.getRole().equals(UserService.ROLE_ADMIN)){
                        addUserInSession(loggedInUser, session);
                        return "redirect:admin/dashboard";
                    }
                    else if(loggedInUser.getRole().equals(UserService.ROLE_USER)){
                        return "redirect:user/dashboard";
                    }
                    else{
                    //add error message and go back to login form
                    m.addAttribute("err","Invalid User ROLE");
                    return "index";
                    }
                 }
       }
        
        catch(UserBlockedException ex){
            //error message if user account is blocked and go back to login-form
            m.addAttribute("err",ex.getMessage());
            return "index"; 
        }
    }
    
    @RequestMapping(value="/logout")
    public String logout(HttpSession session){
//        this page will be displayed when user is logged off
        session.invalidate();
        return "redirect:index?act=lo";  //JSP - WEB-INF/view/index.jsp
    }
    
    @RequestMapping(value="/user/dashboard")
    public String userDashboard(){
//        this page will be displayed when user is logged in
        return "dashboard_user";  //JSP - WEB-INF/view/index.jsp
    }
    
    @RequestMapping(value="/admin/dashboard")
    public String adminDashboard(){
        return "dashboard_admin";  //JSP - WEB-INF/view/index.jsp
    }
    
    @RequestMapping(value="/admin/users")
    public String getUserList(Model m){
        m.addAttribute("userList", userService.getUserList());
        return "users";  //JSP 
    }
    
    @RequestMapping(value="/reg_form")
    public String registerationForm(Model m){
        UserCommand cmd = new UserCommand();
        m.addAttribute("command", cmd);
        return "reg_form"; //JSP
    }
    
    @RequestMapping(value="/register")
    public String registerUser(@ModelAttribute("command") UserCommand cmd ,Model m){
        try {
            User user = cmd.getUser();
            user.setRole(UserService.ROLE_USER);
            user.setLoginStatus(UserService.LOGIN_STATUS_ACTIVE);
            userService.register(user);
            return "redirect:index?act=reg"; //Login page
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            m.addAttribute("err","Username is already resgistered. Please save another username");
            return "reg_form"; //JSP
        }
 
    }
    
    private void addUserInSession(User u, HttpSession session){
        session.setAttribute("user",u);
        session.setAttribute("userId",u.getUserId());
        session.setAttribute("role",u.getRole());

    }
}
