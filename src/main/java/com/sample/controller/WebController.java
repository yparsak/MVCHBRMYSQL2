package com.sample.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sample.model.User;
import com.sample.service.UserService;

@Controller
public class WebController {

	
	private UserService userService;
	
	@Autowired(required=true)
	@Qualifier(value="UserService")
	public void setUserService(UserService us){
		this.userService = us;
	}	
	
	@RequestMapping("/")
	public String index(Model model, HttpServletResponse response, HttpServletRequest request) {
		
		Long cnt = (long) 0;
		
		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("counter")) {
				cnt = Long.parseLong(c.getValue());
				cnt ++;
			}
		}

		Cookie cookie = new Cookie("counter",cnt.toString());
		response.addCookie(cookie);
		
		
		model.addAttribute("cnt", cnt);
		
		return "index";
	}

	@RequestMapping(value = {"/user"}, method = RequestMethod.GET)
	public String listUsers(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("listUsers", this.userService.listUsers());
		return "user";
	}
	
	@RequestMapping(value= "/user/add", method = RequestMethod.POST)
	public String addUser(@ModelAttribute("user") User u){
		
		if(u.getId() == 0){
			this.userService.addUser(u);
		}else{
			this.userService.updateUser(u);
		}	
		return "redirect:/user";
	}
	

	
	
	@RequestMapping("/remove/{id}")
    public String removeUser(@PathVariable("id") int id){
        this.userService.removeUserById(id);
        return "redirect:/user";
    }
 
	@RequestMapping("/edit/{id}")
    public String editUser(@PathVariable("id") int id, Model model){
        model.addAttribute("user", this.userService.getUserById(id));
        model.addAttribute("listUsers", this.userService.listUsers());
        return "user";
    }

	
	@RequestMapping(value = {"/login"}, method = RequestMethod.GET)
	public String Login(Model model) {
		model.addAttribute("user", new User());
		return "login";
	}
	
	@RequestMapping(value= "/login/check", method = RequestMethod.POST)
	public String CheckLogin(@ModelAttribute("user") User u, Model model){
		
		boolean is_user = this.userService.isUser(u.getName(), u.getPassword());
		boolean is_admin = this.userService.isAdmin(u.getName(), u.getPassword());

		System.out.println("user: "+u.getName()+" passoword: "+u.getPassword());
		
		if (is_user) {
			System.out.println("User is found !!");
		} else {
			System.out.println("User is NOT found !!");
		}
		
		if (is_admin) {
			System.out.println("User is admin !!");
		} else {
			System.out.println("User is NOT admin !!");
		}
		
		model.addAttribute("listUsers", this.userService.getUser(u.getName(),u.getPassword() ));

		return "user";
	}
	
}
