package com.goodhouse.bill.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodhouse.ele_contract.model.Ele_ContractService;
import com.goodhouse.ele_contract.model.Ele_ContractVO;

//@WebServlet(name="/BillSchedule" , loadOnStartup = 1 ,urlPatterns="/BillSchedule")
public class BillSchedule extends HttpServlet{

	Timer timerSch;
//	SimpleDateFormat sdf,df;
//	Date timeStart;//開始時間
	
	protected void doGet(HttpServletRequest req , HttpServletResponse res) {
		doGet(req, res);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/plain");
	}
	
	public void init() {
		
//		SimpleDateFormat sdf = new SimpleDateFormat();
//		long long_now =  System.currentTimeMillis();
		
		TimerTask task = new TimerTask() {
			
			public void run() {
				
				Ele_ContractService eleConSvc = new Ele_ContractService();
				
				
				
				
				
			}
		};
		Calendar cal = new GregorianCalendar(2019,Calendar.MARCH, 3,0,0,0);
		timerSch.scheduleAtFixedRate(task, cal.getTime(), 1*60*60*1000);
	}
	

	@Override
	public void destroy() {
		if(timerSch != null) {
			timerSch.cancel();
		}
	}
	
	public static void insertNewBill() {
		
		
		
		
	}
}
