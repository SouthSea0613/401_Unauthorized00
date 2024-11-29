package bean;

public class PhoneCompanyInfo extends PhoneInfo {
	private String company;
	
	public PhoneCompanyInfo(String name, String phoneNumber, String company) {
		super(name, phoneNumber);
		this.company = company;
	}
	
	public String getCompany() {
		return company;
	}
	
	@Override
	public String toString() {
		return super.getName() + "\t" + company + "\t" + super.getPhone();
	}
}
