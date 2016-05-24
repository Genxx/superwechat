package cn.gen.superwechat.bean;


import java.util.Objects;

/**
 * EMUser entity. @author MyEclipse Persistence Tools
 */
public class User extends Location implements java.io.Serializable {
	private static final long serialVersionUID = 6848921231724157394L;

	// Fields

	/**
	 * 
	 */
	private Integer muserId;//用户在环信服务器上的id
	private String muserName;//用户在环信服务器上的账号
	private String muserPassword;//用户在环信服务器上的密码
	private String muserNick;//用户在环信服务器上的昵称
	private Integer muserUnreadMsgCount=0;//用户位置信息
	private String header;//保存昵称首字母的索引

	// Constructors

	/** default constructor */
	public User() {
	}
	
	public User(boolean result, int msg) {
		this.setResult(result);
		this.setMsg(msg);
	}

	/** minimal constructor */
	public User(Integer MUserId, String MUserName, String MUserPassword, String MUserNick) {
		this.muserId = MUserId;
		this.muserName = MUserName;
		this.muserPassword = MUserPassword;
		this.muserNick = MUserNick;
	}

	/** full constructor */
	public User(Integer MUserId, String MUserName, String MUserPassword, String MUserNick,
			Integer MUserUnreadMsgCount) {
		this(MUserId, MUserName, MUserPassword, MUserNick);
		this.muserUnreadMsgCount = MUserUnreadMsgCount;
	}

	// Property accessors
	public Integer getMUserId() {
		return this.muserId;
	}

	public void setMUserId(Integer MUserId) {
		this.muserId = MUserId;
	}

	public String getMUserName() {
		return this.muserName;
	}

	public void setMUserName(String MUserName) {
		this.muserName = MUserName;
	}

	public String getMUserPassword() {
		return this.muserPassword;
	}

	public void setMUserPassword(String MUserPassword) {
		this.muserPassword = MUserPassword;
	}

	public String getMUserNick() {
		return this.muserNick;
	}

	public void setMUserNick(String MUserNick) {
		this.muserNick = MUserNick;
	}

	public Integer getMUserUnreadMsgCount() {
		return this.muserUnreadMsgCount;
	}

	public void setMUserUnreadMsgCount(Integer MUserUnreadMsgCount) {
		this.muserUnreadMsgCount = MUserUnreadMsgCount;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User)) return false;
		User user = (User) o;
		return muserName.equals(user.muserName);
	}

	@Override
	public int hashCode() {
		return muserName.hashCode();
	}

	@Override
	public String toString() {
		return "EMUser [MUserId=" + muserId + ", MUserName=" + muserName
				+ ", MUserPassword=" + muserPassword + ", MUserNick="
				+ muserNick + ", MUserUnreadMsgCount=" + muserUnreadMsgCount
				+ "]";
	}
	

}