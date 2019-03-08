package com.goodhouse.bill.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class BillJNDIDAO implements BillDAO_interface{
	
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/goodhouse");
		}catch(NamingException e) {
			e.printStackTrace();
		}
	}
	
	private static final String INSERT_STMT = //新增
			"INSERT INTO BILL (BILL_ID,ELE_CON_ID,BILL_PAY,BILL_DATE,BILL_PRODUCETIME," + 
			"BILL_STATUS,BILL_PAYMETHOD,BILL_PAYMENTTYPE) VALUES (to_char(sysdate,'yyyymmdd')||'-B'||LPAD(BILL_SEQ.NEXTVAL,5,0)," + 
			"?,?,?,?,?,?,?)";
	private static final String UPDATE = //修改
			"UPDATE BILL SET ELE_CON_ID = ?  , BILL_PAY = ? , BILL_DATE = ?  , BILL_PRODUCETIME = ? , BILL_STATUS = ? , BILL_PAYMETHOD = ? , BILL_PAYMENTTYPE = ? WHERE BILL_ID = ?";
	private static final String DELETE = //刪除
			"DELETE FROM BILL WHERE BILL_ID = ? ";
	private static final String GET_ONE_STMT = //單一查詢
			"SELECT BILL_ID , ELE_CON_ID  , BILL_PAY , BILL_DATE , BILL_PRODUCETIME , BILL_STATUS , BILL_PAYMETHOD , BILL_PAYMENTTYPE FROM BILL WHERE BILL_ID=?";
	private static final String GET_ALL_STMT = //查詢全部
			"SELECT BILL_ID , ELE_CON_ID  , BILL_PAY , BILL_DATE , BILL_PRODUCETIME , BILL_STATUS , BILL_PAYMETHOD , BILL_PAYMENTTYPE FROM BILL ORDER BY BILL_ID";
	
	@Override//新增
	public void insert(BillVO bVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, bVO.getEle_con_id());
			pstmt.setInt(2, bVO.getBill_pay());
			pstmt.setDate(3, bVO.getBill_date());
			pstmt.setDate(4, bVO.getBill_producetime());
			pstmt.setString(5, bVO.getBill_status());
			pstmt.setString(6, bVO.getBill_paymethod());
			pstmt.setString(7, bVO.getBill_paymenttype());
			
			/*
			 "INSERT INTO BILL (BILL_ID,ELE_CON_ID,BILL_PAY,BILL_DATE,BILL_PRODUCETIME," + 
			 "BILL_STATUS,BILL_PAYMETHOD,BILL_PAYMENTTYPE) VALUES (to_char(sysdate,'yyyymmdd')||'-B'||LPAD(BILL_SEQ.NEXTVAL,5,0)," + 
			 "?,?,?,?,?,?,?)"
			 */
			pstmt.executeUpdate();
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}
	
	@Override//修改
	public void update(BillVO bVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setString(1, bVO.getEle_con_id());
			pstmt.setInt(2, bVO.getBill_pay());
			pstmt.setDate(3, bVO.getBill_date());
			pstmt.setDate(4, bVO.getBill_producetime());
			pstmt.setString(5, bVO.getBill_status());
			pstmt.setString(6, bVO.getBill_paymethod());
			pstmt.setString(7, bVO.getBill_paymenttype());
			pstmt.setString(9, bVO.getBill_id());
			
			//"UPDATE BILL SET ELE_CON_ID = ?  = ? , BILL_PAY = ? , BILL_DATE = ?  , BILL_PRODUCETIME = ? , BILL_STATUS = ? , BILL_PAYMETHOD = ? , BILL_PAYMENTTYPE = ? WHERE BILL_ID = ?"
			pstmt.executeUpdate();
			
		} catch(SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}
	
	@Override//刪除
	public void delete(String bill_id) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setString(1, bill_id);
			
			//DELETE FROM BILL WHERE BILL_ID = ? 
			pstmt.executeUpdate();
			
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}
	
	@Override//單一查詢
	public BillVO findByPrimaryKey(String bill_id) {
		
		BillVO bVO = new BillVO();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setString(1, bill_id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				bVO = new BillVO();
				bVO.setBill_id(rs.getString("BILL_ID"));
				bVO.setEle_con_id(rs.getString("ELE_CON_ID"));
				bVO.setBill_pay(rs.getInt("BILL_PAY"));
				bVO.setBill_date(rs.getDate("BILL_DATE"));
				bVO.setBill_producetime(rs.getDate("BILL_PRODUCETIME"));
				bVO.setBill_status(rs.getString("BILL_STATUS"));
				bVO.setBill_paymethod(rs.getString("BILL_PAYMETHOD"));
				bVO.setBill_paymenttype(rs.getString("BILL_PAYMENTTYPE"));
				
			}
		//SELECT BILL_ID , ELE_CON_ID , BILL_PAY , BILL_DATE , BILL_PRODUCETIME , BILL_STATUS , BILL_PAYMETHOD , BILL_PAYMENTTYPE FROM BILL WHERE BILL_ID=?
		
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		
		return bVO;
	}
	
	@Override//全部查詢
	public List<BillVO> getAll(){
		
		List<BillVO> list = new ArrayList();
		BillVO bVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				bVO = new BillVO();
				bVO.setBill_id(rs.getString("BILL_ID"));
				bVO.setEle_con_id(rs.getString("ELE_CON_ID"));
				bVO.setBill_pay(rs.getInt("BILL_PAY"));
				bVO.setBill_date(rs.getDate("BILL_DATE"));
				bVO.setBill_producetime(rs.getDate("BILL_PRODUCETIME"));
				bVO.setBill_status(rs.getString("BILL_STATUS"));
				bVO.setBill_paymethod(rs.getString("BILL_PAYMETHOD"));
				bVO.setBill_paymenttype(rs.getString("BILL_PAYMENTTYPE"));
				
				list.add(bVO);
			}
		
		} catch(SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		
		return list;
	}

//	@Override
//	public void insert(Connection con, List<BillVO> billVOlist, String eleConKey) {
//		// TODO Auto-generated method stub
//				PreparedStatement pstmt = null;
//				
//				try {
//					con = ds.getConnection();
//					pstmt = con.prepareStatement(INSERT_STMT);
//					
//					for(BillVO billVO : billVOlist) {
//						pstmt.setString(1, eleConKey);
//						pstmt.setString(2, billVO.getEmp_id());
//						pstmt.setInt(3, billVO.getBill_pay());
//						pstmt.setDate(4, billVO.getBill_date());
//						pstmt.setDate(5, billVO.getBill_producetime());
//						pstmt.setString(6, billVO.getBill_status());
//						pstmt.setString(7, billVO.getBill_paymethod());
//						pstmt.setString(8, billVO.getBill_paymenttype());
//						pstmt.executeUpdate();
//					}
//					/*
//					 "INSERT INTO BILL (BILL_ID,ELE_CON_ID,EMP_ID,BILL_PAY,BILL_DATE,BILL_PRODUCETIME," + 
//					 "BILL_STATUS,BILL_PAYMETHOD,BILL_PAYMENTTYPE) VALUES (to_char(sysdate,'yyyymmdd')||'-B'||LPAD(BILL_SEQ.NEXTVAL,5,0)," + 
//					 "?,?,?,?,?,?,?,?)"
//					 */
//					
//				} catch (SQLException se) {
//					try {
//						con.rollback();
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					throw new RuntimeException("A database error occured. "
//							+ se.getMessage());
//				}finally {
//					if (pstmt != null) {
//						try {
//							pstmt.close();
//						} catch (SQLException se) {
//							se.printStackTrace(System.err);
//						}
//					}
//					if (con != null) {
//						try {
//							con.close();
//						} catch (Exception e) {
//							e.printStackTrace(System.err);
//						}
//					}
//				}
//	}
	
}
