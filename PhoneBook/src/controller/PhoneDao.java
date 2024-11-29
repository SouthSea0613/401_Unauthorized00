package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.PhoneInfo;
import bean.PhoneUnivInfo;
import bean.PhoneCompanyInfo;
import common.JdbcUtill;

public class PhoneDao {
	Connection connection;
	PreparedStatement preparedStatement;
	private ResultSet resultSet1;
	private ResultSet resultSet2;	// 테이블을 동시에 최대 2개까지 사용하게되므로 > java에서 sql join을 통해 데이터를 가져오는걸 하지 않았으므로 다른 방법을 위한 두개의 resultSet 생성
	
	// update 판단
	public boolean isSuccess(int result1, int result2) {
		if ((result1 != 0) && (result2 != 0)) {
			System.out.printf("INSERT / DELETE 성공\n\n");
			return true;
		}
		else {
			System.out.printf("INSERT / DELETE 실패\n\n");
			return false;
		}
	}
	
	// resultset.close() > preparedStatement.close() > connection.close() 가 각 기능마다 항상 필요하므로 method화
	private void close() {
		JdbcUtill.close(resultSet1);
		JdbcUtill.close(resultSet2);
		JdbcUtill.close(preparedStatement);
		JdbcUtill.close(connection);
	}
	
	// transaction controll method화 > INSERT, DELETE에서 필요
	private void transaction(boolean result) {
		if (result) {
			JdbcUtill.commit(connection);
		}
		else {
			JdbcUtill.rollback(connection);
		}
	}
	
	// 데이터 입력
	// PhoneInfo 하위 하나의 method로 처리 > 싱글톤도 가능한데 싱글톤의 경우 각각 try-catch-finally까지 너무 길어질것같아서 하기 싫었음
	public void insert(PhoneInfo phoneInfo) {
		connection = JdbcUtill.connect();
		boolean result = false;
		
		try {
			preparedStatement = connection.prepareStatement("INSERT INTO PHONEMEMBERS VALUES(?, ?, ?)");
			preparedStatement.setString(1, phoneInfo.getName());	// 이름, 전화번호는 공통이므로 조건없이 바로
			preparedStatement.setString(2, phoneInfo.getPhone());
			
			if(phoneInfo instanceof PhoneUnivInfo) {	// PhoneUnivInfo타입일경우
				preparedStatement.setString(3, "U");	// 3번째 COLUMN에 U
				int result1 = preparedStatement.executeUpdate();	// PHONEMEMBERS TABLE에서의 처리
				
				PhoneUnivInfo phoneUnivInfo = (PhoneUnivInfo)phoneInfo;	// 형변환 후 나머지 처리
				preparedStatement = connection.prepareStatement("INSERT INTO UNIVMEMBERS VALUES(?, ?, ?)");
				preparedStatement.setString(1, phoneUnivInfo.getPhone());
				preparedStatement.setString(2, phoneUnivInfo.getMajor());
				preparedStatement.setInt(3, phoneUnivInfo.getGrade());
				int result2 = preparedStatement.executeUpdate();	// UNIVMEMBERS TABLE에서의 처리
				
				result = isSuccess(result1, result2);	// #1. 두개의 TABLE을 처리하므로 동시에 성공해야함
			}
			else if(phoneInfo instanceof PhoneCompanyInfo) {	// PhoneCompanyInfo타입일경우
				preparedStatement.setString(3, "C");	// 3번째 COLUMN에 C
				int result1 = preparedStatement.executeUpdate();	// PHONEMEMBERS TABLE에서의 처리
				
				PhoneCompanyInfo phoneCompanyInfo = (PhoneCompanyInfo)phoneInfo;
				preparedStatement = connection.prepareStatement("INSERT INTO COMPANYMEMBERS VALUES(?, ?)");
				preparedStatement.setString(1, phoneCompanyInfo.getPhone());
				preparedStatement.setString(2, phoneCompanyInfo.getCompany());
				int result2 = preparedStatement.executeUpdate();	// COMPANYMEMBERS TABLE에서의 처리
				
				result = isSuccess(result1, result2);	// #1
			}
			else {
				preparedStatement.setString(3, "N");
				int result1 = preparedStatement.executeUpdate();
				
				result = isSuccess(result1, result1);	// = isSuccess(result1)
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			transaction(result);
			close();
		}
	}
	
	// 검색
	public ArrayList<PhoneInfo> search(String name) {
		connection = JdbcUtill.connect();
		ArrayList<PhoneInfo> phoneList = new ArrayList<PhoneInfo>();
		
		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM PHONEMEMBERS WHERE NAME = ?");
			preparedStatement.setString(1, name);
			resultSet1 = preparedStatement.executeQuery();

			while (resultSet1.next()) {	// 이름은 중복될수 있으니 전부 가져오기 위해 while
				String kind = resultSet1.getString("KIND");	// KIND를 통해 타입 확인
				if (kind.equals("N")) {	// N타입은 PHONEMEMBERS의 NAME, PHONE이 전부
					String name2 = resultSet1.getString("NAME");
					String phone = resultSet1.getString("PHONE");
					PhoneInfo phoneInfo = new PhoneInfo(name2, phone);
					phoneList.add(phoneInfo);
				}
				else if (kind.equals("U")) {	// U타입은 PHONEMEMBERS의 NAME, PHONE과 UNIVMEMBERS의 MAJOR, GRADE
					preparedStatement = connection.prepareStatement("SELECT * FROM UNIVMEMBERS WHERE PHONE = ?");
					preparedStatement.setString(1, resultSet1.getString("PHONE"));	// #2. 부모TABLE의 외래키가 PHONE이므로 PHONE을 기준으로 검색
					
					resultSet2 = preparedStatement.executeQuery();
					resultSet2.next();
					
					phoneList.add(
						new PhoneUnivInfo(
							resultSet1.getString("NAME"), 
							resultSet1.getString("PHONE"), 
							resultSet2.getString("MAJOR"), 
							resultSet2.getInt("GRADE")
							)
						);
				}
				else if (kind.equals("C")) {	// C타입은 PHONEMEMBERS의 NAME, PHONE과 COMPANYMEMBERS의 COMPANY
					preparedStatement = connection.prepareStatement("SELECT * FROM COMPANYMEMBERS WHERE PHONE = ?");
					preparedStatement.setString(1, resultSet1.getString("PHONE"));	// #2
					
					resultSet2 = preparedStatement.executeQuery();
					resultSet2.next();
					
					phoneList.add(
						new PhoneCompanyInfo(
							resultSet1.getString("NAME"), 
							resultSet1.getString("PHONE"), 
							resultSet2.getString("COMPANY")
							)
						);
				}
				else {	// 만일을 위해 만들어둠 > 예외처리해야할때 쓸 공간
					
				}
			}
			
			return phoneList;
		} 
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		} 
		finally {
			close();
		}
		
		//여기
	}
	
	// 전체출력 > 위에꺼 검색에서 WHERE절 삭제
	public ArrayList<PhoneInfo> showAll() {
		connection = JdbcUtill.connect();
		ArrayList<PhoneInfo> phoneList = new ArrayList<PhoneInfo>();
		
		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM PHONEMEMBERS");
			resultSet1 = preparedStatement.executeQuery();

			while (resultSet1.next()) {
				String kind = resultSet1.getString("KIND");
				if (kind.equals("N")) {
					phoneList.add(
						new PhoneInfo(
							resultSet1.getString("NAME"), 
							resultSet1.getString("PHONE")
							)
						);
				}
				else if (kind.equals("U")) {
					preparedStatement = connection.prepareStatement("SELECT * FROM UNIVMEMBERS WHERE PHONE = ?");
					preparedStatement.setString(1, resultSet1.getString("PHONE"));
					resultSet2 = preparedStatement.executeQuery();
					resultSet2.next();
					phoneList.add(
						new PhoneUnivInfo(
							resultSet1.getString("NAME"), 
							resultSet1.getString("PHONE"), 
							resultSet2.getString("MAJOR"), 
							resultSet2.getInt("GRADE")
							)
						);
				}
				else if (kind.equals("C")) {
					preparedStatement = connection.prepareStatement("SELECT * FROM COMPANYMEMBERS WHERE PHONE = ?");
					preparedStatement.setString(1, resultSet1.getString("PHONE"));
					resultSet2 = preparedStatement.executeQuery();
					resultSet2.next();
					phoneList.add(
						new PhoneCompanyInfo(
							resultSet1.getString("NAME"), 
							resultSet1.getString("PHONE"), 
							resultSet2.getString("COMPANY")
							)
						);
				}
				else {
					
				}
			}
			
			return phoneList;
		} 
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		} 
		finally {
			close();
		}
	}
	
	// 수정 : 전화번호 기준으로 수정
	public void update(String phone, PhoneInfo phoneInfo) {
		connection = JdbcUtill.connect();
		boolean result = false;
		
		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM PHONEMEMBERS WHERE PHONE = ?");
			preparedStatement.setString(1, phone);
			resultSet1 = preparedStatement.executeQuery();
			if(resultSet1.next()) {
				preparedStatement = connection.prepareStatement("UPDATE PHONEMEMBERS SET NAME = ? WHERE PHONE = ?");
				preparedStatement.setString(1, phoneInfo.getName());
				preparedStatement.setString(2, phone);
				preparedStatement.executeUpdate();
				
				if(phoneInfo instanceof PhoneUnivInfo) {
					if(resultSet1.getString("KIND").equals("C")) {	// C타입이었을경우 자식테이블 삭제해둬야함
						preparedStatement = connection.prepareStatement("DELETE FROM COMPANYMEMBERS WHERE PHONE = ?");
						preparedStatement.setString(1, phone);
						preparedStatement.executeUpdate();
					}
					
					if(!resultSet1.getString("KIND").equals("U")) {	// U타입이 아니었을경우 U타입으로 변경
						preparedStatement = connection.prepareStatement("UPDATE PHONEMEMBERS SET KIND = U WHERE PHONE = ?");
						preparedStatement.setString(1, phone);
						preparedStatement.executeUpdate();
					}
					
					PhoneUnivInfo phoneUnivInfo = (PhoneUnivInfo)phoneInfo;	// 데이터 삽입
					preparedStatement = connection.prepareStatement("INSERT INTO UNIVMEMBERS VALUES(?, ?, ?)");
					preparedStatement.setString(1, phoneUnivInfo.getPhone());
					preparedStatement.setString(2, phoneUnivInfo.getMajor());
					preparedStatement.setInt(3, phoneUnivInfo.getGrade());
					preparedStatement.executeUpdate();
				}
				else if(phoneInfo instanceof PhoneCompanyInfo) {
					if(resultSet1.getString("KIND").equals("U")) {
						preparedStatement = connection.prepareStatement("DELETE FROM UNIVMEMBERS WHERE PHONE = ?");
						preparedStatement.setString(1, phone);
						preparedStatement.executeUpdate();
					}
					
					if(!resultSet1.getString("KIND").equals("C")) {
						preparedStatement = connection.prepareStatement("UPDATE PHONEMEMBERS SET KIND = C WHERE PHONE = ?");
						preparedStatement.setString(1, phone);
						preparedStatement.executeUpdate();
					}
					
					PhoneCompanyInfo phoneCompanyInfo = (PhoneCompanyInfo)phoneInfo;
					preparedStatement = connection.prepareStatement("INSERT INTO COMPANYMEMBERS VALUES(?, ?)");
					preparedStatement.setString(1, phoneCompanyInfo.getPhone());
					preparedStatement.setString(2, phoneCompanyInfo.getCompany());
					preparedStatement.executeUpdate();
				}
				else {
					if(resultSet1.getString("KIND").equals("C")) {
						preparedStatement = connection.prepareStatement("DELETE FROM COMPANYMEMBERS WHERE PHONE = ?");
						preparedStatement.setString(1, phone);
						preparedStatement.executeUpdate();
					}
					if(resultSet1.getString("KIND").equals("U")) {
						preparedStatement = connection.prepareStatement("DELETE FROM UNIVMEMBERS WHERE PHONE = ?");
						preparedStatement.setString(1, phone);
						preparedStatement.executeUpdate();
					}
					if(!resultSet1.getString("KIND").equals("N")) {
						preparedStatement = connection.prepareStatement("UPDATE PHONEMEMBERS SET KIND = N WHERE PHONE = ?");
						preparedStatement.setString(1, phone);
						preparedStatement.executeUpdate();
					}
				}
			}
			else {
				System.out.printf("수정할 데이터가 없습니다.\n");
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			transaction(result);
			close();
		}
	}
	
	// 삭제
	public void delete(String phone) {
		connection = JdbcUtill.connect();
		boolean result = false;
		
		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM PHONEMEMBERS WHERE PHONE = ?");	// 일단 삭제할 데이터가 있는지 확인
			preparedStatement.setString(1, phone);
			resultSet1 = preparedStatement.executeQuery();
			if(resultSet1.next()) {	// 삭제할 대상이 있다면
				String kind = resultSet1.getString("KIND");	// KIND의 값을 보고 판단
				if (kind.equals("N")) {	// 자식테이블에 걸려있는 외래키 없으므로 바로 삭제 가능
					preparedStatement = connection.prepareStatement("DELETE FROM PHONEMEMBERS WHERE PHONE = ?");
					preparedStatement.setString(1, phone);
					int result1 = preparedStatement.executeUpdate();
					
					result = isSuccess(result1, result1);
				}
				else if (kind.equals("U")) {	// # 3. 자식테이블에 외래키 있으므로 삭제 순서 중요
					preparedStatement = connection.prepareStatement("DELETE FROM UNIVMEMBERS WHERE PHONE = ?");	// 자식테이블 먼저 삭제
					preparedStatement.setString(1, phone);
					int result1 = preparedStatement.executeUpdate();
					
					preparedStatement = connection.prepareStatement("DELETE FROM PHONEMEMBERS WHERE PHONE = ?");	// 그다음 상위 테이블 삭제
					preparedStatement.setString(1, phone);
					int result2 = preparedStatement.executeUpdate();
					
					result = isSuccess(result1, result2);
				}
				else if (kind.equals("C")) {	// #3
					preparedStatement = connection.prepareStatement("DELETE FROM COMPANYMEMBERS WHERE PHONE = ?");
					preparedStatement.setString(1, phone);
					int result1 = preparedStatement.executeUpdate();
					
					preparedStatement = connection.prepareStatement("DELETE FROM PHONEMEMBERS WHERE PHONE = ?");
					preparedStatement.setString(1, phone);
					int result2 = preparedStatement.executeUpdate();
					
					result = isSuccess(result1, result2);
				}
				else {
					
				}
				System.out.printf("DELETE 성공\n");
			}
			else {	// 삭제할 대상이 없다면
				System.out.printf("삭제할 데이터가 없습니다.\n");
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			transaction(result);
			close();
		}
	}
}
