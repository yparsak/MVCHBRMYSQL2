package com.sample.dao;

import java.util.List;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sample.model.User;

public class UserDaoImpl implements UserDao {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

	@Override
	public void addUser(User u) {
		Session session = this.sessionFactory.getCurrentSession();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(u.getPassword());
		u.setPassword(hashedPassword);
		session.persist(u);
	}

	@Override
	public void updateUser(User u) {
		Session session = this.sessionFactory.getCurrentSession();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(u.getPassword());
		u.setPassword(hashedPassword);
		session.update(u);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> listUsers() {
		Session session = this.sessionFactory.getCurrentSession();
		List<User> UserList = session.createQuery("from User").list();
		return UserList;
	}

	@Override
	public User getUserById(int id) {
		Session session = this.sessionFactory.getCurrentSession();		
		User u = (User) session.load(User.class, new Integer(id));
		return u;
	}

	@Override
	public void removeUserById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		User u = (User) session.load(User.class, new Integer(id));
		if(null != u){
			session.delete(u);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUser(String name, String password){

		Session session = this.sessionFactory.getCurrentSession();
		String hql = "from User where name='"+name+"' and password='"+password+"'";
		System.out.println(hql);
		List<User> UserList = session.createQuery(hql).list();
		return UserList;
	}
	
	@SuppressWarnings("unchecked")
	public boolean isUser(String name, String password) {
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "from User where name='"+name+"' and password='"+password+"'";
		System.out.println(hql);
		List<User> UserList = session.createQuery(hql).list();
		if (UserList.size() > 0) {
			return true;
		}
		else return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean isAdmin(String name, String password) {
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "from User where name='"+name+"' and password='"+password+"'";
		System.out.println(hql);
		List<User> UserList = session.createQuery(hql).list();
		if (UserList.size() > 0) {
			User user = UserList.get(0);
			if (user.getName().equals("name")) {
				return true;
			}
		}
		
		return false;
	}
}
