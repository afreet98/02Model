package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;


public class ProductDAO {
	
	public ProductDAO(){
	}

	public void insertUser(Product product) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "insert into product values (seq_product_prod_no.NEXTVAL,?,?,?,?,?,sysdate)";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, product.getProdName());//제품명
		stmt.setString(2, product.getProdDetail());//상품상세보기
		stmt.setString(3, product.getManuDate());//제조일자
		stmt.setInt(4, product.getPrice());///가격
		stmt.setString(5, product.getFileName()); //상품 이미지
	
		
		stmt.executeUpdate();
		
		con.close();
	}
	
	
	public Map<String , Object> getProductList(Search search) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();
		
		// Original Query 구성
		String sql = "SELECT prod_no, prod_name, price, reg_date FROM product ";
		
		if (search.getSearchCondition() != null) {
			if ( search.getSearchCondition().equals("0") &&  !search.getSearchKeyword().equals("") ) {
				sql += " WHERE prod_no LIKE '" + search.getSearchKeyword()+"%'";
			} else if ( search.getSearchCondition().equals("1") && !search.getSearchKeyword().equals("")) {
				sql += " WHERE prod_name LIKE'" + search.getSearchKeyword()+"%'";
			} else if ( search.getSearchCondition().equals("2") && !search.getSearchKeyword().equals("")) {
				sql += " WHERE price LIKE'" + search.getSearchKeyword()+"%'";
			}
		}
		sql += " ORDER BY prod_no";
		
		System.out.println("ProductDAO::Original SQL :: " + sql);
		
		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDAO :: totalCount  :: " + totalCount);
		
		//==> CurrentPage 게시물만 받도록 Query 다시구성
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		System.out.println(search);

		List<Product> list = new ArrayList<Product>();
		
		while(rs.next()){
			Product product = new Product();
			product.setProdNo(Integer.parseInt(rs.getString("prod_no")));
			product.setProdName(rs.getString("prod_name"));
			product.setPrice(Integer.parseInt(rs.getString("price")));
			product.setRegDate(rs.getDate("reg_date"));
			list.add(product);
		}
		
		//==> totalCount 정보 저장
		map.put("totalCount", new Integer(totalCount));
		//==> currentPage 의 게시물 정보 갖는 List 저장
		map.put("list", list);

		rs.close();
		pStmt.close();
		con.close();

		return map;
	}
	
	private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*) "+
		          "FROM ( " +sql+ ") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if( rs.next() ){
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
	
	private String makeCurrentPageSql(String sql , Search search){
		sql = 	"SELECT * "+ 
					"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
									" 	FROM (	"+sql+" ) inner_table "+
									"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
					"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
		
		System.out.println("ProductDAO :: make SQL :: "+ sql);	
		
		return sql;
	}
	

	public void updateProduct(Product product) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "update product set prod_name=?,prod_detail=?,manufacture_day=?,price=?,image_file=? where prod_no=?";
		
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, product.getProdName());
		stmt.setString(2, product.getProdDetail());
		stmt.setString(3, product.getManuDate());
		stmt.setInt(4, product.getPrice());
		stmt.setString(5, product.getFileName());
		stmt.setInt(6, product.getProdNo());
		stmt.executeUpdate();
		
		con.close();
	}
	
	public Product findProduct(int ProdNo) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "select * from product where prod_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, ProdNo);

		ResultSet rs = stmt.executeQuery();

		Product product = null;
		while (rs.next()) {
			product = new Product();
			product.setProdNo(Integer.parseInt(rs.getString("prod_no")));
			product.setFileName(rs.getString("image_file"));
			product.setManuDate(rs.getString("manufacture_day"));
			product.setPrice(Integer.parseInt(rs.getString("price")));
			product.setProdDetail(rs.getString("prod_detail"));
			product.setProdName(rs.getString("prod_name"));
			product.setRegDate(rs.getDate("reg_date"));
		}
		
		con.close();

		return product;
	}
	
}