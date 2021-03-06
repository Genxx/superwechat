package cn.gen.superwechat.bean;

public class Message {

	boolean result;
	int msg;//就是我们在String中使用的消息。
	
	public Message() {
		super();
	}
	
	public Message(boolean result, int msg) {
		super();
		this.result = result;
		this.msg = msg;
	}
	
	public boolean isResult() {
		return result;
	}
	
	public void setResult(boolean result) {
		this.result = result;
	}
	
	public int getMsg() {
		return msg;
	}
	
	public void setMsg(int msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return "Message [result=" + result + ", msg=" + msg + "]";
	}
	
}
